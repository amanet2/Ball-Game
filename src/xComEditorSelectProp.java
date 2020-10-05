public class xComEditorSelectProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            cEditorLogic.state.selectedPropId = Integer.parseInt(toks[1]);
            return "";
        }
        return "usage: e_selectprop <propId>";
    }
}
