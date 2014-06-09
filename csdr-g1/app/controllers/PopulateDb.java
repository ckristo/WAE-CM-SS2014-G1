package controllers;

import models.Enumerated.Category;
import models.Enumerated.TransportType;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.populate.*;

public class PopulateDb extends Controller {
	
	@Transactional
	public static Result categories() {
		Category category = new Category();
		category.setName("Hochwasserschutz");
		JPA.em().persist(category);
		
		category = new Category();
		category.setName("Haushaltsutensilien");
		JPA.em().persist(category);
		
		category = new Category();
		category.setName("Hygieneartikel");
		JPA.em().persist(category);

        return ok(index.render("Categories"));
    }
	
	@Transactional
	public static Result transportTypes() {
		TransportType transportType = new TransportType();
		transportType.setName("Die Spende muss von mir zu Hause abgeholt werden.");
		JPA.em().persist(transportType);
		
		transportType = new TransportType();
		transportType.setName("Ich liefere die Spende selber.");
		JPA.em().persist(transportType);
		
		transportType = new TransportType();
		transportType.setName("Die Spende wird von mir per Post versendet.");
		JPA.em().persist(transportType);

        return ok(index.render("Transport Types"));
    }
}
