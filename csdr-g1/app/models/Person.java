package models;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class Person {

	@Required
	public String name;
	
	@Required
	@Email
	public String email;
	
}
