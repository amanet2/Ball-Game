public class xComSlot3 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.Type.AUTORIFLE.code());
        return fullCommand;
    }
}