public interface eGameLogic {

    void init() throws Exception;

    void input();

    void update(long gameTimeMillis);

    void render(long gameTimeMillis);

    void cleanup();
}
