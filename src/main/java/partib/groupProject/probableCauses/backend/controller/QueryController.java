package partib.groupProject.probableCauses.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import partib.groupProject.probableCauses.backend.model.bql.query.*;

/***
 * This controller class hanldes all APIs related to BQL query.
 *
 * 1) /bql/query/estimate/{unparsed}
 * Gets any form of ESTIMATE query, checks the format and returns results in string
 * Throws an exception on invalid calls
 *
 * 2) /bql/query/infer/{unparsed}
 * Gets any form of INFER query, checks the format and returns results in string
 * Throws an exception on invalid calls
 *
 * 2) /bql/query/simulate/{unparsed}
 * Gets any form of SIMULATE query, checks the format and returns results in string
 * Throws an exception on invalid calls
 *
 * 2) /bql/query/select/{unparsed}
 * Gets any form of SELECT query, checks the format and returns results in string
 * Throws an exception on invalid calls
 *
 * 2) /bql/query/find_outliers/{unparsed}
 * Gets any form of FIND_OUTLIERS query, checks the format and returns results in string
 * Throws an exception on invalid calls
 */

@RestController
@RequestMapping("/bql/query")
public class QueryController {
    public static final String db = "african_traffic_data.bdb";
    private final Logger log = LoggerFactory.getLogger(QueryController.class);

    @GetMapping("/estimate/{unparsed}")
    String getEstimate(@PathVariable String unparsed) throws Exception {
        System.out.println(unparsed);
        Estimate query = new Estimate(unparsed);
        String data = ServerConnector.queryCaller(db, query.getBQL());
        return EstimateOutputCleaner.process(data, query);
    }

    @GetMapping("/infer/{unparsed}")
    String getInfer(@PathVariable String unparsed) throws InvalidCallException {
        System.out.println(unparsed);
        Infer query = new Infer(unparsed);
        return ServerConnector.queryCaller(db, query.getBQL());
    }

    @GetMapping("/simulate/{unparsed}")
    String getSimulate(@PathVariable String unparsed) throws InvalidCallException {
        System.out.println(unparsed);
        Simulate query = new Simulate(unparsed);
        return ServerConnector.queryCaller(db, query.getBQL());
    }

    @GetMapping("/select/{unparsed}")
    String getSelect(@PathVariable String unparsed) throws InvalidCallException {
        System.out.println(unparsed);
        Select query = new Select(unparsed);
        return ServerConnector.queryCaller(db, query.getBQL());
    }

    @GetMapping("/find_outliers/{unparsed}")
    String getFindOutliers(@PathVariable String unparsed) throws InvalidCallException {
        System.out.println(unparsed);
        Find_Outliers query = new Find_Outliers(unparsed);
        return ServerConnector.queryCaller(db, query.getBQL());
    }
}
