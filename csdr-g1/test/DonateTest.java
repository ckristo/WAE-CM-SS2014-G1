import java.util.HashMap;
import java.util.Map;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.db.jpa.JPA;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import models.*;

/**
 * Tests for the Donate controller.
 */
public class DonateTest extends AbstractTest {
	
	static final Map<String, String> requestData1 = new HashMap<>();
	static {
		// donation data
		requestData1.put("label", "Test-Spendenangebot#1");
		requestData1.put("description", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
		requestData1.put("category.id", "1");
		requestData1.put("number", "1");
		requestData1.put("transportType.id", "6");
		// donor data
		requestData1.put("name", "Max Mustermann");
		requestData1.put("email", "max@mustermann.com");
	}
	
	static final Map<String, String> requestData2 = new HashMap<>();
	static {
		// donation data
		requestData2.put("label", "Test-Spendenangebot#2");
		requestData2.put("description", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
		requestData2.put("category.id", "1");
		requestData2.put("number", "1");
		requestData2.put("transportType.id", "8");
		// donor data
		requestData2.put("name", "Max Mustermann");
		requestData2.put("email", "max@mustermann.com");
		requestData2.put("street", "Kärtnerstraße 1");
		requestData2.put("zip", "1010");
		requestData2.put("city", "Wien");
		requestData2.put("phone", "+43 1 530 000");
	}

	/**
	 * Tests the summary page with correct request data (minimal data required because of TransportType == "Ich liefere die Spende selbst")
	 */
	@Test
	public void test_donateSummary1_shouldReturn200() {
		Map<String, String> data = new HashMap<>(requestData1);
		Result result = callAction(controllers.routes.ref.Donate.summary(), fakeRequest().withFormUrlEncodedBody(data));
		
		// check reply HTTP status
		assertThat(status(result)).isEqualTo(Http.Status.OK);
		
		// summary page mustn't persist data!
		JPA.withTransaction(new play.libs.F.Callback0() {
			public void invoke() {
				assertThat(Donation.findAll()).isEmpty();
				assertThat(Donor.findAll()).isEmpty();
			}
		});
	}
	
	/**
	 * Tests the summary page with correct request data (maximal data required because of TransportType == "Die Spende soll abgeholt werden")
	 */
	@Test
	public void test_donateSummary2_shouldReturn200() {
		Map<String, String> data = new HashMap<>(requestData2);
		Result result = callAction(controllers.routes.ref.Donate.summary(), fakeRequest().withFormUrlEncodedBody(data));
		
		// check reply HTTP status
		assertThat(status(result)).isEqualTo(Http.Status.OK);
		
		// summary page mustn't persist data!
		JPA.withTransaction(new play.libs.F.Callback0() {
			public void invoke() {
				assertThat(Donation.findAll()).isEmpty();
				assertThat(Donor.findAll()).isEmpty();
			}
		});
	}
	
	/**
	 * Tests the summary page with incomplete request data -- label is missing
	 */
	@Test
	public void test_donateSummary3_shouldReturn400_missingLabel() {
		Map<String, String> data = new HashMap<>(requestData1);
		
		// remove label
		data.remove("label");
		
		Result result = callAction(controllers.routes.ref.Donate.summary(), fakeRequest().withFormUrlEncodedBody(data));
		
		// check reply HTTP status
		assertThat(status(result)).isEqualTo(Http.Status.BAD_REQUEST);
		assertThat(contentAsString(result)).contains("Bitte befüllen Sie dieses Feld!");
	}
	
	/**
	 * Tests the summary page with incomplete request data -- description is missing
	 */
	@Test
	public void test_donateSummary3_shouldReturn400_missingDescription() {
		Map<String, String> data = new HashMap<>(requestData1);
		
		// remove label
		data.remove("description");
		
		Result result = callAction(controllers.routes.ref.Donate.summary(), fakeRequest().withFormUrlEncodedBody(data));
		
		// check reply HTTP status
		assertThat(status(result)).isEqualTo(Http.Status.BAD_REQUEST);
		assertThat(contentAsString(result)).contains("Bitte befüllen Sie dieses Feld!");
	}
	
	// ...
}