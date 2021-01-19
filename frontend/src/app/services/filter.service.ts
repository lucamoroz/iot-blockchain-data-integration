import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';
import {Observable} from 'rxjs';
import {AnemometerFilter} from '../model/anemometer-filter';
import {MultimeterFilter} from '../model/multimeter-filter';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  constructor(private http: HttpClient) { }
  public sendAnemometerFilter(filter: AnemometerFilter): Observable<any> {
    return this.http.post(environment.dataFilterAddress + '/filters/anemometer', filter);
  }
  public sendMultimeterFilter(filter: MultimeterFilter): Observable<any> {
    return this.http.post(environment.dataFilterAddress + '/filters/multimeter/', filter);
  }
}
