public class eGameClient extends eGameSession {
    public eGameClient() {
        super(new eGameClientLogic(), sSettings.rategame);
    }
}
