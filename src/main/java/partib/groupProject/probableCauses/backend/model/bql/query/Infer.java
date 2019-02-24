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
		HashMap<String, String> cleanInputs = new HashMap<>();
		for(String field : super.parsedInputs.keySet()){
			String value = parsedInputs.get(field);
			cleanInputs.put(field, cleanExpression(value));
		}
		
		String res = "INFER ";
		
		if(!modeOptions.contains(cleanInputs.get("MODE")))
			throw new MalformedParametersException("Invalid Mode: " + cleanInputs.get("MODE"));

		boolean expl = cleanInputs.get("MODE").equals("EXPLICIT FROM");


		if(expl){

			res += "EXPLICIT";
			res += " " + cleanInputs.get("COLEXP") + " ";

			if(cleanInputs.containsKey("WITH CONFIDENCE"))
				throw new MalformedParametersException("Mode " + cleanInputs.get("MODE") + " not supported with field 'WITH CONFIDENCE'!");

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

		//sanity check on inputs
		for (String k : compulsoryFields) {
			if (!super.fields.contains(k)) {
				throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
			}
		}
		for (String k : super.fields) {
			if (!compulsoryFields.contains(k) && !optionalFields.contains(k) && !earlyOptionalFields.contains(k)) {
				throw new MalformedParametersException("Error: Query field <"+k+"> not present");
			}
		}
	}

	public static void main(String[] args) {
		Infer targinf = new Infer("EXPRESSION=col1-MODE=EXPLICIT!FROM-POPULATION=hell_data");
		//Infer targinf = new Infer("COLEXP=col1-MODE=FROM-POPULATION=hell_data");
		System.out.println(targinf.getBQL());
	}
}
