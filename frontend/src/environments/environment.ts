// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  blockChainAddressWS: 'ws://127.0.0.1:8545',
  blockChainAddressHttp: 'http://127.0.0.1:8545',
  dataFilterAddress: 'http://127.0.0.1',
  contractAddress: '0x2a889013ff5d6f8315659d4230ef128efa49075d',
  privateAccountKey: ''
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
