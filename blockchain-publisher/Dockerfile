# Base image
FROM python:3.8

# Install our package manager
RUN pip install pipenv

# Set the working directory
WORKDIR /app

# Copy only the necessary files to install the deps
COPY Pipfile* ./

# Create the requirements file for pip to install the deps
RUN pipenv lock -r > requirements.txt && \
    pip install -r requirements.txt

# Copy rest 
COPY . .

ENV CONTRACT_SOL_PATH ./src/contracts/SensorData.sol

# Execute
CMD ./wait-for-it.sh $BLOCKCHAIN_HOST -- \
    ./wait-for-it.sh $MQTT_HOST:$MQTT_PORT -- \
    python -u src/blockchain_publisher.py \
        --mqtt-host $MQTT_HOST \
        --mqtt-port $MQTT_PORT \
        --blockchain-host "http://$BLOCKCHAIN_HOST" \
        --topic $MQTT_TOPIC \
        --contract-sol-path $CONTRACT_SOL_PATH
