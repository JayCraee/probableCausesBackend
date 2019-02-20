package partib.groupProject.probableCauses.backend.model.bql.query;

import java.util.ArrayList;
import java.util.Arrays;

public class Select extends Query {
    private static final ArrayList<String> compulsoryFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"COLUMNS"}));

    private static final ArrayList<String> optionalFields =
            new ArrayList<>(Arrays.asList(
                    new String[]{"MODE", "TABLE", "WHERE", "GROUP BY", "ORDER BY", "LIMIT"}));

    public Select(String unparsed) {
        super(unparsed);
    }
}