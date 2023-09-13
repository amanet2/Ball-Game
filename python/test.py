import json
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
        # print(java_state_str_json)
        try:
            json.loads(java_state_str_json)
        except Exception as e:
            print(str(e))
        else:
            print(json.dumps(json.loads(java_state_str_json)))
exit()
