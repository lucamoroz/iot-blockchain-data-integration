import json
import os
import sys
import argparse
import solcx

from web3 import Web3


def compile_source_file(file_path):
    """Read and compile the source file with the solc Solidity compiler.
    If the compiler is not available, it will be installed.
    """
    # Check if solc compiler is available otherwise install
    try:
        print(solcx.get_solc_version())
    except solcx.exceptions.SolcNotInstalled as e:
        print(e)
        version = solcx.install_solc('0.7.4')
        print(f'Installed version: {version}')

    with open(file_path, 'r') as f:
        source = f.read()

    return solcx.compile_source(source)


def write_abi(abi, output_path):
    output_path = os.path.splitext(output_path)[0] + '.abi'
    with open(output_path, 'w') as f:
        f.write(json.dumps(abi))
    return output_path


def deploy_contract(w3, contract_file_path, account):
    """Deploy the contract by compiling the source file, creating the contract transaction, 
    signing it with the given account and sending it.
    """
    # Compile
    compiled_sol = compile_source_file(contract_file_path)

    # Destruct
    contract_id, contract_interface = compiled_sol.popitem()

    txn = w3.eth.contract(
        abi=contract_interface['abi'],
        bytecode=contract_interface['bin']).constructor().buildTransaction()

    txn.pop('to', None)
    txn['from'] = account.address
    txn['nonce'] = w3.eth.getTransactionCount(account.address)

    signed_txn = w3.eth.account.sign_transaction(txn, account.privateKey)
    tx_hash = w3.eth.sendRawTransaction(signed_txn.rawTransaction)
    tx_receipt = w3.eth.waitForTransactionReceipt(tx_hash)

    address = tx_receipt['contractAddress']
    print(
        f'Deployed contract! Address: {address} Transaction {tx_receipt.transactionHash.hex()}')

    # Write ABI to contract_file_path
    abi_path = write_abi(contract_interface['abi'], contract_file_path)
    print(f'Wrote abi to {abi_path}')

    return (address, contract_interface['abi'])