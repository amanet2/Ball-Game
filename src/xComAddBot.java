public class xComAddBot extends xCom {
    public String doCommand(String fullCommand) {
        String[] botnameselection = sSettings.botnameSelection;
        String[] colorselection = sSettings.colorSelection;
        String botname = botnameselection[(int)(Math.random()*(botnameselection.length))];
        String botcolor = colorselection[(int)(Math.random()*(colorselection.length))];

        gPlayer p = new gPlayer("bot"+eManager.createBotId(), -6000,-6000,
                Integer.parseInt(xCon.ex("cv_maxhp")),
                eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
        cServerLogic.scene.getThingMap("THING_PLAYER").put(p.get("id"), p);
        cServerLogic.scene.getThingMap("THING_BOTPLAYER").put(p.get("id"), p);
        nVarsBot.update(p, gTime.gameTime);
        nServer.instance().masterStateMap.put(p.get("id"), new nStateBallGame());
        nServer.instance().masterStateMap.get(p.get("id")).put("color", botcolor);
        nServer.instance().masterStateMap.get(p.get("id")).put("name", botname);
        gScoreboard.addId(p.get("id"));
        nServer.instance().addExcludingNetCmd("server", "echo " + botname + " joined the game");
        xCon.ex("exec scripts/respawnnetplayer " + p.get("id"));
        return "spawned bot";
    }
}
