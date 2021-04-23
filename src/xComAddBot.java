public class xComAddBot extends xCom {
    public String doCommand(String fullCommand) {
        String[] botnameselection = sVars.getArray("botnameselection");
        String[] colorselection = sVars.getArray("colorselection");
        String[] hatselection = sVars.getArray("hatselection");
        String botname = botnameselection[(int)(Math.random()*(botnameselection.length))];
        String botcolor = colorselection[(int)(Math.random()*(colorselection.length))];
        String bothat = hatselection[(int)(Math.random()*(hatselection.length))];

        gPlayer p = new gPlayer(-6000,-6000,
                eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
//        p.putInt("tag", eManager.currentMap.scene.playersMap().size());
        p.put("id", "bot"+cScripts.createBotId());
        p.put("hat", bothat);
        eManager.currentMap.scene.playersMap().put(p.get("id"), p);
        eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").put(p.get("id"), p);
        nVarsBot.update(p);
        nServer.instance().clientArgsMap.put(p.get("id"), nVarsBot.copyArgsForId(p.get("id")));
        nServer.instance().clientArgsMap.get(p.get("id")).put("color", botcolor);
        nServer.instance().clientArgsMap.get(p.get("id")).put("name", botname);
        nServer.instance().clientIds.add(p.get("id"));
        cScoreboard.addId(p.get("id"));
        nServer.instance().addNetCmd("echo " + botname + " joined the game");
        xCon.ex("respawnplayer " + p.get("id"));
        return "spawned bot";
    }
}
