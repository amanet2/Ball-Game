public class xComDropFlagRed extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer p = cGameLogic.userPlayer();
        if(p != null) {
            System.out.println("DROP FLAG (RED)");
//            String doString = String.format("putprop %d %s %s %d %d %d %d",
//                    gProps.getTitleForCode(gProps.FLAGRED), "1", "0", p.getInt("coordx") + p.getInt("dimw"),
//                    p.getInt("coordy") + p.getInt("dimh"), 300, 300);
//            cVars.putInt("weaponstock"+p.get("weapon"), 0);
//            xCon.ex(doString);
        }
        return "drop weapon";
    }
}
