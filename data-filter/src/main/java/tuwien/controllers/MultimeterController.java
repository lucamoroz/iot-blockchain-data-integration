package tuwien.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tuwien.filters.MultimeterFilter;

import java.util.logging.Logger;

@RestController
@RequestMapping("/filters/multimeter/")
public class MultimeterController {
    private final static Logger LOGGER = Logger.getLogger(MultimeterController.class.getName());

    @Autowired
    MultimeterFilter filter;

    /**
     * Example return:
     * {
     *     "temperatureConstraint": {
     *         "value": 50,
     *         "comparison": "GREATER_OR_EQUAL"
     *     },
     *     "humidityConstraint": {
     *         "value": 20,
     *         "comparison": "LESS"
     *     },
     *     "pressureConstraint": {
     *         "value": 5,
     *         "comparison": "GREATER"
     *     }
     * }
     */
    @GetMapping("")
    MultimeterFilter getMultimeterFilter() {
        // Automatically serialized to JSON
        return filter;
    }

    @CrossOrigin
    @PostMapping("")
    MultimeterFilter updateMultimeterFilter(@RequestBody String body) {
        LOGGER.info(String.format("updateMultimeterFilter received %s", body));
        ObjectMapper mapper = new ObjectMapper();
        try {
            MultimeterFilter newFilter = mapper.readValue(body, MultimeterFilter.class);
            filter.update(newFilter);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", body, e.getMessage()));
        }
        return filter;
    }

}
