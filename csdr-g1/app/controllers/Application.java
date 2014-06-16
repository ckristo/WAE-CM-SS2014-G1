package controllers;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("Title"));
    }
	
    public static Result contact() {
        return ok(contact.render("Title"));
    }

}
