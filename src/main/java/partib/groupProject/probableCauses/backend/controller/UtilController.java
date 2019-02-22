package partib.groupProject.probableCauses.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.*;

import java.io.StringReader;
import java.util.ArrayList;

import static partib.groupProject.probableCauses.backend.controller.ServerConnector.singleQueryCaller;

@RestController
@RequestMapping("/util")
public class UtilController {
    @GetMapping("/tableNames")
    public static String getTableName() throws InvalidCallException {
        return singleQueryCaller("foo.bdb", "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
    }

    @GetMapping("/columnNames/{tableName}")
    public static String getColumnNames(@PathVariable String tableName) throws InvalidCallException {
        String row = singleQueryCaller("foo.bdb", "SELECT * FROM " + tableName + " LIMIT 1");
        JsonReader jsonReader = Json.createReader(new StringReader(row));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();

        ArrayList<String> columnList = new ArrayList<>();
        for(Object key : jsonArray.getJsonArray(0).getJsonObject(0).keySet()) {
            columnList.add((String) key);
        }

        String statTypes = singleQueryCaller("foo.bdb", "GUESS SCHEMA FOR " + tableName);
        jsonReader = Json.createReader(new StringReader(statTypes));
        jsonArray = jsonReader.readArray();
        jsonReader.close();

        ArrayList<String> statList = new ArrayList<>();
        for(int i = 0; i < jsonArray.getJsonArray(0).size(); i++) {
            statList.add(jsonArray.getJsonArray(0).getJsonObject(i).get("stattype").toString());
        }

        String json = "[";
        for(int i = 0; i < columnList.size(); i++) {
            json += "{\"columnName\": \"" + columnList.get(i) + "\", \"stattype\": \"" + statList.get(i) + "\"}";
            if (i+1 < columnList.size()) {
                json += ", ";
            }
        }

        return json + "]";
    }

    // for testing purposes
    @GetMapping("/anyQuery/{query}")
    public static String runAnyQuery(@PathVariable String query) throws InvalidCallException {
        return singleQueryCaller("foo.bdb", query);
    }

    public static String getDimensions(String stringJson) {

        return null;
    }

}