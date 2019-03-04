package partib.groupProject.probableCauses.backend.controller;

import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static partib.groupProject.probableCauses.backend.controller.ServerConnector.singleQueryCaller;

public class EstimateOutputCleaner {


	public static String process(String data, Estimate query) throws Exception {

		String columns;
		// Grab the correlation between any two columns
		String row = singleQueryCaller(QueryController.db, "xxxGET POPULATION COLUMNS");
		ArrayList<String> columnList = new ArrayList<>(Arrays.asList(row.split(",")));

		switch(query.type){

			case CORRELATION:
				switch (query.getMode()) {

					case "BY":
						String[] cols = query.getCols().split(",");
						String corr = data.substring(3, data.length() - 3).split(":")[1];
						return "[[{\"corr\":" + corr + ", \"name0\":\"" + cols[0] + "\", \"name1\":\"" + cols[1] + "\"}]]";
					case "FROM VARIABLES OF":
						JsonReader jsonReader = Json.createReader(new StringReader(data));
						JsonArray jsonArray = jsonReader.readArray();
						jsonReader.close();
						ArrayList<String> correlations = new ArrayList();
						for (int i = 0; i < jsonArray.getJsonArray(0).size(); i++) {
							correlations.add(jsonArray.getJsonArray(0).getJsonObject(i).get("corr").toString());
						}
						// Construct and return json output
						String json = "[[";
						for(int i = 0; i < columnList.size(); i++) {
							json += "{\"corr\":"+correlations.get(i)+", \"name0\":\"" + query.getCols().toLowerCase() + "\", \"name1\":\"" + columnList.get(i) + "\"}";
							if (i+1 < columnList.size()) {
								json += ", ";
							}
						}
						json += "]]";
						return json;
					case "FROM PAIRWISE VARIABLES OF":
						return data;
				}
				break;
			default: return data;

			case SIMILARITY:
				switch(query.getMode()){

					case "BY":
						String[] rows = query.getCols().split(",");
						String value = data.substring(3, data.length() - 3).split(":")[1];
						return "[[{\"value\":" + value + ", \"rowid0\":\"" + rows[0] + "\", \"rowid1\":\"" + rows[1] + "\"}]]";
					case "FROM":

				}
				break;
		}

		return data;
	}
}
