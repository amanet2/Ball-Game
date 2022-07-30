public class xComPutCollision extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gCollisionFactory factory = gCollisionFactory.instance();
            String xarrString = toks[1];
            String yarrString = toks[2];

            String[] args = new String[]{xarrString, yarrString};
            gDoableCollisionReturn collisionReturn = factory.collisionLoader;
            gCollision newCollision = collisionReturn.getCollision(args);
            String collisionId = Integer.toString(cServerLogic.scene.collisionIdCtr);
            cServerLogic.scene.getThingMap("THING_COLLISION").put(collisionId, newCollision);
            cServerLogic.scene.collisionIdCtr += 1;
        }
        return "usage: putcollision <xarr> <yarr>";
    }
}
