package partib.groupProject.probableCauses.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import partib.groupProject.probableCauses.backend.model.bql.query.Estimate;
import partib.groupProject.probableCauses.backend.model.bql.query.EstimateRepository;
import partib.groupProject.probableCauses.backend.model.table.Table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("bql/query")
public class QueryController {

    private final Logger log = LoggerFactory.getLogger(QueryController.class);
    private EstimateRepository estimateRepository;

    @GetMapping("/estimate/{unparsed}")
    ResponseEntity<?> getEstimate(@PathVariable Long id, @PathVariable String unparsed) {
        Optional<Table> table; // need BQL connection
        return null;
    }

}
