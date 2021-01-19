import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, Subscription} from 'rxjs';
import {BlockchainService} from './services/blockchain.service';
import {MultimeterFilter} from './model/multimeter-filter';
import {AnemometerFilter} from './model/anemometer-filter';
import {Comparison} from './model/comparison';
import {AnemometerData} from './model/anemometer-data';
import {MultimeterData} from './model/multiMeter-data';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  constructor(private blockChainService: BlockchainService) {
    this.blockChainSubject = blockChainService.blockChainData;
  }
  title = 'frontend';
  private blockChainSubject: Subject<any>;
  private subscriptions = new Subscription();
  public temperatureDataToDisplay: MultimeterData[] = [];
  public temperatureDataToLoad: MultimeterData[] = [];
  public windDataToLoad: AnemometerData[] = [];
  public windDataToDisplay: AnemometerData[] = [];
  public notifications: string[] = [];
  public adminSelected = true;
  public userSelected = false;
  public currentFilter: { multiMeter: MultimeterFilter, anemometer: AnemometerFilter } = {
    multiMeter: {
      temperatureConstraint: { value: 10, comparison: Comparison.EQUAL},
      pressureConstraint: { value: 10, comparison: Comparison.EQUAL},
      humidityConstraint: { value: 10, comparison: Comparison.EQUAL}},
    anemometer: {
      windBearingConstraint: { value: 10, comparison: Comparison.EQUAL},
      windSpeedConstraint: { value: 10, comparison: Comparison.EQUAL}
    }
  };
  private static checkValueAgainstComparor(value: number, otherValue: number, comparor: Comparison): boolean {
    switch (comparor) {
      case Comparison.EQUAL:
        return value === otherValue;
      case Comparison.GREATER:
        return value > otherValue;
      case Comparison.GREATER_OR_EQUAL:
        return value >= otherValue;
      case Comparison.LESS:
        return value < otherValue;
      case Comparison.LESS_OR_EQUAL:
        return value <= otherValue;
    }
  }
  private static getStringFromEnum(enumV: Comparison): string {
    switch (enumV) {
      case Comparison.LESS_OR_EQUAL:
        return 'less or equal to ';
      case Comparison.LESS:
        return 'less than ';
      case Comparison.GREATER:
        return 'greater than ';
      case Comparison.GREATER_OR_EQUAL:
        return 'greater or equal to ';
      case Comparison.EQUAL:
        return 'equal to ';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.blockChainSubject.subscribe(x => {
        if (x.hasOwnProperty('humidity')) {
          this.temperatureDataToLoad.push(x);
          this.checkMultimeter(x, this.currentFilter.multiMeter);
        } else{
          this.windDataToLoad.push(x);
          this.checkAnemometer(x, this.currentFilter.anemometer);
        }
      })
    );
  }

  addTemperatureDataToTable(): void {
    this.temperatureDataToDisplay.push(...this.temperatureDataToLoad);
    this.temperatureDataToLoad = [];
  }
  addWindDataToTable(): void {
    this.windDataToDisplay.push(...this.windDataToLoad);
    this.windDataToLoad = [];
  }

  selectAdmin(): void {
    this.adminSelected = true;
    this.userSelected = false;
  }

  selectUser(): void {
    this.adminSelected = false;
    this.userSelected = true;
  }

  private checkMultimeter(data: MultimeterData, filter: MultimeterFilter): void {
    if (AppComponent
      .checkValueAgainstComparor(data.temperature, filter.temperatureConstraint.value, filter.temperatureConstraint.comparison)) {
      this.notifications
        .push(
          'Temperature '
          + AppComponent.getStringFromEnum(filter.temperatureConstraint.comparison) + ' ' + filter.temperatureConstraint.value);
    }
    if (AppComponent
      .checkValueAgainstComparor(data.pressure, filter.pressureConstraint.value, filter.pressureConstraint.comparison)) {
      this.notifications
        .push('Pressure ' + AppComponent.getStringFromEnum(filter.pressureConstraint.comparison) + ' ' + filter.pressureConstraint.value);
    }
    if (AppComponent
      .checkValueAgainstComparor(data.humidity, filter.humidityConstraint.value, filter.humidityConstraint.comparison)) {
      this.notifications
        .push('Humidity ' + AppComponent.getStringFromEnum(filter.humidityConstraint.comparison) + ' ' + filter.humidityConstraint.value);
    }
  }

  private checkAnemometer(data: AnemometerData, filter: AnemometerFilter): void {
    if (AppComponent
      .checkValueAgainstComparor(data.windBearing, filter.windBearingConstraint.value, filter.windBearingConstraint.comparison)) {
      this.notifications
        .push(
          'Wind Bearing '
          + AppComponent.getStringFromEnum(filter.windSpeedConstraint.comparison) + ' ' + filter.windBearingConstraint.value);
    }
    if (AppComponent
      .checkValueAgainstComparor(data.windSpeed, filter.windSpeedConstraint.value, filter.windSpeedConstraint.comparison)) {
      this.notifications
        .push('Wind Speed '
          + AppComponent.getStringFromEnum(filter.windSpeedConstraint.comparison) + ' ' + filter.windSpeedConstraint.value);
    }
  }
  setFilter($event: {multiMeter: MultimeterFilter; anemometer: AnemometerFilter}): void {
    this.currentFilter = { multiMeter: $event.multiMeter, anemometer: $event.anemometer };
  }
}
