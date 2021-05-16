import java.util.HashMap;
import java.util.LinkedHashMap;

public class gThing {
    private LinkedHashMap<String, String> vars;
    public String get(String s) {
        return vars.get(s);
    }
    public int getInt(String s) {
        return Integer.parseInt(get(s));
    }
    public double getDouble(String s) {
        return Double.parseDouble(get(s));
    }
    public long getLong(String s) {
        return Long.parseLong(get(s));
    }
    public void put(String s, String v) {
        vars.put(s, v);
    }
    public void putInt(String s, int v) {
        put(s, Integer.toString(v));
    }
    public void putDouble(String s, double v) {
        put(s, Double.toString(v));
    }
    public void putLong(String s, long v) {
        put(s, Long.toString(v));
    }

    public void remove(String s) {
        vars.remove(s);
    }

    public boolean isVal(String k, String v) {
        return get(k).equals(v);
    }
    public boolean isInt(String k, int v) {
        return isVal(k, Integer.toString(v));
    }
    public boolean isOne(String s) {
        return isVal(s,"1");
    }
    public boolean isZero(String s) {
        return isVal(s,"0");
    }

    public boolean contains(String s) {
        return vars().containsKey(s);
    }

    public LinkedHashMap<String, String> vars() {
        refresh();
        return vars;
    }

    public boolean containsFields(String[] fields) {
        for(String required : fields) {
            if(get(required) == null)
                return false;
        }
        return true;
    }

    public String keysString() {
        return vars().keySet().toString();
    }

    public String toString() {
        return vars().toString();
    }

    public gThing(){
        refresh();
    }

    public void refresh() {
        if(vars == null)
            vars = new LinkedHashMap<>();
    }

    public void addVal(String key, int val) {
        if(contains(key)) {
            putInt(key, getInt(key)+val);
        }
    }

    public void subtractVal(String key, int val) {
        if(contains(key)) {
            putInt(key, getInt(key)-val);
        }
    }

    public boolean coordsWithinBounds(int x, int y) {
        return (x >= eUtils.scaleInt(getInt("coordx")-cVars.getInt("camx"))
                && x <= eUtils.scaleInt(getInt("coordx")-cVars.getInt("camx")+getInt("dimw")))
                && (y >= eUtils.scaleInt(getInt("coordy")-cVars.getInt("camy"))
                && y <= eUtils.scaleInt(getInt("coordy")-cVars.getInt("camy")+getInt("dimh")));
    }
}
