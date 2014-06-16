package controllers;

import models.Donor;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class Login extends Controller {

	static Form<Login.LoginModel> loginForm = Form.form(Login.LoginModel.class);

	public static Result form() {
		System.out.println("form");
		return ok(login.render(loginForm));
	}

	public static Result login() {
		System.out.println("login");
		Form<Login.LoginModel> formLm = loginForm.bindFromRequest("username",
				"password");

		if (formLm.hasErrors()) {
			flash("failed", "failed");
			return badRequest(login.render(loginForm));
		}

		LoginModel lm = formLm.get();

		// STATIC PASSWORD COMPARISON
		if (formLm.field("username").value().equals("waecm")
				&& formLm.field("password").value().equals("waecm")) {
			System.out.println("correct login");
			session().clear();
	        session("username", formLm.field("username").value());
			return redirect(routes.Application.index());
		} else {
			flash("wrongCredentials", "failed");
		}

		return ok(login.render(loginForm));
	}
	
	public static Result logout() {
		session().clear();
		return redirect(routes.Application.index());
	}

	public static class LoginModel {
		@Required
		public String username;
		@Required
		public String password;
	}
}
