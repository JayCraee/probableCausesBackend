package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.lang.reflect.MalformedParametersException;

public class SelectTest {
	private String start = "http://localhost:8080/bql/query/select/";

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
	public void testBase() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testConstraint() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop" +
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);

		uri = start +
				"COLUMNS=col1" +
				"-TABLE=pop" +
				"-WHERE=FALSE";
		expectedColumnNames = Arrays.asList("col1");
		expectedNumberOfRows = 0;
		
		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

}
