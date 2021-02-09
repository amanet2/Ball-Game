public class xComSlot3 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.AUTORIFLE.code());
        return fullCommand;
    }
}