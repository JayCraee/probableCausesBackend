package unitTests;

import org.junit.Test;
import partib.groupProject.probableCauses.backend.model.bql.query.Simulate;

import java.lang.reflect.MalformedParametersException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulateTest {
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
    public void singleTest(String input, String expectedBQLOutput, boolean shouldFail) {
        Simulate e = new Simulate(input);
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
    public void testSimulate() {
        String input =
                "COLNAMES=col1,col2" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT col1||\"--\"||col2 as col1_col2, COUNT(col1_col2) AS frequency FROM (SIMULATE col1,col2 FROM pop LIMIT 5000) GROUP BY col1_col2 ORDER BY frequency DESC LIMIT 50";
        singleTest(input, expectedOutput);

        input =
                "COLNAMES=col1,col2" +
                "-POPULATION=pop" +
                "-GIVEN=exp1" +
                "-LIMIT1=10000" +
                "-LIMIT2=100";
        expectedOutput = "SELECT col1||\"--\"||col2 as col1_col2, COUNT(col1_col2) AS frequency FROM (SIMULATE col1,col2 FROM pop GIVEN exp1 LIMIT 10000) GROUP BY col1_col2 ORDER BY frequency DESC LIMIT 100";
        singleTest(input, expectedOutput);
    }

    @Test
    public void testDifferentNumbersOfColumns() {
        String input = // 1 column
                "COLNAMES=col" +
                "-POPULATION=pop";
        String expectedOutput = "SELECT col as col, COUNT(col) AS frequency FROM (SIMULATE col FROM pop LIMIT 5000) GROUP BY col ORDER BY frequency DESC LIMIT 50";
        singleTest(input, expectedOutput);

        input = // 3 columns
                "COLNAMES=col1,col2,col3" +
                "-POPULATION=pop";
        expectedOutput = "SELECT col1||\"--\"||col2||\"--\"||col3 as col1_col2_col3, COUNT(col1_col2_col3) AS frequency FROM (SIMULATE col1,col2,col3 FROM pop LIMIT 5000) GROUP BY col1_col2_col3 ORDER BY frequency DESC LIMIT 50";
        singleTest(input, expectedOutput);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingColNames() {
        String input = "-POPULATION=pop";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulation() {
        String input = "COLNAMES=col1,col2";
        singleTest(input, null, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() {
        String input =
                "COLNAMES=col1,col2" +
                "-POPULATION=pop" +
                "-fake_news=exp";
        singleTest(input, null, true);
    }


}