# Fake Sensor

This is program acts like a sensor device publishing data from csv files to a MQTT message broker.
The fake sensor programm can be used to emulate an IoT device (eg. [Anemometer](https://en.wikipedia.org/wiki/Anemometer)) measuring data (eg. wind speed, wind direction) and publishing those measurements to a specific topic.

## How to run

```
usage: fake-sensor.py [-h] --host HOST [--port PORT] --topic TOPIC --columns COLUMNS [COLUMNS ...] [--replay-speed REPLAY_SPEED] [--time TIME] FILE [FILE ...]

Send data from a csv file to a topic in an MQTT broker

positional arguments:
  FILE                  file names of csv file data will be read from

optional arguments:
  -h, --help            show this help message and exit
  --host HOST           the hostname of the MQTT broker the messages will be published to
  --port PORT           the port of the MQTT broker the messages will be published to
  --topic TOPIC         the topic the messages will be published to
  --columns COLUMNS [COLUMNS ...]
                        the name of the columns in the csv file that will be sent as messages
  --replay-speed REPLAY_SPEED
                        the speed the messages will be replayed at
  --time TIME           the column name that contains the unix timestamps
```

## Examples

### Anemometer
To emulate an anemometer publishing messages to a broker (at `127.0.0.1:1883` and topic `/homea/anemometer`) run the script with the following program arguments:
```shell script
python3 fake-sensor/src/fakesensor.py --host "127.0.0.1" --port 1883 --topic "/homea/anemometer" --columns windSpeed windBearing --replay-speed 1.0 --time "time" ../data/HomeA/homeA2014.csv ../data/HomeA/homeA2015.csv 
```

The file `./data/HomeA/homeA2014.csv` contains a set of sensor measurements like temperature, humidity, windSpeed, windBearing, etc.

| time        | temperature | ... | windSpeed | windBearing |
| -----------| ----- | --- |  ---  |  ---  |
| 1388552400 | 16.67 | ... | 11.23 |  271.0  |
| 1388556000 | 16.19 | ... | 9.92  |  268.0  |
| 1388559600 | 15.69 | ... | 8.92  |  266.0  |
|     ...    |  ...  | ... |  ...  |  ...  |

In order to emulate an anemometer from this data the fake sensor program can read the windSpeed and windBearing data from this file and produce MQTT messages.
Those messages will be published to a given topic of a MQTT message broker.

The message payload is in JSON format. Example:
```json
{
  "time": "2014-01-01 05:00:00.000000",
  "windSpeed": 11.23,
  "windBearing": 271.0
}
```