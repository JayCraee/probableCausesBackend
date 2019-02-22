package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import partib.groupProject.probableCauses.backend.ProbableCausesApplication;
import partib.groupProject.probableCauses.backend.controller.QueryController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)
public class InferTest {
	@Autowired
	private MockMvc mockMvc;

	private String start = "/bql/query/infer/";

	@Test
	public void testDefault() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicit() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultLimit() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" +
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitLimit() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop"+
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultOrderBy() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" +
				"-ORDER_BY=col1";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitOrderBy() throws BQLException, InvalidReturnFormatException, Exception {
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop"+
				"-ORDER_BY=col1";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultConfidence() throws BQLException, InvalidReturnFormatException, Exception {
		// N.B. Confidence works differently in default mode and explicit mode. i
		// This infers missing values iff this can be done with confidence >= given value.
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" +
				"-WITH_CONFIDENCE=0.8"+
				"-LIMIT=0";		//XXX Bad way of ensuring the number of columns is actually achieved 
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 0;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitConfidence() throws BQLException, InvalidReturnFormatException, Exception {
		// N.B. Confidence works differently in default mode and explicit mode. i
		// This just infers the values and writes the confidence into another column.
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1!PREDICT!col1!AS!c!CONFIDENCE!conf" +
				"-POPULATION=pop"+
				"-ORDER_BY=col1" + 
				"-LIMIT=0";		//XXX Bad way of ensuring the number of columns is actually achieved
		List<String> expectedColumnNames = Arrays.asList("c, conf");
		int expectedNumberOfRows = 0;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultConstraint() throws BQLException, InvalidReturnFormatException, Exception{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" + 
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitConstraint() throws BQLException, InvalidReturnFormatException, Exception{
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop" +
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}



}
