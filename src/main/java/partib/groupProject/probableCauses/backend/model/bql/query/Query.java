package partib.groupProject.probableCauses.backend.model.bql.query;

import java.lang.reflect.MalformedParametersException;
import java.util.*;
import java.util.Arrays;


/**
 * This is the Query parent class, from which all Query methods called by the client subclass.
 *
 * We expect a call of the form: '/bql/query/<queryname>/{unparsed}', where unparsed is of format:
 *  * ...-<field1>=<field1_value>-<field2>=<field2_value>-...
 * <queryname> should take one of the implemented queries, such as 'estimate', or 'infer'.
 *
 * Note that the ordering of (field, value) pairs is not relevant, and may be specified in any desired order.
 *
 * Expressions are expected to have their spaces replaced with '!' exclamation marks, and are subsequently cleaned
 * by the method cleanExpression below.
 *
 * Additionally, spaces in query names and fields should be replaced with '_' underscores, which are subsequently
 * cleaned by the constructor below.
 *
 */

public class Query {
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
                    parsedInputs.put(kv[0].replace("_", " "), value);
                    fields.add(kv[0].replace("_", " "));
                } else {
                    throw new MalformedParametersException("Error: Repeated field <"+kv[0]+">");
                }
            }
        }
    }
}