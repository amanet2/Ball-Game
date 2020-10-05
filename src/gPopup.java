public class gPopup extends gThing {

    public gPopup(int x, int y, String tt, Double fv) {
        super();
        putInt("coordx", x);
        putInt("coordy", y);
        put("text", tt);
        putLong("timestamp", System.currentTimeMillis());
        putDouble("fv", fv);
    }
}
