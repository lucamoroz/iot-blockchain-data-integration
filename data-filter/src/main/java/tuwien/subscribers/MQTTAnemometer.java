package tuwien.subscribers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import tuwien.filters.AnemometerFilter;
import tuwien.models.AnemometerRecord;

import java.util.logging.Logger;

@Component
public class MQTTAnemometer implements CommandLineRunner, IMqttMessageListener {
    private final static Logger LOGGER = Logger.getLogger(MQTTAnemometer.class.getName());

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Autowired
    AnemometerFilter filter;

    @Autowired
    private Environment environment;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            // Subscribe to electrical data topic
            String anemometerTopic = environment.getRequiredProperty("MQTT_ANEMOMETER_TOPIC");
            try {
                mqttClient.subscribe(anemometerTopic, this);
                LOGGER.info("Subscribed to " + anemometerTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + anemometerTopic + ": " + e.getMessage());
            }

        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String anemometerFilteredTopic = environment.getRequiredProperty("MQTT_ANEMOMETER_FILTERED_TOPIC");

        ObjectMapper mapper = new ObjectMapper();
        AnemometerRecord ar;
        try {
            String json = new String(message.getPayload());
            ar = mapper.readValue(json, AnemometerRecord.class);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", message.toString(), e.getMessage()));
            return;
        }

        if (isToFilter(ar)) {
            LOGGER.info("Filtered: " + ar.toString());
            return;
        } else {
            LOGGER.info(String.format("Publishing to topic %s: %s", anemometerFilteredTopic, ar.toString()));
        }

        message.setQos(2);
        try {
            mqttClient.publish(anemometerFilteredTopic, message);
        } catch (MqttException e) {
            LOGGER.severe("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(AnemometerRecord record) {
        return filter.isToFilter(record);
    }
}
