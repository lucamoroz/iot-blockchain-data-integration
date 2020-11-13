import json
import os
import argparse
import datetime
import sys

from web3 import Web3
import paho.mqtt.client as mqtt
from deploy_contract import deploy_contract


class BlockchainPublisher:

    def __init__(self, mqtt_host, mqtt_port, mqtt_topic, blockchain_host, contract_address, contract_abi):
        self._mqtt_client = BlockchainPublisher.init_mqtt_client(
            mqtt_host, mqtt_port, mqtt_topic, self.handle_message)
        self._w3 = Web3(Web3.HTTPProvider(blockchain_host))
        self._w3.eth.defaultAccount = self._w3.eth.accounts[0]
        self._contract = self._w3.eth.contract(
            address=contract_address, abi=contract_abi)

    @staticmethod
    def init_mqtt_client(mqtt_host, mqtt_port, mqtt_topic, on_message):
        client = mqtt.Client()
        client.on_message = on_message
        client.connect(mqtt_host, mqtt_port)
        client.subscribe(mqtt_topic)
        return client

    def handle_message(self, client, userdata, msg):
        print(f'[{datetime.datetime.now()}] {msg.topic} {msg.payload}', end='')

        # TODO Probably better to stay in binary
        # Decode message
        decoded = msg.payload.decode('ascii')
        decoded = decoded.strip('"]["').split('", "')

        tx_hash = self._contract.functions.addDataItem(
            *tuple(decoded)).transact()
        tx_receipt = self._w3.eth.waitForTransactionReceipt(tx_hash)
        print(f' -> {tx_receipt.gasUsed} {tx_receipt.transactionHash.hex()}')

    def run(self):
        print('Running loop forever...')
        self._mqtt_client.loop_forever()


def parse_args():
    arg_parser = argparse.ArgumentParser(
        description='Consume data from the MQTT broker and transact it to the blockchain endlessly')
    arg_parser.add_argument('--mqtt-host', type=str, required=True,
                            help='the hostname of the MQTT broker the messages will be consumed from')
    arg_parser.add_argument('--mqtt-port', type=int, default=1883,
                            help='the port of the MQTT broker the messages will be consumed from')
    arg_parser.add_argument('--topic', type=str, required=True,
                            help='the topic the messages will be consumed from')
    arg_parser.add_argument('--blockchain-host', type=str, required=True,
                            help='the hostname of the Blockchain where the data will be published to')
    arg_parser.add_argument('--contract-sol-path', type=str,
                            help='the file path to the source file of the contract in use. Note: Either contract-abi-path and contract-address must be given or the contract-sol-path. If only the source path is given, the program will deploy the contract first to the blockchain.')
    arg_parser.add_argument('--contract-abi-path', type=str,
                            help='the file path to the abi file of the contract in use')
    arg_parser.add_argument('--contract-address', type=str,
                            help='the address of the contract in use')

    return arg_parser.parse_args()


def main():

    args = parse_args()

    mqtt_host = args.mqtt_host
    mqtt_port = args.mqtt_port
    mqtt_topic = args.topic
    blockchain_host = args.blockchain_host
    contract_sol_path = args.contract_sol_path
    contract_abi_path = args.contract_abi_path
    contract_address = args.contract_address

    # Deploy if no ABI or address is given
    if contract_abi_path is None or contract_address is None:
        if contract_sol_path:
            contract_address, contract_abi = deploy_contract(
                blockchain_host, contract_sol_path)
        else:
            print(
                'Either contract-abi-path and contract-address must be given or the contract-sol-path!')
            sys.exit(1)
    else:
        with open(contract_abi_path, 'r') as f:
            contract_abi = f.read()

    pub = BlockchainPublisher(mqtt_host, mqtt_port,
                              mqtt_topic, blockchain_host, contract_address, contract_abi)
    pub.run()


if __name__ == '__main__':
    main()
