import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {AnemometerFilter, AnemometerFilterString} from '../model/anemometer-filter';
import {MultimeterFilter, MultimeterFilterString} from '../model/multimeter-filter';
import {Comparison} from '../model/comparison';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  private currentFilter: { multiMeter: MultimeterFilter, anemometer: AnemometerFilter } = {
    multiMeter: {
      temperatureConstraint: { value: 5, comparison: Comparison.GREATER_OR_EQUAL},
      pressureConstraint: { value: 5, comparison: Comparison.GREATER},
      humidityConstraint: { value: 1, comparison: Comparison.LESS}},
    anemometer: {
      windBearingConstraint: { value: 250, comparison: Comparison.GREATER_OR_EQUAL},
      windSpeedConstraint: { value: 5, comparison: Comparison.GREATER_OR_EQUAL}
    }};
  constructor(private http: HttpClient) { }
  public getAnemometerFilter(): Observable<AnemometerFilterString> {
    return this.http.get<AnemometerFilterString>(environment.dataFilterAddress + '/filters/anemometer');
  }
  public getMultimeterFilter(): Observable<MultimeterFilterString> {
    return this.http.get<MultimeterFilterString>(environment.dataFilterAddress + '/filters/multimeter/');
  }
  public sendAnemometerFilter(filter: AnemometerFilter): Observable<any> {
    return this.http.post(environment.dataFilterAddress + '/filters/anemometer', filter);
  }
  public sendMultimeterFilter(filter: MultimeterFilter): Observable<any> {
    return this.http.post(environment.dataFilterAddress + '/filters/multimeter/', filter);
  }
  public getFilter(): { multiMeter: MultimeterFilter, anemometer: AnemometerFilter } {
    return this.currentFilter;
  }
  public setFilter(filter: { multiMeter: MultimeterFilter, anemometer: AnemometerFilter }): void {
    this.currentFilter = filter;
  }
}
