import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class eGameLogicShell extends eGameLogicAdapter {
    private long frameCounterTime = -1;
    gArgSet serverVars;
    gArgSet clientVars;
    gScriptFactory scriptFactory;
    oDisplay displayPane;
    xCon console;
    gScene serverScene;
    gScene clientScene;
    gScene clientPreviewScene;
    gScheduler scheduledEvents;
    eGameLogicSimulation serverSimulationThread;
    public eGameLogicServer serverNetThread;
    eGameLogicClient clientNetThread;
    TexturePaint floorTexture;
    TexturePaint wallTexture;
    TexturePaint topTexture;

    public eGameLogicShell() throws IOException {
        serverVars = new gArgSet();
        clientVars = new gArgSet();
        scriptFactory = new gScriptFactory();
        displayPane = new oDisplay();
        console = new xCon();
        scheduledEvents = new gScheduler();
        floorTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/floor.png"))),
                new Rectangle2D.Double(0,0,1200, 1200));
        wallTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/wall.png"))),
                new Rectangle2D.Double(0,0, 300, 300));
        topTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/top.png"))),
                new Rectangle2D.Double(0,0, 300, 300));
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
        clientScene = new gScene();
        clientPreviewScene = new gScene();
        serverScene = new gScene();
        gWeapons.init();
    }

    @Override
    public void init() {
        eManager.init();
        scriptFactory.init();

        //init serverVars
        serverVars.putArg(new gArg("listenport", "5555") {
            public void onChange() {
                sSettings.serverListenPort = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                sSettings.serverTimeLimit = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                sSettings.serverGameMode = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("maxhp", Integer.toString(sSettings.serverMaxHP)) {
            public void onChange() {
                sSettings.serverMaxHP = Integer.parseInt(value);
                if(sSettings.IS_SERVER) {
                    int newmaxhp = Integer.parseInt(value);
                    serverNetThread.addIgnoringNetCmd("server", "cl_setvar maxhp " + newmaxhp);
                    nStateMap cState = new nStateMap(serverNetThread.masterStateSnapshot);
                    for(String clid : cState.keys()) {
                        serverNetThread.setClientState(clid, "hp", value);
                    }
                }
            }
        });
        serverVars.putArg(new gArg("velocityplayerbase", Integer.toString(sSettings.serverVelocityPlayerBase)) {
            public void onChange() {
                sSettings.serverVelocityPlayerBase = Integer.parseInt(value);
                if(sSettings.IS_SERVER)
                    console.ex("addcom cl_setvar velocityplayerbase " + sSettings.serverVelocityPlayerBase);
            }
        });
        serverVars.putArg(new gArg("voteskiplimit", "2") {
            public void onChange() {
                sSettings.serverVoteSkipLimit = Integer.parseInt(value);
                if(serverNetThread != null)
                    serverNetThread.checkForVoteSkip();
            }
        });
        serverVars.putArg(new gArg("respawnwaittime", Integer.toString(sSettings.serverRespawnDelay)) {
            public void onChange() {
                sSettings.serverRespawnDelay = Integer.parseInt(value);
            }
        });
        serverVars.putArg(new gArg("respawnenabled", Boolean.toString(sSettings.respawnEnabled)) {
            public void onChange() {
                sSettings.respawnEnabled = value.equalsIgnoreCase("true") || value.equals("1");
            }
        });
        serverVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        serverVars.loadFromLaunchArgs(xMain.launchArgs);
        //init client vars
        clientVars.putArg(new gArg("width", "1920") {
            public void onChange() {
                sSettings.width = Integer.parseInt(value);
                if(displayPane.frame != null) {
                    displayPane.refreshResolution();
                    dMenus.refreshLogos();
                }
            }
        });
        clientVars.putArg(new gArg("height", "1080") {
            public void onChange() {
                sSettings.height = Integer.parseInt(value);
                dFonts.refreshFonts();
                if(displayPane.frame != null) {
                    displayPane.refreshResolution();
                    dMenus.refreshLogos();
                }
            }
        });
        clientVars.putArg(new gArg("refresh", "60") {
            public void onChange() {
                sSettings.refresh = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(!sSettings.audioenabled) {
                    for(Clip c : clientScene.soundClips) {
                        c.stop();
                    }
                    clientScene.soundClips.clear();
                }
            }
        });
        clientVars.putArg(new gArg("debug", "0") {
            public void onChange() {
                sSettings.clientDebug = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("showmapmakerui", "0") {
            public void onChange() {
                sSettings.show_mapmaker_ui = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("debuglog", "0") {
            public void onChange() {
                sSettings.clientDebugLog = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("volume", "10") {
            public void onChange() {
                sSettings.clientVolume = Double.parseDouble(value);
            }
        });
        clientVars.putArg(new gArg("playercolor", "blue") {
            public void onChange() {
                sSettings.clientPlayerColor = value;
            }
        });
        clientVars.putArg(new gArg("playername", "player") {
            public void onChange() {
                sSettings.clientPlayerName = value;
            }
        });
        clientVars.putArg(new gArg("borderless", "0") {
            public void onChange() {
                sSettings.borderless = value.equalsIgnoreCase("true") || value.equals("1");
                if(displayPane.frame != null) {
                    displayPane.createPanels();
                    displayPane.showFrame();
                }
            }
        });
        clientVars.putArg(new gArg("vfxenableanimations", "1"){
            public void onChange() {
                sSettings.vfxenableanimations = value.equalsIgnoreCase("true") || value.equals("1");
            }
        });
        clientVars.putArg(new gArg("vfxenableflares", "1"){
            public void onChange() {
                sSettings.vfxenableflares = value.equalsIgnoreCase("true") || value.equals("1");
            }
        });
        clientVars.putArg(new gArg("vfxenableshading", "1"){
            public void onChange() {
                sSettings.vfxenableshading = value.equalsIgnoreCase("true") || value.equals("1");
            }
        });
        clientVars.putArg(new gArg("vfxenableshadows", "1"){
            public void onChange() {
                sSettings.vfxenableshadows = value.equalsIgnoreCase("true") || value.equals("1");
            }
        });
        clientVars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                sSettings.clientGameMode = Integer.parseInt(value);
                sSettings.clientGameModeTitle = console.ex("cl_setvar GAMETYPE_"+value+"_title");
                sSettings.clientGameModeText = console.ex("cl_setvar GAMETYPE_"+value+"_text");
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.refreshGametypeCheckBoxMenuItems();
            }
        });
        clientVars.putArg(new gArg("maploaded", "0") {
            public void onChange() {
                sSettings.clientMapLoaded = Integer.parseInt(value) > 0;
            }
        });
        clientVars.putArg(new gArg("maxhp", "500") {
            public void onChange() {
                sSettings.clientMaxHP = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("velocityplayerbase", Integer.toString(sSettings.clientVelocityPlayerBase)) {
            public void onChange() {
                sSettings.clientVelocityPlayerBase = Integer.parseInt(value);
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
                "640x480,800x600,1024x768,1280x720,1600x900,1920x1080,2560x1440,3840x2160") {
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
                sSettings.showfps = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showcam", "0"){
            public void onChange() {
                sSettings.showcam = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showmouse", "0"){
            public void onChange() {
                sSettings.showmouse = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("shownet", "0"){
            public void onChange() {
                sSettings.shownet = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showplayer", "0"){
            public void onChange() {
                sSettings.showplayer = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showtick", "0"){
            public void onChange() {
                sSettings.showtick = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showscale", "0"){
            public void onChange() {
                sSettings.showscale = value.equals("1");
            }
        });
        clientVars.putArg(new gArg("showscore", "0"){
            public void onChange() {
                sSettings.showscore = value.equals("1");
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
        gAnimations.init();
    }

    @Override
    public void input() {
        sSettings.gameTime = System.currentTimeMillis();
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        super.update();
        long gameTimeMillis = sSettings.gameTime;
        if(sSettings.IS_CLIENT) {
            clientVars.put("gametimemillis", Long.toString(gameTimeMillis));
            scheduledEvents.executeCommands();
            if(displayPane.frame.isVisible()) {
                if(getUserPlayer() != null)
                   pointPlayerAtMousePointer();
                else if(sSettings.show_mapmaker_ui)
                    selectThingUnderMouse();
            }
            checkAudio(); //setting to mute game when not in focus?
            if(getUserPlayer() != null)
                checkPlayerFire();
            updateEntityPositions(gameTimeMillis);
        }
        sSettings.tickReport = getTickReport();
    }

    public gPlayer getUserPlayer() {
        return clientScene.getPlayerById(sSettings.uuid);
    }

    private void checkAudio() {
        if(clientScene.soundClips.size() > 0){
            ArrayList<Clip> tr = new ArrayList<>();
            for (Clip c : clientScene.soundClips) {
                if(!c.isRunning()) {
//                    System.out.println("REMOVE ACTIVE SOUND CLIP: " + c);
                    tr.add(c);
//                    c.stop();
//                    c.flush();
//                    c.close();
//                    System.out.println("REMOVED ACTIVE SOUND CLIP: " + c);
//                    c = null;
//                    System.out.println("CLIP SET TO NULL");
                }
            }
            for (Clip c : tr) {
//                System.out.println("REMOVE ACTIVE SOUND CLIP: " + c);
                c.stop();
                c.flush();
                c.close();
                c.drain();
                clientScene.soundClips.remove(c);
//                System.out.println("REMOVED ACTIVE SOUND CLIP: " + c);
                c = null;
//                System.out.println("CLIP SET TO NULL");
            }
        }
    }

    public Collection<String> getPlayerIds() {
        return clientScene.getThingMap("THING_PLAYER").keySet();
    }

    public int getNewItemIdClient() {
        int itemId = 0;
        for(String id : clientScene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return itemId+1; //want to be the _next_ id
    }

    private void updateEntityPositions(long gameTimeMillis) {
        if(sSettings.show_mapmaker_ui && getUserPlayer() == null)
            gCamera.updatePosition();
        double mod = (double)sSettings.ratesimulation/(double)sSettings.rateShell;
        synchronized (clientScene.objectMaps) {
            try {
                for (String id : getPlayerIds()) {
                    gPlayer obj = getPlayerById(id);
                    if (obj == null)
                        continue;
                    int mx = obj.vel3 - obj.vel2;
                    int my = obj.vel1 - obj.vel0;
                    int dx = obj.coords[0] + (int) (mx * mod);
                    int dy = obj.coords[1] + (int) (my * mod);
                    if (obj.acceltick < gameTimeMillis) {
                        obj.acceltick = gameTimeMillis + obj.acceldelay;
                        //user player
                        if (isUserPlayer(obj)) {
                            if(obj.mov0 > 0)
                                obj.vel0 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel0 + obj.accelrate);
                            else
                                obj.vel0 = Math.max(0, obj.vel0 - obj.decelrate);
                            if(obj.mov1 > 0)
                                obj.vel1 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel1 + obj.accelrate);
                            else
                                obj.vel1 = Math.max(0, obj.vel1 - obj.decelrate);
                            if(obj.mov2 > 0)
                                obj.vel2 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel2 + obj.accelrate);
                            else
                                obj.vel2 = Math.max(0, obj.vel2 - obj.decelrate);
                            if(obj.mov3 > 0)
                                obj.vel3 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel3 + obj.accelrate);
                            else
                                obj.vel3 = Math.max(0, obj.vel3 - obj.decelrate);
                        }
                    }
                    if (!obj.wontClipOnMove(dx, obj.coords[1], clientScene))
                        dx = obj.coords[0];
                    if (!obj.wontClipOnMove(obj.coords[0], dy, clientScene))
                        dy = obj.coords[1];
                    if (isUserPlayer(obj))
                        gCamera.coords = new int[]{
                                dx + obj.dims[0]/2 - eUtils.unscaleInt(sSettings.width/2),
                                dy + obj.dims[1]/2 - eUtils.unscaleInt(sSettings.height/2)
                        };
                    obj.coords[0] = dx;
                    obj.coords[1] = dy;
                }
                //bullets
                ConcurrentHashMap<String, gThing> thingMap = clientScene.getThingMap("THING_BULLET");
                for (String id : thingMap.keySet()) {
                    gThing obj = thingMap.get(id);
                    obj.coords[0] -= (int) ((double)gWeapons.fromCode(obj.src).bulletVel*mod*Math.cos(obj.fv+Math.PI/2));
                    obj.coords[1] -= (int) ((double)gWeapons.fromCode(obj.src).bulletVel*mod*Math.sin(obj.fv+Math.PI/2));
                }
                checkBulletSplashes();
//                //popups
                thingMap = clientScene.getThingMap("THING_POPUP");
                for (String id : thingMap.keySet()) {
                    gThing obj = thingMap.get(id);
                    obj.coords[0] -= (int) (sSettings.velocity_popup*Math.cos(obj.fv+Math.PI/2));
                    obj.coords[1] -= (int) (sSettings.velocity_popup*Math.sin(obj.fv+Math.PI/2));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkBulletSplashes() {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gThing> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gThing> grenadeSeeds = new ArrayList<>();
        ConcurrentHashMap<String, gThing> bulletsMap = clientScene.getThingMap("THING_BULLET");
        for (String id : bulletsMap.keySet()) {
            gThing t = bulletsMap.get(id);
            for(String blockId : clientScene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = clientScene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(t.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(t.id);
                    if(t.src == gWeapons.launcher)
                        grenadeSeeds.add(t);
                }
            }
            for(String playerId : getPlayerIds()) {
                gPlayer p = getPlayerById(playerId);
                if(p != null && t.collidesWithThing(p) && !t.srcId.equals(playerId)) {
                    bulletsToRemovePlayerMap.put(p, t);
                    if(t.src == gWeapons.launcher)
                        grenadeSeeds.add(t);
                }
            }
        }
        if(grenadeSeeds.size() > 0) {
            for(gThing pseed : grenadeSeeds) {
                gWeapons.createGrenadeExplosion(pseed, clientScene);
            }
        }
        for(String bulletId : bulletsToRemoveIds) {
            clientScene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            clientScene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).id);
        }
    }

    public gPlayer getPlayerById(String id) {
        if(!clientScene.getThingMap("THING_PLAYER").containsKey(id))
            return null;
        return (gPlayer) clientScene.getThingMap("THING_PLAYER").get(id);
    }

    private void selectThingUnderMouse() {
        if(!sSettings.clientMapLoaded)
            return;
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : clientScene.getThingMap("THING_ITEM").keySet()) {
            gThing item = clientScene.getThingMap("THING_ITEM").get(id);
            if(item.coordsWithinBounds(mc[0], mc[1])) {
                sSettings.clientSelectedItemId = item.id;
                sSettings.clientSelectedPrefabId = "";
                return;
            }
        }
        for(String id : clientScene.getThingMap("THING_BLOCK").keySet()) {
            gBlock block = (gBlock) clientScene.getThingMap("THING_BLOCK").get(id);
            if(block.coordsWithinBounds(mc[0], mc[1])) {
                sSettings.clientSelectedPrefabId = block.prefabId;
                sSettings.clientSelectedItemId = "";
                return;
            }
        }
        sSettings.clientSelectedPrefabId = "";
        sSettings.clientSelectedItemId = "";
    }

    private boolean isUserPlayer(gPlayer player) {
        return player.id.equals(sSettings.uuid);
    }

    private void checkPlayerFire() {
        if(getUserPlayer() != null && iMouse.holdingMouseLeft) {
            gPlayer player = getUserPlayer();
            int weapint = player.weapon;
            long gametimemillis = sSettings.gameTime;
            if(player.cooldown <= gametimemillis) {
                clientNetThread.addNetCmd(String.format("fireweapon %s %d", sSettings.uuid, weapint));
                player.cooldown = gametimemillis + gWeapons.fromCode(weapint).refiredelay;
            }
        }
    }

    private void pointPlayerAtMousePointer() {
        gPlayer p = getUserPlayer();
        int[] mc = uiInterface.getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.coords[0] + p.dims[0]/2 - gCamera.coords[0]);
        double dy = mc[1] - eUtils.scaleInt(p.coords[1] + p.dims[1]/2 - gCamera.coords[1]);
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.fv = angle;
        p.checkSpriteFlip();
    }

    @Override
    public void render() {
        displayPane.frame.repaint();
        long gameTimeMillis = sSettings.gameTime;
        if (frameCounterTime < gameTimeMillis) {
            sSettings.fpsReport = sSettings.frames;
            sSettings.frames = 0;
            frameCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void cleanup() {
        serverVars.saveToFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        clientVars.saveToFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        if(sSettings.clientDebugLog)
            console.saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }
}
