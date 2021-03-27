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
            collisionReturn.storeCollision(collisionReturn.getCollision(args), eManager.currentMap.scene);
        }
        return "usage: putcollision <xarr> <yarr> <npoints>";
    }
}
