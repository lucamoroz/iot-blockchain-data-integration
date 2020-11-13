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
import tuwien.models.MultimeterRecord;

@Component
public class MQTTMultimeter implements CommandLineRunner, IMqttMessageListener {

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Value("${multimeterTopic}")
    private String multimeterTopic;

    @Value("${multimeterFilteredTopic}")
    private String multimeterFilteredTopic;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            try {
                mqttClient.subscribe(multimeterTopic, this);
                System.out.println("Subscribed to " + multimeterTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + multimeterTopic + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ObjectMapper mapper = new ObjectMapper();
        MultimeterRecord wr;
        try {
            String json = new String(message.getPayload());
            wr = mapper.readValue(json, MultimeterRecord.class);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't parse: " + message.toString() + " - Error: " + e.getMessage());
            return;
        }
        System.out.println("Received: " + wr.toString());

        if (isToFilter(wr)) return;

        message.setQos(2);
        try {
            mqttClient.publish(multimeterFilteredTopic, message);
        } catch (MqttException e) {
            System.out.println("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(MultimeterRecord record) {
        // TODO
        return false;
    }
}
