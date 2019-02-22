package partib.groupProject.probableCauses.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;
import partib.groupProject.probableCauses.backend.model.bql.query.Infer;
import partib.groupProject.probableCauses.backend.model.bql.query.Select;
import partib.groupProject.probableCauses.backend.model.bql.query.Simulate;

@RestController
@RequestMapping("/bql/query")
public class QueryController {
    private final String db = "crime.bdb";
    private final Logger log = LoggerFactory.getLogger(QueryController.class);

    @GetMapping("/estimate/{unparsed}")
    String getEstimate(@PathVariable String unparsed) throws InvalidCallException {
        System.out.println(unparsed);
        Estimate query = new Estimate(unparsed);
        return ServerConnector.queryCaller(db, query.getBQL());
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

    @GetMapping("/selectTest/{parsed}")
    String getSelectTest(@PathVariable String parsed) throws InvalidCallException {
        return ServerConnector.singleQueryCaller(db, "SELECT * FROM sys.tables");
    }
}
