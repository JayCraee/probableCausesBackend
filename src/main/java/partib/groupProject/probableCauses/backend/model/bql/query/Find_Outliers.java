package partib.groupProject.probableCauses.backend.model.bql.query;

import java.util.ArrayList;
import java.util.List;

public class Find_Outliers extends Query {

    @Override
    public List<String> getBQL() {
        List<String> ret = new ArrayList<>();



        return ret;
    }



    public Find_Outliers(String unparsed) {
        super(unparsed);

        


    }






    public static void main(String[] args) {
        Find_Outliers targfo = new Find_Outliers("FIELD=PLACEHOLDER");
        System.out.println(targfo.getBQL());








    }



}
