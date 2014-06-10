package controllers;

import helper.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import models.Donation;
import models.Donator;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.db.jpa.*;
import play.mvc.*;
import views.html.donate.*;

public class Donate extends Controller {

	static Form<Donator> donatorForm = Form.form(Donator.class);
	
	static Form<Donation> donationForm = Form.form(Donation.class);
	
	@Transactional
	public static Result form() {
        return ok(form.render(donatorForm, donationForm));
    }
	
	@Transactional
	public static Result submit() {
		Form<Donator> filledDonatorForm = donatorForm.bindFromRequest();
		Form<Donation> filledDonationForm = donationForm.bindFromRequest();
		
		// check for input errors
		if (filledDonatorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonatorForm, filledDonationForm));
		}
		
		// get model objects
		Donator donator = filledDonatorForm.get();
		Donation donation = filledDonationForm.get();
		
		JPA.em().persist(donator);
		
		// File upload handling
		MultipartFormData body = request().body().asMultipartFormData();
		uploadImages(body, donation);
		
		//DBG
		return ok(donator+" "+donation);
	}
	
	/**
	 * Basic method to handle image uploads.
	 * 
	 * @param MultipartFormData body - The form request body containing the images
	 * @param Donation donation - The donation object the uploaded images belong to
	 * @return boolean true if all images have been uploaded successfully, false otherwise
	 */
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
