public class eGameSessionClient extends eGameSession {
    public eGameSessionClient() {
        super(new eGameLogicClient(), sSettings.rategame);
        if(sSettings.show_mapmaker_ui) {
            sSettings.drawhitboxes = true;
            sSettings.drawmapmakergrid = true;
            cClientVars.instance().put("zoomlevel", "0.5");
        }
    }
}
