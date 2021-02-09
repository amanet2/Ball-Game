public class xComSlot4 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.LAUNCHER.code());
        return fullCommand;
    }
}