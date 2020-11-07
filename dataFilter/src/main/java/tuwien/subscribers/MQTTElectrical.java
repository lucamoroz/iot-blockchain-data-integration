package tuwien.subscribers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import tuwien.models.ElectricalRecord;

@Component
public class MQTTElectrical implements CommandLineRunner, IMqttMessageListener {

    @Autowired
    TaskExecutor executor;

    @Autowired
    MqttClient mqttClient;

    @Value("${electricalTopic}")
    private String electricalTopic;

    @Value("${electricalFilteredTopic}")
    private String electricalFilteredTopic;

    // Executed after all beans have been initialized
    @Override
    public void run(String... args) {
        executor.execute(() -> {
            // Subscribe to electrical data topic
            try {
                mqttClient.subscribe(electricalTopic, this);
                System.out.println("Subscribed to " + electricalTopic);
            } catch (MqttException e) {
                throw new RuntimeException("Unable to subscribe to " + electricalTopic + ": " + e.getMessage());
            }

        });
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (!topic.equals(electricalTopic)) return;

        ObjectMapper mapper = new ObjectMapper();
        ElectricalRecord er;
        try {
            String json = new String(message.getPayload());
            er = mapper.readValue(json, ElectricalRecord.class);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't parse: " + message.toString());
            return;
        }
        System.out.println("Received: " + er.toString());

        if (isToFilter(er)) return;

        message.setQos(2);
        try {
            mqttClient.publish(electricalFilteredTopic, message);
        } catch (MqttException e) {
            System.out.println("Unable to forward message: " + e.getMessage());
        }
    }

    private boolean isToFilter(ElectricalRecord record) {
        // TODO
        return false;
    }

    /*private void publish(ElectricalRecord record) {

        String content      = "test message";
        String clientId     = "ElectricalDataFilter";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            // Deliver exatly once
            message.setQos(2);
            sampleClient.publish(electricalFilteredTopic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }*/
}
