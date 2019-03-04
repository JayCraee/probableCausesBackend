package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The SIMULATE query can be called by the client via the appropriate method.
 * SIMULATE is for generating more data based on the model learned by BayesDB based on the data at hand.
 *
 * The format, according to the documentation at: http://probcomp.csail.mit.edu/dev/bayesdb/doc/bql.html,
 * is as follows:
 *
 * SIMULATE <colnames> FROM <population> [MODELED BY <g>] [USING [MODEL <num>] [MODELS <num0>-<num1>]]
 *        [GIVEN <constraints>] [LIMIT <limit>]
 *
 * However, from the front-end, we expect a call of the form: '/estimate/{unparsed}', where unparsed is of format:
 * ...-<field1>=<field1_value>-<field2>=<field2_value>-...
 * The compulsory fields required are: 'COLNAMES', 'POPULATION',
 * and the optional fields are: 'GIVEN', 'LIMIT1', 'LIMIT2'.
 *
 * An example call might take the form:
 * /bql/query/simulate/COLNAMES=col1,col2-POPULATION=pop-GIVEN=exp1-LIMIT1=10000-LIMIT2=100
 *
 * Note that the ordering of (field, value) pairs is not relevant, and may be specified in any desired order.
 *
 * In the absence of certain optional fields being specified, a reasonable default will be assumed:
 * LIMIT1 defaults=> 5000
 * LIMIT2 defaults=> 50
 *
 */

public class Simulate extends Query {
    private static final List<String> compulsoryFields =
            Arrays.asList("COLNAMES", "POPULATION");

    private static final List<String> optionalFields =
            Arrays.asList("GIVEN", "LIMIT1", "LIMIT2");

    public Simulate(String unparsed) {
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

    @Override
    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "SELECT ";
        String combinedColNames = String.join("||\"--\"||", parsedInputs.get("COLNAMES").split(","));
        String newFieldName = String.join("_", parsedInputs.get("COLNAMES").split(","));
        ss += combinedColNames;
        ss += " AS " + newFieldName + ", COUNT(" + newFieldName + ") AS frequency FROM (";
        ss += " SIMULATE " + parsedInputs.get("COLNAMES");
        ss += " FROM " + parsedInputs.get("POPULATION");
        if (super.fields.contains("GIVEN")) {
            ss += " GIVEN " + parsedInputs.get("GIVEN");
        }
        if (super.fields.contains("LIMIT1")) {
            ss += " LIMIT " + parsedInputs.get("LIMIT1");
        } else {
            ss += " LIMIT 5000";
        }
        ss += " ) GROUP BY " + newFieldName;
        ss += " ORDER BY frequency DESC";
        if (super.fields.contains("LIMIT2")) {
            ss += " LIMIT " + parsedInputs.get("LIMIT2");
        } else {
            ss += " LIMIT 50";
        }
        System.out.println(ss);
        ret.add(ss);
        return ret;
    }
}
