import {NumberConstraint} from './number-constraint';

export interface MultimeterFilter{
  temperatureConstraint: NumberConstraint;
  humidityConstraint: NumberConstraint;
  pressureConstraint: NumberConstraint;
}
