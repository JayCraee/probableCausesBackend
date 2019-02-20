package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Infer extends Query {
    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"MODE", "POPULATION"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"COLNAMES", "EXPRESSION", "WITH CONFIDENCE", "WHERE", "GROUP BY", "ORDER BY", "LIMIT"}));

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
                System.out.println("CASE FROM");
                ss += " " + parsedInputs.get("COLNAMES");
                if (fields.contains("WITH CONFIDENCE")) {
                    ss += " WITH CONFIDENCE " + parsedInputs.get("WITH CONFIDENCE");
                } else {
                    ss += " WITH CONFIDENCE 0.7";
                }
                break;
            }
            case "EXPLICIT FROM": {
                System.out.println("CASE EXPLICIT FROM");
                ss += " EXPLICIT " + parsedInputs.get("EXPRESSION");
                ss += " FROM ";
                break;
            }
        }
        if (fields.contains("WHERE")) {
            // TODO
        }
        if (fields.contains("GROUP BY")) {
            // TODO
        }
        if (fields.contains("ORDER BY")) {
            ss += " ORDER BY " + parsedInputs.get("ORDER BY");
        }
        if (fields.contains("LIMIT")) {
            ss += " LIMIT " + parsedInputs.get("LIMIT");
        } else {
            ss += " LIMIT 50";
        }
        ret.add(ss);
        return ret;
    }

    public Infer(String unparsed) {
        super(unparsed);

        if (super.fields.contains("EXPRESSION")) {
            parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSOIN")));
        }

        //sanity check on inputs
        for (String k : compulsoryFields) {
            if (!super.fields.contains(k)) {
                throw new MalformedParametersException("Error: Missing compulsory field <"+k+">");
            }
        }
        for (String k : super.fields) {
            if (!compulsoryFields.contains(k) && !optionalFields.contains(k)) {
                throw new MalformedParametersException("Error: Query field <"+k+"> not present");
            }
        }

        if (parsedInputs.get("MODE") == "FROM") {
            if (!super.fields.contains("COLNAMES")) {
                throw new MalformedParametersException("Error: COLNAMES field required when MODE=FROM");
            }
        }
        if (parsedInputs.get("MODE") == "EXPLICIT FROM") {
            if (!super.fields.contains("EXPRESSION")) {
                throw new MalformedParametersException("Error: EXPRESSION field required when MODE=EXPLICIT_FROM");
            }
        }
    }

    public static void main(String[] args) {
        Infer targinf = new Infer("COLNAMES=col1-MODE=FROM-POPULATION=hell_data");
        //Infer targinf = new Infer("COLNAMES=col1-MODE=FROM-POPULATION=hell_data");
        System.out.println(targinf.getBQL());
    }
}