package partib.groupProject.probableCauses.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;

import static partib.groupProject.probableCauses.backend.controller.ServerConnector.singleQueryCaller;

@RestController
@RequestMapping("/util")
public class UtilController {
    @GetMapping("/tableNames")
    public static String getTableName() throws InvalidCallException {
        return singleQueryCaller("foo.bdb", "SELECT * FROM sys.tables");
    }

    @GetMapping("/columnNames/{tableName}")
    public static String getColumnNames(@PathVariable String tableName) throws InvalidCallException {
        String row = singleQueryCaller("foo.bdb", "SELECT * FROM " + tableName + " ORDER BY RAND() LIMIT 1");
        JsonReader jsonReader = Json.createReader(new StringReader(row));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        String columnNames = "";
        for(Object key : jsonObject.keySet()) columnNames += key + ", ";
        return columnNames.length() == 0 ? "" : columnNames.substring(0,columnNames.length()-2);
    }

    public static String getDimensions(String stringJson) {

        return null;
    }

}
