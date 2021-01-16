import { Component, OnInit } from '@angular/core';
import {Comparison} from '../model/comparison';
import {FilterService} from '../services/filter.service';
import {Subscription} from 'rxjs';
import {FormControl, FormGroup} from '@angular/forms';
import {Options} from '@angular-slider/ngx-slider';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  private subscriptions = new Subscription();
  public filterChanged = false;
  public filterForm: FormGroup = new FormGroup({
    windSpeedControl: new FormControl([20, 80]),
    windBearingControl: new FormControl([20, 80]),
    humidityControl: new FormControl([20, 80]),
    temperatureControl: new FormControl([20, 80]),
    pressureControl: new FormControl([20, 80])
  });
  windSpeedOptions: Options = {
    floor: 0,
    ceil: 100
  };
  windBearingOptions: Options = {
    floor: 0,
    ceil: 100
  };
  humidityOptions: Options = {
    floor: 0,
    ceil: 100
  };
  temperatureOptions: Options = {
    floor: 0,
    ceil: 100
  };
  pressureOptions: Options = {
    floor: 0,
    ceil: 100
  };
  constructor(private filterService: FilterService) { }

  ngOnInit(): void {
    this.subscriptions.add(
      this.filterForm.valueChanges.subscribe(_ => {
        this.filterChanged = true;
      })
    );
  }

  applyFilter(): void {
    this.subscriptions.add(this.filterService.sendAnemometerFilter({
      windSpeedConstraint: { value: 20, comparison: Comparison.GREATER_OR_EQUAL },
      windBearingConstraint: { value: 20, comparison: Comparison.GREATER_OR_EQUAL }
    }).subscribe(x =>
    {console.log(x); }));
    this.subscriptions.add(this.filterService.sendMultimeterFilter({
      temperatureConstraint: { value: 20, comparison: Comparison.GREATER_OR_EQUAL },
      humidityConstraint: { value: 20, comparison: Comparison.GREATER_OR_EQUAL },
      pressureConstraint: { value: 20, comparison: Comparison.GREATER_OR_EQUAL }
    }).subscribe(x =>
    {console.log(x); }));
    this.filterChanged = false;
  }
}
