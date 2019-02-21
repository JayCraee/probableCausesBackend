package integrationTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstimateTest {
    private String start = ":8080/bql/query/estimate";

    @Test
    public void testBY() throws BQLException, InvalidReturnFormatException{
        String uri =
                start +
                "/MODE=BY" +
                "-EXPRESSION=col1" +
                "-EXPNAME=column1" +
                "-POPULATION=pop";
        List<String> expectedColumnNames = Arrays.asList("col1");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows);
    }
}