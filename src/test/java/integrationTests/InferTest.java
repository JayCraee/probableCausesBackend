package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.lang.reflect.MalformedParametersException;

public class InferTest {
	private String start = "http://localhost:8080/bql/query/infer/";

	@Test (expected = MalformedParametersException.class)
	public void testEmptyRejected() throws BQLException, InvalidReturnFormatException{
		String uri = start;

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoColNames() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-POPULATION=pop";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoPopulation() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}

	@Test (expected = MalformedParametersException.class)
	public void testBadField() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"COLNAMES=col1" +
				"-POPULATION=pop" + 
				"-ANFIELD=bad";

		IntegrationTestFramework.singleTest(uri, null, 0);
	}


	@Test
	public void testDefault() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testExplicit() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testDefaultLimit() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" +
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testExplicitLimit() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop"+
				"-LIMIT=25";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 25;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testDefaultOrderBy() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" +
				"-ORDER_BY=col1";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testExplicitOrderBy() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop"+
				"-ORDER_BY=col1";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testDefaultConfidence() throws BQLException, InvalidReturnFormatException{
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

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testExplicitConfidence() throws BQLException, InvalidReturnFormatException{
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

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testDefaultConstraint() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=FROM" +
				"-COLNAMES=col1" +
				"-POPULATION=pop" + 
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}

	@Test
	public void testExplicitConstraint() throws BQLException, InvalidReturnFormatException{
		String uri = start +
				"MODE=EXPLICIT_FROM" +
				"-EXPRESSION=col1" +
				"-POPULATION=pop" +
				"-WHERE=TRUE";
		List<String> expectedColumnNames = Arrays.asList("col1");
		int expectedNumberOfRows = 50;

		IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
	}



}
