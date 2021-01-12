import java.util.HashMap;
import java.util.LinkedHashMap;

public class gThing {
    private LinkedHashMap<String, String> vars;
    private HashMap<String, gDoable> doables;
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

    public String keys() {
        return vars().keySet().toString();
    }

    public void registerDoable(String k, gDoable doable) {
        doables.put(k, doable);
    }

    public boolean canDo(String doString) {
        return doables.containsKey(doString);
    }

    public void doDoable(String k) {
        if(doables.containsKey(k))
            doables.get(k).doItem(this);
    }

    public String toString() {
        return vars().toString();
    }

    public gThing(){
        refresh();
    }

    public void refresh() {
        if(vars == null) {
            vars = new LinkedHashMap<>();
        }
        if(doables == null) {
            doables = new HashMap<>();
        }
    }

    public boolean coordsWithinBounds(int x, int y) {
        return (x >= eUtils.scaleInt(getInt("coordx")-cVars.getInt("camx"))
                && x <= eUtils.scaleInt(getInt("coordx")-cVars.getInt("camx")+getInt("dimw")))
                && (y >= eUtils.scaleInt(getInt("coordy")-cVars.getInt("camy"))
                && y <= eUtils.scaleInt(getInt("coordy")-cVars.getInt("camy")+getInt("dimh")));
    }
}
