public class xComDropWeapon extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer p = cGameLogic.userPlayer();
        if(p != null) {
            String doString = String.format("e_putprop %d %d %d %d %d %d %d",
                    gProps.POWERUP, p.getInt("weapon"), cVars.getInt("weaponstock"+p.get("weapon")),
                    p.getInt("coordx") + p.getInt("dimw"), p.getInt("coordy") + p.getInt("dimh") + 50,
                    gWeapons.fromCode(p.getInt("weapon")).dims[0],
                    gWeapons.fromCode(p.getInt("weapon")).dims[1]);
            cVars.putInt("weaponstock"+p.get("weapon"), 0);
            cScripts.changeWeapon(gWeapons.type.NONE.code());
            xCon.ex(doString);
            switch (sSettings.NET_MODE) {
                case sSettings.NET_CLIENT:
                    nClient.addSendCmd(doString);
                    break;
                case sSettings.NET_SERVER:
                    nServer.addSendCmd(doString);
                    break;
            }
        }
        return "drop weapon";
    }
}
