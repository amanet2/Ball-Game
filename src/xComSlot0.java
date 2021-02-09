public class xComSlot0 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.type.NONE.code());
        return fullCommand;
    }
}