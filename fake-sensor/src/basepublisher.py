import json


class BasePublisher:
    def publish(self, data: dict, next_in_seconds: int = None):
        print(json.dumps(data, sort_keys=True, indent=4))

    def shutdown(self):
        print('Nothing to do')
