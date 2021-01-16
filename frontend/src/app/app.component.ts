import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, Subscription} from 'rxjs';
import {BlockchainService} from './services/blockchain.service';

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
  public notifications: string[] = [];
  public adminSelected = true;
  public userSelected = false;

  constructor(private blockChainService: BlockchainService) {
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
          this.checkMultimeter(x, this.notifications);
        } else{
          this.windDataToLoad.push(x);
          this.checkAnemometer(x, this.notifications);
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

  private checkMultimeter(x: any, notifications: string[]) {

  }

  private checkAnemometer(x: any, notifications: string[]) {

  }
}
