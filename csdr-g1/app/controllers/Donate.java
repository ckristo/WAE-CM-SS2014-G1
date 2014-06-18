package controllers;

import helper.FileHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        return ok(form.render(donorForm, donationForm, null));
    }
	
	@Transactional(readOnly=true)
	public static Result summary() {
		// Get files
//		System.out.println("Files:");
//		for(String key : request().body().asFormUrlEncoded().keySet()) {
//			if(key.startsWith("file[")) {
//				System.out.println(key);
//			}
//		}
//		System.out.println("------------");
		
		Form<Donor> filledDonorForm = donorForm.bindFromRequest("name", "email", "street", "phone", "zip", "city");
		Form<Donation> filledDonationForm = donationForm.bindFromRequest("label", "pickUpDate", "description", "files", "number");
		
		Donor donor = getDonorFromRequest(filledDonorForm);
		Donation donation = getDonationFromRequest(filledDonationForm);
		
		// Get files
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		List<models.File> files = getFiles(dynamicForm);
		
		// check for form errors
		if (filledDonorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonorForm, filledDonationForm, files));
		}
		
		return ok(summary.render(donor, donation, files));
	}
	
	@Transactional
	public static Result submit() {
		Form<Donor> filledDonorForm = donorForm.bindFromRequest("name", "email", "street", "phone", "zip", "city");
		Form<Donation> filledDonationForm = donationForm.bindFromRequest("label", "pickUpDate", "description", "files", "number");
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		
		Donor donor = getDonorFromRequest(filledDonorForm);
		Donation donation = getDonationFromRequest(filledDonationForm);
		
		// Get files
		List<models.File> files = getFiles(dynamicForm);
		
		// check for form errors
		if (filledDonorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonorForm, filledDonationForm, files));
		}
		
		// if user didn't choose confirm, go back to donate form
		if (dynamicForm.field("confirm").value() == null) {
			return ok(form.render(filledDonorForm, filledDonationForm, files));
		}
		
		// persist data
		donation.setDonor(donor);
		JPA.em().persist(donor);
		JPA.em().persist(donation);
		
		// Add files to donation
		moveFilesToFinalDestination(donation, files);
		donation.setFiles(files); // Donation has been persisted and should be watched -> calling set automatically updates the corresponding database entry
		
		flash("success", "success");
		
		return ok(form.render(donorForm, donationForm, null));
	}
	
	public static Result upload() {
		String tmpFolderName = session("tmpFolderName");
		SecureRandom random = new SecureRandom();
		if(tmpFolderName == null) {
			tmpFolderName = new BigInteger(130, random).toString(32);
			session("tmpFolderName", tmpFolderName);
		}
		System.out.println("tmpFolderName: " + tmpFolderName);

		Http.MultipartFormData body = request().body().asMultipartFormData();
		if(body.getFiles().size() > 1) { // Ajax upload only can handle one file per request
			return badRequest();
		}
		ObjectNode jsonFile = Json.newObject();
		for(Http.MultipartFormData.FilePart file : body.getFiles()) {
			String destPath = "/vagrant/csdr-g1/public/files/tmp/" + tmpFolderName + "/";
			String ext = file.getFilename().substring(file.getFilename().lastIndexOf("."));
			String randomFilename = new BigInteger(130, random).toString(32);
			randomFilename = randomFilename + ext;
			
			if(FileHelper.moveFile(file.getFile(), destPath, randomFilename)) {
				jsonFile.put("filename", file.getFilename());
				jsonFile.put("randomFilename", randomFilename);
			} else {
				System.out.println("Failed to move file '" + file.getFilename() + "' to '" + destPath + randomFilename + "'.");
				return badRequest();
			}
	    }
		
		ObjectNode result = Json.newObject();
		result.put("file", jsonFile);
		return ok(result);
	}
	
	public static List<models.File> getFiles(DynamicForm dynamicForm) {
		Integer numberOfFiles = new Integer(dynamicForm.get("numberOfFiles"));
		List<models.File> files = new ArrayList<models.File>();
		for(int i = 0;i<numberOfFiles;i++) {
			String filename = dynamicForm.field("file_" + i + "-filename").value();
			String tmpFilename = dynamicForm.field("file_" + i + "-tmpname").value();
			models.File file = new models.File();
			file.setFilename(filename);
			file.setTmpFilename(tmpFilename);
			files.add(file);
		}
		
		return files;
	}
	
	@Transactional
	public static boolean moveFilesToFinalDestination(Donation donation, List<models.File> files) {
		String srcPath = "/vagrant/csdr-g1/public/files/tmp/" + session("tmpFolderName") + "/";
		String destPath = "/vagrant/csdr-g1/public/files/" + donation.getId() + "/";
		for(models.File file : files) {
			File src = new File(srcPath + file.getTmpFilename());
			if(FileHelper.moveFile(src, destPath)) {
				file.setDonation(donation);
				JPA.em().persist(file);
				
				src.delete();
			} else {
				System.out.println("Failed to move file '" + file.getFilename() + "' to '" + destPath + file.getFilename() + "'.");
				return false;
			}
		}
		return true;
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
				if(!FileHelper.moveFile(file.getFile(), "/vagrant/csdr-g1/app/files/" + file.getFilename())) {
					return false;
				}
			} catch (IOException ioe) {
				return false;
			}
		}
		
		return true;
	}
	
}
