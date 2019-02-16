package partib.groupProject.probableCauses.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;

@RestController
@RequestMapping("/bql/query")
public class QueryController {

    private final Logger log = LoggerFactory.getLogger(QueryController.class);

    @GetMapping("/estimate/{unparsed}")
    String getEstimate(@PathVariable String unparsed) throws InvalidCallException {

        System.out.println(unparsed);

        Estimate query = new Estimate(unparsed);
        return ServerConnector.queryCaller("foo.bdb", query.getBQL());
    }

}
