package tuwien.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import tuwien.filters.utils.Filter;
import tuwien.filters.utils.NumberConstraint;
import tuwien.models.MultimeterRecord;
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
public class MultimeterFilter {
    private final static Logger LOGGER = Logger.getLogger(MultimeterFilter.class.getName());
    private final static String filterPath = "/app/filters/multimeterFilter.json";

    public NumberConstraint temperatureConstraint;
    public NumberConstraint humidityConstraint;
    public NumberConstraint pressureConstraint;

    public MultimeterFilter() { }

    @PostConstruct
    public void loadFilter() {
        try {
            Path path = Paths.get(filterPath);
            String multimeterJson = String.join("\n", Files.readAllLines(path));
            ObjectMapper mapper = new ObjectMapper();
            MultimeterFilter defaultFilter = mapper.readValue(multimeterJson, MultimeterFilter.class);
            update(defaultFilter);

        } catch (InvalidPathException e) {
            LOGGER.severe("Couldn't load multimeter filter, invalid path: " + e.getMessage());
            loadDefaultFilter();
        } catch (IOException e) {
            LOGGER.severe("Couldn't load multimeter filter: " + e.getMessage());
            loadDefaultFilter();
        }
    }

    public boolean isToFilter(MultimeterRecord record) {
        return !Filter.isNumberConstraintValid(temperatureConstraint, record.getTemperature())
                || !Filter.isNumberConstraintValid(humidityConstraint, record.getHumidity())
                || !Filter.isNumberConstraintValid(pressureConstraint, record.getPressure());
    }

    public synchronized void update(MultimeterFilter newFilter) {
        this.temperatureConstraint = newFilter.temperatureConstraint;
        this.humidityConstraint = newFilter.humidityConstraint;
        this.pressureConstraint = newFilter.pressureConstraint;

        saveFilter();
    }

    private void saveFilter() {
        try {
            Path path = Paths.get(filterPath);
            ObjectMapper mapper = new ObjectMapper();
            String newFilterJson = mapper.writeValueAsString(this);
            Files.write(path, newFilterJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.severe("Could save multimeter filter: " + e.getMessage());
        }
    }

    private void loadDefaultFilter() {
        this.temperatureConstraint = new NumberConstraint(20, Comparison.GREATER_OR_EQUAL);
        this.humidityConstraint = new NumberConstraint(0.9, Comparison.LESS_OR_EQUAL);
        this.pressureConstraint = new NumberConstraint(10, Comparison.GREATER_OR_EQUAL);
    }
}
