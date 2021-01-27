public class xComSlot5 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.GLOVES.code());
        return fullCommand;
    }
}