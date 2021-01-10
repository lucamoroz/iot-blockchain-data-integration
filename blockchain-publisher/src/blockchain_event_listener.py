import json
import datetime
import time
import sys

from web3 import Web3


def handle_event(w3, contract, event):
    print('EVENT:', event.args)


def log_loop(w3, contract, event_filter, poll_interval):
    while True:
        for event in event_filter.get_new_entries():
            handle_event(w3, contract, event)
            time.sleep(poll_interval)


if __name__ == '__main__':
    
    # ARGS
    BLOCKCHAIN_HOST = 'https://kovan.infura.io/v3/4c8b0ab9755f4828a2957ca6fd25f2f8'
    CONTRACT_ABI_FILE_PATH = './src/contracts/SensorData.abi'
    CONTRACT_ADDRESS = '0x6b651f25125964a084627e78e703799cf2bdf5ca'
    privateKey = ''

    with open('account_private_key.key')as f:
        privateKey = f.read()

    with open(CONTRACT_ABI_FILE_PATH) as f:
        abi = f.read()

    # Setup connection
    w3 = Web3(Web3.HTTPProvider(BLOCKCHAIN_HOST))
    
    if not w3.isConnected():
        print('Could not connect to ' + BLOCKCHAIN_HOST)
        sys.exit(1)

    # get account from private key
    account = w3.eth.account.privateKeyToAccount(privateKey)

    # Setup default account
    # w3.eth.defaultAccount = w3.eth.accounts[0]

    contract = w3.eth.contract(address=w3.toChecksumAddress(CONTRACT_ADDRESS), abi=abi)

    # possible to filter
    event_filter = contract.events.Notification.createFilter(fromBlock="latest")

    log_loop(w3, contract, event_filter, 4.0)

    # print(contract.functions.getLatestDataItem().call())
    # print(contract.functions.getDataItem(1).call())
