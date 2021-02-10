public class cClient {
    public static void processActionLoadClient(String actionload) {
        String[] actions = actionload.split("\\|");
        for(String action : actions) {
            if(action.contains("explode")) {
                String[] args = action.split(":");
                if (sVars.isOne("vfxenableanimations")) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createID(8), new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                    Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                }
            }
            if(action.contains("playsound")) {
                nClient.sfxreceived = 1;
                xCon.ex(String.format("playsound %s",
                        action.split("-")[0].replace("playsound","")));
            }
            if(action.contains("sendcmd")) {
                System.out.println(action);
                nClient.cmdreceived = 1;
                xCon.ex(action.replaceFirst("sendcmd_",""));
            }
        }
    }
}
