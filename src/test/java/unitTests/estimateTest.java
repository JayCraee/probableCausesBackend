package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;

import java.lang.reflect.MalformedParametersException;


public class estimateTest{
    private static String standardiseQuery(String a){
        String standardWhitespace = a.trim().replaceAll("\\s+", " ");
        return standardWhitespace.toUpperCase();
    }

    public void singleTest(String input, String expectedBQLOutput) {
        Estimate e = new Estimate(input);
        String BQLOutput = e.getBQL().get(0); //.get(0) because getBQL() currently returns a list of 1 element

        assertEquals(
                standardiseQuery(expectedBQLOutput),
                standardiseQuery(BQLOutput),
                "BQL incorrectly generated: "
        );
    }

    @Test (expected = MalformedParametersException.class)
    public void testEmptyInput() {
        String input = "";
        String expectedOutput = "";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testConstraint() {
        String input = "MODE=FROM_VARIABLES_OF-EXPRESSION=PROBABILITY!DENSITY!OF!VALUE!val!GIVEN!x<5-EXPNAME=ProbDensity-POPULATION=AFRICAN_DATA-LIMIT=100";
        String expectedOutput = "SELECT * FROM ( ESTIMATE PROBABILITY DENSITY OF VALUE VAL GIVEN X<5 FROM VARIABLES OF AFRICAN_DATA AS PROBDENSITY) ORDER BY PROBDENSITY LIMIT 100";
        singleTest(input, expectedOutput);
    }
}