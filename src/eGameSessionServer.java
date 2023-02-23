public class eGameSessionServer extends eGameSession {
    public eGameSessionServer() {
        super(new eGameLogicServer(), sSettings.rateserver);
    }
}
