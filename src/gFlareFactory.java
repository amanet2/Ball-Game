public class gFlareFactory {
    gDoableFlareReturn flareLoader;
    private static gFlareFactory instance = null;

    private gFlareFactory() {
        flareLoader = new gDoableFlareReturn();
    }

    public static gFlareFactory instance() {
        if(instance == null)
            instance = new gFlareFactory();
        return instance;
    }
}
