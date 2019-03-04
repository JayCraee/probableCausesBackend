package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

/**
 * The INFER query can be called by the client via the appropriate methods.
 * INFER is for imputation of data into null fields, predicated on a user-defined confidence threshold.
 *
 * The format, according to the documentation at: http://probcomp.csail.mit.edu/dev/bayesdb/doc/bql.html,
 * is as follows:
 *
 * 1.
 * INFER <colnames> [WITH CONFIDENCE <conf>] FROM <population> [MODELED BY <g>] [USING [MODEL <num>]
 * 		[MODELS <num0>-<num1>]] [WHERE <condition>] [GROUP BY <grouping>] [ORDER BY <ordering>] [LIMIT <limit>]
 *
 * 2.
 * INFER EXPLICIT <expression> FROM <population> [MODELED BY <g>] [USING [MODEL <num>] [MODELS <num0>-<num1>]]
 * 		[WHERE <condition>] [GROUP BY <grouping>] [ORDER BY <ordering>] [LIMIT <limit>]
 *
 * However, from the front-end, we expect a call of the form: '/infer/{unparsed}', where unparsed is of format:
 * ...-<field1>=<field1_value>-<field2>=<field2_value>-...
 * The compulsory fields required are: 'MODE', 'POPULATION', 'COLEXP'
 * and the optional fields are: 'WITH CONFIDENCE', 'WHERE', 'GROUP BY', 'ORDER BY', 'LIMIT'.
 *
 * An example call might take the form:
 * /bql/query/infer/MODE=EXPLICIT!FROM-POPULATION=pop-COLEXP=PREDICT!col2!AS!two!CONFIDENCE!conf2
 *
 * Note that the ordering of (field, value) pairs is not relevant, and may be specified in any desired order.
 *
 * In the absence of certain optional fields being specified, a reasonable default will be assumed:
 * 1. WITH CONFIDENCE defaults=> 0.7
 * 2. LIMIT defaults=> 50
 *
 */

public class Infer extends Query {
	private static final List<String> compulsoryFields = Arrays.asList("MODE", "POPULATION", "COLEXP");
	
	private static final List<String> earlyOptionalFields = Arrays.asList("WITH CONFIDENCE");

	private static final List<String> optionalFields = Arrays.asList("WHERE", "GROUP BY", "ORDER BY", "LIMIT");
	
	private static final List<String> modeOptions = Arrays.asList("FROM", "EXPLICIT FROM");
	
	@Override
	public List<String> getBQL() {
		// Clean inputs
		HashMap<String, String> cleanInputs = new HashMap<>();
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
		// Check for missing compulsory fields
		for (String k : compulsoryFields) {
			if (!super.fields.contains(k)) {
				throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
			}
		}
		// Check for invalid fields in the call from the front-end.
		for (String k : super.fields) {
			if (!compulsoryFields.contains(k) && !optionalFields.contains(k) && !earlyOptionalFields.contains(k)) {
				throw new MalformedParametersException("Error: Query field <"+k+"> not present");
			}
		}
	}
}
