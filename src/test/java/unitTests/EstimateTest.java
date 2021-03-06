package unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;

import java.lang.reflect.MalformedParametersException;

public class EstimateTest {
    // Converts query to 'standard form' with whitespace cut down to just a single space between
    // each two tokens, and all in upper case. This is used to test equality of queries.
    private static String standardiseQuery(String a){
        String standardWhitespace = a.trim().replaceAll("\\s+", " ");
        String removeWhitespaceInBrackets = standardWhitespace
                .replaceAll("\\(\\s+", "(")
                .replaceAll("\\s+\\)", ")");
        return removeWhitespaceInBrackets.toUpperCase();
    }

    // Given an input and an expected output, tests whether Estimate produces the correct BQL
    public static void singleTest(String input, String expectedBQLOutput, boolean shouldFail) {
        Estimate e = new Estimate(input);
        String BQLOutput = e.getBQL().get(0); //.get(0) because getBQL() returns a list of 1 element

        if (!shouldFail){
            assertEquals(
                    standardiseQuery(expectedBQLOutput),
                    standardiseQuery(BQLOutput),
                    "BQL incorrectly generated: "
            );
        }
    }

    public void singleTest(String input, String expectedBQLOutput) {
        singleTest(input, expectedBQLOutput, false);
    }

    @Test (expected = MalformedParametersException.class)
    public void testEmptyInputError() {
        String input = "";
        singleTest(input, null, true);
    }

    @Test
    public void testBY() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y BY POP) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=BY" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-EXPNAME=corr" +
                "-POPULATION=pop" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR BY POP) LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError1() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-WHERE=cond";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError2() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError3() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-ORDER_BY=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testFROM() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=CORRELATION!OF!x!WITH!y" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM POP) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM" +
                "-EXPRESSION=CORRELATION!OF!x!WITH!y" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-GROUP_BY=exp3" +
                "-ORDER_BY=exp4" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM POP WHERE EXP2 GROUP BY EXP3) ORDER BY EXP4 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testFROM_VARIABLES_OF() {
        String input =
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM VARIABLES OF POP) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-GROUP_BY=exp3" +
                "-ORDER_BY=exp4" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM VARIABLES OF POP WHERE EXP2 GROUP BY EXP3) ORDER BY EXP4 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OF() {
        String input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM PAIRWISE VARIABLES OF POP) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-ORDER_BY=exp3" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM PAIRWISE VARIABLES OF POP WHERE EXP2) ORDER BY EXP3 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISE_VARIABLES_OFWithInvalidOptionalsError() {
        String input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testFROM_PAIRWISE() {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM PAIRWISE POP) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-ORDER_BY=exp3" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM PAIRWISE POP WHERE EXP2) ORDER BY EXP3 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISEWithInvalidOptionalsError() {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidModeError() {
        String input =
                "MODE=fake_mode" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingModeError() {
        String input =
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpressionError() {
        String input =
                "MODE=FROM" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulationError() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=CORRELATION OF x WITH y";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-fake_news=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testWithEqualityInCondition() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=CORRELATION OF x WITH y" +
                "-POPULATION=pop" +
                "-WHERE=x=y";
        String expectedOutput = "SELECT * FROM (ESTIMATE CORRELATION OF X WITH Y AS CORR FROM POP WHERE X=Y) LIMIT 50";
        singleTest(input, expectedOutput);
    }
}