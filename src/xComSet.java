public class xComSet extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String key = toks[1];
            StringBuilder comm = new StringBuilder();
            for(int i = 2; i < toks.length; i++) {
                comm.append(toks[i]).append(" ");
            }
            String finalcomm = comm.toString().substring(0,comm.length()-1);
            if(key.length() > 2 && key.substring(0,3).equals("cv_")) {
                key = key.substring(3);
                if(finalcomm.length() > 2 && finalcomm.substring(0,3).equals("cv_")
                        && cVars.contains(finalcomm.substring(3)))
                    cVars.put(key, cVars.get(finalcomm.substring(3)));
                else
                    cVars.put(key, finalcomm);
                return "cv_" + key + " = " + cVars.get(key);
            }
            else {
                if(sVars.contains(finalcomm))
                    sVars.put(key, sVars.get(finalcomm));
                else
                    sVars.put(key, finalcomm);
                return key + " = " + sVars.get(key);
            }
        }
        return "usage: set <cvar> <value>";
    }

    public String undoCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String key = toks[1];
            if(key.length() > 2 && key.substring(0,3).equals("cv_"))
                cVars.remove(key.substring(3));
            else
                sVars.remove(key);
            return key + " removed";
        }
        return "usage: -set <cvar>";
    }
}
