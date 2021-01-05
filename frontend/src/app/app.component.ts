import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, Subscription} from 'rxjs';
import {BlockchainService} from './services/blockchain.service';
import {Options} from '@angular-slider/ngx-slider';
import {FormControl, FormGroup} from '@angular/forms';
import {FilterService} from './services/filter.service';
import {Comparison} from './model/comparison';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'frontend';
  private blockChainSubject: Subject<any>;
  private subscriptions = new Subscription();
  public temperatureDataToDisplay: object[] = [];
  public temperatureDataToLoad: object[] = [];
  public windDataToLoad: object[] = [];
  public windDataToDisplay: object[] = [];
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

  constructor(private blockChainService: BlockchainService, private filterService: FilterService) {
    this.blockChainSubject = blockChainService.blockChainData;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.blockChainSubject.subscribe(x => {
        if (x.hasOwnProperty('humidity')) {
          this.temperatureDataToLoad.push(x);
        } else{
          this.windDataToLoad.push(x);
        }
      })
    );
    this.subscriptions.add(
      this.filterForm.valueChanges.subscribe(_ => {
        this.filterChanged = true;
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
