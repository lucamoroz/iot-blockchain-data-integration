package tuwien.models;

import lombok.Value;

@Value
public class WeatherRecord {

    float temperature;
    String icon;
    float humidity;
    float visibility;
    String summary;
    float apparentTemperature;
    float pressure;
    float windSpeed;
    float cloudCover;
    long time;
    int windBearing;
    float precipIntensity;
    float dewPoint;
    float precipProbability;
}
