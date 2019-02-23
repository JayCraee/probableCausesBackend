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

import java.lang.reflect.MalformedParametersException;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)

public class SelectTest {
	@Autowired
	private MockMvc mockMvc;

	private String start = "/bql/query/select/";

	@Test (expected = MalformedParametersException.class)
	public void testEmptyRejected() throws BQLException, InvalidReturnFormatException{
		String uri = start;

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoTable() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"COLUMNS=col1";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoColumns() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"TABLE=tab";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testBadField() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"COLUMNS=col1" +
				"-TABLE=tab" + 
				"-ANFIELD=bad";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}


	@Test
	public void testBase() throws BQLException, InvalidReturnFormatException, Exception{
		String uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

	@Test
	public void testConstraint() throws BQLException, InvalidReturnFormatException, Exception{
		String uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop" +
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);

		uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop" +
				"-WHERE=FALSE";
		expectedColumnNames = Arrays.asList("col1");
		expectedNumberOfRows = 0;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
	}

}
