import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import scala.Option;
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
		
		// init JPA
		Option<JPAPlugin> jpaPlugin = fakeApplication.getWrappedApplication().plugin(JPAPlugin.class);
		entityManager = jpaPlugin.get().em("test");
		JPA.bindForCurrentThread(entityManager);
		
		// put test data into mem db
		for (Category c : categories) {
			entityManager.persist(c);
		}
		for (TransportType t : transportTypes) {
			entityManager.persist(t);
		}
	}
	
	@After
	public void tearDown() {
		JPA.bindForCurrentThread(null);
		entityManager.close();
	}
}
