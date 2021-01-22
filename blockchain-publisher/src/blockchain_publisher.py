import json
import os
import argparse
import datetime
import sys

from web3 import Web3
from util import compile_source_file, write_abi, deploy_contract
import paho.mqtt.client as mqtt


class BlockchainPublisher:

    def __init__(self, mqtt_host, mqtt_port, mqtt_topic, blockchain_host, account_prv_key, contract_sol_path=None, contract_address=None, contract_abi_path=None):
        self._mqtt_client = BlockchainPublisher.init_mqtt_client(
            mqtt_host, mqtt_port, mqtt_topic, self.handle_message)
        self._w3 = Web3(Web3.HTTPProvider(blockchain_host))

        self._account = self._w3.eth.account.privateKeyToAccount(account_prv_key)

        print(f'Using account {self._account.address}')

        # Deploy if no ABI and address is given
        if contract_abi_path is not None and contract_address is not None:
            with open(contract_abi_path, 'r') as f:
                contract_abi = f.read()
        elif contract_sol_path is not None:
            contract_address, contract_abi = deploy_contract(self._w3, contract_sol_path, self._account)
        else:
            print(
                'Either contract-abi-path and contract-address must be given or the contract-sol-path!')
            sys.exit(1)

        self._contract = self._w3.eth.contract(
            address=self._w3.toChecksumAddress(contract_address), abi=contract_abi)

    @staticmethod
    def init_mqtt_client(mqtt_host, mqtt_port, mqtt_topic, on_message):
        client = mqtt.Client()
        client.on_message = on_message
        client.connect(mqtt_host, mqtt_port)
        client.subscribe(mqtt_topic)
        return client

    def handle_message(self, client, userdata, msg):
        print(f'[{datetime.datetime.now()}] {msg.topic} {msg.payload}', end='')

        nonce = self._w3.eth.getTransactionCount(self._account.address)
        txn = self._contract.functions.addDataItem(msg.payload).buildTransaction(
            {'gas': 200000, 'gasPrice': self._w3.toWei('1', 'gwei'), 'nonce': nonce})
        signed_txn = self._w3.eth.account.sign_transaction(txn, private_key=self._account.privateKey)
        tx_hash = self._w3.eth.sendRawTransaction(signed_txn.rawTransaction)
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
                            help='The file path to the source .sol-file of the contract in use. Note: Either contract-abi-path and contract-address must be given or the contract-sol-path. If only the source path is given, the program will deploy the contract first to the blockchain.')
    arg_parser.add_argument('--contract-abi-path', type=str,
                            help='the file path to the abi file of the contract in use')
    arg_parser.add_argument('--contract-address', type=str,
                            help='The address of the contract in use')
    arg_parser.add_argument('--account-private-key', type=str,
                            help='The private key of the account to use.')

    return arg_parser.parse_args()



if __name__ == '__main__':

    args = parse_args()

    print(f'ARGS: {args}')

    pub = BlockchainPublisher(args.mqtt_host,
                              args.mqtt_port,
                              args.topic,
                              args.blockchain_host,
                              args.account_private_key,
                              args.contract_sol_path,
                              args.contract_address,
                              args.contract_abi_path)

    pub.run()

