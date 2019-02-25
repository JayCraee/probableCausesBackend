package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class Infer extends Query {
	private static final List<String> compulsoryFields = Arrays.asList("MODE", "POPULATION", "COLEXP");
	
	private static final List<String> earlyOptionalFields = Arrays.asList("WITH CONFIDENCE");

	private static final List<String> optionalFields = Arrays.asList("WHERE", "GROUP BY", "ORDER BY", "LIMIT");
	
	private static final List<String> modeOptions = Arrays.asList("FROM", "EXPLICIT FROM");
	
	@Override
	public List<String> getBQL() {
		// Clean inputs
		HashMap<String, String> cleanInputs = new HashMap<>(); // TODO currently broken- not stripping out _s, so string comparisons fail later
		for(String field : super.parsedInputs.keySet()){
			String value = parsedInputs.get(field);
			cleanInputs.put(field, cleanExpression(value));
		}
		// Check MODE is valid
		if(!modeOptions.contains(cleanInputs.get("MODE")))
			throw new MalformedParametersException("Invalid Mode: " + cleanInputs.get("MODE"));

		String res = "INFER ";
		if(cleanInputs.get("MODE").equals("EXPLICIT FROM")){
			res += "EXPLICIT";
			res += " " + cleanInputs.get("COLEXP") + " ";
			// Check haven't put WITH CONFIDENCE when it's not allowed
			if(cleanInputs.containsKey("WITH CONFIDENCE")) {
				throw new MalformedParametersException("Mode " + cleanInputs.get("MODE") + " not supported with field 'WITH CONFIDENCE'!");
			}
		} else {
			res += " " + cleanInputs.get("COLEXP") + " ";
			if(!cleanInputs.keySet().contains("WITH CONFIDENCE"))
				cleanInputs.put("WITH CONFIDENCE", "0.7");
			res += "WITH CONFIDENCE " + cleanInputs.get("WITH CONFIDENCE");
		}

		res += " FROM " + cleanInputs.get("POPULATION");

		if(!cleanInputs.keySet().contains("LIMIT"))
			cleanInputs.put("LIMIT", "50");

		for(String field : optionalFields){
			if(cleanInputs.containsKey(field)){
				res += " " + field + " " + cleanInputs.get(field);
			}
		}

		return Arrays.asList(res + ";");
	}
	
	public Infer(String unparsed) {
		super(unparsed);
		// Check for missing compulsory fields TODO Consider putting this in a function of Query
		for (String k : compulsoryFields) {
			if (!super.fields.contains(k)) {
				throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
			}
		}
		// TODO what does this bit check for exactly? Please add a comment
		for (String k : super.fields) {
			if (!compulsoryFields.contains(k) && !optionalFields.contains(k) && !earlyOptionalFields.contains(k)) {
				throw new MalformedParametersException("Error: Query field <"+k+"> not present");
			}
		}
	}
}
