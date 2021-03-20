public class xComPutFlare extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 12) {
            gFlare p = new gFlare(
                Integer.parseInt(toks[1]),
                Integer.parseInt(toks[2]),
                Integer.parseInt(toks[3]),
                Integer.parseInt(toks[4]),
                Integer.parseInt(toks[5]),
                Integer.parseInt(toks[6]),
                Integer.parseInt(toks[7]),
                Integer.parseInt(toks[8]),
                Integer.parseInt(toks[9]),
                Integer.parseInt(toks[10]),
                Integer.parseInt(toks[11]),
                Integer.parseInt(toks[12])
            );
            p.put("tag", Integer.toString(eManager.currentMap.scene.flares().size()));
            eManager.currentMap.scene.flares().add(p);
            return "";
        }
        return "usage: putflare <x> <y> <w> <h> <r1> <g1> <b1> <a1> <r2> <g2> <b2> <a2>";
    }
}
