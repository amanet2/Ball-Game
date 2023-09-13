import json
"""
TODO:
1. Needs debugging
    * Java hook needs to be able to handle stream of messages e.g. exception output
"""
while True:
    java_state_str = input("Enter message...\n")
    if java_state_str == "exit":
        break
    else:
        java_state_dict = json.loads(java_state_str.replace(":", "/").replace("=", ":"))
        java_state_delta_str = str(java_state_dict).replace(":", "=").replace("/", ":")
        print(java_state_delta_str)
exit()
