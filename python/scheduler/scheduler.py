class scheduler:
    def __init__(self):
        self.events_dict = {}
        self.current_event_queue = []

    def clear(self):
        self.events_dict = {}
        self.current_event_queue = []

    def put(self, time_millis, doable_item):
        if time_millis not in self.events_dict:
            self.events_dict[time_millis] = []
        self.events_dict[time_millis].append(doable_item)
