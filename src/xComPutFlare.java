public class xComPutFlare extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 12) {
            gFlareFactory factory = gFlareFactory.instance();
            String[] args = new String[13];
            System.arraycopy(toks, 1, args, 1, args.length - 1);
            gDoableFlareReturn flareReturn = factory.flareLoader;
            gFlare newFlare = flareReturn.getFlare(args);
            newFlare.put("flareid", cVars.get("flareid"));
            String flareId = Integer.toString(cServerLogic.scene.flareIdCtr);
            cServerLogic.scene.getThingMap("THING_FLARE").put(flareId, newFlare);
            cServerLogic.scene.flareIdCtr += 1;
        }
        return "usage: putflare <x> <y> <w> <h> <r1> <g1> <b1> <a1> <r2> <g2> <b2> <a2>";
    }
}
