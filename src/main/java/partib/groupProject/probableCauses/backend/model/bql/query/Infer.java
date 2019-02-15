package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Infer extends Query {
    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"COLNAMES", "MODE", "POPULATION"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"WITH CONFIDENCE", "WHERE", "GROUP BY", "ORDER BY", "LIMIT"}));

    private static final ArrayList<String> modeOptions =
            new ArrayList<>(Arrays.asList(
                    new String[]{"FROM", "EXPLICIT FROM"}));

    @Override
    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "";

        ss += "INFER";
        switch (super.parsedInputs.get("MODE")) {
            case "FROM": {
                ss += " " + parsedInputs.get("COLNAMES");
            }
            case "EXPLICIT FROM": {

            }
        }



        return null;
    }


    public Infer(String unparsed) {
        super(unparsed);

        if (super.fields.contains("EXPRESSION")) {
            parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSOIN")));
        }

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


    public static void main(String[] args) {
        Infer targinf = new Infer("COLNAMES=col1;MODE=FROM;POPULATION=hell_data");
        System.out.println(targinf.getBQL());

    }


}
