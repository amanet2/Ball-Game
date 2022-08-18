public class xComSpawnAnimationClient extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.vfxenableanimations) {
            String[] toks = fullCommand.split(" ");
            if (toks.length > 3) {
                int animcode = Integer.parseInt(toks[1]);
                int x = Integer.parseInt(toks[2]);
                int y = Integer.parseInt(toks[3]);
                String aid = eManager.createId();
                cClientLogic.scene.getThingMap("THING_ANIMATION").put(aid,
                        new gAnimationEmitter(animcode, x, y));
                cClientLogic.timedEvents.put(Long.toString(gTime.gameTime
                        + gAnimations.animation_selection[animcode].frames.length*gAnimations.animation_selection[animcode].framerate
                ), new gTimeEvent() {
                    public void doCommand() {
                        cClientLogic.scene.getThingMap("THING_ANIMATION").remove(aid);
                    }
                });
                return "spawned animation " + animcode + " at " + x + " " + y;
            }
        }
        return "usage: cl_spawnanimation <animation_code> <x> <y>";
    }
}
