import signal

from filereplay import FileReplay

file_player: FileReplay


def sigterm_handler(_signo, _stack_frame):
    print("\nGot signal " + str(_signo))
    if file_player:
        file_player.stop()


if __name__ == '__main__':
    print("Starting fake sensor")

    print("Registering signal handler")
    signal.signal(signal.SIGTERM, sigterm_handler)
    signal.signal(signal.SIGINT, sigterm_handler)

    file_player = FileReplay()

    file_player.load("/Users/Konstantin/IntelliJProjects/IOT20_Assignment/fake-sensor/data/HomeA/homeA2014.csv")

    file_player.play()

    print("[end of program]")
