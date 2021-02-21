public class xComJoingame extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String[] comps = toks[1].split(":");
            sVars.put("joinip", comps[0]);
            if(comps.length > 1)
                sVars.put("joinport", comps[1]);
        }
        xCon.ex("pause");
        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
        sSettings.net_client = true;
        sSettings.net_server = false;
        sSettings.NET_MODE = sSettings.NET_CLIENT;
        nClient.hasDisconnected = 0;
        cVars.put("quitconfirmed", "0");
        cVars.put("quitting", "0");
        cVars.put("disconnectconfirmed", "0");
        cVars.put("disconnecting", "0");
        nSend.sendMap = null;
        nVars.reset();
        eManager.currentMap = new gMap();
        int[] npcs = new int[] {-10000, -10000};
        cVars.putLong("starttime", System.currentTimeMillis());
        gPlayer player0 = new gPlayer(npcs[0], npcs[1],150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
        player0.put("tag", "0");
        player0.put("id", sSettings.net_server ? "server" : uiInterface.uuid);
        cGameLogic.setUserPlayer(player0);
        player0.put("color", sVars.get("playercolor"));
        eManager.currentMap.scene.playersMap().put(player0.get("id"), player0);
        xCon.ex("centercamera");
        xCon.ex("respawn");
        cVars.put("canvoteskip", "");
        return "joined game";
    }
}
