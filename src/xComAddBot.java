import java.util.HashMap;

public class xComAddBot extends xCom {
    public String doCommand(String fullCommand) {
        String[] botnameselection = sVars.getArray("botnameselection");
        String[] colorselection = sVars.getArray("colorselection");
        String[] hatselection = sVars.getArray("hatselection");
        String botname = botnameselection[(int)(Math.random()*(botnameselection.length-1))];
        String botcolor = colorselection[(int)(Math.random()*(colorselection.length-1))];
        String bothat = hatselection[(int)(Math.random()*(hatselection.length-1))];

        gPlayer p = new gPlayer(-6000,-6000,150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
        p.put("color", botcolor);
        p.put("name", botname);
        p.put("color", botcolor);
        p.putInt("tag", eManager.currentMap.scene.players().size());
        p.putInt("bottag", eManager.currentMap.scene.botplayers().size());
        p.put("id", "bot"+cScripts.createID(5));
        p.put("hat", bothat);
        eManager.currentMap.scene.players().add(p);
        eManager.currentMap.scene.botplayers().add(p);
        nVarsBot.update(p);
        nServer.clientArgsMap.put(p.get("id"), nVarsBot.copyArgsForId(p.get("id")));
        nServer.scoresMap.put(p.get("id"), new HashMap<>());
        xCon.ex("botrespawn " + p.getInt("bottag"));
        return "spawned bot";
    }
}
