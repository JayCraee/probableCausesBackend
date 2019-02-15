package partib.groupProject.probableCauses.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import partib.groupProject.probableCauses.backend.model.table.Table;

import java.util.Optional;

@RestController
@RequestMapping("bql/query")
public class QueryController {

    private final Logger log = LoggerFactory.getLogger(QueryController.class);

    @GetMapping("/estimate/{unparsed}")
    ResponseEntity<?> getEstimate(@PathVariable Long id, @PathVariable String unparsed) {
        Optional<Table> table; // need BQL connection
        return null;
    }

}
