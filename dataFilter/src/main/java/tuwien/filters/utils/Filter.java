package tuwien.filters.utils;


import java.math.BigDecimal;

public class Filter {
    public static boolean checkNumberConstraint(NumberConstraint constraint, Number value) {
        BigDecimal b1 = BigDecimal.valueOf(constraint.getValue().doubleValue());
        BigDecimal b2 = BigDecimal.valueOf(value.doubleValue());
        switch (constraint.getComparison()) {
            case LESS:
                return b1.compareTo(b2) < 0;
            case LESS_OR_EQUAL:
                return b1.compareTo(b2) <= 0;
            case EQUAL:
                return b1.compareTo(b2) == 0;
            case GREATER_OR_EQUAL:
                return b1.compareTo(b2) >= 0;
            case GREATER:
                return b1.compareTo(b2) > 0;
        }

        return true;
    }
}
