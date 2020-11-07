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
import tuwien.models.WeatherRecord;

@Component
public class MQTTWeather implements CommandLineRunner, IMqttMessageListener {

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Value("${weatherTopic}")
    private String weatherTopic;

    @Value("${weatherFilteredTopic}")
    private String weatherFilteredTopic;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            try {
                mqttClient.subscribe(weatherTopic, this);
                System.out.println("Subscribed to " + weatherTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + weatherTopic + ": " + e.getMessage());
            }
        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (!topic.equals(weatherTopic)) return;

        ObjectMapper mapper = new ObjectMapper();
        WeatherRecord wr;
        try {
            String json = new String(message.getPayload());
            wr = mapper.readValue(json, WeatherRecord.class);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't parse: " + message.toString());
            return;
        }
        System.out.println("Received: " + wr.toString());

        if (isToFilter(wr)) return;

        message.setQos(2);
        try {
            mqttClient.publish(weatherFilteredTopic, message);
        } catch (MqttException e) {
            System.out.println("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(WeatherRecord record) {
        // TODO
        return false;
    }
}
