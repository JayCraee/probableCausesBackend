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
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE exp BY pop) LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=BY" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE exp BY pop) LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError1() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=cond";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError2() {
        String input =
                "MODE=BY" +
                        "-EXPRESSION=exp1" +
                        "-EXPNAME=col" +
                        "-POPULATION=pop" +
                        "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError3() {
        String input =
                "MODE=BY" +
                        "-EXPRESSION=exp1" +
                        "-EXPNAME=col" +
                        "-POPULATION=pop" +
                        "-ORDER_BY=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testFROM() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE exp FROM pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-GROUP_BY=exp3" +
                "-ORDER_BY=exp4" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE exp1 FROM pop WHERE exp2 GROUP BY exp3) ORDER BY exp4LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testFROM_VARIABLES_OF() {
        String input =
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE exp FROM VARIABLES OF pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-GROUP_BY=exp3" +
                "-ORDER_BY=exp4" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE exp1 FROM VARIABLES OF pop WHERE exp2 GROUP BY exp3) ORDER BY exp4 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OF() {  // "[FOR <subcolumns>]" doesn't appear to be supported at this point in time
        String input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE exp FROM PAIRWISE VARIABLES OF pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-ORDER_BY=exp3" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE exp1 FROM PAIRWISE VARIABLES OF pop WHERE exp2) ORDER BY exp3 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISE_VARIABLES_OFWithInvalidOptionalsError() {
        String input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testFROM_PAIRWISE() {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE exp FROM PAIRWISE pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=exp2" +
                "-ORDER_BY=exp3" +
                "-LIMIT=1000";
        expectedOutput = "SELECT * FROM (ESTIMATE exp1 FROM PAIRWISE pop WHERE exp2) ORDER BY exp3 LIMIT 1000";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISEWithInvalidOptionalsError() {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidModeError() {
        String input =
                "MODE=fake_mode" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingModeError() {
        String input =
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpressionError() {
        String input =
                "MODE=FROM" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpNameError() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulationError() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-fake_news=exp2";
        singleTest(input, null, true);
    }

    @Test
    public void testWithRealisticExpression() {
        String input =
                "MODE=BY" +
                "-EXPRESSION=x,!y" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT * FROM (ESTIMATE x, y FROM pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "MODE=FROM" +
                "-EXPRESSION=x,!y" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        expectedOutput = "SELECT * BY (ESTIMATE x, y FROM pop AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testWithEqualityInExpression() {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=x=y";
        String expectedOutput = "SELECT * FROM (ESTIMATE x, y FROM pop WHERE x=y AS col) ORDER BY col LIMIT 50";
        singleTest(input, expectedOutput);
    }
}