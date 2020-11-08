import os.path

import pandas as pd
import time


class FileReplay:
    _running: bool
    _data = None

    def __init__(self):
        self._running = True

    def load(self, path_to_file: str, time_column: str = "time"):
        if not os.path.isfile(path_to_file):
            raise FileNotFoundError

        if not path_to_file.lower().endswith(".csv"):
            raise ValueError("Only .csv files are supported")

        df = pd.read_csv(path_to_file, index_col=time_column)
        df.index = pd.to_datetime(df.index, unit='s')

        if self._data is None:
            self._data = df
        else:
            self._data = self._data.append(df)

    def play(self, replay_speed: float = 1.0):
        if self._data is None:
            print("No data to play\n Hint: use load(self, path_to_file: str) to load data from a csv file")
            return

        print("Replaying %d measurements at speed %.2f\n" % (len(self._data), replay_speed))

        for index, row in self._data.iterrows():
            if not self._running:
                return
            print(index, row)
            time.sleep(1)

    def stop(self):
        self._running = False
        print("Will stop playing after sleep cycle")
