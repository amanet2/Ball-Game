import json
from sys import path
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
path.append(dir_path)
from custom.sv_checkgamestate import check_game_state
"""
TODO:
1. Needs debugging
    * Java hook needs to be able to handle stream of messages e.g. exception output
2. Needs to parse incoming state into real JSON object so values can be changed and returned
"""
do_break = False
while True:
    java_state_str = input("Enter message...\n")
    if do_break:
        break
    if java_state_str == "exit":
        print("Exiting Python...")
        do_break = True
    else:
        # 2. Needs to parse incoming state into real JSON object so values can be changed and returned
        java_state_str_json = java_state_str.replace(
            ':', '/').replace(
            '=', '"="').replace(
            ',', '","').replace(
            '"="', '":"').replace(
            '{', '{"').replace(
            ':"{', ':{').replace(
            '}', '"}').replace(
            '}"', '}')
        java_state_dict = None
        try:
            java_state_dict = json.loads(java_state_str_json)
            check_game_state(java_state_dict)
        except Exception as e:
            # 1. Needs more debugging info e.g. line number
            print(str(e))
        else:
            # print(json.dumps(java_state_dict))
            print(json.dumps(java_state_dict).replace(' ', '').replace(':', '=').replace('/', ':').replace('"', ''))
exit()
