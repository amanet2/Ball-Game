public class xComSlot2 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.SHOTGUN.code());
        return fullCommand;
    }
}