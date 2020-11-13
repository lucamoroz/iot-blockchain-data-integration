package tuwien;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class Application implements MqttCallback {

	@Value("${broker}")
	private String broker;

	@Autowired
	MqttClient mqttClient;

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	/**
	 * Define the executor for the application's tasks (e.g. MQTT subscribers).
	 * The executor is a SyncTaskExecutor: each invocation takes place in the calling thread.
	 * @return TaskExecutor
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		return new SyncTaskExecutor();
	}

	@Bean
	MqttClient mqttClient() {
		try {
			MqttClient client = new MqttClient(broker, "filter", new MemoryPersistence());

			MqttConnectOptions options = new MqttConnectOptions();
			// TODO which type of session?
			options.setCleanSession(false);
			options.setAutomaticReconnect(true);
			client.setCallback(this);
			client.connect(options);

			System.out.println("Connected to: " + broker);

			return client;
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("MQTT connection lost: " + cause.getMessage());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// Messages non consumed here
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

	}

	@PreDestroy
	public void destroy() throws Exception {
		System.out.println("Closing...");
		mqttClient.disconnect();
		mqttClient.close();
	}

}
