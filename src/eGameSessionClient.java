public class eGameSessionClient extends eGameSession {
    public eGameSessionClient() {
        super(new eGameLogicClient(), sSettings.rategame);
    }
}
