package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.*;


enum EstimateType{
    CORRELATION, SIMILARITY
}

public class Estimate extends Query {

    public EstimateType type;

    private static final List<String> modeOptions =
            Arrays.asList("BY", "FROM", "FROM VARIABLES OF", "FROM PAIRWISE VARIABLES OF", "FROM PAIRWISE");

    private static final List<String> compulsoryFields =
            Arrays.asList("MODE", "EXPRESSION",  "POPULATION");

    private static final List<String> optionalFields =
            Arrays.asList("EXPNAME", "WHERE", "GROUP BY", "ORDER BY", "LIMIT");

    private static final List<String> innerFields =
            Arrays.asList("WHERE", "GROUP BY");

    public Estimate(String unparsed) throws MalformedParametersException {
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
                throw new MalformedParametersException("Error: Query field <"+k+"> not present");
            }
        }
        // Check MODE is valid
        parsedInputs.put("MODE", parsedInputs.get("MODE").replace("_", " "));
        if (!modeOptions.contains(super.parsedInputs.get("MODE"))) {
            throw new MalformedParametersException("Error: Invalid mode supplied");
        }
        // Clean up expressions
        parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSION")));
        if(parsedInputs.get("EXPRESSION").contains("CORRELATION")) type = EstimateType.CORRELATION;
        else if(parsedInputs.get("EXPRESSION").contains("SIMILARITY")) type = EstimateType.SIMILARITY;
        else throw new MalformedParametersException("Cannot estimate " + parsedInputs.get("EXPRESSION"));

        //Check that we don't have superfluous fields given the MODE option.
        List<String> disallowedFields;
        switch (super.parsedInputs.get("MODE")) {
            case "BY":
                disallowedFields = Arrays.asList("WHERE", "GROUP BY", "ORDER BY");
                break;
            case "FROM PAIRWISE":
                disallowedFields = Arrays.asList("GROUP BY");
                break;
            case "FROM PAIRWISE VARIABLES OF":
                disallowedFields = Arrays.asList("GROUP BY");
                break;
            default:
                disallowedFields = new ArrayList<>();
        }
        if(type == EstimateType.SIMILARITY) disallowedFields.add("EXPNAME");
        for (String k : disallowedFields) {
            if (super.fields.contains(k)) {
                throw new MalformedParametersException("Error: field <"+k+"> is not allowed with the <"+super.parsedInputs.get("MODE")+"> mode and type " + type);
            }
        }
    }

    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();
        String ss = "";

        switch (super.parsedInputs.get("MODE")) {
            case "BY": {
                ss += "SELECT * FROM ( ";
                ss += "ESTIMATE";
                ss += " " + parsedInputs.get("EXPRESSION");
                ss += (parsedInputs.containsKey("EXPNAME")) ? " AS " + parsedInputs.get("EXPNAME") : "";
                ss += " " + parsedInputs.get("MODE");
                ss += " " + parsedInputs.get("POPULATION");
                ss += " ) ";

                if (super.fields.contains("LIMIT")) {
                    ss += "LIMIT " + parsedInputs.get("LIMIT");
                } else {
                    ss += "LIMIT 1000000";
                }
                break;
            }
            case "FROM": {
                //[DISTINCT|ALL]?
            }
            default: {
                ss += "SELECT * FROM ( ESTIMATE";
                ss += " " + parsedInputs.get("EXPRESSION");
                ss += (parsedInputs.containsKey("EXPNAME")) ? " AS " + parsedInputs.get("EXPNAME") : "";
                ss += " " + parsedInputs.get("MODE");
                ss += " " + parsedInputs.get("POPULATION");
                for (String opt : innerFields) {
                    if (fields.contains(opt)) {
                        ss += " " + opt.replace("_", " ") + " " + parsedInputs.get(opt);
                    }
                }

                ss += ")";

                if (super.fields.contains("ORDER BY")) {
                    ss += " ORDER BY " + super.parsedInputs.get("ORDER BY");
                } else {
                    ss += " ORDER BY " + super.parsedInputs.get("EXPNAME");
                }
                if (super.fields.contains("LIMIT")) {
                    ss += " LIMIT " + super.parsedInputs.get("LIMIT");
                } else {
                    ss += " LIMIT 100000";
                }
            }
        }
        ret.add(ss);
        System.out.println(ss);
        return ret;
    }

    public String getMode(){
        return parsedInputs.get("MODE");
    }

    public String getPopulation(){
        return parsedInputs.get("POPULATION");
    }
}