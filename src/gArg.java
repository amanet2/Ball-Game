public class gArg {
    String key;
    String value;

    public gArg(String k, String v) {
        key = k;
        value = v;
    }

    public String toString() {
        return value;
    }

    public void onChange() {

    }

    public void onUpdate() {
        //executed every time argset.put is called for this arg
    }
}
