public class xComEditorPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int propcode = Integer.parseInt(toks[1]);
            int int0 = Integer.parseInt(toks[2]);
            int int1 = Integer.parseInt(toks[3]);
            int x = Integer.parseInt(toks[4]);
            int y = Integer.parseInt(toks[5]);
            int w = Integer.parseInt(toks[6]);
            int h = Integer.parseInt(toks[7]);
            switch(propcode) {
                case gProp.TELEPORTER:
                    gPropTeleporter teleporter = new gPropTeleporter(int0, int1, x, y, w, h);
                    teleporter.put("id", cScripts.createID(8));
                    teleporter.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").size());
                    teleporter.putInt("native", 1);
                    eManager.currentMap.scene.props().add(teleporter);
                    eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").put(teleporter.get("id"), teleporter);
                    break;
                case gProp.SCOREPOINT:
                    gPropScorepoint scorepoint = new gPropScorepoint(int0, int1, x, y, w, h);
                    scorepoint.put("id", cScripts.createID(8));
                    scorepoint.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT").size());
                    scorepoint.putInt("native", 1);
                    eManager.currentMap.scene.props().add(scorepoint);
                    eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT").put(scorepoint.get("id"), scorepoint);
                    break;
                case gProp.FLAGRED:
                    gPropFlagRed flagred = new gPropFlagRed(int0, int1, x, y, w, h);
                    flagred.put("id", cScripts.createID(8));
                    flagred.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_FLAGRED").size());
                    flagred.putInt("native", 1);
                    eManager.currentMap.scene.props().add(flagred);
                    eManager.currentMap.scene.getThingMap("PROP_FLAGRED").put(flagred.get("id"), flagred);
                    break;
                case gProp.FLAGBLUE:
                    gPropFlagBlue flagblue = new gPropFlagBlue(int0, int1, x, y, w, h);
                    flagblue.put("id", cScripts.createID(8));
                    flagblue.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE").size());
                    flagblue.putInt("native", 1);
                    eManager.currentMap.scene.props().add(flagblue);
                    eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE").put(flagblue.get("id"), flagblue);
                    break;
                case gProp.POWERUP:
                    gPropPowerup powerup = new gPropPowerup(int0, int1, x, y, w, h);
                    powerup.put("id", cScripts.createID(8));
                    powerup.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_POWERUP").size());
                    powerup.putInt("native", 1);
                    eManager.currentMap.scene.props().add(powerup);
                    eManager.currentMap.scene.getThingMap("PROP_POWERUP").put(powerup.get("id"), powerup);
                    break;
                case gProp.BOOSTUP:
                    gPropBoostup boostup = new gPropBoostup(int0, int1, x, y, w, h);
                    boostup.put("id", cScripts.createID(8));
                    boostup.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_BOOSTUP").size());
                    boostup.putInt("native", 1);
                    eManager.currentMap.scene.props().add(boostup);
                    eManager.currentMap.scene.getThingMap("PROP_BOOSTUP").put(boostup.get("id"), boostup);
                    break;
                case gProp.SPAWNPOINT:
                    gPropSpawnpoint spawnpoint = new gPropSpawnpoint(int0, int1, x, y, w, h);
                    spawnpoint.put("id", cScripts.createID(8));
                    spawnpoint.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
                    spawnpoint.putInt("native", 1);
                    eManager.currentMap.scene.props().add(spawnpoint);
                    eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").put(spawnpoint.get("id"), spawnpoint);
                    break;
                default:
                    break;
            }
            return "";
        }
        return "usage: e_putprop <code> <int0> <int1> <x> <y> <w> <h>";
    }
}
