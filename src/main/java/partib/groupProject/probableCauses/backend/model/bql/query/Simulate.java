package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulate extends Query {
    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"COLNAMES", "POPULATION"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"GIVEN", "LIMIT1", "LIMIT2"}));

    public Simulate(String unparsed) {
        super(unparsed);
        //sanity check on inputs
        for (String k : compulsoryFields) {
            if (!super.fields.contains(k)) {
                throw new MalformedParametersException();
            }
        }
        for (String k : super.fields) {
            if (!compulsoryFields.contains(k) && !optionalFields.contains(k)) {
                throw new MalformedParametersException();
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


    public static void main(String[] args) {
        Simulate targsim = new Simulate("COLNAMES=col1,col2-POPULATION=AFRICA_ACCIDENT-LIMIT1=4000-LIMIT2=40-GIVEN=cont");
        System.out.println(targsim.getBQL());
    }
}
