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
import tuwien.filters.MultimeterFilter;
import tuwien.models.MultimeterRecord;

import java.util.logging.Logger;

@Component
public class MQTTMultimeter implements CommandLineRunner, IMqttMessageListener {
    private final static Logger LOGGER = Logger.getLogger(MQTTMultimeter.class.getName());

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Autowired
    MultimeterFilter filter;

    @Autowired
    private Environment environment;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            String multimeterTopic = environment.getRequiredProperty("MQTT_MULTIMETER_TOPIC");
            try {
                mqttClient.subscribe(multimeterTopic, this);
                LOGGER.info("Subscribed to " + multimeterTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + multimeterTopic + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String multimeterFilteredTopic = environment.getRequiredProperty("MQTT_MULTIMETER_FILTERED_TOPIC");

        ObjectMapper mapper = new ObjectMapper();
        MultimeterRecord mr;
        try {
            String json = new String(message.getPayload());
            mr = mapper.readValue(json, MultimeterRecord.class);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", message.toString(), e.getMessage()));
            return;
        }

        if (isToFilter(mr)) {
            LOGGER.info("Filtered: " + mr.toString());
            return;
        } else {
            LOGGER.info(String.format("Publishing to topic %s: %s", multimeterFilteredTopic, mr.toString()));
        }

        message.setQos(2);
        try {
            mqttClient.publish(multimeterFilteredTopic, message);
        } catch (MqttException e) {
            LOGGER.severe("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(MultimeterRecord record) {
        return filter.isToFilter(record);
    }
}
