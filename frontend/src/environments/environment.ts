// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  blockChainAddressWS: 'wss://kovan.infura.io/ws/v3/4c8b0ab9755f4828a2957ca6fd25f2f8',
  blockChainAddressHttp: 'https://kovan.infura.io/v3/4c8b0ab9755f4828a2957ca6fd25f2f8',
  dataFilterAddress: 'http://127.0.0.1',
  contractAddress: '0x6b651f25125964a084627e78e703799cf2bdf5ca',
  privateAccountKey: '0x2e3f3ddf0986ce65c0a179f43692c388150c4ec67ecaab6c48a7685cf43988df'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
