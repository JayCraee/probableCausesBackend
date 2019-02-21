package integrationTests;

import java.util.*;
import org.json.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IntegrationTestFramework {
    public String doRequest(String input){  // TODO Replace this with code from Jay
        return "";
    }

    // Given an input and an expected output
    public static void singleTest(String input, List<String> expectedColumns, int expectedRows) throws BQLException, InvalidReturnFormatException{
        JSONArray tables;
        try {
            tables = new JSONArray(input);
        } catch (JSONException e){
            throw new BQLException(input); // This is the case that input is an error message rather than JSON (hopefully)
        }

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