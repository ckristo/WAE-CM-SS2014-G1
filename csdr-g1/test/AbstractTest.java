import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.start;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.api.test.Helpers;
import play.db.jpa.JPA;
import play.test.FakeApplication;
import models.Enumerated.Category;
import models.Enumerated.TransportType;


public abstract class AbstractTest {

	/**
	 * Category test data
	 */
	static Category[] categories = new Category[] {
		new Category("Bekleidung"),
		new Category("Lebensmittel"),
		new Category("Hygieneartikel"),
		new Category("Haushaltsutensilien"),
		new Category("Elektrogeräte"),
	};
	
	/**
	 * TransportType test data
	 */
	static TransportType[] transportTypes = new TransportType[] {
		new TransportType("Ich liefere die Spende"),
		new TransportType("Ich sende die Spende per Post"),
		new TransportType("Die Spende soll abgeholt werden"),
	};
	
	/**
	 * FakeApplication used for testing our Play application.
	 */
	FakeApplication fakeApplication;
	
	/**
	 * JPA entity manager mapping test data source (in-memory DB).
	 */
	EntityManager entityManager;
	
	/**
	 * Performs set up task for each test.
	 */
	@Before
	public void setUp() {
		// init fake application
		fakeApplication = fakeApplication();
        start(fakeApplication);
		
        // insert test data into test DB
		JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            	for (Category c : categories) {
        			JPA.em().persist(c);
        		}
        		for (TransportType t : transportTypes) {
        			JPA.em().persist(t);
        		}
            }
        });
	}
	
	@After
	public void tearDown() {
	}
}
