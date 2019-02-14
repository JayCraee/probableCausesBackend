package partib.groupProject.probableCauses.backend.model.bql.query;

import lombok.Data;
import javax.persistence.Entity;

import java.lang.reflect.MalformedParametersException;
import java.util.*;

@Data
@Entity
public class Estimate extends Query {


    private static final ArrayList<String> modeOptions =
            new ArrayList<>(Arrays.asList(
                    new String[]{"BY","FROM", "FROM VARIABLES OF", "FROM PAIRWISE VARIABLES OF", "FROM PAIRWISE"}));

    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"MODE", "EXPRESSION", "EXPNAME", "POPULATION"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"WHERE", "GROUP BY", "ORDER BY", "LIMIT"}));

    private static final ArrayList<String> innerFields=
            new ArrayList<>(Arrays.asList(
                    new String[]{"WHERE", "GROUP BY"}));



    public Estimate(String unparsed) throws MalformedParametersException {
        super(unparsed);

        //clean up expressions
        parsedInputs.put("EXPRESSION", super.cleanExpression(parsedInputs.get("EXPRESSION")));


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
        parsedInputs.put("MODE", parsedInputs.get("MODE").replace("_", " "));
        if (!modeOptions.contains(super.parsedInputs.get("MODE"))) {
            throw new MalformedParametersException();
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

    public static void main(String[] args) {

        Estimate[] testimate = new Estimate[10];

        testimate[0] = new Estimate("MODE=FROM_VARIABLES_OF;EXPRESSION=PROBABILITY!DENSITY!OF!VALUE!val!GIVEN!literalconstraint;EXPNAME=ProbDensity;POPULATION=AFRICAN_DATA;LIMIT=100");
        /*
        for (Estimate est : testimate) {
            if (est != null) {
                System.out.println(est.getParsedInputs());
                System.out.println(est.getBQL());
            }
        }
        */

        //System.out.println(testimate[0].getParsedInputs());
        System.out.println(testimate[0].getBQL());

        testimate[1] = new Estimate("MODE=BY;EXPRESSION=DEPENDENCE!PROBABILITY!OF!col1!WITH!col2;EXPNAME=Dependence_Probability;POPULATION=AFRICA_DATA");
        //System.out.println(testimate[1].getParsedInputs());
        System.out.println(testimate[1].getBQL());




    }
}
