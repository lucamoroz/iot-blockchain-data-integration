package tuwien.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import tuwien.filters.utils.Filter;
import tuwien.filters.utils.NumberConstraint;
import tuwien.models.AnemometerRecord;
import tuwien.filters.utils.Comparison;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class AnemometerFilter {
    private final static Logger LOGGER = Logger.getLogger(AnemometerFilter.class.getName());
    private final static String filterPath = "/app/filters/anemometerFilter.json";

    public NumberConstraint windSpeedConstraint;
    public NumberConstraint windBearingConstraint;

    public AnemometerFilter() { }

    @PostConstruct
    public void loadFilter() {
        try {
            Path path = Paths.get(filterPath);
            String anemometerJson = String.join("\n", Files.readAllLines(path));
            ObjectMapper mapper = new ObjectMapper();
            AnemometerFilter defaultFilter = mapper.readValue(anemometerJson, AnemometerFilter.class);
            update(defaultFilter);

        } catch (InvalidPathException e) {
            LOGGER.severe("Couldn't load anemometer filter, invalid path: " + e.getMessage());
            loadDefaultFilter();
        } catch (IOException e) {
            LOGGER.severe("Couldn't load anemometer filter: " + e.getMessage());
            loadDefaultFilter();
        }
    }

    public boolean isToFilter(AnemometerRecord record) {
        return !Filter.isNumberConstraintValid(windSpeedConstraint, record.getWindSpeed())
                || !Filter.isNumberConstraintValid(windBearingConstraint, record.getWindBearing());
    }

    public synchronized void update(AnemometerFilter newFilter) {
        this.windSpeedConstraint = newFilter.windSpeedConstraint;
        this.windBearingConstraint = newFilter.windBearingConstraint;

        saveFilter();
    }

    private void saveFilter() {
        try {
            Path path = Paths.get(filterPath);
            ObjectMapper mapper = new ObjectMapper();
            String newFilterJson = mapper.writeValueAsString(this);
            Files.write(path, newFilterJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.severe("Could save anemometer filter: " + e.getMessage());
        }
    }

    private void loadDefaultFilter() {
        this.windSpeedConstraint = new NumberConstraint(10, Comparison.GREATER_OR_EQUAL);
        this.windBearingConstraint = new NumberConstraint(300, Comparison.GREATER_OR_EQUAL);
    }
}
