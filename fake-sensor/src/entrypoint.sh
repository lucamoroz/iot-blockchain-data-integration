#!/usr/bin/env bash
python3 -u fakesensor.py --host "$HOST" --port "$PORT" --topic "$TOPIC" --columns $COLUMNS --time time --replay-speed "$REPLAY_SPEED" $FILES
