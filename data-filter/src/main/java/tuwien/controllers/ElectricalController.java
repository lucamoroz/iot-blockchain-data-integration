package tuwien.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tuwien.filters.ElectricalFilter;

import java.util.logging.Logger;

@RestController
@RequestMapping("/filters/electrical")
public class ElectricalController {
    private final static Logger LOGGER = Logger.getLogger(ElectricalController.class.getName());

    @Autowired
    ElectricalFilter filter;

    @GetMapping("")
    ElectricalFilter getElectricalFilter() {
        // Automatically serialized to JSON
        return filter;
    }

    @PostMapping
    ElectricalFilter updateElectricalFilter(@RequestBody String body) {
        LOGGER.info(String.format("updateElectricalFilter received %s", body));
        ObjectMapper mapper = new ObjectMapper();
        try {
            ElectricalFilter newFilter = mapper.readValue(body, ElectricalFilter.class);
            filter.update(newFilter);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", body, e.getMessage()));
        }

        return filter;
    }
}
