package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.*;


public class Estimate extends Query {
    private final Map<String, String> metaData;

    private static final ArrayList<String> modeOptions =
            new ArrayList<>(Arrays.asList(
                    new String[]{"BY","FROM", "FROM VARIABLES OF", "FROM PAIRWISE VARIABLES OF", "FROM PAIRWISE"}));

    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"MODE", "EXPRESSION", "EXPNAME", "POPULATION"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"WHERE", "GROUP BY", "ORDER BY", "LIMIT"}));

    private static final ArrayList<String> innerFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"WHERE", "GROUP BY"}));

    public Estimate(String unparsed) throws MalformedParametersException {
        super(unparsed);
        metaData = new HashMap<>();

        //clean up expressions
        parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSION")));

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
        parsedInputs.put("MODE", parsedInputs.get("MODE").replace("_", " "));
        if (!modeOptions.contains(super.parsedInputs.get("MODE"))) {
            throw new MalformedParametersException("Error: Mode not supplied");
        }
    }

    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "";

        switch (super.parsedInputs.get("MODE")) {
            case "BY": {
                ss += "ESTIMATE";
                ss += " " + parsedInputs.get("EXPRESSION");
                ss += " " + parsedInputs.get("MODE");
                ss += " " + parsedInputs.get("POPULATION");
                break;
            }
            case "FROM": {
                //[DISTINCT|ALL]?
            }
            default: {
                ss += "SELECT * FROM ( ESTIMATE";
                ss += " " + parsedInputs.get("EXPRESSION");
                ss += " " + parsedInputs.get("MODE");
                ss += " " + parsedInputs.get("POPULATION");
                for (String opt : innerFields) {
                    if (fields.contains(opt)) {
                        ss += " " + opt.replace("_", " ") + " " + parsedInputs.get(opt);
                    }
                }
                ss += " AS " + parsedInputs.get("EXPNAME");

                ss += ")";

                if (super.fields.contains("ORDER_BY")) {
                    ss += " ORDER BY " + super.parsedInputs.get("ORDER BY");
                } else {
                    ss += " ORDER BY " + super.parsedInputs.get("EXPNAME");
                }
                if (super.fields.contains("LIMIT")) {
                    ss += " LIMIT " + super.parsedInputs.get("LIMIT");
                } else {
                    ss += " LIMIT 50";
                }
            }
        }
        ret.add(ss);
        return ret;
    }
}