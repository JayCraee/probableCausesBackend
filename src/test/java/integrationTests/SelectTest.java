package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectTest {
	private String start = "http://localhost:8080/bql/query/select/";

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
