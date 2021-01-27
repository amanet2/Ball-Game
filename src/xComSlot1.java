public class xComSlot1 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.Type.PISTOL.code());
        return fullCommand;
    }
}