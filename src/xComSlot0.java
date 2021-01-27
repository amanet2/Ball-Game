public class xComSlot0 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.Type.NONE.code());
        return fullCommand;
    }
}