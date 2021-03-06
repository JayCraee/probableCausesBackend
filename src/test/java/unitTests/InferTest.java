package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import partib.groupProject.probableCauses.backend.model.bql.query.Infer;

import java.lang.reflect.MalformedParametersException;



public class InferTest {
	private static String standardiseQuery(String a){
		String standardWhitespace = a.trim().replaceAll("\\s+", " ");
		return standardWhitespace.toUpperCase();
	}
	
	public void singleTest(String input, String expectedBQLOutput) {
		singleTest(input, expectedBQLOutput, false);
	}


	public void singleTest(String input, String expectedBQLOutput, boolean shouldFail) {
		Infer e = new Infer(input);
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
	public void testDefaultNoCOLEXP() {
	
		String in = "MODE=FROM-POPULATION=pop";
		singleTest(in, null, true);
	
	}

	@Test (expected = MalformedParametersException.class)
	public void testExplicitNoCOLEXP() {
	//TODO: This will currently definitely fail. However, otherwise the BQL generator allows INFER EXPLICITs without column names which is a terrible idea.
	
		String in = "MODE=EXPLICIT!FROM-POPULATION=pop";
		singleTest(in, null, true);
	

	}
	@Test (expected = MalformedParametersException.class)
	public void testDefaultNoPopulation() {
	
		String in = "MODE=FROM-COLEXP=col";
		singleTest(in, null, true);
	
	}

	@Test (expected = MalformedParametersException.class)
	public void testExplicitNoPopulation() {

		String in = "MODE=EXPLICIT!FROM-COLEXP=col";
		singleTest(in, null, true);
	
	}

	@Test
	public void testDefaultBase() {
	
		String in = "MODE=FROM-POPULATION=pop-COLEXP=col";
		String exp = "INFER COL WITH CONFIDENCE 0.7 FROM POP LIMIT 50;";
		
		singleTest(in, exp);
	
	}

	
	@Test
	public void testExplicitBase() {
	
		String in = "MODE=EXPLICIT!FROM-POPULATION=pop-COLEXP=col";
		String exp = "INFER EXPLICIT COL FROM POP LIMIT 50;";
		
		singleTest(in, exp);
	}

	
	@Test
	public void testDefaultConf() {
	
		String in = "MODE=FROM-POPULATION=pop-COLEXP=col-WITH_CONFIDENCE=0.8";
		String exp = "INFER COL WITH CONFIDENCE 0.8 FROM POP LIMIT 50;";
		
		singleTest(in, exp);
	}

	
	@Test
	public void testExplicitConf() {
	
		String in = "MODE=EXPLICIT!FROM-POPULATION=pop-COLEXP=PREDICT!col2!AS!two!CONFIDENCE!conf2";
		String exp = "INFER EXPLICIT PREDICT COL2 AS TWO CONFIDENCE CONF2 FROM POP LIMIT 50;";
		
		singleTest(in, exp);
	}

	
	@Test
	public void testDefaultConstraint() {
		String in = "MODE=FROM-POPULATION=pop-COLEXP=col1,col2-WHERE=col3>5-GROUP_BY=col4-ORDER_BY=col6!ASC";
		String exp = "INFER COL1,COL2 WITH CONFIDENCE 0.7 FROM POP WHERE COL3>5 GROUP BY COL4 ORDER BY COL6 ASC LIMIT 50;";
		
		singleTest(in, exp);
	}

	@Test (expected = MalformedParametersException.class)
	public void testInvalidField() {
		String in = "MODE=FROM-POPULATION=pop-COLEXP=col-ANFIELD=bad";
		singleTest(in, null, true);
	}

}

