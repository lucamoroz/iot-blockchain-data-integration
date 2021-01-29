import {NumberConstraint, NumberConstraintString} from './number-constraint';

export interface AnemometerFilter{
  windSpeedConstraint: NumberConstraint;
  windBearingConstraint: NumberConstraint;
}

export interface AnemometerFilterString{
  windSpeedConstraint: NumberConstraintString;
  windBearingConstraint: NumberConstraintString;
}

