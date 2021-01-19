import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Comparison} from '../model/comparison';
import {FilterService} from '../services/filter.service';
import {Subscription} from 'rxjs';
import {FormControl, FormGroup} from '@angular/forms';
import {Options} from '@angular-slider/ngx-slider';
import {MultimeterFilter} from '../model/multimeter-filter';
import {AnemometerFilter} from '../model/anemometer-filter';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  constructor(private filterService: FilterService) { }

  @Input() currentFilter: { multiMeter: MultimeterFilter, anemometer: AnemometerFilter };
  @Output() filter: EventEmitter<{ multiMeter: MultimeterFilter, anemometer: AnemometerFilter }> =
    new EventEmitter<{multiMeter: MultimeterFilter; anemometer: AnemometerFilter}>();
  private subscriptions = new Subscription();
  public filterChanged = false;
  public comparerValues = ['Less', 'Less or Equal', 'Equal', 'Greater or Equal', 'Greater'];
  public filterForm: FormGroup = new FormGroup({
    windSpeedControl: new FormControl(20),
    windSpeedComparer: new FormControl(['Equal']),
    windBearingControl: new FormControl(20),
    windBearingComparer: new FormControl(['Less']),
    humidityControl: new FormControl(20),
    humidityComparer: new FormControl(['Less']),
    temperatureControl: new FormControl(20),
    temperatureComparer: new FormControl(['Less']),
    pressureControl: new FormControl(20),
    pressureComparer: new FormControl(['Less'])
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
  private static getEnumFromString(value: string): Comparison  {
    switch (value) {
      case 'Less':
        return Comparison.LESS;
      case 'Less or Equal':
        return Comparison.LESS_OR_EQUAL;
      case 'Equal':
        return Comparison.EQUAL;
      case 'Greater or Equal':
        return Comparison.GREATER_OR_EQUAL;
      case 'Greater':
        return Comparison.GREATER;
      default:
        return Comparison.LESS;
    }
  }
  private static getStringFromEnum(enumV: Comparison): string {
    switch (enumV) {
      case Comparison.LESS_OR_EQUAL:
        return 'Less or Greater';
      case Comparison.LESS:
        return 'Less';
      case Comparison.GREATER:
        return 'Greater';
      case Comparison.GREATER_OR_EQUAL:
        return 'Greater or Equal';
      case Comparison.EQUAL:
        return 'Equal';
    }
  }

  ngOnInit(): void {
    if (!!this.currentFilter) {
      this.filterForm.get('windBearingControl')
        .setValue(this.currentFilter.anemometer.windBearingConstraint.value);
      this.filterForm.get('windSpeedControl')
        .setValue(this.currentFilter.anemometer.windSpeedConstraint.value);
      this.filterForm.get('windSpeedComparer')
        .setValue(AdminComponent.getStringFromEnum(this.currentFilter.anemometer.windSpeedConstraint.comparison));
      this.filterForm.get('windBearingComparer')
        .setValue(AdminComponent.getStringFromEnum(this.currentFilter.anemometer.windBearingConstraint.comparison));
      this.filterForm.get('humidityControl')
        .setValue(this.currentFilter.multiMeter.humidityConstraint.value);
      this.filterForm.get('humidityComparer')
        .setValue(AdminComponent.getStringFromEnum(this.currentFilter.multiMeter.humidityConstraint.comparison));
      this.filterForm.get('temperatureControl')
        .setValue(this.currentFilter.multiMeter.temperatureConstraint.value);
      this.filterForm.get('temperatureComparer')
        .setValue(AdminComponent.getStringFromEnum(this.currentFilter.multiMeter.temperatureConstraint.comparison));
      this.filterForm.get('pressureControl')
        .setValue(this.currentFilter.multiMeter.pressureConstraint.value);
      this.filterForm.get('pressureComparer')
        .setValue(AdminComponent.getStringFromEnum(this.currentFilter.multiMeter.pressureConstraint.comparison));
    }
    this.subscriptions.add(
      this.filterForm.valueChanges.subscribe(_ => {
        this.filterChanged = true;
      })
    );
  }

  applyFilter(): void {
    const windSpeedConstraint = {
      value: this.filterForm.get('windSpeedControl').value,
      comparison: AdminComponent.getEnumFromString(this.filterForm.get('windSpeedComparer').value) };
    const windBearingConstraint = {
      value: this.filterForm.get('windBearingControl').value,
      comparison: AdminComponent.getEnumFromString(this.filterForm.get('windBearingComparer').value) };
    this.subscriptions.add(this.filterService.sendAnemometerFilter({
      windSpeedConstraint,
      windBearingConstraint
    }).subscribe(x =>
    {console.log(x); }));
    const temperatureConstraint = {
      value: this.filterForm.get('temperatureControl').value,
      comparison: AdminComponent.getEnumFromString(this.filterForm.get('temperatureComparer').value) };
    const humidityConstraint = {
      value: this.filterForm.get('humidityControl').value,
      comparison: AdminComponent.getEnumFromString(this.filterForm.get('humidityComparer').value) };
    const pressureConstraint = {
      value: this.filterForm.get('pressureControl').value,
      comparison: AdminComponent.getEnumFromString(this.filterForm.get('pressureComparer').value) };
    this.subscriptions.add(this.filterService.sendMultimeterFilter({
      temperatureConstraint,
      humidityConstraint,
      pressureConstraint
    }).subscribe(x =>
    {console.log(x); }));
    this.filter.emit({
      multiMeter: { humidityConstraint, pressureConstraint, temperatureConstraint},
      anemometer: { windSpeedConstraint, windBearingConstraint}
    });
    this.filterChanged = false;
  }
}
