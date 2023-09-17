import time

player_recharge_times = {}
player_old_hps = {}


def check_game_state(game_state):
    for client in game_state.keys():
        game_state[client]["mov1"] = "1"
        # WIP: Check health recharge
        # TODO: hprechargetime is set when damage occurs, so this prolly should be in java anyways
        # TODO: OR everytime we check, we save the old hp and if the new check is less than old hp, we know we took dmg
        gametime = time.time()  # gametime should be sent to python from java
        delayhp = 3600
        if client not in player_recharge_times:
            player_recharge_times[client] = 0  # hprechargetime, playervars needs to be loaded to python
        if client not in player_old_hps:
            player_old_hps[client] = 500  # maxhp
        if player_old_hps[client] > int(game_state[client]["hp"]):
            player_recharge_times[client] = int(gametime)
        rctime = player_recharge_times[client]
        rcplusdelay = rctime + delayhp
        if gametime >= rcplusdelay:
            newhp = int(game_state[client]["hp"]) + 1
            if newhp <= 500:  # maxhp, config vars need to be loaded into python
                game_state[client]["hp"] = newhp
        player_old_hps[client] = int(game_state[client]["hp"])
        print(player_old_hps)
        print(player_recharge_times)
