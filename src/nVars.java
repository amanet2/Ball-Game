import java.util.HashMap;
import java.util.Set;

public class nVars {
    public static HashMap<String,String> getMapFromNetString(String argload) {
        HashMap<String,String> toReturn = new HashMap<>();
        String argstr = argload.substring(1,argload.length()-1);
        for(String pair : argstr.split(",")) {
            String[] vals = pair.split("=");
            toReturn.put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
        }
        return  toReturn;
    }

    public static HashMap<String, HashMap<String, String>> getMapFromNetMapString(String argload) {
        HashMap<String, HashMap<String, String>> toReturn = new HashMap<>();
        String modstr = argload.substring(1,argload.length()-1);
        String[] idsplit = modstr.split("},");
        for(String s : idsplit) {
            int ind = s.indexOf("={");
            String idtok = s;
            if(ind > -1)
                idtok = s.substring(0,ind);
            System.out.println(idtok);
            toReturn.put(idtok, new HashMap<>());
            String argstr = s.replace(idtok+"=","").substring(1,
                    s.replace(idtok+"=","").length()-1);
            for(String pair : argstr.split(",")) {
                String[] vals = pair.split("=");
                toReturn.get(idtok).put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
            }
        }
        return toReturn;
    }
}
