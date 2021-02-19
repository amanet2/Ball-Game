public class cClient {
    public static void processActionLoadClient(String actionload) {
        String[] actions = actionload.split("\\|");
        for(String action : actions) {
            if(action.contains("explode")) {
                String[] args = action.split(":");
                if (sVars.isOne("vfxenableanimations")) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createId(), new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                    Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                }
            }
        }
    }

    public static void processCmd(String cmdload) {
        nSend.sendMap.put("netcmdrcv","");
        xCon.ex(cmdload);
    }
}
