public class xComSlot2 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.Type.SHOTGUN.code());
        return fullCommand;
    }
}