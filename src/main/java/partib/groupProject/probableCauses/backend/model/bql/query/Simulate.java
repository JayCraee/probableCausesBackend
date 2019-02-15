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
                    new String[]{"GIVEN", "LIMIT"}));

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
        String ss = "";

        ss += "SIMULATE " + super.parsedInputs.get("COLNAMES");
        ss += " FROM " + super.parsedInputs.get("POPULATION");

        if (super.fields.contains("GIVEN")) {
            ss += " GIVEN " + parsedInputs.get("GIVEN");
        }
        if (super.fields.contains("LIMIT")) {
            ss += " LIMIT " + parsedInputs.get("LIMIT");
        }


        ret.add(ss);
        return ret;
    }


    public static void main(String[] args) {
        Simulate targsim = new Simulate("COLNAMES=col;POPULATION=AFRICA_ACCIDENT;LIMIT=50;GIVEN=cont");
        System.out.println(targsim.getBQL());
    }
}
