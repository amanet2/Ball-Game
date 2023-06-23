import java.awt.*;
import java.util.HashMap;

public class gThing {
    private final int[] coords = {0, 0};
    private final int[] dims = {0, 0};

    public void setCoords(int x, int y) {
        coords[0] = x;
        coords[1] = y;
    }

    public int getX() {
        return coords[0];
    }

    public int getY() {
        return coords[1];
    }

    public void setDims(int w, int h) {
        dims[0] = w;
        dims[1] = h;
    }

    public int getWidth() {
        return dims[0];
    }

    public int getHeight() {
        return dims[1];
    }

    private HashMap<String, String> vars;
    public String get(String s) {
        if(!vars.containsKey(s))
            return "null";
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

    public boolean isVal(String k, String v) {
        if(!contains(k))
            return false;
        return get(k).equals(v);
    }
    public boolean isInt(String k, int v) {
        return isVal(k, Integer.toString(v));
    }

    public boolean contains(String s) {
        return vars().containsKey(s);
    }

    public HashMap<String, String> vars() {
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

    public String toString() {
        return vars().toString();
    }

    public gThing(){
        refresh();
    }

    public void refresh() {
        if(vars == null)
            vars = new HashMap<>();
    }

    public boolean coordsWithinBounds(int x, int y) {
        return (x >= eUtils.scaleInt(getInt("coordx")-gCamera.getX())
                && x <= eUtils.scaleInt(getInt("coordx")-gCamera.getX()+getInt("dimw")))
                && (y >= eUtils.scaleInt(getInt("coordy")-gCamera.getY())
                && y <= eUtils.scaleInt(getInt("coordy")-gCamera.getY()+getInt("dimh")));
    }

    public boolean collidesWithThing(gThing target) {
        String tx = target.get("coordx");
        String ty = target.get("coordy");
        String tw = target.get("dimw");
        String th = target.get("dimh");
        String dx = get("coordx");
        String dy = get("coordy");
        String dw = get("dimw");
        String dh = get("dimh");
        for(String s : new String[]{tx,ty,tw,th,dx,dy,dw,dh}) {
            if(s == null || s.equalsIgnoreCase("null"))
                return false;
        }
        int itx = Integer.parseInt(tx);
        int ity = Integer.parseInt(ty);
        int itw = Integer.parseInt(tw);
        int ith = Integer.parseInt(th);
        int idx = Integer.parseInt(dx);
        int idy = Integer.parseInt(dy);
        int idw = Integer.parseInt(dw);
        int idh = Integer.parseInt(dh);
        return new Rectangle(itx, ity, itw, ith).intersects(new Rectangle(idx, idy, idw, idh));
    }
}
