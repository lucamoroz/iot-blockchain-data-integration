#!/usr/bin/env bash
exec python3 -u fakesensor.py --host "$HOST" --port "$PORT" --topic "$TOPIC" --columns $COLUMNS --time time --replay-speed "$REPLAY_SPEED" --keep-connection "$KEEP_CONNECTION" --client-id "$CLIENT_ID" $FILES
