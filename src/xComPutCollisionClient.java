public class xComPutCollisionClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 5)
            return "usage: cl_putcollision <id> <pid> <xarr> <yarr>";
        gCollisionFactory factory = gCollisionFactory.instance();
        String id = toks[1];
        String pid = toks[2];
        String xarrString = toks[3];
        String yarrString = toks[4];
        String[] args = new String[]{id, pid, xarrString, yarrString};
        gDoableCollisionReturn collisionReturn = factory.collisionLoader;
        gCollision newCollision = collisionReturn.getCollision(args);
        cClientLogic.scene.getThingMap("THING_COLLISION").put(newCollision.get("id"), newCollision);
        return "usage: cl_putcollision <xarr> <yarr>";
    }
}
