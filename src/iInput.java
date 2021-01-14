import java.awt.*;
import java.awt.event.*;

public class iInput {
	static final iKeyboard keyboardInput = new iKeyboard();
	static final iMouse mouseInput = new iMouse();
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
	        if(command == KeyEvent.VK_UP) {
                xCon.instance().prevCommandIndex =
                    xCon.instance().prevCommandIndex > -1 ? xCon.instance().prevCommandIndex-1
                        : xCon.instance().previousCommands.size()-1;
                xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                        ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                xCon.instance().cursorIndex = xCon.instance().commandString.length();
	            return;
            }
            if(command == KeyEvent.VK_DOWN) {
                xCon.instance().prevCommandIndex =
                    xCon.instance().prevCommandIndex < xCon.instance().previousCommands.size() - 1
                        ? xCon.instance().prevCommandIndex+1 : -1;
                xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                    ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                xCon.instance().cursorIndex = xCon.instance().commandString.length();
                return;
            }
            if(command == KeyEvent.VK_LEFT) {
                xCon.instance().cursorIndex = xCon.instance().cursorIndex > 0
                    ? xCon.instance().cursorIndex - 1 : 0;
                return;
            }
            if(command == KeyEvent.VK_RIGHT) {
                xCon.instance().cursorIndex = xCon.instance().cursorIndex
                    < xCon.instance().commandString.length() ? xCon.instance().cursorIndex + 1
                    : xCon.instance().commandString.length();
                return;
            }
            if(iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
                for (int i = 0; i < iKeyboard.shiftKeyCodes.length; i++) {
                    if (command == iKeyboard.shiftKeyCodes[i]) {
                        String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                        String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                        xCon.instance().commandString = a+iKeyboard.shiftKeyTexts[i]+b;
                        xCon.instance().cursorIndex++;
                        return;
                    }
                }
            }
            for (int i = 0; i < iKeyboard.specialKeys.length; i++) {
                if (command == iKeyboard.specialKeys[i]) {
                    String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                    String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                    xCon.instance().commandString = a+iKeyboard.specialKeySubs[i]+b;
                    xCon.instance().cursorIndex = xCon.instance().commandString.length();
                    return;
                }
            }
            if(command == KeyEvent.VK_ESCAPE) {
                xCon.ex("console");
                return;
            }
            if(command == KeyEvent.VK_BACK_SPACE) {
                if(xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                    String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex-1);
                    String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                    xCon.instance().commandString = a+b;
                    xCon.instance().cursorIndex--;
                }
                return;
            }
            if(command == KeyEvent.VK_DELETE) {
                if(xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                    String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                    String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex+1);
                    xCon.instance().commandString = a+b;
                }
                return;
            }
            if(command == KeyEvent.VK_ENTER) {
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
            }
            if(command == KeyEvent.VK_SHIFT) {
                iKeyboard.shiftMode = true;
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
                for (int i = 0; i < iKeyboard.shiftKeyCodes.length; i++) {
                    if (command == iKeyboard.shiftKeyCodes[i]) {
                        gMessages.msgInProgress += iKeyboard.shiftKeyTexts[i];
                        return;
                    }
                }
            }
            for (int i = 0; i < iKeyboard.specialKeys.length; i++) {
                if (command == iKeyboard.specialKeys[i]) {
                    gMessages.msgInProgress += iKeyboard.specialKeySubs[i];
                    return;
                }
            }
            if(command == KeyEvent.VK_BACK_SPACE) {
                if(gMessages.msgInProgress.length() > 0)
                    gMessages.msgInProgress = gMessages.msgInProgress.substring(0,
                        gMessages.msgInProgress.length()-1);
                return;
            }
            if(command == KeyEvent.VK_ENTER) {
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
            }
            if(command == KeyEvent.VK_SHIFT) {
                iKeyboard.shiftMode = true;
                return;
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
                if(command == KeyEvent.VK_SHIFT) {
                    iKeyboard.shiftMode = true;
                    return;
                }
                if(command == KeyEvent.VK_CONTROL) {
                    iKeyboard.ctrlMode = true;
                    return;
                }
                if(command == KeyEvent.VK_LEFT) {
                    xCon.ex(String.format("e_newtilequick %d %d",
                        cEditorLogic.state.newTile.getInt("dimw") > 50
                                ? cEditorLogic.state.newTile.getInt("dimw") - 50 : 50,
                        cEditorLogic.state.newTile.getInt("dimh")));
                    return;
                }
                if(command == KeyEvent.VK_RIGHT) {
                    xCon.ex(String.format("e_newtilequick %d %d",
                        cEditorLogic.state.newTile.getInt("dimw") + 50, cEditorLogic.state.newTile.getInt("dimh")));
                    return;
                }
                if(command == KeyEvent.VK_UP) {
                    xCon.ex(String.format("e_newtilequick %d %d", cEditorLogic.state.newTile.getInt("dimw"),
                            cEditorLogic.state.newTile.getInt("dimh") > 50 ?
                            cEditorLogic.state.newTile.getInt("dimh") - 50 : 50));
                    return;
                }
                if(command == KeyEvent.VK_DOWN) {
                    xCon.ex(String.format("e_newtilequick %d %d", cEditorLogic.state.newTile.getInt("dimw"),
                    cEditorLogic.state.newTile.getInt("dimh") + 50));
                    return;
                }
                if(command == KeyEvent.VK_Z && iKeyboard.shiftMode && iKeyboard.ctrlMode) {
                    xCon.ex("-e_undo");
                    return;
                }
                else if(command == KeyEvent.VK_Z && iKeyboard.ctrlMode) {
                    xCon.ex("e_undo");
                    return;
                }
                if(command == KeyEvent.VK_Q && iKeyboard.ctrlMode) {
                    xCon.ex("quit");
                }
                if(command == KeyEvent.VK_O && iKeyboard.ctrlMode) {
                    xCon.ex("e_openfile");
                }
                if(command == KeyEvent.VK_N && iKeyboard.ctrlMode) {
                    if(xCon.getInt("e_showlossalert") > 0)
                        xCon.ex("load");
                }
                if(command == KeyEvent.VK_S && iKeyboard.ctrlMode) {
                    if(eManager.currentMap.wasLoaded < 1)
                        xCon.ex("e_saveas");
                    else
                        xCon.ex("e_save");
                }
                if(command == KeyEvent.VK_C && iKeyboard.ctrlMode) {
                    xCon.ex("e_copytile");
                }
                if(command == KeyEvent.VK_X && iKeyboard.ctrlMode) {
                    xCon.ex("e_copytile");
                    xCon.ex("e_deltile");
                }
                if(command == KeyEvent.VK_V && iKeyboard.ctrlMode) {
                    xCon.ex("e_pastetile");
                }
            }
	        if(xCon.instance().pressBinds.containsKey(command)) {
                xCon.ex(xCon.instance().pressBinds.get(command));
            }
        }
	}

	public static void processKeyReleaseInput(int command) {
	    if(sVars.isOne("inconsole")) {
	        if(command == KeyEvent.VK_BACK_QUOTE) {
                xCon.ex("console");
                return;
            }
            if (command == KeyEvent.VK_SHIFT) {
                iKeyboard.shiftMode = false;
            }
        }
        else if(gMessages.enteringMessage) {
            if (command == KeyEvent.VK_ESCAPE) {
                gMessages.msgInProgress = "";
                gMessages.enteringMessage = false;
                gMessages.enteringOptionText = "";
                return;
            }
            if (command == KeyEvent.VK_SHIFT) {
                iKeyboard.shiftMode = false;
            }
        }
	    else {
            if(sSettings.show_mapmaker_ui) {
                if (command == KeyEvent.VK_SHIFT) {
                    iKeyboard.shiftMode = false;
                }
                if(command == KeyEvent.VK_CONTROL) {
                    iKeyboard.ctrlMode = false;
                }
            }
            //regular comms
            if(xCon.instance().releaseBinds.containsKey(command)) {
                xCon.ex(xCon.instance().releaseBinds.get(command));
            }
        }
	}
}
