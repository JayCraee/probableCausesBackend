package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.*;
import java.util.Arrays;

public class Query {
    protected Map<String, String> metaData;
    protected String unparsed;
    protected final Map<String, String> parsedInputs;
    protected final Set<String> fields;

    public Map<String, String> getParsedInputs() {
        Map<String, String> ret = new HashMap<>();
        for (String k : fields) {
            ret.put(k, parsedInputs.get(k));
        }
        return ret;
    }

    protected String cleanExpression(String exp) {
        return exp.replace("!", " ");
    }

    public Map<String, String> getMetaData() {
        return null;
    }

    public List<String> getBQL() {
        return null;
    }

    protected Query(String ss) throws MalformedParametersException  {
        parsedInputs = new HashMap<>();
        fields = new HashSet<>();

        for (String s : ss.split("-")) {
            String[] kv = s.split("=");
            if (kv.length == 1) {
                throw new MalformedParametersException("Error: No value found for field <"+kv[0]+">");
            } else {
                if (!fields.contains(kv[0])) {
                    ArrayList<String> tmp = new ArrayList(Arrays.asList(kv));
                    tmp.remove(0);
                    String value = String.join("=", tmp);
                    parsedInputs.put(kv[0].replace("_", " "), kv[1]);
                    fields.add(kv[0].replace("_", " "));
                } else {
                    throw new MalformedParametersException("Error: Repeated field <"+kv[0]+">");
                }
            }
        }
    }
}