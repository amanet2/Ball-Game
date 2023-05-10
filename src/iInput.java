import java.awt.event.KeyEvent;
import java.awt.Toolkit;

public class iInput {
	static final iKeyboard keyboardInput = new iKeyboard();
	static final iMouse mouseInput = new iMouse();
	static final iMouseMotion mouseMotion = new iMouseMotion();
	static final iMouseWheel mouseWheelInput = new iMouseWheel();

	public static void readKeyInputs() {
        while (iKeyboard.inputPressQueue.size() > 0) {
            Integer cm = iKeyboard.inputPressQueue.poll();
            if(cm != null)
                processKeyPressInput(cm);
        }
        while (iKeyboard.inputReleaseQueue.size() > 0) {
            Integer cm = iKeyboard.inputReleaseQueue.poll();
            if(cm != null)
                processKeyReleaseInput(cm);
        }
	}

	public static void processKeyPressInput(int command) {
	    if(uiInterface.inconsole) {
            switch (command) {
                case KeyEvent.VK_UP -> {
                    xCon.instance().prevCommandIndex =
                            xCon.instance().prevCommandIndex > -1 ? xCon.instance().prevCommandIndex - 1
                                    : xCon.instance().previousCommands.size() - 1;
                    xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                            ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                    xCon.instance().cursorIndex = xCon.instance().commandString.length();
                    return;
                }
                case KeyEvent.VK_DOWN -> {
                    xCon.instance().prevCommandIndex =
                            xCon.instance().prevCommandIndex < xCon.instance().previousCommands.size() - 1
                                    ? xCon.instance().prevCommandIndex + 1 : -1;
                    xCon.instance().commandString = xCon.instance().prevCommandIndex > -1
                            ? xCon.instance().previousCommands.get(xCon.instance().prevCommandIndex) : "";
                    xCon.instance().cursorIndex = xCon.instance().commandString.length();
                    return;
                }
                case KeyEvent.VK_LEFT -> {
                    xCon.instance().cursorIndex = xCon.instance().cursorIndex > 0
                            ? xCon.instance().cursorIndex - 1 : 0;
                    return;
                }
                case KeyEvent.VK_RIGHT -> {
                    xCon.instance().cursorIndex = xCon.instance().cursorIndex
                            < xCon.instance().commandString.length() ? xCon.instance().cursorIndex + 1
                            : xCon.instance().commandString.length();
                    return;
                }
                case KeyEvent.VK_ESCAPE -> {
                    xCon.ex("console");
                    return;
                }
                case KeyEvent.VK_BACK_SPACE -> {
                    if (xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                        String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex - 1);
                        String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex);
                        xCon.instance().commandString = a + b;
                        xCon.instance().cursorIndex--;
                    }
                    return;
                }
                case KeyEvent.VK_DELETE -> {
                    if (xCon.instance().cursorIndex > 0 && xCon.instance().commandString.length() > 0) {
                        String a = xCon.instance().commandString.substring(0, xCon.instance().cursorIndex);
                        String b = xCon.instance().commandString.substring(xCon.instance().cursorIndex + 1);
                        xCon.instance().commandString = a + b;
                    }
                    return;
                }
                case KeyEvent.VK_ENTER -> {
                    String res = xCon.ex(xCon.instance().commandString);
                    xCon.instance().previousCommands.add(xCon.instance().commandString);
                    xCon.instance().stringLines.add(String.format("console:~$ %s",
                            xCon.instance().commandString));
                    if (res.length() > xCon.charlimit()) {
                        xCon.instance().stringLines.add(res.substring(0, xCon.charlimit()));
                        for (int i = xCon.charlimit(); i < res.length();
                             i += xCon.charlimit()) {
                            int lim = Math.min(res.length(), i + xCon.charlimit());
                            xCon.instance().stringLines.add(res.substring(i, lim));
                        }
                    } else
                        xCon.instance().stringLines.add(res);
                    xCon.instance().linesToShowStart = Math.max(0,
                            xCon.instance().stringLines.size() - xCon.instance().linesToShow);
                    xCon.instance().prevCommandIndex = -1;
                    xCon.instance().commandString = "";
                    xCon.instance().cursorIndex = 0;
                    return;
                }
                case KeyEvent.VK_SHIFT -> {
                    iKeyboard.shiftMode = true;
                    return;
                }
                default -> {
                }
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
            if(xCon.instance().commandString.length() < xCon.maxlinelength){
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
                case KeyEvent.VK_BACK_SPACE -> {
                    if (gMessages.msgInProgress.length() > 0)
                        gMessages.msgInProgress = gMessages.msgInProgress.substring(0,
                                gMessages.msgInProgress.length() - 1);
                    return;
                }
                case KeyEvent.VK_ENTER -> {
                    switch (gMessages.prompt) {
                        case "Enter New Name" -> {
                            xMain.shellLogic.clientVars.put("playername", gMessages.msgInProgress);
                            uiMenus.menuSelection[uiMenus.MENU_PROFILE].refresh();
                            if (sSettings.show_mapmaker_ui)
                                uiEditorMenus.menus.get("Settings").getItem(0).setText("Name: " + cClientLogic.playerName);
                            gMessages.msgInProgress = "";
                        }
                        case "Enter New IP Address" -> {
                            xMain.shellLogic.clientVars.put("joinip", gMessages.msgInProgress);
                            gMessages.msgInProgress = "";
                        }
                        case "Enter New Port" -> {
                            xMain.shellLogic.clientVars.put("joinport", gMessages.msgInProgress);
                            gMessages.msgInProgress = "";
                        }
                        default -> xCon.ex(String.format("say %s", gMessages.msgInProgress));
                    }
                    gMessages.enteringMessage = false;
                    return;
                }
                case KeyEvent.VK_SHIFT -> {
                    iKeyboard.shiftMode = true;
                    return;
                }
                default -> {
                }
            }
            if(gMessages.msgInProgress.length() < 64){
                gMessages.msgInProgress += KeyEvent.getKeyText(command).toLowerCase().contains("numpad")
                    ? KeyEvent.getKeyText(command).toLowerCase().replace("numpad",
                    "").replace("-","").replace(" ", "")
                    : iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)
                        ? KeyEvent.getKeyText(command) : KeyEvent.getKeyText(command).toLowerCase();
            }
        }
        else if(xCon.instance().pressBinds.containsKey(command))
            xCon.ex(xCon.instance().pressBinds.get(command));
	}

	public static void processKeyReleaseInput(int command) {
	    if(uiInterface.inconsole) {
            switch (command) {
                case KeyEvent.VK_BACK_QUOTE -> {
                    xCon.ex("console");
                }
                case KeyEvent.VK_SHIFT -> iKeyboard.shiftMode = false;
                default -> {
                }
            }
        }
        else if(gMessages.enteringMessage) {
            switch (command) {
                case KeyEvent.VK_ESCAPE -> {
                    gMessages.msgInProgress = "";
                    gMessages.enteringMessage = false;
                }
                case KeyEvent.VK_SHIFT -> iKeyboard.shiftMode = false;
                default -> {
                }
            }
        }
	    else {
            if(sSettings.show_mapmaker_ui) {
                switch (command) {
                    case KeyEvent.VK_SHIFT -> iKeyboard.shiftMode = false;
                    case KeyEvent.VK_CONTROL -> iKeyboard.ctrlMode = false;
                    default -> {
                    }
                }
            }
            //regular comms
            if(xCon.instance().releaseBinds.containsKey(command)) {
                xCon.ex(xCon.instance().releaseBinds.get(command));
            }
        }
	}
}
