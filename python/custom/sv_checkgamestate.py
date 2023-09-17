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
        rctime = game_state[client]["hprechargetime"]
        if rctime == "null":
            game_state[client]["hprechargetime"] = "null"  # hprechargetime, playervars needs to be loaded to python
            rctime = 0
        rcplusdelay = rctime + delayhp
        healdue = gametime >= rcplusdelay
        if healdue:
            playerhp = game_state[client]["hp"]
            if playerhp != "null":
                newhp = playerhp + 1
                if newhp <= 500:  # maxhp, config vars need to be loaded into python
                    game_state[client]["hp"] = newhp
