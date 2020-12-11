package tuwien.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tuwien.filters.AnemometerFilter;

import java.util.logging.Logger;

@RestController
@RequestMapping("/filters/anemometer")
public class AnemometerController {
    private final static Logger LOGGER = Logger.getLogger(AnemometerController.class.getName());

    @Autowired
    AnemometerFilter filter;

    @GetMapping("")
    AnemometerFilter getAnemometerFilter() {
        // Automatically serialized to JSON
        return filter;
    }

    @PostMapping
    AnemometerFilter updateAnemometerFilter(@RequestBody String body) {
        LOGGER.info(String.format("updateAnemometerFilter received %s", body));
        ObjectMapper mapper = new ObjectMapper();
        try {
            AnemometerFilter newFilter = mapper.readValue(body, AnemometerFilter.class);
            filter.update(newFilter);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", body, e.getMessage()));
        }

        return filter;
    }
}
