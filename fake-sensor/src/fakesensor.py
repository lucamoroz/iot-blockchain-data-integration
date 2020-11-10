import argparse
import signal

import sys

from basepublisher import BasePublisher
from filereplay import FileReplay
from mqttpublisher import MqttPublisher

file_player: FileReplay
arg_parser: argparse.ArgumentParser
args: argparse.Namespace


def sigterm_handler(_signo, _stack_frame):
    print("\nGot signal " + str(_signo))
    if file_player:
        file_player.stop()


def setup_args_parser():
    global arg_parser
    global args
    arg_parser = argparse.ArgumentParser(description='Send data from a csv file to a topic in an MQTT broker')
    arg_parser.add_argument('files', metavar='FILE', type=str, nargs='+',
                            help='file names of csv file data will be read from')
    arg_parser.add_argument('--host', type=str, nargs=1, required=True,
                            help='the hostname of the MQTT broker the messages will be published to')
    arg_parser.add_argument('--port', type=int, nargs=1, default=1883,
                            help='the port of the MQTT broker the messages will be published to')
    arg_parser.add_argument('--topic', type=str, nargs=1, required=True,
                            help='the topic the messages will be published to')
    arg_parser.add_argument('--columns', type=str, nargs='+', required=True,
                            help='the name of the columns in the csv file that will be sent as messages')
    arg_parser.add_argument('--replay-speed', type=float, nargs=1, default=[1.0],
                            help='the speed the messages will be replayed at')
    arg_parser.add_argument('--time', type=str, nargs=1, default=['time'],
                            help='the column name that contains the unix timestamps')
    arg_parser.add_argument('--print-only', type=bool, nargs=1, default=[False],
                            help='only prints the messages to stdout instead of publishing to a topic (no broker '
                                 'required)')
    args = arg_parser.parse_args()


def setup_signal_handlers():
    print("Registering signal handler")
    signal.signal(signal.SIGTERM, sigterm_handler)
    signal.signal(signal.SIGINT, sigterm_handler)


print("argv: ", sys.argv)
if __name__ == '__main__':
    setup_args_parser()
    setup_signal_handlers()

    print("Starting fake sensor")
    if args.files:
        print("Reading %d file(s)" % len(args.files))
    else:
        raise ValueError("No file paths")

    if args.print_only[0]:
        publisher = BasePublisher()
    else:
        publisher = MqttPublisher(args.host[0], args.port[0], args.topic[0])

    file_player = FileReplay(publisher)

    for file in args.files:
        try:
            file_player.load(file, args.columns, args.time[0])
        except FileNotFoundError:
            print("File %s does not exist" % file)
            exit(1)

    file_player.play(args.replay_speed[0])

    print("[end of program]")
