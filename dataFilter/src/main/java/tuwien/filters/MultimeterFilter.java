package tuwien.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;
import tuwien.filters.utils.Comparison;
import tuwien.filters.utils.Filter;
import tuwien.filters.utils.NumberConstraint;
import tuwien.models.MultimeterRecord;

@Component
@Data
public class MultimeterFilter {

    public NumberConstraint temperatureConstraint;
    public NumberConstraint humidityConstraint;
    public NumberConstraint pressureConstraint;

    public MultimeterFilter() {
        // TODO load from file
        temperatureConstraint = new NumberConstraint(50, Comparison.GREATER_OR_EQUAL);
        humidityConstraint = new NumberConstraint(20, Comparison.LESS);
        pressureConstraint = new NumberConstraint(5, Comparison.GREATER);
    }

    @JsonCreator
    public MultimeterFilter(
            @JsonProperty("temperatureConstraint") NumberConstraint temperatureConstraint,
            @JsonProperty("humidityConstraint") NumberConstraint humidityConstraint,
            @JsonProperty("pressureConstraint") NumberConstraint pressureConstraint) {
        super();
        this.temperatureConstraint = temperatureConstraint;
        this.humidityConstraint = humidityConstraint;
        this.pressureConstraint = pressureConstraint;
    }

    public boolean filter(MultimeterRecord record) {
        return Filter.checkNumberConstraint(temperatureConstraint, record.getTemperature())
                || Filter.checkNumberConstraint(humidityConstraint, record.getHumidity())
                || Filter.checkNumberConstraint(pressureConstraint, record.getPressure());
    }

    public synchronized void update(MultimeterFilter newFilter) {
        this.temperatureConstraint = newFilter.temperatureConstraint;
        this.humidityConstraint = newFilter.humidityConstraint;
        this.pressureConstraint = newFilter.pressureConstraint;

        // todo save to file
    }
}
