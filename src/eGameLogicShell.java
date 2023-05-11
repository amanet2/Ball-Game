import javafx.scene.media.AudioClip;

import java.awt.*;
import java.util.*;

public class eGameLogicShell extends eGameLogicAdapter {
    private long frameCounterTime = -1;
    ArrayList<AudioClip> audioClips;
    gArgSet serverVars;
    gArgSet clientVars;
    gScriptFactory scriptFactory;
    gBlockFactory blockFactory;
    oDisplay displayPane;
    xCon console;
    gScene serverScene;
    gScene clientScene;
    gTimeEventSet scheduledEvents;
    eGameLogicSimulation serverSimulationThread;
    eGameLogicServer serverNetThread;
    eGameLogicClient clientNetThread;

    public eGameLogicShell() {
        audioClips = new ArrayList<>();
        serverVars = new gArgSet();
        clientVars = new gArgSet();
        scriptFactory = new gScriptFactory();
        blockFactory = new gBlockFactory();
        displayPane = new oDisplay();
        console = new xCon();
        scheduledEvents = new gTimeEventSet();
    }

    private void initGameObjectsAndScenes() {
        console.ex("exec " + sSettings.CONFIG_FILE_LOCATION_GAME);
        int ctr = 0;
        ArrayList<String> thingTypes = new ArrayList<>();
        while(!console.ex("setvar THING_"+ctr).equals("null")) {
            thingTypes.add(console.ex("setvar THING_"+ctr));
            ctr++;
        }
        sSettings.object_titles = thingTypes.toArray(String[]::new);
        xMain.shellLogic.clientScene = new gScene();
        serverScene = new gScene();
        uiEditorMenus.previewScene = new gScene();
    }

    @Override
    public void init() {
        eManager.init();
        scriptFactory.init();

        //init serverVars
        serverVars.putArg(new gArg("listenport", "5555") {
            public void onChange() {
                cServerLogic.listenPort = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                sSettings.serverTimeLimit = Integer.parseInt(value);
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
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_setvar maxhp " + newmaxhp);
                    nStateMap cState = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                    for(String clid : cState.keys()) {
                        xMain.shellLogic.serverNetThread.setClientState(clid, "hp", value);
                    }
                }
            }
        });
        serverVars.putArg(new gArg("velocityplayerbase", Integer.toString(cServerLogic.velocityplayerbase)) {
            public void onChange() {
                cServerLogic.velocityplayerbase = Integer.parseInt(value);
                if(sSettings.IS_SERVER)
                    console.ex("addcom cl_setvar velocityplayerbase " + cServerLogic.velocityplayerbase);
            }
        });
        serverVars.putArg(new gArg("voteskiplimit", "2") {
            public void onChange() {
                cServerLogic.voteskiplimit = Integer.parseInt(value);
                if(xMain.shellLogic.serverNetThread != null)
                    xMain.shellLogic.serverNetThread.checkForVoteSkip();
            }
        });
        serverVars.putArg(new gArg("respawnwaittime", Integer.toString(cServerLogic.respawnwaittime)) {
            public void onChange() {
                cServerLogic.respawnwaittime = Integer.parseInt(value);
            }
        });
        serverVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        serverVars.loadFromLaunchArgs(xMain.launchArgs);
        //init client vars
        clientVars.putArg(new gArg("vidmode", "1920,1080,60") {
            public void onChange() {
                String[] vidmodetoks = value.split(",");
                int[] sres = new int[]{
                        Integer.parseInt(vidmodetoks[0]),
                        Integer.parseInt(vidmodetoks[1]),
                        Integer.parseInt(vidmodetoks[2])
                };
                sSettings.framerate = sres[2];
                if(sSettings.width != sres[0] || sSettings.height != sres[1]) {
                    sSettings.width = sres[0];
                    sSettings.height = sres[1];
                    //refresh fonts
                    dFonts.fontNormal = new Font(clientVars.get("fontui"), Font.PLAIN,
                            dFonts.fontsize * sSettings.height / sSettings.gamescale);
                    dFonts.fontGNormal = new Font(clientVars.get("fontui"), Font.PLAIN, dFonts.fontsize);
                    dFonts.fontSmall = new Font(clientVars.get("fontui"), Font.PLAIN,
                            dFonts.fontsize*sSettings.height/sSettings.gamescale/2);
                    dFonts.fontConsole = new Font(dFonts.fontnameconsole, Font.PLAIN,
                            dFonts.fontsize*sSettings.height/sSettings.gamescale/2);
                    if(displayPane.frame != null) {
                        displayPane.refreshResolution();
                        dMenus.refreshLogos();
                    }
                }
            }
        });
        clientVars.putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(!sSettings.audioenabled) {
                    for(AudioClip c : audioClips) {
                        c.stop();
                    }
                }
            }
        });
        clientVars.putArg(new gArg("debug", "0") {
            public void onChange() {
                cClientLogic.debug = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("showmapmakerui", "0") {
            public void onChange() {
                sSettings.show_mapmaker_ui = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("debuglog", "0") {
            public void onChange() {
                cClientLogic.debuglog = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("volume", "100") {
            public void onChange() {
                cClientLogic.volume = Double.parseDouble(value);
            }
        });
        clientVars.putArg(new gArg("playercolor", "blue") {
            public void onChange() {
                cClientLogic.playerColor = value;
            }
        });
        clientVars.putArg(new gArg("playername", "player") {
            public void onChange() {
                cClientLogic.playerName = value;
            }
        });
        clientVars.putArg(new gArg("displaymode", "0") {
            public void onChange() {
                sSettings.displaymode = Integer.parseInt(value);
                if(displayPane.frame != null) {
                    displayPane.refreshDisplaymode();
                }
            }
        });
        clientVars.putArg(new gArg("vfxenableanimations", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableanimations = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        clientVars.putArg(new gArg("vfxenableflares", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableflares = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        clientVars.putArg(new gArg("vfxenableshading", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableshading = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        clientVars.putArg(new gArg("vfxenableshadows", "1"){
            public void onChange() {
                sSettings.vfxenableshadows = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                cClientLogic.gamemode = Integer.parseInt(value);
                cClientLogic.gamemodeTitle = console.ex("cl_setvar GAMETYPE_"+value+"_title");
                cClientLogic.gamemodeText = console.ex("cl_setvar GAMETYPE_"+value+"_text");
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.refreshGametypeCheckBoxMenuItems();
            }
        });
        clientVars.putArg(new gArg("maploaded", "0") {
            public void onChange() {
                cClientLogic.maploaded = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("maxhp", "500") {
            public void onChange() {
                cClientLogic.maxhp = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("velocityplayerbase", Integer.toString(cClientLogic.velocityPlayerBase)) {
            public void onChange() {
                cClientLogic.velocityPlayerBase = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("framerates", "24,30,60,75,98,120,144,165,240,320,360") {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.framerates = new int[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    int tok = Integer.parseInt(toks[i].strip());
                    sSettings.framerates[i] = tok;
                }
            }
        });
        clientVars.putArg(new gArg("resolutions",
                "640x480,800x600,1024x768,1280x720,1280x1024,1680x1050,1600x1200,1920x1080,2560x1440,3840x2160") {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.resolutions = new String[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    String tok = toks[i].strip();
                    sSettings.resolutions[i] = tok;
                }
            }
        });
        clientVars.putArg(new gArg("fontui", "None"));
        clientVars.putArg(new gArg("showfps", "0"){
            public void onChange() {
                dScreenMessages.showfps = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showcam", "0"){
            public void onChange() {
                dScreenMessages.showcam = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showmouse", "0"){
            public void onChange() {
                dScreenMessages.showmouse = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("shownet", "0"){
            public void onChange() {
                dScreenMessages.shownet = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showplayer", "0"){
            public void onChange() {
                dScreenMessages.showplayer = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showtick", "0"){
            public void onChange() {
                dScreenMessages.showtick = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showscale", "0"){
            public void onChange() {
                dScreenMessages.showscale = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showscore", "0"){
            public void onChange() {
                dScreenMessages.showscore = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("joinip", "localhost"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        clientVars.putArg(new gArg("joinport", "5555"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        clientVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        clientVars.loadFromLaunchArgs(xMain.launchArgs);
        
        initGameObjectsAndScenes();
        if(sSettings.show_mapmaker_ui) {
            sSettings.drawhitboxes = true;
            sSettings.drawmapmakergrid = true;
            sSettings.zoomLevel = 0.5;
        }
        displayPane.showFrame();
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
            clientVars.put("gametimemillis", Long.toString(gameTimeMillis));
            scheduledEvents.executeCommands();
            if(displayPane.frame.isVisible()) {
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
            if(!obj.wontClipOnMove(dx, obj.getInt("coordy"), xMain.shellLogic.clientScene))
                dx = obj.getInt("coordx");
            if(!obj.wontClipOnMove(obj.getInt("coordx"), dy, xMain.shellLogic.clientScene))
                dy = obj.getInt("coordy");
            if(isUserPlayer(obj))
                gCamera.put("coords", dx + ":" + dy);
            obj.putInt("coordx", dx);
            obj.putInt("coordy", dy);
        }
        try {
            HashMap<String, gThing> thingMap = xMain.shellLogic.clientScene.getThingMap("THING_BULLET");
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
            thingMap = xMain.shellLogic.clientScene.getThingMap("THING_POPUP");
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
        HashMap<String, gThing> bulletsMap = xMain.shellLogic.clientScene.getThingMap("THING_BULLET");
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
            for(String blockId : xMain.shellLogic.clientScene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = xMain.shellLogic.clientScene.getThingMap("BLOCK_COLLISION").get(blockId);
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
            xMain.shellLogic.clientScene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            xMain.shellLogic.clientScene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).get("id"));
        }
    }

    private void selectThingUnderMouse() {
        if(!cClientLogic.maploaded)
            return;
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_ITEM").keySet()) {
            gThing item = xMain.shellLogic.clientScene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selecteditemid = item.get("id");
                cClientLogic.selectedPrefabId = "";
                return;
            }
        }
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").get(id);
            if(!block.get("type").equals("BLOCK_FLOOR")
                    && block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selectedPrefabId = block.get("prefabid");
                cClientLogic.selecteditemid = "";
                return;
            }
        }
        for(String id : xMain.shellLogic.clientScene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing block = xMain.shellLogic.clientScene.getThingMap("BLOCK_FLOOR").get(id);
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
                    clientNetThread.addNetCmd(String.format("fireweapon %s %d", uiInterface.uuid, weapint));
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
        displayPane.frame.repaint();
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
        clientVars.saveToFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        if(cClientLogic.debuglog)
            console.saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }
}
