package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // TODO what does this bit check for exactly? Please add a comment
        // dks28: This should check for invalid fields -- it works for Infer and Select. Not sure why it doesn't in this case.
        for (String k : super.fields) {
            if (!compulsoryFields.contains(k) && !optionalFields.contains(k)) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n CAUGHT BAD FIELD IN CONSTRUCTOR \n\n\n\n\n\n\n\n\n");
                throw new MalformedParametersException("Error: Query field <"+k+"> not present");
            }
        }
    }

    @Override
    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "SELECT ";
        String combinedColNames = String.join("||\"--\"||", parsedInputs.get("COLNAMES").split(","));
        String newFieldName = String.join("_", parsedInputs.get("COLNAMES").split(",")); //TODO read: dks28 -- Changed Delimiter to underscore because dash is a syntax error.
        ss += combinedColNames;
        ss += " AS " + newFieldName + ", COUNT(" + newFieldName + ") AS frequency FROM (";              //TODO read: dks28 -- Added Comma. How did this get through unit tests?
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
