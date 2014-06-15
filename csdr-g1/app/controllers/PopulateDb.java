package controllers;

import javax.persistence.Query;

import models.Enumerated.Category;
import models.Enumerated.TransportType;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.populate.*;

public class PopulateDb extends Controller {
	
	static Category[] categories = new Category[] {
		new Category("Bekleidung"),
		new Category("Lebensmittel"),
		new Category("Hygieneartikel"),
		new Category("Haushaltsutensilien"),
		new Category("Elektrogeräte"),
	};
	
	static TransportType[] transportTypes = new TransportType[] {
		new TransportType("Ich liefere die Spende"),
		new TransportType("Ich sende die Spende per Post"),
		new TransportType("Die Spende soll abgeholt werden"),
	};
	
	@Transactional
	public static Result categories() {
		// delete all categories first
		Query query = JPA.em().createQuery("DELETE FROM Category");
		query.executeUpdate();
		
		// save categories to DB
		for (Category c : categories) {
			JPA.em().persist(c);
		}

        return ok(index.render("Categories"));
    }
	
	@Transactional
	public static Result transportTypes() {
		// delete all transport types first
		Query query = JPA.em().createQuery("DELETE FROM TransportType");
		query.executeUpdate();
				
		// save transport types to DB
		for (TransportType t : transportTypes) {
			JPA.em().persist(t);
		}
		
        return ok(index.render("Transport Types"));
    }
}
