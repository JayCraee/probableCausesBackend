package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // dks28: This should check for invalid fields -- it works for Infer and Select. Not sure why it doesn't in this case.
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