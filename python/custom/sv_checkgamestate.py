import time

player_recharge_times = {}
player_old_hps = {}


def check_game_state(game_state):
    gametime = time.time()*1000  # gametime should be sent to python from java
    for client in game_state.keys():
        # gravity simulation
        # game_state[client]["mov1"] = "1"
        # check health: if hp < old_hp, we took dmg
        if client not in player_recharge_times:
            player_recharge_times[client] = 0  # hprechargetime, playervars needs to be loaded to python
        if client not in player_old_hps:
            player_old_hps[client] = 500  # maxhp
        oldhp = int(game_state[client]["hp"])
        if player_old_hps[client] > oldhp:
            player_recharge_times[client] = gametime + 3600  # delay time
        if gametime >= player_recharge_times[client]:
            newhp = oldhp + 1
            if newhp <= 500:  # maxhp, config vars need to be loaded into python
                game_state[client]["hp"] = newhp
        player_old_hps[client] = oldhp
