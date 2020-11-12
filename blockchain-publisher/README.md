# Blockchain Publisher

## Deploy the contract
In order to use the publisher the contract (`src/contracts/SensorData.sol`) on which the functions are executed must first be deployed t

```
usage: deploy_contract.py [-h] --blockchain-host BLOCKCHAIN_HOST --contract-sol-path CONTRACT_SOL_PATH

Compile and deploy a contract to the specified blockchain. ABI will be written to directory of CONTRACT_SOL_PATH

optional arguments:
  -h, --help            show this help message and exit
  --blockchain-host BLOCKCHAIN_HOST
                        the hostname of the Blockchain where the data will be published to
  --contract-sol-path CONTRACT_SOL_PATH
                        the file path to the abi file of the contract in use
```


## Program outline:
  - Init blockchain connection
  - Init Mqtt connection
  - start listening on mqtt messages in output queue of the broker
