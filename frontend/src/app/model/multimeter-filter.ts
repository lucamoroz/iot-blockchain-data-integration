import {NumberConstraint, NumberConstraintString} from './number-constraint';

export interface MultimeterFilter{
  temperatureConstraint: NumberConstraint;
  humidityConstraint: NumberConstraint;
  pressureConstraint: NumberConstraint;
}

export interface MultimeterFilterString{
  temperatureConstraint: NumberConstraintString;
  humidityConstraint: NumberConstraintString;
  pressureConstraint: NumberConstraintString;
}
