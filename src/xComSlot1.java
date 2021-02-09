public class xComSlot1 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.PISTOL.code());
        return fullCommand;
    }
}