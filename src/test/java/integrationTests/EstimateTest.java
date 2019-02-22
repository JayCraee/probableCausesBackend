package integrationTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

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

    private String start = "/bql/query/estimate";

    @Test
    public void testBY() throws Exception, BQLException, InvalidReturnFormatException{
        String uri =
                start +
                "/MODE=BY" +
                "-EXPRESSION=col1" +
                "-EXPNAME=column1" +
                "-POPULATION=pop";
        List<String> expectedColumnNames = Arrays.asList("col1");
        int expectedNumberOfRows = 50;

        IntegrationTestFramework.singleTest(uri, expectedColumnNames, expectedNumberOfRows, mockMvc);
    }
}