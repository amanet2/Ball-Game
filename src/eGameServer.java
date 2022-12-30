public class eGameServer extends eGameSession {
    public eGameServer() {
        super(new eGameServerLogic(), sSettings.rateserver);
    }
}
