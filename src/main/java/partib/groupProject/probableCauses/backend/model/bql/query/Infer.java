package partib.groupProject.probableCauses.backend.model.bql.query;

import java.util.ArrayList;
import java.util.Arrays;

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



    public Infer(String unparsed) {
        super(unparsed);

    }


    public static void main(String[] args) {

    }


}
