package tuwien;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PreDestroy;
import java.util.logging.Logger;

@SpringBootApplication
public class Application implements MqttCallback {
	private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

	@Autowired
	private Environment environment;

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
			String broker = environment.getRequiredProperty("MQTT_HOST");
			MqttClient client = new MqttClient("tcp://" + broker, "filter", new MemoryPersistence());

			MqttConnectOptions options = new MqttConnectOptions();

			options.setCleanSession(false);
			options.setAutomaticReconnect(true);
			client.setCallback(this);
			LOGGER.info("Connecting to: " + broker);
			client.connect(options);

			LOGGER.info("Connected to: " + broker);

			return client;
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.info("MQTT connection lost: " + cause.getMessage());
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
		LOGGER.info("Closing...");
		mqttClient.disconnect();
		mqttClient.close();
	}

}
