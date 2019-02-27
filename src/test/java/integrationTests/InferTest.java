package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Ignore;
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

import java.lang.reflect.MalformedParametersException;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)

public class InferTest {
	@Autowired
	private MockMvc mockMvc;

	private String start = "/bql/query/infer/";

/*
	@Ignore // djh242 25/02/19 - can safely ignore this for now as it's not a case that should occur in practice
	@Test (expected = MalformedParametersException.class)
	public void testEmptyRejected() throws Exception{
		String uri = start;

		IntegrationTestFramework.singleTest(uri, null, 0, mockMvc);
	}
*/
	@Test (expected = MalformedParametersException.class)
	public void testNoCOLEXP() throws Exception{
		String uri = start +
				"MODE=FROM" +
				"-POPULATION=CRIMEDATA";

		IntegrationTestFramework.singleTest(uri, null, 0, mockMvc);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoPopulation() throws Exception{
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID";

		IntegrationTestFramework.singleTest(uri, null, 0, mockMvc);
	}

	@Test (expected = MalformedParametersException.class)
	public void testBadField() throws Exception{
		String uri = start +
				"COLEXP=ID" +
				"-POPULATION=CRIMEDATA" + 
				"-ANFIELD=bad";

		IntegrationTestFramework.singleTest(uri, null, 0, mockMvc);
	}


	@Test
	public void testDefault() throws Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicit() throws Exception {
		String uri = start +
				"MODE=EXPLICIT!FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultLimit() throws Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA" +
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitLimit() throws Exception {
		String uri = start +
				"MODE=EXPLICIT!FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA"+
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultOrderBy() throws Exception {
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA" +
				"-ORDER_BY=ID";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitOrderBy() throws Exception {
		String uri = start +
				"MODE=EXPLICIT!FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA"+
				"-ORDER_BY=ID";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultConfidence() throws Exception {
		// N.B. Confidence works differently in default mode and explicit mode. i
		// This infers missing values iff this can be done with confidence >= given value.
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA" +
				"-WITH_CONFIDENCE=0.8"+
				"-LIMIT=0";		//XXX Bad way of ensuring the number of columns is actually achieved 
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 0;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitConfidence() throws Exception {
		// N.B. Confidence works differently in default mode and explicit mode. i
		// This just infers the values and writes the confidence into another column.
		String uri = start +
				"MODE=EXPLICIT!FROM" +
				"-COLEXP=PREDICT!ID!AS!i!CONFIDENCE!conf" +
				"-POPULATION=CRIMEDATA"+
				"-ORDER_BY=ID" + 
				"-LIMIT=0";		//XXX Bad way of ensuring the number of columns is actually achieved
		List<String> expectedColumnNames = Arrays.asList("i, conf");
		int expectedNumberOfRows = 0;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testDefaultConstraint() throws Exception{
		String uri = start +
				"MODE=FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA" + 
				"-WHERE=ID>1";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testExplicitConstraint() throws Exception{
		String uri = start +
				"MODE=EXPLICIT!FROM" +
				"-COLEXP=ID" +
				"-POPULATION=CRIMEDATA" +
				"-WHERE=ID>1";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}



}
