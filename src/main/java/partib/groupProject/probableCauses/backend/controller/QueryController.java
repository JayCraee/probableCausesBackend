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
import java.util.Optional;

@RestController
@RequestMapping("bql/query")
public class QueryController {

    private final Logger log = LoggerFactory.getLogger(QueryController.class);
    private EstimateRepository estimateRepository;

    @GetMapping("/estimateLogs")
    Collection<Estimate> getEstimateLogs() {
        return estimateRepository.findAll();
    }

    @GetMapping("/estimateLog/{id}")
    ResponseEntity<Estimate> getEstimateLogById(@PathVariable Long id) {
        Optional<Estimate> estimate = estimateRepository.findById(id);
        return estimate.map(response -> ResponseEntity.ok().body(response)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/estimate/{mode}/{expression}/{population}")
    ResponseEntity<?> getEstimate(@PathVariable Long id, @PathVariable String expression, @PathVariable String mode, @PathVariable String population) {
        Optional<Table> table; // need BQL connection
        return null;
    }

}
