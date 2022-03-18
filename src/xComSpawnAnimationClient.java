public class xComSpawnAnimationClient extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.vfxenableanimations) {
            String[] toks = fullCommand.split(" ");
            if (toks.length > 3) {
                int animcode = Integer.parseInt(toks[1]);
                int x = Integer.parseInt(toks[2]);
                int y = Integer.parseInt(toks[3]);
                cClientLogic.scene.getThingMap("THING_ANIMATION").put(eManager.createId(),
                        new gAnimationEmitter(animcode, x, y));
                return "spawned animation " + animcode + " at " + x + " " + y;
            }
        }
        return "usage: cl_spawnanimation <animation_code> <x> <y>";
    }
}
