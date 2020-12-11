package tuwien.filters.utils;


public class Filter {
    public static boolean isNumberConstraintValid(NumberConstraint constraint, Number number) {
        double constraintValue = constraint.getValue().doubleValue();
        double value = number.doubleValue();
        switch (constraint.getComparison()) {
            case LESS:
                return value < constraintValue;
            case LESS_OR_EQUAL:
                return value <= constraintValue;
            case EQUAL:
                return value == constraintValue;
            case GREATER_OR_EQUAL:
                return value >= constraintValue;
            case GREATER:
                return value > constraintValue;
        }

        return true;
    }
}
