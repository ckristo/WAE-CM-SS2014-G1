package controllers;

import helper.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Donation;
import models.Donor;
import models.Enumerated.Category;
import models.Enumerated.TransportType;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.*;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.*;
import play.Play;
import views.html.donate.*;

public class Donate extends Controller {

	/**
	 * form for all donor specific information.
	 */
	static Form<Donor> donorForm = Form.form(Donor.class);
	
	/**
	 * form for all donation specific information.
	 */
	static Form<Donation> donationForm = Form.form(Donation.class);
	
	@Transactional(readOnly=true)
	public static Result form() {
        return ok(form.render(donorForm, donationForm));
    }
	
	@Transactional(readOnly=true)
	public static Result summary() {
		Form<Donor> filledDonorForm = donorForm.bindFromRequest("name", "email", "street", "phone", "zip", "city");
		Form<Donation> filledDonationForm = donationForm.bindFromRequest("label", "pickUpDate", "description", "files", "number");
		
		Donor donor = getDonorFromRequest(filledDonorForm);
		Donation donation = getDonationFromRequest(filledDonationForm);
		
		System.out.println("Files:");
		for(FilePart file : request().body().asMultipartFormData().getFiles()) {
			System.out.println(file.getFilename());
		}
		System.out.println("------------");
		
		// check for form errors
		if (filledDonorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonorForm, filledDonationForm));
		}
		
		return ok(summary.render(donor, donation));
	}
	
	@Transactional
	public static Result submit() {
		Form<Donor> filledDonorForm = donorForm.bindFromRequest("name", "email", "street", "phone", "zip", "city");
		Form<Donation> filledDonationForm = donationForm.bindFromRequest("label", "pickUpDate", "description", "files", "number");
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		
		Donor donor = getDonorFromRequest(filledDonorForm);
		Donation donation = getDonationFromRequest(filledDonationForm);
		
		// check for form errors
		if (filledDonorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonorForm, filledDonationForm));
		}
		
		// if user didn't choose confirm, go back to donate form
		if (dynamicForm.field("confirm").value() == null) {
			return ok(form.render(filledDonorForm, filledDonationForm));
		}
		
		// persist data
		donation.setDonor(donor);
		JPA.em().persist(donor);
		JPA.em().persist(donation);
		
		flash("success", "success");
		
		return ok(form.render(donorForm, donationForm));
	}
	
	public static Result upload() {
		Http.MultipartFormData body = request().body().asMultipartFormData();

	    for(Http.MultipartFormData.FilePart file : body.getFiles()) {
	    }
		
		ObjectNode result = Json.newObject();
		result.put("testKey", "testValue");
		return ok(result);
	}
	
	/**
	 * Validates and returns a Donor object from sent request data. If validation doesn't succeed, null is returned 
	 * (the errors can be retrieved using filledDonorForm.errors())
	 * @param filledDonorForm the donor form containing the request data (created with Form.bindFromRequest())
	 * @return the Donor object or null if validation failed.
	 */
	private static Donor getDonorFromRequest(Form<Donor> filledDonorForm) {
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		
		// check conditionally required values for TransportType pickUp
		String input_transportType_id = dynamicForm.get("transportType.id");
		if (input_transportType_id != null && input_transportType_id.equals(Play.application().configuration().getString("transportType.pickUp.id"))) {
			// -- street
			String street_input = filledDonorForm.field("street").value();
			if (street_input == null || street_input.equals("")) {
				filledDonorForm.reject("street", Messages.get("error.required"));
			}
			// -- zip
			String zip_input = filledDonorForm.field("zip").value();
			if (zip_input == null || zip_input.equals("")) {
				filledDonorForm.reject("zip", Messages.get("error.required"));
			}
			// -- city
			String city_input = filledDonorForm.field("city").value();
			if (city_input == null || city_input.equals("")) {
				filledDonorForm.reject("city", Messages.get("error.required"));
			}
			// -- phone
			String phone_input = filledDonorForm.field("phone").value();
			if (phone_input == null || phone_input.equals("")) {
				filledDonorForm.reject("phone", Messages.get("error.required"));
			}
		}
		// only allow street + zip + city in combination
		else {
			String street_input = filledDonorForm.field("street").value();
			String zip_input    = filledDonorForm.field("zip").value();
			String city_input   = filledDonorForm.field("city").value();
			
			boolean street_empty = street_input == null || street_input.equals("");
			boolean zip_empty    = zip_input == null    || zip_input.equals("");
			boolean city_empty   = city_input == null    || city_input.equals("");
			
			// if not all empty and not all filled -> add global error
			if (!(street_empty && zip_empty && city_empty) && (street_empty || zip_empty || city_empty)) {
				filledDonorForm.reject(Messages.get("error.address_incomplete"));
			}
		}
		
		if (!filledDonorForm.hasErrors()) {
			return filledDonorForm.get();
		}
		return null;
	}
	
	/**
	 * Validates and returns a Donation object from sent request data. If validation doesn't succeed, null is returned 
	 * (the errors can be retrieved using filledDonationForm.errors())
	 * @param filledDonationForm the donation form containing the request data (created with Form.bindFromRequest())
	 * @return the Donation object or null if validation failed.
	 */
	private static Donation getDonationFromRequest(Form<Donation> filledDonationForm) {
		// check related model foreign keys
		Category category = null;
		TransportType transportType = null;
		
		// -- category.id
		String input_category_id = filledDonationForm.field("category.id").value();
		if (input_category_id == null || input_category_id.equals("")) {
			filledDonationForm.reject("category.id", Messages.get("error.required"));
		}
		else if (!input_category_id.matches("\\d+")) {
			filledDonationForm.reject("category.id", Messages.get("error.invalid"));
		}
		else {
			int category_id = Integer.parseInt(input_category_id);
			category = Category.findById(category_id);
			if (category == null) {
				filledDonationForm.reject("category.id", Messages.get("error.unknown.Category"));
			}
		}
		
		// -- transportType.id
		String input_transportType_id = filledDonationForm.field("transportType.id").value();
		if (input_transportType_id == null || input_transportType_id.equals("")) {
			filledDonationForm.reject("transportType.id", Messages.get("error.required"));
		}
		else if (!input_transportType_id.matches("\\d+")) {
			filledDonationForm.reject("transportType.id", Messages.get("error.invalid"));
		}
		else {
			int transportType_id = Integer.parseInt(input_transportType_id);
			transportType = TransportType.findById(transportType_id);
			if (transportType == null) {
				filledDonationForm.reject("transportType.id", Messages.get("error.unknown.TransportType"));
			}
		}
		
		if (!filledDonationForm.hasErrors()) {
			Donation donation = filledDonationForm.get();
			
			// set related models
			donation.setCategory(category);
			donation.setTransportType(transportType);
			
			return donation;
		}
		return null;
	}
	
	/*
	@Transactional
	public static Result submit() {
		// File upload handling
		MultipartFormData body = request().body().asMultipartFormData();
		uploadImages(body, donation);
	}
	*/
	
	/**
	 * Basic method to handle image uploads.
	 * 
	 * @param MultipartFormData body - The form request body containing the images
	 * @param Donation donation - The donation object the uploaded images belong to
	 * @return boolean true if all images have been uploaded successfully, false otherwise
	 */
	@SuppressWarnings("unused")
	private static boolean uploadImages(MultipartFormData body, Donation donation) {
		List<FilePart> files = body.getFiles();
		
		for(FilePart file : files) {
			models.File image = new models.File();
			image.setFilename(file.getFilename());
			image.setThumbnail(file.getFilename()); //@TODO: create thumbnail, set thumbnail name accordingly
			image.setMimetype(file.getContentType());
			image.setIs_first(false); //@TODO: implement checkbox to let user decide which image should appear first when browsing donations
			image.setDonation(donation);
			
			// Move file
			try {
				System.out.println("Moving file " + file.getFile().getCanonicalPath());
				//@TODO: Implement a random filename generation strategy to avoid files with the same name being overridden (suggestion: /app/files/1/2/3/filename.ext, where the donation's id is 123)
				//@TODO: Store the root file path (/vagrant/csdr-g1/app/files/) in a constant or as a config property for better maintainability
				if(!FileHelper.moveFile(file.getFile(), new File("/vagrant/csdr-g1/app/files/" + file.getFilename()))) {
					return false;
				}
			} catch (IOException ioe) {
				return false;
			}
		}
		
		return true;
	}
	
}
