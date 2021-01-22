package tuwien.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import tuwien.filters.utils.Filter;
import tuwien.filters.utils.NumberConstraint;
import tuwien.models.ElectricalRecord;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class ElectricalFilter {
    private final static Logger LOGGER = Logger.getLogger(ElectricalFilter.class.getName());
    private final static String filterPath = "/app/filters/electricalFilter.json";

    public NumberConstraint valueConstraint;

    public ElectricalFilter() { }

    @PostConstruct
    public void loadFilter() {
        try {
            Path path = Paths.get(filterPath);
            String electricalJson = String.join("\n", Files.readAllLines(path));
            ObjectMapper mapper = new ObjectMapper();
            ElectricalFilter defaultFilter = mapper.readValue(electricalJson, ElectricalFilter.class);
            update(defaultFilter);

        } catch (InvalidPathException e) {
            LOGGER.severe("Couldn't load default electrical filter, invalid path: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.severe("Couldn't load default electrical filter: " + e.getMessage());
        }
    }

    public boolean isToFilter(ElectricalRecord record) {
        return !Filter.isNumberConstraintValid(valueConstraint, record.getValue());
    }

    public synchronized void update(ElectricalFilter filter) {
        this.valueConstraint = filter.valueConstraint;
        saveFilter();
    }

    private void saveFilter() {
        try {
            Path path = Paths.get(filterPath);
            ObjectMapper mapper = new ObjectMapper();
            String newFilterJson = mapper.writeValueAsString(this);
            Files.write(path, newFilterJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.severe("Could save electrical filter: " + e.getMessage());
        }
    }
}
