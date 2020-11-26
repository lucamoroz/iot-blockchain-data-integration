package tuwien.filters.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NumberConstraint {
    private Number value;
    private Comparison comparison;

    @JsonCreator
    public NumberConstraint(
            @JsonProperty("value") Number value,
            @JsonProperty("comparison") Comparison comparison) {
        this.value = value;
        this.comparison = comparison;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }
}