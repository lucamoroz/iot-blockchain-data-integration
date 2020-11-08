import json

import paho.mqtt.client as mqtt

from basepublisher import BasePublisher


class MqttPublisher(BasePublisher):
    def __init__(self, host: str, port: int, topic: str):
        self._topic = topic
        self._host = host
        self._port = port
        self._mqtt_client = mqtt.Client()

    def publish(self, data: dict):
        if not self._mqtt_client.is_connected():
            print("Connecting to broker %s:%d" % (self._host, self._port))
            self._mqtt_client.connect(self._host, self._port)
        serialized = json.dumps(data)
        print("Publishing %s to topic %s" % (str(serialized), self._topic))
        self._mqtt_client.publish(self._topic, serialized)
