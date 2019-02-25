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
        String newFieldName = String.join("-", parsedInputs.get("COLNAMES").split(","));
        ss += combinedColNames;
        ss += " AS " + newFieldName + " COUNT(" + newFieldName + ") AS frequency FROM (";
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
        ret.add(ss);
        return ret;
    }
}