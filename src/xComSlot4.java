public class xComSlot4 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.Type.LAUNCHER.code());
        return fullCommand;
    }
}