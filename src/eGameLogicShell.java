import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class eGameLogicShell extends eGameLogicAdapter {
    private long frameCounterTime = -1;
    gArgSet serverVars;
    gArgSet clientVars;
    gScriptFactory scriptFactory;
    JFrame frame;
    JLayeredPane contentPane;
    xCon console;
    gScene serverScene;
    gScene clientScene;
    gScene clientPreviewScene;
    gScheduler scheduledEvents;
    eGameLogicSimulation serverSimulationThread;
    public eGameLogicServer serverNetThread;
    eGameLogicClient clientNetThread;
    BufferedImage[] floorTextureSourceImages;
    BufferedImage[] wallTextureSourceImages;
    BufferedImage[] topTextureSourceImages;
    TexturePaint[] floorTextures;
    TexturePaint[] wallTextures;
    TexturePaint[] topTextures;
    boolean enteringMessage = false;
    String msgInProgress = "";
    String prompt = "SAY";

    public void cancelEnterMessage() {
        msgInProgress = "";
        enteringMessage = false;
    }


    public eGameLogicShell() {
        super();
        serverVars = new gArgSet();
        clientVars = new gArgSet();
        scriptFactory = new gScriptFactory();
        contentPane = new JLayeredPane();
        console = new xCon();
        scheduledEvents = new gScheduler();
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
                if(frame != null) {
                    refreshResolution();
                    dScreenMessages.refreshLogos();
                }
            }
        });
        clientVars.putArg(new gArg("height", "1080") {
            public void onChange() {
                sSettings.height = Integer.parseInt(value);
                dFonts.refreshFonts();
                if(frame != null) {
                    refreshResolution();
                    dScreenMessages.refreshLogos();
                }
            }
        });
        clientVars.putArg(new gArg("refresh", "60") {
            public void onChange() {
                sSettings.rateShell = Integer.parseInt(value);
                xMain.shellSession.tickRate = sSettings.rateShell;
            }
        });
        clientVars.putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(!sSettings.audioenabled && clientScene != null) {
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
                if(sSettings.show_mapmaker_ui)
                    sSettings.culling = false;
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
                if(frame != null) {
                    createPanels();
                    showFrame();
                }
            }
        });
        clientVars.putArg(new gArg("culling", "1") {
            public void onChange() {
                sSettings.culling = value.equalsIgnoreCase("true") || value.equals("1");
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
        clientVars.putArg(new gArg("mapthemes", sSettings.mapThemes[sSettings.mapTheme]) {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.mapThemes = new String[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    sSettings.mapThemes[i] = toks[i].strip();
                }
                uiMenus.menuSelection[uiMenus.MENU_THEME].items = uiMenus.getThemeMenuItems();
            }
        });
        clientVars.putArg(new gArg("maptheme", Integer.toString(sSettings.mapTheme)) {
            public void onChange() {
                int requestedTheme = Integer.parseInt(value);
                if(sSettings.mapThemes.length > requestedTheme)
                    sSettings.mapTheme = requestedTheme;
            }

            public String getValue() {
                return Integer.toString(sSettings.mapTheme);
            }
        });
        clientVars.putArg(new gArg("maxhp", "500") {
            public void onChange() {
                sSettings.clientMaxHP = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("powersave", sSettings.powerSave ? "1" : "0") {
            public void onChange() {
                sSettings.powerSave = value.equals("1");
            }

            public String getValue() {
                return sSettings.powerSave ? "1" : "0";
            }
        });
        clientVars.putArg(new gArg("velocityplayerbase", Integer.toString(sSettings.clientVelocityPlayerBase)) {
            public void onChange() {
                sSettings.clientVelocityPlayerBase = Integer.parseInt(value);
            }
        });
        clientVars.putArg(new gArg("framerates", "30,60,120,240,360,540,1000") {
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
            sSettings.showscale = true;
        }
        try {
            floorTextureSourceImages = new BufferedImage[sSettings.mapThemes.length];
            wallTextureSourceImages = new BufferedImage[sSettings.mapThemes.length];
            topTextureSourceImages = new BufferedImage[sSettings.mapThemes.length];
            floorTextures = new TexturePaint[sSettings.mapThemes.length];
            wallTextures = new TexturePaint[sSettings.mapThemes.length];
            topTextures = new TexturePaint[sSettings.mapThemes.length];
            for(int i = 0; i < sSettings.mapThemes.length; i++) {
                String floorPath = eManager.getPath(String.format("tiles/floor/%s.png", sSettings.mapThemes[i]));
                String wallPath = eManager.getPath(String.format("tiles/wall/%s.png", sSettings.mapThemes[i]));
                String topPath = eManager.getPath(String.format("tiles/top/%s.png", sSettings.mapThemes[i]));
                floorTextureSourceImages[i] = ImageIO.read(new File(floorPath));
                wallTextureSourceImages[i] = ImageIO.read(new File(wallPath));
                topTextureSourceImages[i] = ImageIO.read(new File(topPath));
                floorTextures[i] = new TexturePaint(floorTextureSourceImages[i],
                        new Rectangle2D.Double(0,0,300, 300));
                wallTextures[i] = new TexturePaint(wallTextureSourceImages[i],
                        new Rectangle2D.Double(0,0, 300, 300));
                topTextures[i] = new TexturePaint(topTextureSourceImages[i],
                        new Rectangle2D.Double(0,0, 300, 300));
            }
        }
        catch (IOException err) {
            err.printStackTrace();
        }
        showFrame();
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
            if(frame.isVisible()) {
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
        if(clientScene != null)
            return clientScene.getPlayerById(sSettings.uuid);
        return null;
    }

    public void refreshResolution() {
        showFrame();
        createPanels();
        gTextures.refreshObjectSprites();
    }

    public void showFrame() {
        if(frame != null)
            frame.dispose();
        frame = new JFrame(String.format("Ball Game%s", sSettings.show_mapmaker_ui ? " [EDITOR]" : ""));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                xMain.shellLogic.console.ex("quit");
            }
        });
        frame.setUndecorated(sSettings.borderless);
        if(sSettings.show_mapmaker_ui) {
            uiEditorMenus.setupMapMakerWindow();
            xMain.shellLogic.console.ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", sSettings.clientNewPrefabName));
        }
        frame.setResizable(false);
        contentPane.setPreferredSize(new Dimension(sSettings.width,sSettings.height));
        createPanels();
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //add listeners
        frame.addKeyListener(iInput.keyboardInput);
        frame.addMouseListener(iInput.mouseInput);
        frame.addMouseMotionListener(iInput.mouseInput);
        frame.addMouseWheelListener(iInput.mouseInput);
        frame.setFocusTraversalKeysEnabled(false);
    }

    public void createPanels() {
        contentPane.removeAll();
        contentPane.setBackground(gColors.getColorFromName("clrf_background"));
        dPanel vfxPanel = new dPanel();
        vfxPanel.setBounds(0, 0, sSettings.width, sSettings.height);
        contentPane.setOpaque(true);
        contentPane.add(vfxPanel, 0, 0);
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
        gPlayer userPlayer = getUserPlayer();
        if(sSettings.show_mapmaker_ui && userPlayer == null)
            gCamera.updatePositionMapmaker();
        else if(userPlayer != null)
            gCamera.updatePositionTrackThing(userPlayer);
        synchronized (clientScene.objectMaps) {
            try {
                for (String id : getPlayerIds()) {
                    gPlayer obj = getPlayerById(id);
                    if (obj != null)
                        updatePlayerPositionShell(obj, gameTimeMillis);
                }
                //bullets
                ConcurrentHashMap<String, gThing> thingMap = clientScene.getThingMap("THING_BULLET");
                for (String id : thingMap.keySet()) {
                    gThing thing = thingMap.get(id);
                    updateThingPositionFromVelocity(thingMap.get(id), gWeapons.fromCode(thing.src).bulletVel);
                }
                checkBulletSplashes();
//                //popups
                thingMap = clientScene.getThingMap("THING_POPUP");
                for (String id : thingMap.keySet()) {
                    updateThingPositionFromVelocity(thingMap.get(id), sSettings.velocity_popup);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateThingPositionFromVelocity(gThing thing, double velocity) {
        if(thing == null)
            return;
        double mod = (double)sSettings.ratesimulation/(double)sSettings.rateShell;
        thing.coords = new int[]{
                thing.coords[0] - (int) (velocity*mod*Math.cos(thing.fv+Math.PI/2)),
                thing.coords[1] - (int) (velocity*mod*Math.sin(thing.fv+Math.PI/2))
        };
    }

    public void updatePlayerPositionShell(gPlayer obj, long gameTimeMillis) {
        double mod = (double)sSettings.ratesimulation/(double)sSettings.rateShell;
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
        int velX = obj.vel3 - obj.vel2;
        int velY = obj.vel1 - obj.vel0;
        int newX = (int)(obj.coords[0] + ((double)velX * mod));
        int newY = (int)(obj.coords[1] + ((double)velY * mod));
        if (!obj.wontClipOnMove(newX, obj.coords[1], clientScene))
            newX = obj.coords[0];
        if (!obj.wontClipOnMove(obj.coords[0], newY, clientScene))
            newY = obj.coords[1];
        obj.coords = new int[]{newX, newY};
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

    public int[] getMouseCoordinates() {
        return new int[]{
                MouseInfo.getPointerInfo().getLocation().x - xMain.shellLogic.frame.getLocationOnScreen().x,
                MouseInfo.getPointerInfo().getLocation().y - xMain.shellLogic.frame.getLocationOnScreen().y
        };
    }

    public int[] getPlaceObjCoords() {
        int[] mc = getMouseCoordinates();
        int[] fabdims = uiEditorMenus.getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+(int) gCamera.coords[0] - fabdims[0]/2,
                uiEditorMenus.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+(int) gCamera.coords[1] - fabdims[1]/2,
                uiEditorMenus.snapToY);
        return new int[]{pfx, pfy};
    }

    public synchronized void getUIMenuItemUnderMouse() {
        if(!sSettings.hideMouseUI) {
            int[] mc = getMouseCoordinates();
            int[] xBounds = new int[]{0, sSettings.width / 4};
            int[] yBounds = sSettings.borderless
                    ? new int[]{14 * sSettings.height / 16, sSettings.height}
                    : new int[]{15 * sSettings.height / 16, 17 * sSettings.height / 16};
            if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                if (!uiMenus.gobackSelected) {
                    uiMenus.gobackSelected = true;
                    uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
                }
                return;
            } else
                uiMenus.gobackSelected = false;
            if (uiMenus.selectedMenu != uiMenus.MENU_CONTROLS) {
                for (int i = 0; i < uiMenus.menuSelection[uiMenus.selectedMenu].items.length; i++) {
                    xBounds = new int[]{sSettings.width / 2 - sSettings.width / 8,
                            sSettings.width / 2 + sSettings.width / 8};
                    yBounds = new int[]{11 * sSettings.height / 30 + i * sSettings.height / 30,
                            11 * sSettings.height / 30 + (i + 1) * sSettings.height / 30};
                    if (!sSettings.borderless) {
                        yBounds[0] += 40;
                        yBounds[1] += 40;
                    }
                    if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                        if (uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem != i)
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = i;
                        return;
                    }
                }
            }
            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
        }
    }

    private void selectThingUnderMouse() {
        if(!sSettings.clientMapLoaded)
            return;
        int[] mc = getMouseCoordinates();
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

    public boolean isUserPlayer(gPlayer player) {
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
        int[] mc = getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.coords[0] + p.dims[0]/2 - (int) gCamera.coords[0]);
        double dy = mc[1] - eUtils.scaleInt(p.coords[1] + p.dims[1]/2 - (int) gCamera.coords[1]);
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.fv = angle;
        p.checkSpriteFlip();
    }

    @Override
    public void render() {
        frame.repaint();
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
