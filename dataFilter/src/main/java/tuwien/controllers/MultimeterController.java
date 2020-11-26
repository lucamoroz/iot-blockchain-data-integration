package tuwien.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tuwien.filters.MultimeterFilter;

@RestController
@RequestMapping("/filters/multimeter/")
public class MultimeterController {

    @Autowired
    MultimeterFilter filter;

    MultimeterController() {

    }

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
        // Automatically serialized to json
        return filter;
    }

    @PostMapping("")
    MultimeterFilter updateMultimeterFilter(@RequestBody String body) {
        // todo update filter
        System.out.println(body);
        ObjectMapper mapper = new ObjectMapper();
        try {
            MultimeterFilter newFilter = mapper.readValue(body, MultimeterFilter.class);
            filter.update(newFilter);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't parse: " + body + " - Error: " + e.getMessage());
            //return;
        }
        return filter;
    }

}
