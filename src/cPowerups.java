import java.util.HashMap;

public class cPowerups {
    public static void takepowerupammo(gProp powerup) {
        while(powerup.getInt("int1") > 0 && cVars.getInt("weaponstock" + powerup.getInt("int0"))
                < gWeapons.fromCode(powerup.getInt("int0")).maxAmmo) {
            cVars.increment("weaponstock"+powerup.get("int0"));
            powerup.putInt("int1", powerup.getInt("int1") - 1);
            if(sSettings.net_client) {
                //this is for the case where clients take away from powerup
                cVars.put("sendpowerup", powerup.get("id")+":"+powerup.get("int1"));
            }
        }
        if(powerup.getInt("int1") < 1) {
            powerup.put("int0", "0");
            if(sSettings.net_client) {
                //this is for the case where clients empty out powerup
                cVars.put("sendpowerup", powerup.get("id")+":0");
            }
        }
        xCon.ex("playsound sounds/clampdown.wav");
    }

    public static void checkPlayerPowerups(gProp powerup) {
        int int0 = powerup.getInt("int0");
        if(int0 > 0) {
            if (cVars.isZero("gamespawnarmed")) {
                if(cVars.isZero("currentweapon")) {
                    xCon.ex("THING_PLAYER.0.weapon " + int0);
                    cVars.putInt("currentweapon", int0);
                    takepowerupammo(powerup);
                    xCon.ex("playsound sounds/grenpinpull.wav");
                    cScripts.checkPlayerSpriteFlip(cGameLogic.userPlayer());
                }
                else if(cVars.isInt("currentweapon", int0)
                        && cVars.getInt("weaponstock"+int0) < gWeapons.fromCode(int0).maxAmmo) {
                    takepowerupammo(powerup);
                }
            }
            else if(cVars.getInt("weaponstock"+int0) < gWeapons.fromCode(int0).maxAmmo){
                takepowerupammo(powerup);
            }
//            else if(powerup_selection[int0].equals("slow") && cVars.isZero("sicknessslow")) {
//                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")/2);
//                xCon.ex("THING_PLAYER.0.sicknessslow 1");
//                xCon.ex("cv_sicknessslow 1");
//            }
//            else if(powerup_selection[int0].equals("fast") && cVars.isZero("sicknessfast")) {
//                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")*2);
//                xCon.ex("THING_PLAYER.0.sicknessfast 1");
//                xCon.ex("sicknessfast 1");
//            }
        }
    }

    public static String createPowerupStringServer() {
        StringBuilder str = new StringBuilder();
        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_POWERUP");
        for(String id : thingMap.keySet()) {
            gProp p = (gProp) thingMap.get(id);
            if(p.getInt("int0") > 0) {
                str.append(id+":"+p.get("int0")+":"+p.get("int1")+":"+p.get("coordx")+":"+p.get("coordy")+":");
            }
        }
        String rstr = str.toString();
        if(rstr.length() < 1)
            return "";
        return rstr.substring(0, rstr.length()-1);
    }

    static gProp getPowerupById(String id) {
        return (gProp) eManager.currentMap.scene.getThingMap("PROP_POWERUP").get(id);
    }

    static void processPowerupStringClient(String powerupString) {
        //1. update each pwerup present in the string and create those not found
        //2. anything with an id NOT present in the string gets deleted outright
        String[] powerupStringToks = powerupString.split(":");
        if(powerupStringToks.length >= 5) {
            for(int i = 0; i < powerupStringToks.length; i+=5) {
                String id = powerupStringToks[i];
                String int0 = powerupStringToks[i+1];
                String int1 = powerupStringToks[i+2];
                String x = powerupStringToks[i+3];
                String y = powerupStringToks[i+4];
                gProp prop = getPowerupById(id);
                if(prop != null) {
                    if(!prop.get("int0").equals(int0))
                        prop.put("int0", int0);
                    if(!prop.get("int1").equals(int1))
                        prop.put("int1", int1);
                    if(!prop.get("coordx").equals(x))
                        prop.put("coordx", x);
                    if(!prop.get("coordy").equals(y))
                        prop.put("coordy", y);
                }
                else {
                    xCon.ex(String.format("e_putprop %d %s %s %s %s %d %d",
                            gProp.POWERUP, int0, int1,x, y,gWeapons.fromCode(Integer.parseInt(int0)).dims[0],
                            gWeapons.fromCode(Integer.parseInt(int0)).dims[1]));
                    eManager.currentMap.scene.props().get(eManager.currentMap.scene.props().size()-1).put("id", id);
                }
            }
        }
        //hide anything that shouldn't be on
        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_POWERUP");
        for(String id : thingMap.keySet()) {
            if(!powerupString.contains(id)) {
                thingMap.get(id).put("int0", "0");
            }
        }
    }
}
