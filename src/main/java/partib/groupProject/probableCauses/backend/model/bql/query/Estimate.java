package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.*;


public class Estimate extends Query {

    private static final List<String> modeOptions =
            Arrays.asList("BY", "FROM", "FROM VARIABLES OF", "FROM PAIRWISE VARIABLES OF", "FROM PAIRWISE");

    private static final List<String> compulsoryFields =
            Arrays.asList("MODE", "EXPRESSION", "EXPNAME", "POPULATION");

    private static final List<String> optionalFields =
            Arrays.asList("WHERE", "GROUP BY", "ORDER BY", "LIMIT");

    private static final List<String> innerFields =
            Arrays.asList("WHERE", "GROUP BY");

    public Estimate(String unparsed) throws MalformedParametersException {
        super(unparsed);


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

        //clean up expressions
        parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSION")));

        parsedInputs.put("MODE", parsedInputs.get("MODE").replace("_", " "));
        if (!modeOptions.contains(super.parsedInputs.get("MODE"))) {
            throw new MalformedParametersException("Error: Mode not supplied");
        }

        //Check that we don't have superfluous fields given the MODE option.
        //Construct permittedFields

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

        for (String k : disallowedFields) {
            if (super.fields.contains(k)) {
                throw new MalformedParametersException("Error: field <"+k+"> is not allowed with the <"+super.parsedInputs.get("MODE")+"> mode");
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
                ss += " " + parsedInputs.get("MODE");
                ss += " " + parsedInputs.get("POPULATION");
                ss += " ) ";

                if (super.fields.contains("LIMIT")) {
                    ss += "LIMIT " + parsedInputs.get("LIMIT");
                } else {
                    ss += "LIMIT 50";
                }
                break;
            }
            case "FROM": {
                //[DISTINCT|ALL]?
            }
            default: {
                ss += "SELECT * FROM ( ESTIMATE";
                ss += " " + parsedInputs.get("EXPRESSION");
                ss += " AS " + parsedInputs.get("EXPNAME");
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
                    ss += " LIMIT 50";
                }
            }
        }
        ret.add(ss);
        return ret;
    }

    public static void main(String[] args) {
        String input =
                "MODE=FROM_PAIRWISE_VARIABLES_OF" +
                        "-EXPRESSION=exp1" +
                        "-EXPNAME=col" +
                        "-POPULATION=pop" +
                        "-GROUP_BY=exp2";
        Estimate targest = new Estimate(input);
        System.out.println(targest.getBQL());
        System.out.println(targest.getParsedInputs());
    }
}