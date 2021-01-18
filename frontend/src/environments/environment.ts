// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  blockChainAddressWS: 'ws://127.0.0.1:8545',
  blockChainAddressHttp: 'http://127.0.0.1:8545',
  dataFilterAddress: 'http://127.0.0.1',
  contractAddress: '0x74bdcf0a467194a9290c4a4cd0f499acc22938f0',
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
