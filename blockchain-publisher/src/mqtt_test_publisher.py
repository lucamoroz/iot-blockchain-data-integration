import time
import json
import datetime
import paho.mqtt.client as mqtt
import random
import string

MQTT_TOPIC = '/homea/multimeter'
MQTT_BROKER_HOST = '127.0.0.1'
MQTT_BROKER_PORT = 1883

def random_length_ascii_string():
    return ''.join(random.choice(string.ascii_letters) for x in range(random.randint(1,10)))

def main():
    try:
        client = mqtt.Client()
        client.connect(MQTT_BROKER_HOST, MQTT_BROKER_PORT)

        for i in range(200):
            # Random sized tuple
            data = (f'data {i}', str(datetime.datetime.now()))
            data += tuple(random_length_ascii_string())

            print(f'Publishing {data}')
            client.publish(MQTT_TOPIC, json.dumps(data))
            time.sleep(10)

    except Exception as e:
        print(f'At least we tried! {e}')

if __name__ == '__main__':
    main()
