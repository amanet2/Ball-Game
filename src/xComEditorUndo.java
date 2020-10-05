public class xComEditorUndo extends xCom {
    public String doCommand(String fullCommand) {
        if(cEditorLogic.undoStateStack.size() > 0) {
            cEditorLogic.redoStateStack.push(cEditorLogic.getEditorState());
            cEditorLogic.setEditorState(cEditorLogic.undoStateStack.pop());
        }
        return "";
    }

    public String undoCommand(String fullCommand) {
        if(cEditorLogic.redoStateStack.size() > 0) {
            cEditorLogic.undoStateStack.push(cEditorLogic.getEditorState());
            cEditorLogic.setEditorState(cEditorLogic.redoStateStack.pop());
        }
        return "";
    }
}
