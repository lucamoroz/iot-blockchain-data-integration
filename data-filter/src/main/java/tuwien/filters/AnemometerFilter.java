package tuwien.filters;

import org.springframework.stereotype.Component;
import tuwien.filters.utils.Comparison;
import tuwien.filters.utils.Filter;
import tuwien.filters.utils.NumberConstraint;
import tuwien.models.AnemometerRecord;

@Component
public class AnemometerFilter {

    public NumberConstraint windSpeedConstraint;
    public NumberConstraint windBearingConstraint;

    public AnemometerFilter() {
        // TODO load from file
        windSpeedConstraint = new NumberConstraint(4, Comparison.LESS_OR_EQUAL);
        windBearingConstraint = new NumberConstraint(360, Comparison.GREATER_OR_EQUAL);
    }

    public boolean isToFilter(AnemometerRecord record) {
        return !Filter.isNumberConstraintValid(windSpeedConstraint, record.getWindSpeed())
                || !Filter.isNumberConstraintValid(windBearingConstraint, record.getWindBearing());
    }

    public synchronized void update(AnemometerFilter newFilter) {
        this.windSpeedConstraint = newFilter.windSpeedConstraint;
        this.windBearingConstraint = newFilter.windBearingConstraint;

        // TODO save to file
    }
}
