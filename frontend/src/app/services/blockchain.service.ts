import {Injectable, isDevMode} from '@angular/core';
import Web3 from 'web3';
import {Subject} from 'rxjs';
import { environment } from '../../environments/environment';
import {AnemometerData} from '../model/anemometer-data';
import {MultimeterData} from '../model/multiMeter-data';

@Injectable({
  providedIn: 'root'
})
export class BlockchainService {

  constructor() {
    const web3 = new Web3(new Web3.providers.WebsocketProvider(environment.blockChainAddressWS));
    if (isDevMode()) {
      web3.eth.defaultAccount = web3.eth.accounts[0];
    } else {
      web3.eth.accounts.privateKeyToAccount(environment.privateAccountKey);
    }
    this.contract = new web3.eth.Contract([
      {
        anonymous: false,
        inputs: [
          {
            indexed: false,
            internalType: 'string',
            name: 'message',
            type: 'string'
          },
          {
            indexed: false,
            internalType: 'uint256',
            name: 'index',
            type: 'uint256'
          }
        ],
        name: 'Notification',
        type: 'event'
      },
      {
        inputs: [
          {
            internalType: 'bytes',
            name: 'data',
            type: 'bytes'
          }
        ],
        name: 'addDataItem',
        outputs: [],
        stateMutability: 'nonpayable',
        type: 'function'
      },
      {
        inputs: [
          {
            internalType: 'uint32',
            name: 'itemId',
            type: 'uint32'
          }
        ],
        name: 'getDataItem',
        outputs: [
          {
            internalType: 'bytes',
            name: '',
            type: 'bytes'
          }
        ],
        stateMutability: 'view',
        type: 'function'
      },
      {
        inputs: [],
        name: 'getLatestDataItem',
        outputs: [
          {
            internalType: 'bytes',
            name: '',
            type: 'bytes'
          }
        ],
        stateMutability: 'view',
        type: 'function'
      },
      {
        inputs: [
          {
            internalType: 'uint256',
            name: '',
            type: 'uint256'
          }
        ],
        name: 'items',
        outputs: [
          {
            internalType: 'bytes',
            name: '',
            type: 'bytes'
          }
        ],
        stateMutability: 'view',
        type: 'function'
      }
    ], environment.contractAddress);
    this.contract.events.Notification()
      .on('connected', (subscriptionId) => {
        console.log(subscriptionId);
      })
      .on('data', (event) => {
        console.log(event.returnValues.index); // same results as the optional callback above
        this.getDataFromBlockChain(event.returnValues.index);
      })
      .on('changed', (event) => {
        console.log('event: ' + event);
      })
      .on('error', (error, receipt)  => {
        console.log('error: ' + error);
        console.log('receipt: ' + receipt);
      });
  }

  get blockChainData(): Subject<any> {
    return this.blockChainSubject;
  }
  private blockChainSubject = new Subject<any>();
  private contract;
  private static hex2a(hex): string {
    let str = '';
    for (let i = 0; i < hex.length; i += 2) { str += String.fromCharCode(parseInt(hex.substr(i, 2), 16)); }
    return str;
  }
  private getDataFromBlockChain(index: number): void {
    console.log('getting Data');
    this.contract.methods.getDataItem(index).call()
      .then(result => {
        console.log(JSON.parse(BlockchainService.hex2a(result).substring(1)));
        const resultParsed = JSON.parse(BlockchainService.hex2a(result).substring(1));
        const data: AnemometerData | MultimeterData = resultParsed.hasOwnProperty('humidity')
          ? { time: resultParsed.time, humidity: resultParsed.humidity,
            temperature: resultParsed.temperature, pressure: resultParsed.pressure}
          : { time: resultParsed.time, windSpeed: resultParsed.windSpeed, windBearing: resultParsed.windBearing };
        this.blockChainSubject.next(data);
      });
  }
}
