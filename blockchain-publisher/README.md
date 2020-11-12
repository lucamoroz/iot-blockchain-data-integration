# Blockchain Publisher

## Outline of the python program
1. Deploy the contract if necessary
2. Setup conntections to blockchain and mqtt
3. Loop over messages doing:
  - Receive data
  - Decode it
  - Transact via contract

```
usage: blockchain_publisher.py [-h] --mqtt-host MQTT_HOST [--mqtt-port MQTT_PORT] --topic TOPIC --blockchain-host BLOCKCHAIN_HOST [--contract-sol-path CONTRACT_SOL_PATH] [--contract-abi-path CONTRACT_ABI_PATH]
                               [--contract-address CONTRACT_ADDRESS]

Consume data from the MQTT broker and transact it to the blockchain endlessly

optional arguments:
  -h, --help            show this help message and exit
  --mqtt-host MQTT_HOST
                        the hostname of the MQTT broker the messages will be consumed from
  --mqtt-port MQTT_PORT
                        the port of the MQTT broker the messages will be consumed from
  --topic TOPIC         the topic the messages will be consumed from
  --blockchain-host BLOCKCHAIN_HOST
                        the hostname of the Blockchain where the data will be published to
  --contract-sol-path CONTRACT_SOL_PATH
                        the file path to the source file of the contract in use. Note: Either contract-abi-path and contract-address must be given or the contract-sol-path. If only the source path is given,
                        the program will deploy the contract first to the blockchain.
  --contract-abi-path CONTRACT_ABI_PATH
                        the file path to the abi file of the contract in use
  --contract-address CONTRACT_ADDRESS
                        the address of the contract in use
```

## The Contract
Located at src/contracts/SensorData.sol provides functions for adding and getting data to/of a simple array of items.
