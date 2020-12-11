package tuwien.subscribers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import tuwien.models.ElectricalRecord;

import java.util.logging.Logger;

@Component
public class MQTTElectrical implements CommandLineRunner, IMqttMessageListener {

    private final static Logger LOGGER = Logger.getLogger(MQTTElectrical.class.getName());

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Autowired
    private Environment environment;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            // Subscribe to electrical data topic
            String electricalTopic = environment.getRequiredProperty("MQTT_ELECTRICAL_TOPIC");
            try {
                mqttClient.subscribe(electricalTopic, this);
                LOGGER.info("Subscribed to " + electricalTopic);

            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + electricalTopic + ": " + e.getMessage());
            }

        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String electricalFilteredTopic = environment.getRequiredProperty("MQTT_ELECTRICAL_FILTERED_TOPIC");

        ObjectMapper mapper = new ObjectMapper();
        ElectricalRecord er;
        try {
            String json = new String(message.getPayload());
            er = mapper.readValue(json, ElectricalRecord.class);
        } catch (JsonProcessingException e) {
            LOGGER.severe(String.format("Couldn't parse: %s - Error: %s", message.toString(), e.getMessage()));
            return;
        }

        if (isToFilter(er)) {
            LOGGER.info("Filtered: " + er.toString());
            return;
        } else {
            LOGGER.info(String.format("Publishing to topic %s: %s", electricalFilteredTopic, er.toString()));
        }

        message.setQos(2);
        try {
            mqttClient.publish(electricalFilteredTopic, message);
        } catch (MqttException e) {
            LOGGER.severe("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(ElectricalRecord record) {
        // TODO
        return false;
    }
}
