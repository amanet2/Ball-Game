public class xComEditorSelectFlare extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            cEditorLogic.state.selectedFlareId = toks[1];
            return "";
        }
        return "usage: e_selectflare <flareId>";
    }
}
