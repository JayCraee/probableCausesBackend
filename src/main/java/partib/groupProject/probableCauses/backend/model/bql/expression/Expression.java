package partib.groupProject.probableCauses.backend.model.bql.expression;

import java.lang.reflect.MalformedParametersException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Expression {
    protected final Map<String, String> parsedInputs;
    protected final Set<String> fields;

    


    private Map<String, String> getParsedInputs() {
        Map<String, String> ret = new HashMap<>();
        for (String k : fields) {
            ret.put(k, parsedInputs.get(k));
        }
        return ret;
    }


    protected Expression(String ss) throws MalformedParametersException {
        parsedInputs = new HashMap<>();
        fields = new HashSet<>();

        for (String s : ss.split("-")) {
            String[] kv = s.split("=");
            if (kv.length != 2) {
                throw new MalformedParametersException();
            } else {
                if (!fields.contains(kv[0])) {
                    parsedInputs.put(kv[0], kv[1]);
                    fields.add(kv[0]);
                } else {
                    throw new MalformedParametersException();
                }
            }
        }



    }

    public static void main(String[] args) {
        Expression testexp = new Expression("COLUMN=targcol-GIVEN=targcols");







    }

}
