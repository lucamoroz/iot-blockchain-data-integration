import os.path

import pandas as pd
import time


class FileReplay:
    _path_to_file: str
    _running: bool
    _data = None

    def __init__(self):
        self._running = True

    def load(self, path_to_file: str):
        self._path_to_file = path_to_file
        if not os.path.isfile(self._path_to_file):
            raise FileNotFoundError

        if not self._path_to_file.lower().endswith(".csv"):
            raise ValueError("Only .csv files are supported")
        self._data = pd.read_csv(self._path_to_file, index_col='time')

        self._data.index = pd.to_datetime(self._data.index, unit='s')

    def play(self):
        if self._data is None:
            print("No data to play\n Hint: use load(self, path_to_file: str) to load data from a csv file")
            return

        for index, row in self._data.iterrows():
            if not self._running:
                print("Stopping reading from file %s" % self._path_to_file)
                return
            print(index, row)
            time.sleep(1)

    def stop(self):
        self._running = False
        print("Will stop playing after sleep cycle")
