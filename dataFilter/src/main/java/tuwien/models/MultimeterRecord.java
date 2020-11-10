package tuwien.models;

import lombok.Value;

@Value
public class MultimeterRecord {

    float temperature;
    float humidity;
    float pressure;
    String time;
}
