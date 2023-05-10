import javafx.scene.media.AudioClip;

import java.util.*;

public class eGameLogicShell extends eGameLogicAdapter {
    private long frameCounterTime = -1;
    ArrayList<AudioClip> audioClips;
    gArgSet serverVars;

    public eGameLogicShell(String[] args) {
        audioClips = new ArrayList<>();
        serverVars = new gArgSet();
    }

    private static void initGameObjectsAndScenes() {
        xCon.ex("exec " + sSettings.CONFIG_FILE_LOCATION_GAME);
        int ctr = 0;
        ArrayList<String> thingTypes = new ArrayList<>();
        while(!xCon.ex("setvar THING_"+ctr).equals("null")) {
            thingTypes.add(xCon.ex("setvar THING_"+ctr));
            ctr++;
        }
        sSettings.object_titles = thingTypes.toArray(String[]::new);
        cClientLogic.scene = new gScene();
        cServerLogic.scene = new gScene();
        uiEditorMenus.previewScene = new gScene();
    }

    @Override
    public void init() {
        eManager.init();
        gExecDoableFactory.instance().init();
        gScriptFactory.instance().init();

        //init serverVars

        serverVars.putArg(new gArg("listenport", "5555") {
            public void onChange() {
                cServerLogic.listenPort = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.timelimit = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                cServerLogic.gameMode = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("maxhp", Integer.toString(cServerLogic.maxhp)) {
            public void onChange() {
                cServerLogic.maxhp = Integer.parseInt(value);
                if(sSettings.IS_SERVER) {
                    int newmaxhp = Integer.parseInt(value);
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_setvar maxhp " + newmaxhp);
                    nStateMap cState = new nStateMap(cServerLogic.netServerThread.masterStateSnapshot);
                    for(String clid : cState.keys()) {
                        cServerLogic.netServerThread.setClientState(clid, "hp", value);
                    }
                }
            }
        });
        serverVars.putArg(new gArg("velocityplayerbase", Integer.toString(cServerLogic.velocityplayerbase)) {
            public void onChange() {
                cServerLogic.velocityplayerbase = Integer.parseInt(value);
                if(sSettings.IS_SERVER)
                    xCon.ex("addcom cl_setvar velocityplayerbase " + cServerLogic.velocityplayerbase);
            }
        });
        serverVars.putArg(new gArg("voteskiplimit", "2") {
            public void onChange() {
                cServerLogic.voteskiplimit = Integer.parseInt(value);
                if(cServerLogic.netServerThread != null)
                    cServerLogic.netServerThread.checkForVoteSkip();
            }
        });
        serverVars.putArg(new gArg("respawnwaittime", Integer.toString(cServerLogic.respawnwaittime)) {
            public void onChange() {
                cServerLogic.respawnwaittime = Integer.parseInt(value);
            }
        });
        serverVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        serverVars.loadFromLaunchArgs(xMain.launchArgs);

        cClientLogic.init(xMain.launchArgs);
        initGameObjectsAndScenes();
        if(sSettings.show_mapmaker_ui) {
            sSettings.drawhitboxes = true;
            sSettings.drawmapmakergrid = true;
            sSettings.zoomLevel = 0.5;
        }
        oDisplay.instance().showFrame();
        gCamera.init();
        gAnimations.init();
    }

    @Override
    public void input() {
        gTime.gameTime = System.currentTimeMillis();
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        super.update();
        long gameTimeMillis = gTime.gameTime;
        if(sSettings.IS_CLIENT) {
            cClientLogic.vars.put("gametimemillis", Long.toString(gameTimeMillis));
            cClientLogic.timedEvents.executeCommands();
            if(oDisplay.instance().frame.isVisible()) {
                if(cClientLogic.getUserPlayer() != null)
                   pointPlayerAtMousePointer();
                else if(sSettings.show_mapmaker_ui)
                    selectThingUnderMouse();
            }
            checkAudio(); //setting to mute game when not in focus?
            if(cClientLogic.getUserPlayer() != null)
                checkPlayerFire();
            updateEntityPositions(gameTimeMillis);
        }
        uiInterface.tickReport = getTickReport();
    }

    private void checkAudio() {
        //TODO: fix concurrency issues here
        if(audioClips.size() > 0){
            ArrayList<AudioClip> tr = new ArrayList<>();
            for (AudioClip c : audioClips) {
                if (!c.isPlaying())
                    tr.add(c);
            }
            for (AudioClip c : tr) {
                audioClips.remove(c);
            }
        }
    }

    private void updateEntityPositions(long gameTimeMillis) {
        if(sSettings.show_mapmaker_ui && cClientLogic.getUserPlayer() == null)
            gCamera.updatePosition();
        double mod = (double)sSettings.ratesimulation /(double)sSettings.rateShell;
        for(String id : cClientLogic.getPlayerIds()) {
            gPlayer obj = cClientLogic.getPlayerById(id);
            if(obj == null)
                continue;
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "acceldelay", "accelrate",
                    "decelrate"
            };
            //check null fields
            if(!obj.containsFields(requiredFields))
                continue;
            int mx = obj.getInt("vel3") - obj.getInt("vel2");
            int my = obj.getInt("vel1") - obj.getInt("vel0");
            int dx = obj.getInt("coordx") + (int)(mx*mod);
            int dy = obj.getInt("coordy") + (int)(my*mod);
            if(obj.getLong("acceltick") < gameTimeMillis) {
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("acceldelay"));
                //user player
                if(isUserPlayer(obj)) {
                    for (int i = 0; i < 4; i++) {
                        if (obj.getInt("mov" + i) > 0)
                            obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayerBase,
                                    obj.getInt("vel" + i) + obj.getInt("accelrate"))));
                        else
                            obj.putInt("vel" + i, Math.max(0, obj.getInt("vel" + i) - obj.getInt("decelrate")));
                    }
                }
            }
            if(!obj.wontClipOnMove(dx, obj.getInt("coordy"), cClientLogic.scene))
                dx = obj.getInt("coordx");
            if(!obj.wontClipOnMove(obj.getInt("coordx"), dy, cClientLogic.scene))
                dy = obj.getInt("coordy");
            if(isUserPlayer(obj))
                gCamera.put("coords", dx + ":" + dy);
            obj.putInt("coordx", dx);
            obj.putInt("coordy", dy);
        }
        try {
            HashMap<String, gThing> thingMap = cClientLogic.scene.getThingMap("THING_BULLET");
            Queue<gThing> checkQueue = new LinkedList<>();
            String[] keys = thingMap.keySet().toArray(new String[0]);
            for (String id : keys) {
                checkQueue.add(thingMap.get(id));
            }
            while (checkQueue.size() > 0) {
                gBullet obj = (gBullet) checkQueue.remove();
                obj.putInt("coordx", obj.getInt("coordx")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel * Math.cos(obj.getDouble("fv") + Math.PI / 2)));
                obj.putInt("coordy", obj.getInt("coordy")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel * Math.sin(obj.getDouble("fv") + Math.PI / 2)));
            }
            checkBulletSplashes(gameTimeMillis);
            //popups
            thingMap = cClientLogic.scene.getThingMap("THING_POPUP");
            checkQueue = new LinkedList<>();
            for (String id : thingMap.keySet()) {
                checkQueue.add(thingMap.get(id));
            }
            while (checkQueue.size() > 0) {
                gPopup obj = (gPopup) checkQueue.remove();
                obj.put("coordx", Integer.toString(obj.getInt("coordx")
                        - (int) (sSettings.velocity_popup * Math.cos(obj.getDouble("fv") + Math.PI / 2))));
                obj.put("coordy", Integer.toString(obj.getInt("coordy")
                        - (int) (sSettings.velocity_popup * Math.sin(obj.getDouble("fv") + Math.PI / 2))));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap<String, gThing> bulletsMap = cClientLogic.scene.getThingMap("THING_BULLET");
        Queue<gThing> checkThings = new LinkedList<>();
        String[] keys = bulletsMap.keySet().toArray(new String[0]);
        for (String id : keys) {
            checkThings.add(bulletsMap.get(id));
        }
        while (checkThings.size() > 0) {
            gBullet t = (gBullet) checkThings.remove();
            if(gameTimeMillis - t.getLong("timestamp") > t.getInt("ttl")){
                bulletsToRemoveIds.add(t.get("id"));
                //grenade explosion
                if(t.isInt("src", gWeapons.launcher))
                    pseeds.add(t);
                continue;
            }
            for(String blockId : cClientLogic.scene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = cClientLogic.scene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(t.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(t.get("id"));
                    if(t.isInt("src", gWeapons.launcher))
                        pseeds.add(t);
                }
            }
            for(String playerId : cClientLogic.getPlayerIds()) {
                gPlayer p = cClientLogic.getPlayerById(playerId);
                if(p != null && p.containsFields(new String[]{"coordx", "coordy"})
                        && t.collidesWithThing(p) && !t.get("srcid").equals(playerId)) {
                    bulletsToRemovePlayerMap.put(p, t);
                    if(t.isInt("src", gWeapons.launcher))
                        pseeds.add(t);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                gWeaponsLauncher.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            cClientLogic.scene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            cClientLogic.scene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).get("id"));
        }
    }

    private void selectThingUnderMouse() {
        if(!cClientLogic.maploaded)
            return;
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selecteditemid = item.get("id");
                cClientLogic.selectedPrefabId = "";
                return;
            }
        }
        for(String id : cClientLogic.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
            if(!block.get("type").equals("BLOCK_FLOOR")
                    && block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selectedPrefabId = block.get("prefabid");
                cClientLogic.selecteditemid = "";
                return;
            }
        }
        for(String id : cClientLogic.scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing block = cClientLogic.scene.getThingMap("BLOCK_FLOOR").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selectedPrefabId = block.get("prefabid");
                cClientLogic.selecteditemid = "";
                return;
            }
        }
        cClientLogic.selectedPrefabId = "";
        cClientLogic.selecteditemid = "";
    }

    private boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    private void checkPlayerFire() {
        if(cClientLogic.getUserPlayer() != null && iMouse.holdingMouseLeft) {
            gPlayer player = cClientLogic.getUserPlayer();
            if(player.contains("cooldown")) {
                int weapint = player.getInt("weapon");
                long gametimemillis = gTime.gameTime;
                if(player.getLong("cooldown") <= gametimemillis) {
                    cClientLogic.netClientThread.addNetCmd(String.format("fireweapon %s %d", uiInterface.uuid, weapint));
                    player.putLong("cooldown", gametimemillis + gWeapons.fromCode(weapint).refiredelay);
                }
            }
        }
    }

    private void pointPlayerAtMousePointer() {
        gPlayer p = cClientLogic.getUserPlayer();
        int[] mc = uiInterface.getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.getInt("coordx") + p.getInt("dimw")/2 - gCamera.getX());
        double dy = mc[1] - eUtils.scaleInt(p.getInt("coordy") + p.getInt("dimh")/2 - gCamera.getY());
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        p.checkSpriteFlip();
    }

    @Override
    public void render() {
        oDisplay.instance().frame.repaint();
        long gameTimeMillis = gTime.gameTime;
        if (frameCounterTime < gameTimeMillis) {
            uiInterface.fpsReport = uiInterface.frames;
            uiInterface.frames = 0;
            frameCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void cleanup() {
       serverVars.saveToFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        cClientLogic.vars.saveToFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        if(cClientLogic.debuglog)
            xCon.instance().saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }
}
