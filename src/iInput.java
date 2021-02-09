import java.awt.*;
import java.awt.event.*;

public class iInput {
	static final iKeyboard keyboardInput = new iKeyboard();
	static final iMouse mouseInput = new iMouse();
	static final iMouseMotion mouseMotion = new iMouseMotion();
	static final iMouseWheel mouseWheelInput = new iMouseWheel();

	public static void readKeyInputs() {
		while (iKeyboard.inputPressQueue.size() > 0) {
			Integer cm = iKeyboard.inputPressQueue.remove();
			processKeyPressInput(cm);
        }
        while (iKeyboard.inputReleaseQueue.size() > 0) {
            Integer cm = iKeyboard.inputReleaseQueue.remove();
            processKeyReleaseInput(cm);
        }
	}

	public static void processKeyPressInput(int command) {
	    if(sVars.isOne("inconsole")) {
	        switch (command) {
                case KeyEvent.VK_UP:
                    xCon.instance().prevCommandIndex =
                            xCon.instance().prevCommandIndex > -1 ? xCon.instance().prevCommandIndex-1
                                    : xCon.instance().previousCommands.size()-1;
                    xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                            ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                    xCon.instance().cursorIndex = xCon.instance().commandString.length();
                    return;
                case KeyEvent.VK_DOWN:
                    xCon.instance().prevCommandIndex =
                            xCon.instance().prevCommandIndex < xCon.instance().previousCommands.size() - 1
                                    ? xCon.instance().prevCommandIndex+1 : -1;
                    xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                            ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                    xCon.instance().cursorIndex = xCon.instance().commandString.length();
                    return;
                case KeyEvent.VK_LEFT:
                    xCon.instance().cursorIndex = xCon.instance().cursorIndex > 0
                            ? xCon.instance().cursorIndex - 1 : 0;
                    return;
                case KeyEvent.VK_RIGHT:
                    xCon.instance().cursorIndex = xCon.instance().cursorIndex
                            < xCon.instance().commandString.length() ? xCon.instance().cursorIndex + 1
                            : xCon.instance().commandString.length();
                    return;
                case KeyEvent.VK_ESCAPE:
                    xCon.ex("console");
                    return;
                case KeyEvent.VK_BACK_SPACE:
                    if(xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                        String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex-1);
                        String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                        xCon.instance().commandString = a+b;
                        xCon.instance().cursorIndex--;
                    }
                    return;
                case KeyEvent.VK_DELETE:
                    if(xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                        String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                        String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex+1);
                        xCon.instance().commandString = a+b;
                    }
                    return;
                case KeyEvent.VK_ENTER:
                    String res = xCon.ex(xCon.instance().commandString);
                    xCon.instance().previousCommands.add(xCon.instance().commandString);
                    xCon.instance().stringLines.add(String.format("console:~$ %s",
                            xCon.instance().commandString));
                    if(res != null) {
                        if(res.length() > xCon.charlimit()) {
                            xCon.instance().stringLines.add(res.substring(0, xCon.charlimit()));
                            for(int i = xCon.charlimit(); i < res.length();
                                i+= xCon.charlimit()) {
                                int lim = Math.min(res.length(), i+ xCon.charlimit());
                                xCon.instance().stringLines.add(res.substring(i,lim));
                            }
                        }
                        else
                            xCon.instance().stringLines.add(res);
                    }
                    xCon.instance().linesToShowStart = Math.max(0,
                            xCon.instance().stringLines.size() - xCon.instance().linesToShow);
                    xCon.instance().prevCommandIndex = -1;
                    xCon.instance().commandString = "";
                    xCon.instance().cursorIndex = 0;
                    return;
                case KeyEvent.VK_SHIFT:
                    iKeyboard.shiftMode = true;
                    return;
                default:
                    break;
            }
            if(iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                String key = iKeyboard.getShiftKeyForCode(command);
                if(key != null) {
                    String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                    String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                    xCon.instance().commandString = a+key+b;
                    xCon.instance().cursorIndex++;
                    return;
                }
            }
            String specialKey = iKeyboard.getSpecialKeyForCode(command);
            if (specialKey != null) {
                String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                xCon.instance().commandString = a+specialKey+b;
                xCon.instance().cursorIndex = xCon.instance().commandString.length();
                return;
            }
            if(xCon.instance().commandString.length() < 64){
                String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                String toAdd = KeyEvent.getKeyText(command).toLowerCase().contains("numpad")
                    ? KeyEvent.getKeyText(command).toLowerCase().replace("numpad",
                    "").replace("-","").replace(" ", "")
                    : iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)
                        ? KeyEvent.getKeyText(command) : KeyEvent.getKeyText(command).toLowerCase();
                xCon.instance().commandString = a + toAdd + b;
                xCon.instance().cursorIndex++;
            }
        }
	    else if(gMessages.enteringMessage) {
	        if(iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                String key = iKeyboard.getShiftKeyForCode(command);
                if(key != null) {
                    gMessages.msgInProgress += key;
                    return;
                }
            }
            String specialKey = iKeyboard.getSpecialKeyForCode(command);
            if (specialKey != null) {
                    gMessages.msgInProgress += specialKey;
                    return;
            }
            switch (command) {
                case KeyEvent.VK_BACK_SPACE:
                    if(gMessages.msgInProgress.length() > 0)
                        gMessages.msgInProgress = gMessages.msgInProgress.substring(0,
                                gMessages.msgInProgress.length()-1);
                    return;
                case KeyEvent.VK_ENTER:
                    if("skip".contains(gMessages.msgInProgress.toLowerCase().strip())) {
                        if(cVars.contains("canvoteskip")) {
                            cVars.remove("canvoteskip");
                        }
                        else {
                            gMessages.msgInProgress = "";
                            gMessages.enteringMessage = false;
                            xCon.ex("echo YOU HAVE ALREADY VOTED TO SKIP");
                            return;
                        }
                    }
                    xCon.ex(String.format("say %s", gMessages.msgInProgress));
                    gMessages.enteringMessage = false;
                    return;
                case KeyEvent.VK_SHIFT:
                    iKeyboard.shiftMode = true;
                    return;
                default:
                    break;
            }
            if(gMessages.msgInProgress.length() < 64){
                gMessages.msgInProgress += KeyEvent.getKeyText(command).toLowerCase().contains("numpad")
                    ? KeyEvent.getKeyText(command).toLowerCase().replace("numpad",
                    "").replace("-","").replace(" ", "")
                    : iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)
                        ? KeyEvent.getKeyText(command) : KeyEvent.getKeyText(command).toLowerCase();
            }
        }
        else {
            if(sSettings.show_mapmaker_ui) {
                switch(command) {
                    case KeyEvent.VK_SHIFT:
                        iKeyboard.shiftMode = true;
                        return;
                    case KeyEvent.VK_CONTROL:
                        iKeyboard.ctrlMode = true;
                        return;
                    case KeyEvent.VK_LEFT:
                        xCon.ex(String.format("e_newtilequick %d %d",
                                cEditorLogic.state.newTile.getInt("dimw") > 50
                                        ? cEditorLogic.state.newTile.getInt("dimw") - 50 : 50,
                                cEditorLogic.state.newTile.getInt("dimh")));
                        return;
                    case KeyEvent.VK_RIGHT:
                        xCon.ex(String.format("e_newtilequick %d %d",
                                cEditorLogic.state.newTile.getInt("dimw") + 50, cEditorLogic.state.newTile.getInt("dimh")));
                        return;
                    case KeyEvent.VK_UP:
                        xCon.ex(String.format("e_newtilequick %d %d", cEditorLogic.state.newTile.getInt("dimw"),
                                cEditorLogic.state.newTile.getInt("dimh") > 50 ?
                                        cEditorLogic.state.newTile.getInt("dimh") - 50 : 50));
                        return;
                    case KeyEvent.VK_DOWN:
                        xCon.ex(String.format("e_newtilequick %d %d", cEditorLogic.state.newTile.getInt("dimw"),
                                cEditorLogic.state.newTile.getInt("dimh") + 50));
                        return;
                    case KeyEvent.VK_Z:
                        if(iKeyboard.shiftMode && iKeyboard.ctrlMode) {
                            xCon.ex("-e_undo");
                            return;
                        }
                        else if(iKeyboard.ctrlMode) {
                            xCon.ex("e_undo");
                            return;
                        }
                        break;
                    case KeyEvent.VK_Q:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            xCon.ex("quit");
                        }
                        break;
                    case KeyEvent.VK_O:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            xCon.ex("e_openfile");
                        }
                        break;
                    case KeyEvent.VK_N:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            if(xCon.getInt("e_showlossalert") > 0)
                                xCon.ex("load");
                        }
                        break;
                    case KeyEvent.VK_S:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            if(eManager.currentMap.wasLoaded < 1)
                                xCon.ex("e_saveas");
                            else
                                xCon.ex("e_save");
                        }
                        break;
                    case KeyEvent.VK_C:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            xCon.ex("e_copytile");
                        }
                        break;
                    case KeyEvent.VK_X:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            xCon.ex("e_copytile");
                            xCon.ex("e_delthing");
                        }
                        break;
                    case KeyEvent.VK_V:
                        if(iKeyboard.ctrlMode) {
                            iKeyboard.ctrlMode = false;
                            xCon.ex("e_pastetile");
                        }
                        break;
                    default:
                        break;
                }
            }
	        if(xCon.instance().pressBinds.containsKey(command)) {
                xCon.ex(xCon.instance().pressBinds.get(command));
            }
        }
	}

	public static void processKeyReleaseInput(int command) {
	    if(sVars.isOne("inconsole")) {
	        switch (command) {
                case KeyEvent.VK_BACK_QUOTE:
                    xCon.ex("console");
                    return;
                case KeyEvent.VK_SHIFT:
                    iKeyboard.shiftMode = false;
                    break;
                default:
                    break;
            }
        }
        else if(gMessages.enteringMessage) {
            switch (command) {
                case KeyEvent.VK_ESCAPE:
                    gMessages.msgInProgress = "";
                    gMessages.enteringMessage = false;
                    gMessages.enteringOptionText = "";
                    return;
                case KeyEvent.VK_SHIFT:
                    iKeyboard.shiftMode = false;
                    break;
                default:
                    break;
            }
        }
	    else {
            if(sSettings.show_mapmaker_ui) {
                switch (command) {
                    case KeyEvent.VK_SHIFT:
                        iKeyboard.shiftMode = false;
                        break;
                    case KeyEvent.VK_CONTROL:
                        iKeyboard.ctrlMode = false;
                        break;
                    default:
                        break;
                }
            }
            //regular comms
            if(xCon.instance().releaseBinds.containsKey(command)) {
                xCon.ex(xCon.instance().releaseBinds.get(command));
            }
        }
	}
}
