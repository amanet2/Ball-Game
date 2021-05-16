public class xComPutCollision extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 3) {
            gCollisionFactory factory = gCollisionFactory.instance();
            String xarrString = toks[1];
            String yarrString = toks[2];
            String npointsString = toks[3];

            String[] args = new String[]{xarrString, yarrString, npointsString};
            gDoableCollisionReturn collisionReturn = factory.collisionLoader;
            gCollision newCollision = collisionReturn.getCollision(args);
            newCollision.put("prefabid", cVars.get("prefabid"));
            String collisionId = Integer.toString(cServerLogic.scene.collisionIdCtr);
            cServerLogic.scene.getThingMap("THING_COLLISION").put(collisionId, newCollision);
            cServerLogic.scene.collisionIdCtr += 1;
        }
        return "usage: putcollision <xarr> <yarr> <npoints>";
    }
}
