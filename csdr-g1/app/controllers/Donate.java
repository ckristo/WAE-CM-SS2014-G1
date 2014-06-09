package controllers;

import models.Donation;
import models.Donator;

import play.*;
import play.data.Form;
import play.mvc.*;
import play.db.jpa.*;

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
		// check for input errors
		Form<Donator> filledDonatorForm = donatorForm.bindFromRequest();
		Form<Donation> filledDonationForm = donationForm.bindFromRequest();
		if (filledDonatorForm.hasErrors() || filledDonationForm.hasErrors()) {
			return badRequest(form.render(filledDonatorForm, filledDonationForm));
		}
		
		// get model objects
		Donator donator = filledDonatorForm.get();
		Donation donation = filledDonationForm.get();
		
		JPA.em().persist(donator);
		
		//DBG
		return ok(donator+" "+donation);
	}
	
}
