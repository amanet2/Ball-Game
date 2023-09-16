def check_game_state(game_state):
    for client in game_state.keys():
        game_state[client]["mov1"] = "1"
