package controllers;

import models.Person;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.donate.*;

public class Donate extends Controller {

	public static Result index() {
		//TODO form submission
		Form<Person> personForm = Form.form(Person.class); //TODO: use Donation class
        return ok(index.render(personForm));
    }
	
}
