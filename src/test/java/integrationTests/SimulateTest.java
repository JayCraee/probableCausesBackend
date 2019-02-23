package integrationTests;

import org.junit.Test;
import java.lang.reflect.MalformedParametersException;
import java.util.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import partib.groupProject.probableCauses.backend.ProbableCausesApplication;
import partib.groupProject.probableCauses.backend.controller.QueryController;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)
public class SimulateTest {
    @Autowired
    private MockMvc mockMvc;

    private String start = "/bql/query/simulate/";

    @Test
    public void testSimulate() throws Exception{
        String input =
                start +
                "COLNAMES=Year,Arrest" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("Year-Arrest", "frequency");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testOptionals() throws Exception {
        String input =
                start +
                "COLNAMES=Year,Arrest" +
                "-POPULATION=CRIMEDATA" +
                "-GIVEN=District=7" +
                "-LIMIT1=10000" +
                "-LIMIT2=100";

        List<String> expectedColumnNames = Arrays.asList("Year-Arrest", "frequency");
        int expectedNumberOfRows = 100;
        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testOneColumn() throws Exception {
        String input =
                start +
                "COLNAMES=Year" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("Year", "frequency");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testThreeColumns() throws Exception {
        String input =
                start +
                "COLNAMES=Year,Arrest,Domestic" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("Year-Arrest-Domestic", "frequency");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingColNames() throws Exception {
        String input = "-POPULATION=CRIMEDATA";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulation() throws Exception {
        String input = "COLNAMES=ID,Year";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() throws Exception {
        String input =
                "COLNAMES=ID,Year" +
                "-POPULATION=CRIMEDATA" +
                "-fake_news=oh!no";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }
}
