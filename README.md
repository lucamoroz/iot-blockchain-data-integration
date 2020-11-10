
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
All components should be provided as [Docker](https://www.docker.com/) containers. 
For communication with the message broker the [MQTT](https://mqtt.org/) protocol will be used (message broker: [Eclipse Mosquitto](https://mosquitto.org/)).
The fake sensors will be implemented in [Python](https://www.python.org/).
The filters will be implemented with [Java](https://www.java.com/en/) and the [Spring Boot](https://spring.io/projects/spring-boot) framework.
The frontend will be use [Angular](https://angular.io/) as a framework.

## Components

TODO

## Requirements
- Maven 

## How to run
`$make start`


## How to debug

TODO
