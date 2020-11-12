import json
import os
import sys
import argparse
import solcx

from web3 import Web3


def compile_source_file(file_path):
    # Check if solc compiler is available otherwise install
    try:
        print(solcx.get_solc_version())
    except solcx.exceptions.SolcNotInstalled as e:
        print(e)
        version = solcx.install_solc()
        print(f'Installed version: {version}')

    with open(file_path, 'r') as f:
      source = f.read()

    return solcx.compile_source(source)


def transact_contract(w3, contract_interface):
    tx_hash = w3.eth.contract(
        abi=contract_interface['abi'],
        bytecode=contract_interface['bin']).constructor().transact()

    tx_receipt = w3.eth.getTransactionReceipt(tx_hash)
    return tx_receipt


def write_abi(abi, output_path):
    output_path = os.path.splitext(output_path)[0] + '.abi'
    with open(output_path, 'w') as f:
        f.write(json.dumps(abi))
    return output_path


def parse_args():
    arg_parser = argparse.ArgumentParser(
        description='Compile and deploy a contract to the specified blockchain. ABI will be written to directory of CONTRACT_SOL_PATH')
    arg_parser.add_argument('--blockchain-host', type=str, required=True,
                            help='the hostname of the Blockchain where the data will be published to')
    arg_parser.add_argument('--contract-sol-path', type=str, required=True,
                            help='the file path to the source file of the contract in use')
    return arg_parser.parse_args()


def deploy_contract(blockchain_host, contract_file_path):
    
    # Setup connection
    w3 = Web3(Web3.HTTPProvider(blockchain_host))

    if not w3.isConnected():
        print('Could not connect to ' + blockchain_host)
        sys.exit(1)

    # Default address
    w3.eth.defaultAccount = w3.eth.accounts[0]

    # Compile 
    compiled_sol = compile_source_file(contract_file_path)

    # Destruct
    contract_id, contract_interface = compiled_sol.popitem()

    # Deploy
    tx_receipt = transact_contract(w3, contract_interface)
    
    address = tx_receipt['contractAddress']
    print(f'Deployed contract! Address: {address} Transaction {tx_receipt.transactionHash.hex()}')

    # Write ABI to contract_file_path
    abi_path = write_abi(contract_interface['abi'], contract_file_path)
    print(f'Wrote abi to {abi_path}')

    return (address, contract_interface['abi'])


def main():

    args = parse_args()

    blockchain_host = args.blockchain_host      # 'http://127.0.0.1:7545'
    contract_file_path = args.contract_sol_path # './src/contracts/SensorData.sol'

    # Deploy 
    transact_contract(blockchain_host, contract_file_path)
    

if __name__ == '__main__':
    main()
