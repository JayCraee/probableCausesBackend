package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Select extends Query {
	private static final List<String> compulsoryFields =
			Arrays.asList("COLNAMES", "TABLE");
	
	private static final List<String> optionalFields =
			Arrays.asList("WHERE", "GROUP BY", "ORDER BY", "LIMIT");
	
	public Select(String unparsed) throws MalformedParametersException{
		super(unparsed);

		//sanity check on inputs
		for (String k : compulsoryFields) {
			if (!super.fields.contains(k)) {
				throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
			}
		}
		for (String k : super.fields) {
			if (!compulsoryFields.contains(k) && !optionalFields.contains(k)) {
				throw new MalformedParametersException("Error: Query field <"+k+"> not present");
			}
		}
	}	
	public List<String> getBQL() {
		HashMap<String, String> cleanInputs = new HashMap();
		for(String field : super.parsedInputs.keySet()){
			boolean compulsory = compulsoryFields.contains(field);
			boolean   optional =   optionalFields.contains(field);
			if(!(compulsory || optional)) throw new MalformedParametersException("Invalid field detected: " + field);

			String value = parsedInputs.get(field);
			cleanInputs.put(field, cleanExpression(value));

		}

		if(!(cleanInputs.keySet().contains("LIMIT")))
			cleanInputs.put("LIMIT", "50");

		String res = "SELECT ";
		res += cleanInputs.get("COLNAMES");
		res += " FROM " + cleanInputs.get("TABLE");
		
		for(String field : optionalFields){
			if(cleanInputs.keySet().contains(field)){
				res += " " + field + " " + cleanInputs.get(field);
			}
		}

		return Arrays.asList(res);
	}
}
