package integrationTests;

import org.junit.Ignore;
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

// Inspired by: https://spring.io/guides/gs/testing-web/

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)
public class EstimateTest {
    @Autowired
    private MockMvc mockMvc;

    private String start = "/bql/query/estimate/";

    @Test
    public void testBY() throws Exception{
        String uri =
                start +
                "/MODE=BY" +
                "-EXPRESSION=CORRELATION!OF!Id!WITH!Year" +
                "-EXPNAME=corr" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("corr", "name0", "name1");
        int expectedNumberOfRows = 1;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError1() throws Exception {
        String input = start +
                "MODE=BY" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=cond";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError2() throws Exception {
        String input = start +
                "MODE=BY" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError3() throws Exception {
        String input = start +
                "MODE=BY" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-ORDER_BY=exp2";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test
    public void testFROM() throws Exception {
        String uri =
                start +
                "/MODE=FROM" +
                "-EXPRESSION=ID,CaseNumber,Year" +
                "-EXPNAME=Year" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("ID", "CaseNumber", "Year");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROMOptionals() throws Exception {
        String uri =
                start +
                "/MODE=FROM" +
                "-EXPRESSION=ID,CaseNumber,Year" +
                "-EXPNAME=Year" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=ID>1000" +
                "-GROUP_BY=Year" +
                "-ORDER_BY=Year,ID" +
                "-LIMIT=5";
        List<String> expectedColumnNames = Arrays.asList("ID", "CaseNumber", "Year");
        int expectedNumberOfRows = 5;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_VARIABLES_OF() throws Exception {
        String input =
                start +
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=PROBABILITY!DENSITY!OF!VALUE!5" +
                "-EXPNAME=probDensity" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("probDensity");
        int expectedNumberOfRows = 8; // number of columns in table

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_VARIABLES_OFOptionals() throws Exception {
        String input =
                start +
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=PROBABILITY!DENSITY!OF!VALUE!5" +
                "-EXPNAME=probDensity" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=1=1" +
                "-GROUP_BY=Year" +
                "-ORDER_BY=Arrest" +
                "-LIMIT=5";
        List<String> expectedColumnNames = Arrays.asList("probDensity");
        int expectedNumberOfRows = 5;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OF() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION" +
                "-EXPNAME=corr" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("corr", "population_id", "name0", "name1");
        int expectedNumberOfRows = 64;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OFOptionals() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION" +
                "-EXPNAME=corr" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=1=1" + // We don't understand how to use where with FROM PAIRWISE VARIABLES OF. Avoid if possible.
                "-ORDER_BY=corr" +
                "-LIMIT=5";

        List<String> expectedColumnNames = Arrays.asList("corr", "population_id", "name0", "name1");
        int expectedNumberOfRows = 5;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISE_VARIABLES_OFInvalidOptionalsError() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=ID" +
                "-EXPNAME=mutinf" +
                "-POPULATION=CRIMEDATA" +
                "-GROUP_BY=Year";

        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    // djh242: As of 25/02/19, this fails with an empty BQLException and I don't know why.
    // Possible that there's an SQLException that isn't being delivered properly.
    @Test
    public void testFROM_PAIRWISE() throws Exception {
        String input = start + //
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF!Year" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("value", "rowid0", "rowid1");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    // djh242: As of 25/02/19, this fails with an empty BQLException and I don't know why.
    // Possible that there's an SQLException that isn't being delivered properly.
/*
    @Test
    public void testFROM_PAIRWISEOptionals() throws Exception {
        String input = start + //
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF!Year" +
                "-POPULATION=CRIMEDATA" +
                "-ORDER_BY=value" +
                "-LIMIT=10";
        List<String> expectedColumnNames = Arrays.asList("value", "rowid0", "rowid1");
        int expectedNumberOfRows = 10;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }
/*
    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISEInvalidOptionalsError() throws Exception {
        String input = start +
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF!Year" +
                "-EXPNAME=Similarity" +
                "-POPULATION=CRIMEDATA" +
                "-GROUP_BY=Year";

        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }
*/
    @Test (expected = MalformedParametersException.class)
    public void testInvalidModeError() throws Exception {
        String input = start +
                "MODE=fake_mode" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingModeError() throws Exception {
        String input = start +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpressionError() throws Exception {
        String input = start +
                "MODE=FROM" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulationError() throws Exception {
        String input = start +
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() throws Exception {
        String input = start +
                "MODE=FROM" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-fake_news=exp2";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }
}