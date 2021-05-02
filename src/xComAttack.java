public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer br = cGameLogic.userPlayer();
        int playerWeapon = br.getInt("weapon");
        if(playerWeapon == gWeapons.type.NONE.code()
            || playerWeapon == gWeapons.type.GLOVES.code()
            || cVars.getInt("weaponstock"+playerWeapon) > 0) {
            if(br.getLong("cooldown") < System.currentTimeMillis()) {
                String fireString = "fireweapon " + br.get("id") + " " + playerWeapon;
                boolean isflagmaster = false;
                if(cVars.isInt("gamemode", cGameMode.FLAG_MASTER) && nServer.instance().clientArgsMap != null
                        && nServer.instance().clientArgsMap.containsKey("server")
                        && nServer.instance().clientArgsMap.get("server").containsKey("state")
                        && nServer.instance().clientArgsMap.get("server").get("state").contains(uiInterface.uuid)) {
                    isflagmaster = true;
                }
                if(isflagmaster)
                    return "can't attack while flagmaster";
                cGameLogic.doCommand(fireString);
                br.putLong("cooldown", System.currentTimeMillis()
                        + (long)(gWeapons.fromCode(br.getInt("weapon")).refiredelay));
            }
        }
        else {
            cScripts.changeWeapon(0);
        }
        return "attack";
    }

    public String undoCommand(String fullCommand) {
        return "-attack";
    }
}
