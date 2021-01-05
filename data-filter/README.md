
## Build
- With docker:
`sudo docker build -t tuwien/iot .`
- With Maven:
`sudo ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=tuwien/iot`

## Run
`sudo docker run -p 8080:8080 tuwien/iot`

## Configuration
Set the following environment variables:
- `MQTT_HOST` address of the MQTT broker
- `MQTT_ANEMOMETER_TOPIC`
- `MQTT_ANEMOMETER_FILTERED_TOPIC` where anemometer filtered data will be published
- `MQTT_MULTIMETER_TOPIC`
- `MQTT_MULTIMETER_FILTERED_TOPIC` where multimeter filtered data will be published
- `MQTT_ELECTRICAL_TOPIC`
- `MQTT_ELECTRICAL_FILTERED_TOPIC` where electrical filtered data will be published

Filtering parameters are saved in the folder `filters/`, following the JSON format.
See REST Endpoints section for more info: the format is the same.


## Notes
- Allow IDEA IDE to process lombok annotations by install plugin lombok.

## Sensors REST Endpoints

### Anemometer

- `GET {HOST}/filters/anemometer/`
Returns JSON data of the filter, e.g.:
```
{
    "windSpeedConstraint": {
        "value": 7,
        "comparison": "GREATER"
    },
    "windBearingConstraint": {
        "value": 280,
        "comparison": "GREATER_OR_EQUAL"
    }
}
```

- `POST {HOST}/filters/anemometer/`
Accepts anemometer filters as JSON body (see GET above).
Valid comparisons are:  LESS, LESS_OR_EQUAL, EQUAL, GREATER_OR_EQUAL, GREATER.

The server replies with the new filter data  as JSON if correctly updated, otherwise it replies with the old filter data as JSON.

### Multimeter

- `GET {HOST}/filters/multimeter/`
Returns JSON data of the filter, e.g.:
```
{
    "temperatureConstraint": {
        "value": 50,
        "comparison": "GREATER_OR_EQUAL"
    },
    "humidityConstraint": {
        "value": 20,
        "comparison": "LESS"
    },
    "pressureConstraint": {
        "value": 5,
        "comparison": "GREATER"
    }
}
```

- `POST {HOST}/filters/multimeter/`
Accepts anemometer filters as JSON body (see GET above).
Valid comparisons are:  LESS, LESS_OR_EQUAL, EQUAL, GREATER_OR_EQUAL, GREATER.

The server replies with the new filter data  as JSON if correctly updated, otherwise it replies with the old filter data as JSON.

### Electrical
- `GET {HOST}/filters/electrical/`
  Returns JSON data of the filter, e.g.:
```
{
    "valueConstraint": {
        "value": 300,
        "comparison": "GREATER_OR_EQUAL"
    }
}
```

Where value is the electrical power usage.

- `POST {HOST}/filters/electrical/`
  Accepts anemometer filters as JSON body (see GET above).
  Valid comparisons are:  LESS, LESS_OR_EQUAL, EQUAL, GREATER_OR_EQUAL, GREATER.

The server replies with the new filter data as JSON if correctly updated, otherwise it replies with the old filter data as JSON.