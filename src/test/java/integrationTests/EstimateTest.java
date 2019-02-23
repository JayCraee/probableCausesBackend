package integrationTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.MalformedParametersException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
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
                "-EXPNAME=Correlation" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("Correlation");
        int expectedNumberOfRows = 1;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError1() throws Exception {
        String input =
                "MODE=BY" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-WHERE=cond";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError2() throws Exception {
        String input =
                "MODE=BY" +
                "-EXPRESSION=exp1" +
                "-EXPNAME=col" +
                "-POPULATION=pop" +
                "-GROUP_BY=exp2";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testBYWithInvalidOptionalsError3() throws Exception {
        String input =
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
                "-EXPRESSION=CORRELATION!OF!Id!WITH!Year" +
                "-EXPNAME=correlation" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("correlation");
        int expectedNumberOfRows = 1;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);


    }

    @Test
    public void testFROMOptionals() throws Exception {
        String uri =
                start +
                "/MODE=FROM" +
                "-EXPRESSION=ID,CaseNumber,Year" +
                "-EXPNAME=col" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=Id<1000" +
                "-GROUP_BY=Year" +
                "-ORDER_BY=Year,ID" +
                "-LIMIT=100";
        List<String> expectedColumnNames = Arrays.asList("ID", "CaseNumber", "Year");
        int expectedNumberOfRows = 100;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_VARIABLES_OF() throws Exception {
        String input =
                start +
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=CORRELATION" +
                "-EXPNAME=Correlation" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("Correlation", "ID", "Beat", "District", "Year", "Arrest", "Domestic", "CommunityArea", "Ward");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_VARIABLES_OFOptionals() throws Exception {
        String input =
                start +
                "MODE=FROM_VARIABLES_OF" +
                "-EXPRESSION=PREDICTIVE!PROBABILITY!OF!Year" +
                "-EXPNAME=predictiveProbability" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=CaseNumber=1000" +
                "-GROUP_BY=Year" +
                "-ORDER_BY=Arrest" +
                "-LIMIT=100";
        List<String> expectedColumnNames = Arrays.asList("predictiveProbability", "ID", "Beat", "District", "Year", "Arrest", "Domestic", "CommunityArea", "Ward");
        int expectedNumberOfRows = 100;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OF() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=MUTUAL!INFORMATION" +
                "-EXPNAME=mutinf" +
                "-POPULATION=CRIMEDATA";
        List<String> expectedColumnNames = Arrays.asList("ID", "Beat", "District", "Year", "Arrest", "Domestic", "CommunityArea", "Ward");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_PAIRWISE_VARIABLES_OFOptionals() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=MUTUAL!INFORMATION" +
                "-EXPNAME=mutinf" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=Year=2000" +
                "-ORDER_BY=mutinf" +
                "-LIMIT=100";
        List<String> expectedColumnNames = Arrays.asList("ID", "Beat", "District", "Year", "Arrest", "Domestic", "CommunityArea", "Ward", "mutinf");
        int expectedNumberOfRows = 100;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISE_VARIABLES_OFInvalidOptionalsError() throws Exception {
        String input =
                start +
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                "-EXPRESSION=MUTUAL!INFORMATION" +
                "-EXPNAME=mutinf" +
                "-POPULATION=CRIMEDATA" +
                "-GROUP_BY=Year";

        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test
    public void testFROM_PAIRWISE() throws Exception {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF" +
                "-EXPNAME=Similarity" +
                "-POPULATION=CRIMEDATA";

        List<String> expectedColumnNames = Arrays.asList("ID", "Similarity");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test
    public void testFROM_PAIRWISEOptionals() throws Exception {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF!Year" +
                "-EXPNAME=Similarity" +
                "-POPULATION=CRIMEDATA" +
                "-WHERE=Id<1000" +
                "-ORDER_BY=ID" +
                "-LIMIT=100";

        List<String> expectedColumnNames = Arrays.asList("ID", "Similarity");
        int expectedNumberOfRows = 100;

        IntegrationTestFramework.singleTest(input, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }

    @Test (expected = MalformedParametersException.class)
    public void testFROM_PAIRWISEInvalidOptionalsError() throws Exception {
        String input =
                "MODE=FROM_PAIRWISE" +
                "-EXPRESSION=SIMILARITY!IN!THE!CONTEXT!OF!Year" +
                "-EXPNAME=Similarity" +
                "-POPULATION=CRIMEDATA" +
                "-GROUP_BY=Year";

        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidModeError() throws Exception {
        String input =
                "MODE=fake_mode" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingModeError() throws Exception {
        String input =
                "-EXPRESSION=exp" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpressionError() throws Exception {
        String input =
                "MODE=FROM" +
                "-EXPNAME=col" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingExpNameError() throws Exception {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-POPULATION=pop";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testMissingPopulationError() throws Exception {
        String input =
                "MODE=FROM" +
                "-EXPRESSION=exp" +
                "-EXPNAME=col";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }

    @Test (expected = MalformedParametersException.class)
    public void testInvalidFieldNameError() throws Exception {
        String input =
                "MODE=FROM" +
                        "-EXPRESSION=exp1" +
                        "-EXPNAME=col" +
                        "-POPULATION=pop" +
                        "-fake_news=exp2";
        IntegrationTestFramework.singleTest(input, null, null, mockMvc, true);
    }
}