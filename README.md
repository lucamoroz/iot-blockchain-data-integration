# Internet of Things for Smart Systems WS 2020 - Group 5 Topic B

Project Topic B – Blockchain-based Sensor Data Integration

## Team

Samir Duvelek (01426832)

David Kirchsteiger (01402259)

Luca Moroldo (12016307)

Konstantin Strümpf (01526204)

## Overview

TODO

## Architecture

In general the architecture follows a [pipes and filters](https://docs.microsoft.com/en-us/azure/architecture/patterns/pipes-and-filters) pattern for integrating the data from fake sensors into the blockchain.
![Architecture Diagram](./docs/img/architecture-diagram.png)

### Tech Stack
All components are provided as [Docker](https://www.docker.com/) containers. 
For communication with the message broker the [MQTT](https://mqtt.org/) protocol is used (message broker: [Eclipse Mosquitto](https://mosquitto.org/)).
For communication with the blockchain web3 is used in [js](https://web3js.readthedocs.io) and [python](https://web3py.readthedocs.io).
The fake sensors are implemented in [Python](https://www.python.org/).
The filters are implemented with [Java](https://www.java.com/en/) and the [Spring Boot](https://spring.io/projects/spring-boot) framework.
The frontend uses [Angular](https://angular.io/) as a framework.

## Components

TODO

## How to run
`$make start`

## How to run in the cloud
VM: http://35.207.109.228/

TODO


## Testnet

As a testnet we decided for [Kovan](https://kovan-testnet.github.io/website/), since it has the least block time (according to this [stackexchange-post](https://ethereum.stackexchange.com/a/30072)) and is supported by [Infura](https://infura.io/). We use Infura to connect to Kovan via Infura without requiring to run a full node. We use this [account](https://kovan.etherscan.io/address/0xdbd5927b822456c88b5e0a803ac08c26d2b6cb5e) to create the contract and transact the data.

## Deploy the contract

Depending on the commandline arguments the blockchain-publisher project is configured to either use an existing contract or deploy the contract by compiling its ´.sol´-file and make the contract-creation-transaction using the provided blockchain account. For details see its [usage description](./blockchain-publisher/README.md).

## How to debug
You can run all docker containers locally with the environment file for development:
```
    docker-compose --env-file .env.dev up --build
```

or run each sub project directly on your host by checking out the README files there.
