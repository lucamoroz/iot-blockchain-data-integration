import {Comparison} from './comparison';

export interface NumberConstraint{
  value: number;
  comparison: Comparison;
}

export interface NumberConstraintString{
  value: number;
  comparison: string;
}
