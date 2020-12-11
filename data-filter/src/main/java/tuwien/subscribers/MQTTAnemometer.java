package tuwien.subscribers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import tuwien.models.AnemometerRecord;

@Component
public class MQTTAnemometer implements CommandLineRunner, IMqttMessageListener {

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Value("${anemometerTopic}")
    private String anemometerTopic;

    @Value("${anemometerFilteredTopic}")
    private String anemometerFilteredTopic;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            // Subscribe to electrical data topic
            try {
                mqttClient.subscribe(anemometerTopic, this);
                System.out.println("Subscribed to " + anemometerTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + anemometerTopic + ": " + e.getMessage());
            }

        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ObjectMapper mapper = new ObjectMapper();
        AnemometerRecord ar;
        try {
            String json = new String(message.getPayload());
            ar = mapper.readValue(json, AnemometerRecord.class);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't parse: " + message.toString() + " - Error: " + e.getMessage());
            return;
        }
        System.out.println("Received: " + ar.toString());

        if (isToFilter(ar)) return;

        message.setQos(2);
        try {
            mqttClient.publish(anemometerFilteredTopic, message);
        } catch (MqttException e) {
            System.out.println("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(AnemometerRecord record) {
        // TODO
        return false;
    }
}
