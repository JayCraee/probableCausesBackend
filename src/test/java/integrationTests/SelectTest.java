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
import org.springframework.web.util.NestedServletException;
import partib.groupProject.probableCauses.backend.ProbableCausesApplication;
import partib.groupProject.probableCauses.backend.controller.QueryController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.lang.reflect.MalformedParametersException;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)

public class SelectTest {
	@Autowired
	private MockMvc mockMvc;

	private String start = "/bql/query/select/";

	@Ignore // djh242 25/02/19 - can safely ignore this for now as it's not a case that should occur in practice
	@Test (expected = MalformedParametersException.class)
	public void testEmptyRejected() throws Exception{
		String uri = start;

		IntegrationTestFramework.singleTest(uri, null, null, mockMvc, true);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoTable() throws Exception{
		String uri = start +
				"COLNAMES=ID";

		IntegrationTestFramework.singleTest(uri, null, null, mockMvc, true);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoCOLNAMES() throws Exception{
		String uri = start +
				"TABLE=CRIMEDATA";

		IntegrationTestFramework.singleTest(uri, null, null, mockMvc, true);
	}

	@Test (expected = MalformedParametersException.class)
	public void testBadField() throws Exception{
		String uri = start +
				"COLNAMES=ID" +
				"-TABLE=CRIMEDATA" + 
				"-ANFIELD=bad";

		IntegrationTestFramework.singleTest(uri, null, null, mockMvc, true);
	}


	@Test
	public void testBase() throws Exception{
		String uri = start +
				"COLNAMES=ID" +
				"-TABLE=CRIMEDATA";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testAs() throws Exception{
		String uri = start +
				"COLNAMES=ID!AS!IDE" +
				"-TABLE=CRIMEDATA";
		List<String> expectedColumnNames = Arrays.asList("IDE");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testOrderBy() throws Exception{
		String uri = start +
				"COLNAMES=ID" +
				"-ORDER_BY=ID" +
				"-TABLE=CRIMEDATA";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testConstraint() throws Exception{
		String uri = start +
				"COLNAMES=ID" +
				"-TABLE=CRIMEDATA" +
				"-WHERE=ID>1";
		List<String> expectedColumnNames = Arrays.asList("ID");
		int expectedNumberOfRows = 50;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);

		uri = start +
				"COLNAMES=ID" +
				"-TABLE=CRIMEDATA" +
				"-WHERE=ID<1";
		expectedColumnNames = Arrays.asList("ID");
		expectedNumberOfRows = 0;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

}
