import json

import paho.mqtt.client as mqtt
import time

from basepublisher import BasePublisher


class MqttPublisher(BasePublisher):
    def __init__(self, host: str, port: int, topic: str, keep_connection: int, client_id: str = ''):
        self._keep_connection = keep_connection
        self._topic = topic
        self._host = host
        self._port = port
        self._mqtt_client = mqtt.Client(client_id)

    def publish(self, data: dict, next_in_seconds: int = None):
        self.connect_client()

        serialized = json.dumps(data)
        print("Publishing %s to topic %s" % (str(serialized), self._topic))
        self._mqtt_client.publish(self._topic, serialized)

        if next_in_seconds is None:
            self.disconnect_client()
        elif next_in_seconds > self._keep_connection:
            self.disconnect_client()
        else:
            print('Keeping connection, next value expected in %d seconds' % next_in_seconds)

    def shutdown(self):
        self.disconnect_client()

    def connect_client(self):
        if not self._mqtt_client.is_connected():
            print("Connecting to broker %s:%s" % (self._host, self._port))
            self._mqtt_client.connect(self._host, self._port)
            self._mqtt_client.loop_start()

        # Waiting for connection to be successful
        wait_count = 0.0
        while not self._mqtt_client.is_connected():
            wait_count += 1
            sleep_interval = 0.1
            # Raising exception if waiting too long
            if wait_count * sleep_interval > 5:
                raise ConnectionAbortedError("Failed to connect for %d seconds" % (wait_count * sleep_interval))
            time.sleep(sleep_interval)
            print('Waiting for connection...')

    def disconnect_client(self):
        if self._mqtt_client.is_connected():
            print('Disconnecting from MQTT broker')
            self._mqtt_client.loop_stop()
            self._mqtt_client.disconnect()
        else:
            print('Already disconnected')
