package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;
import partib.groupProject.probableCauses.backend.model.bql.query.Select;

import java.lang.reflect.MalformedParametersException;



public class SelectTest {
	private static String standardiseQuery(String a){
		String standardWhitespace = a.trim().replaceAll("\\s+", " ");
		return standardWhitespace.toUpperCase();
	}
	
	public void singleTest(String input, String expectedBQLOutput) {
		singleTest(input, expectedBQLOutput, false);
	}


	public void singleTest(String input, String expectedBQLOutput, boolean shouldFail) {
		Select e = new Select(input);
		String BQLOutput = e.getBQL().get(0); //.get(0) because getBQL() currently returns a list of 1 element
		
		if (!shouldFail){
			assertEquals(
				standardiseQuery(expectedBQLOutput),
				standardiseQuery(BQLOutput),
				"BQL incorrectly generated: "
			);
		}
	}
	
	@Test (expected = MalformedParametersException.class)
	public void testEmptyInput() {
		String input = "";
		singleTest(input, null, true);
	}

	
	@Test (expected = MalformedParametersException.class)
	public void testDefaultNoColNames() {
	
		String in = "TABLE=pop";
		singleTest(in, null, true);
	
	}

	@Test (expected = MalformedParametersException.class)
	public void testNoTable() {
	//TODO: This will currently definitely fail. However, otherwise the BQL generator allows INFER EXPLICITs without column names which is a terrible idea.
	
		String in = "COLNAMES=col"; 
		singleTest(in, null, true);
	}

	@Test
	public void testBase() {
	
		String in = "COLNAMES=col-TABLE=tab";
		String exp = "SELECT COL FROM TAB LIMIT 50;";
		
		singleTest(in, exp);
	
	}
	
	@Test
	public void testWhere() {
	
		String in = "TABLE=tab-COLNAMES=col-WHERE=col<5";
		String exp = "SELECT COL FROM TAB WHERE COL<5 LIMIT 50;";
		
		singleTest(in, exp);
	}

	@Test
	public void testGroupBy() {
		String in = "COLNAMES=col1,col2-TABLE=tab-GROUP_BY=col1";
		String exp = "SELECT COL1,COL2 FROM TAB GROUP BY COL1 LIMIT 50;";
		
		singleTest(in, exp);
	}

	@Test
	public void testOrderBy() {
		String in = "COLNAMES=col1,col2-TABLE=tab-ORDER_BY=col1";
		String exp = "SELECT COL1,COL2 FROM TAB ORDER BY COL1 LIMIT 50;";
		
		singleTest(in, exp);

		in = "COLNAMES=col1,col2-TABLE=tab-ORDER_BY=col1!DESC";
		exp = "SELECT COL1,COL2 FROM TAB ORDER BY COL1 DESC LIMIT 50;";
		
		singleTest(in, exp);
	}

	@Test
	public void testLimit() {
	
		String in = "COLNAMES=col-TABLE=tab-LIMIT=50";
		String exp = "SELECT COL FROM TAB LIMIT 50;";
		
		singleTest(in, exp);
	
	}

	@Test
	public void testAll() {
	
		String in = "COLNAMES=col1,col2-TABLE=tab-WHERE=col1<5-GROUP_BY=col1-ORDER_BY=col1-LIMIT=50";
		String exp = "SELECT COL1,COL2 FROM TAB WHERE COL1<5 GROUP BY COL1 ORDER BY COL1 LIMIT 50;";
		
		singleTest(in, exp);
	
	}
	
	@Test (expected = MalformedParametersException.class)
	public void testInvalidField() {
		String in = "MODE=FROM-POPULATION=pop-COLNAMES=col-ANFIELD=bad";
		singleTest(in, null, true);
	}

}

