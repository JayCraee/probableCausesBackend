package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * The FIND_OUTLIERS query can be called by the client via the appropriate methods.
 * This is not based on the primitive queries as documented at: http://probcomp.csail.mit.edu/dev/bayesdb/doc/bql.html,
 * rather, it is based one of the example queries demonstrated in a Python workbook by the team at MIT for finding outliers
 * using the ESTIMATE method.
 * Notebook at: https://github.com/probcomp/notebook/blob/master/tutorials/satellites-predictive.ipynb
 * Query in question at: Input 26.
 *
 * From the front-end, we expect a call of the form: '/estimate/{unparsed}', where unparsed is of format:
 * ...-<field1>=<field1_value>-<field2>=<field2_value>-...
 * The compulsory fields required are: 'POPULATION', 'TARGCOL', 'GIVEN'.
 * and the optional fields are: 'ORDER BY', 'LIMIT'.
 *
 * Note that the ordering of (field, value) pairs is not relevant, and may be specified in any desired order.
 *
 * In the absence of certain optional fields being specified, a reasonable default will be assumed:
 * ORDER BY defaults=> <TARGCOL>
 * LIMIT defaults=> 50.
 *
 * NB: This is an untested class.
 *
 */

public class Find_Outliers extends Query {
    private static final List<String> compulsoryFields =
            Arrays.asList("POPULATION", "TARGCOL", "GIVEN");

    private static final List<String> optionalFields =
            Arrays.asList("ORDER BY", "LIMIT");

    @Override
    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "";
        ss += "ESTIMATE * FROM ";
        ss += super.parsedInputs.get("POPULATION");
        ss += " WHERE ( PREDICTIVE PROBABILITY OF ";
        ss += super.parsedInputs.get("TARGCOL");
        ss += " > PREDICTIVE PROBABILITY OF ";
        ss += super.parsedInputs.get("TARGCOL");

        ss += " GIVEN " + "("+parsedInputs.get("GIVEN")+")";
        ss += " ) ";

        if (super.fields.contains("ORDER BY")) {
            ss += "ORDER BY " + parsedInputs.get("ORDER BY");
        } else {
            ss += "ORDER BY " + parsedInputs.get("TARGCOL");
        }

        if (super.fields.contains("LIMIT")) {
            ss += " LIMIT " + parsedInputs.get("LIMIT");
        } else {
            ss += " LIMIT 50";
        }


        ret.add(ss);

        return ret;
    }

    public Find_Outliers(String unparsed) {
        super(unparsed);
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

    public static void main(String[] args) {
        Find_Outliers targfo = new Find_Outliers("POPULATION=AFRICA_DATA-TARGCOL=Bike_Rides-GIVEN=Car_Rides-ORDER_BY=City_Name-LIMIT=40");
        System.out.println(targfo.getBQL());
    }
}