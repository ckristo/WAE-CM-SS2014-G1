package controllers;

import play.mvc.*;
import play.mvc.Http.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
    	// save target for redirect after login
    	ctx.flash().put("redirectUrl", "GET".equals(ctx.request().method()) ? ctx.request().path() : "/");
        return redirect(routes.Login.form());
    }
}
