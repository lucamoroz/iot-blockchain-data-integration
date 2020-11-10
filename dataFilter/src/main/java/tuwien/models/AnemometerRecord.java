package tuwien.models;

import lombok.Value;

@Value
public class AnemometerRecord {
    float windSpeed;
    float windBearing;
    String time;
}
