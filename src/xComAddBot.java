public class xComAddBot extends xCom {
    public String doCommand(String fullCommand) {
        String[] botnameselection = sSettings.botnameSelection;
        String[] colorselection = sSettings.colorSelection;
        String botname = botnameselection[(int)(Math.random()*(botnameselection.length))];
        String botcolor = colorselection[(int)(Math.random()*(colorselection.length))];

        gPlayer p = new gPlayer("bot"+eManager.createBotId(), -6000,-6000, cServerLogic.maxhp,
                eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
        cServerLogic.scene.getThingMap("THING_PLAYER").put(p.get("id"), p);
        cServerLogic.scene.getThingMap("THING_BOTPLAYER").put(p.get("id"), p);
        nVarsBot.update(p, gTime.gameTime);
        nServer.instance().clientArgsMap.put(p.get("id"), nVarsBot.copyArgsForId(p.get("id")));
        nServer.instance().clientArgsMap.get(p.get("id")).put("color", botcolor);
        nServer.instance().clientArgsMap.get(p.get("id")).put("name", botname);
        gScoreboard.addId(p.get("id"));
        nServer.instance().addExcludingNetCmd("server", "echo " + botname + " joined the game");
        xCon.ex("exec scripts/respawnnetplayer " + p.get("id"));
        return "spawned bot";
    }
}
