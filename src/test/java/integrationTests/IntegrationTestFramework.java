package integrationTests;

import java.util.*;
import org.json.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
public class IntegrationTestFramework {
    private static String doRequest(String uri, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(uri)).andReturn().getResponse().getContentAsString();
    }

    public static void singleTest(String input, List<String> expectedColumns, Integer expectedRows, MockMvc mockMvc) throws Exception {
        singleTest(input, expectedColumns, expectedRows, mockMvc, false);
    }

    // Given an input and an expected output
    public static void singleTest(String input, List<String> expectedColumns, Integer expectedRows, MockMvc mockMvc, boolean expectFailure) throws Exception {
        String result;
        try {
            result = doRequest(input, mockMvc);
        } catch (NestedServletException e) { //Catch and rethrow errors in the backend (hopefully mostly MalformedParametersException)
            throw (Exception)e.getCause();
        }

        JSONArray tables;
        try {
            tables = new JSONArray(result);
        } catch (JSONException e){
            throw new BQLException(result); // Case that result is an error message rather than JSON (hopefully)
        }

        if (expectFailure)
                return;

        assertEquals(1, tables.length(), "Incorrect number of tables returned: ");

        JSONArray table;
        try {
            table = tables.getJSONArray(0);
        } catch (JSONException e){
            throw new InvalidReturnFormatException("Didn't get back an array of tables");
        }

        assertEquals(expectedRows, table.length(), "Table has incorrect number of rows: ");

        JSONObject row;
        for(int i=0; i<table.length(); i++){
            try {
                row = table.getJSONObject(i);
            } catch (JSONException e) {
                throw new InvalidReturnFormatException("Can't read "+i+"th row");
            }

            Iterator<String> keys = row.keys();
            Set<String> columnsInRow = new HashSet<>();
            while (keys.hasNext()){
                columnsInRow.add(keys.next());
            }

            assertEquals(new HashSet(expectedColumns), columnsInRow, "Row "+i+" does not have the correct columns");
        }
    }
}