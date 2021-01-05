import {NumberConstraint} from './number-constraint';

export interface AnemometerFilter{
  windSpeedConstraint: NumberConstraint;
  windBearingConstraint: NumberConstraint;
}
