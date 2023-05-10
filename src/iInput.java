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
                    xMain.shellLogic.console.prevCommandIndex =
                            xMain.shellLogic.console.prevCommandIndex > -1 ? xMain.shellLogic.console.prevCommandIndex - 1
                                    : xMain.shellLogic.console.previousCommands.size() - 1;
                    xMain.shellLogic.console.commandString = xMain.shellLogic.console.prevCommandIndex > -1
                            ? xMain.shellLogic.console.previousCommands.get(xMain.shellLogic.console.prevCommandIndex) : "";
                    xMain.shellLogic.console.cursorIndex = xMain.shellLogic.console.commandString.length();
                    return;
                }
                case KeyEvent.VK_DOWN -> {
                    xMain.shellLogic.console.prevCommandIndex =
                            xMain.shellLogic.console.prevCommandIndex < xMain.shellLogic.console.previousCommands.size() - 1
                                    ? xMain.shellLogic.console.prevCommandIndex + 1 : -1;
                    xMain.shellLogic.console.commandString = xMain.shellLogic.console.prevCommandIndex > -1
                            ? xMain.shellLogic.console.previousCommands.get(xMain.shellLogic.console.prevCommandIndex) : "";
                    xMain.shellLogic.console.cursorIndex = xMain.shellLogic.console.commandString.length();
                    return;
                }
                case KeyEvent.VK_LEFT -> {
                    xMain.shellLogic.console.cursorIndex = xMain.shellLogic.console.cursorIndex > 0
                            ? xMain.shellLogic.console.cursorIndex - 1 : 0;
                    return;
                }
                case KeyEvent.VK_RIGHT -> {
                    xMain.shellLogic.console.cursorIndex = xMain.shellLogic.console.cursorIndex
                            < xMain.shellLogic.console.commandString.length() ? xMain.shellLogic.console.cursorIndex + 1
                            : xMain.shellLogic.console.commandString.length();
                    return;
                }
                case KeyEvent.VK_ESCAPE -> {
                    xMain.shellLogic.console.ex("console");
                    return;
                }
                case KeyEvent.VK_BACK_SPACE -> {
                    if (xMain.shellLogic.console.cursorIndex > 0 && xMain.shellLogic.console.commandString.length() > 0) {
                        String a = xMain.shellLogic.console.commandString.substring(0, xMain.shellLogic.console.cursorIndex - 1);
                        String b = xMain.shellLogic.console.commandString.substring(xMain.shellLogic.console.cursorIndex);
                        xMain.shellLogic.console.commandString = a + b;
                        xMain.shellLogic.console.cursorIndex--;
                    }
                    return;
                }
                case KeyEvent.VK_DELETE -> {
                    if (xMain.shellLogic.console.cursorIndex > 0 && xMain.shellLogic.console.commandString.length() > 0) {
                        String a = xMain.shellLogic.console.commandString.substring(0, xMain.shellLogic.console.cursorIndex);
                        String b = xMain.shellLogic.console.commandString.substring(xMain.shellLogic.console.cursorIndex + 1);
                        xMain.shellLogic.console.commandString = a + b;
                    }
                    return;
                }
                case KeyEvent.VK_ENTER -> {
                    String res = xMain.shellLogic.console.ex(xMain.shellLogic.console.commandString);
                    xMain.shellLogic.console.previousCommands.add(xMain.shellLogic.console.commandString);
                    xMain.shellLogic.console.stringLines.add(String.format("console:~$ %s",
                            xMain.shellLogic.console.commandString));
                    if (res.length() > xCon.charlimit()) {
                        xMain.shellLogic.console.stringLines.add(res.substring(0, xCon.charlimit()));
                        for (int i = xCon.charlimit(); i < res.length();
                             i += xCon.charlimit()) {
                            int lim = Math.min(res.length(), i + xCon.charlimit());
                            xMain.shellLogic.console.stringLines.add(res.substring(i, lim));
                        }
                    } else
                        xMain.shellLogic.console.stringLines.add(res);
                    xMain.shellLogic.console.linesToShowStart = Math.max(0,
                            xMain.shellLogic.console.stringLines.size() - xMain.shellLogic.console.linesToShow);
                    xMain.shellLogic.console.prevCommandIndex = -1;
                    xMain.shellLogic.console.commandString = "";
                    xMain.shellLogic.console.cursorIndex = 0;
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
                    String a = xMain.shellLogic.console.commandString.substring(0, xMain.shellLogic.console.cursorIndex);
                    String b = xMain.shellLogic.console.commandString.substring(xMain.shellLogic.console.cursorIndex);
                    xMain.shellLogic.console.commandString = a+key+b;
                    xMain.shellLogic.console.cursorIndex++;
                    return;
                }
            }
            String specialKey = iKeyboard.getSpecialKeyForCode(command);
            if (specialKey != null) {
                String a = xMain.shellLogic.console.commandString.substring(0, xMain.shellLogic.console.cursorIndex);
                String b = xMain.shellLogic.console.commandString.substring(xMain.shellLogic.console.cursorIndex);
                xMain.shellLogic.console.commandString = a+specialKey+b;
                xMain.shellLogic.console.cursorIndex = xMain.shellLogic.console.commandString.length();
                return;
            }
            if(xMain.shellLogic.console.commandString.length() < xCon.maxlinelength){
                String a = xMain.shellLogic.console.commandString.substring(0, xMain.shellLogic.console.cursorIndex);
                String b = xMain.shellLogic.console.commandString.substring(xMain.shellLogic.console.cursorIndex);
                String toAdd = KeyEvent.getKeyText(command).toLowerCase().contains("numpad")
                    ? KeyEvent.getKeyText(command).toLowerCase().replace("numpad",
                    "").replace("-","").replace(" ", "")
                    : iKeyboard.shiftMode || Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)
                        ? KeyEvent.getKeyText(command) : KeyEvent.getKeyText(command).toLowerCase();
                xMain.shellLogic.console.commandString = a + toAdd + b;
                xMain.shellLogic.console.cursorIndex++;
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
                        default -> xMain.shellLogic.console.ex(String.format("say %s", gMessages.msgInProgress));
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
        else if(xMain.shellLogic.console.pressBinds.containsKey(command))
            xMain.shellLogic.console.ex(xMain.shellLogic.console.pressBinds.get(command));
	}

	public static void processKeyReleaseInput(int command) {
	    if(uiInterface.inconsole) {
            switch (command) {
                case KeyEvent.VK_BACK_QUOTE -> {
                    xMain.shellLogic.console.ex("console");
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
            if(xMain.shellLogic.console.releaseBinds.containsKey(command)) {
                xMain.shellLogic.console.ex(xMain.shellLogic.console.releaseBinds.get(command));
            }
        }
	}
}
