import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import org.hamcrest.core.IsEqual;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import models.Enumerated.*;


/**
 * Tests for the Donate controller.
 */
public class DonateTest extends AbstractTest {

	@Test
	public void donateTest_1() {
		// DBG
		assertThat(Category.findById(1).getId()).isEqualTo(1);
	}

}
