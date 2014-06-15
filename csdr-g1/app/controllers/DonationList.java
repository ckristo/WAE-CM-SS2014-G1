package controllers;

import models.Donation;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.list.*;

public class DonationList extends Controller {
	
    /**
     * Display the paginated list of computers.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    @Transactional(readOnly=true)
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(
            list.render(
                Donation.page(page, 3, sortBy, order, filter),
                sortBy, order, filter
            )
        );
    }

}
