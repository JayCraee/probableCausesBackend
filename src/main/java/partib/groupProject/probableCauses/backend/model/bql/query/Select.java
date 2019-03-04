package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The SELECT query can be called by the client via the appropriate method.
 * SELECT corresponds with the typical usage of SELECT as in SQL.
 *
 * 1.
 * SELECT <columns>
 *
 * 2.
 * SELECT [DISTINCT|ALL] <columns> FROM <table> [WHERE <condition>] [GROUP BY <grouping>]
 * 		[ORDER BY <ordering>] [LIMIT <limit>]
 *
 * However, from the front-end, we expect a call of the form: '/select/{unparsed}', where unparsed is of format:
 * ...-<field1>=<field1_value>-<field2>=<field2_value>-...
 * The compulsory fields required are: 'COLNAMES', 'TABLE',
 * and the optional fields are: 'WHERE', 'GROUP BY', 'ORDER BY', 'LIMIT'.
 *
 * An example call might take the form:
 * bql/query/estimate/MODE=BY-EXPRESSION=CORRELATION!OF!x!WITH!y-POPULATION=pop
 *
 * Note that the ordering of (field, value) pairs is not relevant, and may be specified in any desired order.
 *
 * In the absence of certain optional fields being specified, a reasonable default will be assumed:
 * LIMIT defaults=> 50.
 *
 */

public class Select extends Query {
	private static final List<String> compulsoryFields =
			Arrays.asList("COLNAMES", "TABLE");
	
	private static final List<String> optionalFields =
			Arrays.asList("WHERE", "GROUP BY", "ORDER BY", "LIMIT");
	
	public Select(String unparsed) throws MalformedParametersException{
		super(unparsed);

		// Check for missing compulsory fields
		for (String k : compulsoryFields) {
			if (!super.fields.contains(k)) {
				throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
			}
		}
		// Check for invalid fields in the call from the front-end.
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

		return Arrays.asList(res + ";");
	}
}
