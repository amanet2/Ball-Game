import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class iKeyboard implements KeyListener {
    static Queue<Integer> inputReleaseQueue = new LinkedList<>();
    static Queue<Integer> inputPressQueue = new LinkedList<>();
    static boolean shiftMode = false;
    static boolean ctrlMode = false;

    private static HashMap<Integer, String> shiftKeyMap = null;
    private static HashMap<Integer, String> specialKeyMap = null;
    private static HashMap<String, Integer> subKeyMap = null;

    private static void init() {
        shiftKeyMap = new HashMap<>();
        shiftKeyMap.put(KeyEvent.VK_SEMICOLON, ":");
        shiftKeyMap.put(KeyEvent.VK_MINUS, "_");
        shiftKeyMap.put(KeyEvent.VK_EQUALS, "+");
        shiftKeyMap.put(KeyEvent.VK_QUOTE, "\"");
        shiftKeyMap.put(KeyEvent.VK_COMMA, "<");
        shiftKeyMap.put(KeyEvent.VK_PERIOD, ">");
        shiftKeyMap.put(KeyEvent.VK_SLASH, "?");
        shiftKeyMap.put(KeyEvent.VK_BACK_QUOTE, "~");
        shiftKeyMap.put(KeyEvent.VK_1, "!");
        shiftKeyMap.put(KeyEvent.VK_2, "@");
        shiftKeyMap.put(KeyEvent.VK_3, "#");
        shiftKeyMap.put(KeyEvent.VK_4, "$");
        shiftKeyMap.put(KeyEvent.VK_5, "%");
        shiftKeyMap.put(KeyEvent.VK_6, "^");
        shiftKeyMap.put(KeyEvent.VK_7, "&");
        shiftKeyMap.put(KeyEvent.VK_8, "*");
        shiftKeyMap.put(KeyEvent.VK_9, "(");
        shiftKeyMap.put(KeyEvent.VK_0, ")");
        specialKeyMap = new HashMap<>();
        specialKeyMap.put(KeyEvent.VK_SPACE, " ");
        specialKeyMap.put(KeyEvent.VK_SEMICOLON, ";");
        specialKeyMap.put(KeyEvent.VK_QUOTE, "'");
        specialKeyMap.put(KeyEvent.VK_MINUS, "-");
        specialKeyMap.put(KeyEvent.VK_EQUALS, "=");
        specialKeyMap.put(KeyEvent.VK_PERIOD, ".");
        specialKeyMap.put(KeyEvent.VK_COLON, ":");
        specialKeyMap.put(KeyEvent.VK_SLASH, "/");
        specialKeyMap.put(KeyEvent.VK_NUM_LOCK, "");
        specialKeyMap.put(KeyEvent.VK_PLUS, "+");
        specialKeyMap.put(KeyEvent.VK_BACK_SLASH, "\\");
        specialKeyMap.put(KeyEvent.VK_SEPARATER, "|");
        specialKeyMap.put(KeyEvent.VK_DOWN, "");
        specialKeyMap.put(KeyEvent.VK_LEFT, "");
        specialKeyMap.put(KeyEvent.VK_RIGHT, "");
        specialKeyMap.put(KeyEvent.VK_UP, "");
        specialKeyMap.put(KeyEvent.VK_CAPS_LOCK, "");
        specialKeyMap.put(KeyEvent.VK_COMMA, ",");
        specialKeyMap.put(KeyEvent.VK_BACK_QUOTE, "");
        specialKeyMap.put(KeyEvent.VK_OPEN_BRACKET, "[");
        specialKeyMap.put(KeyEvent.VK_CLOSE_BRACKET, "]");
        specialKeyMap.put(KeyEvent.VK_TAB, "    ");
        specialKeyMap.put(KeyEvent.VK_INSERT, "");
        specialKeyMap.put(KeyEvent.VK_HOME, "");
        specialKeyMap.put(KeyEvent.VK_PAGE_UP, "");
        specialKeyMap.put(KeyEvent.VK_PAGE_DOWN, "");
        specialKeyMap.put(KeyEvent.VK_END, "");
        specialKeyMap.put(KeyEvent.VK_ALT, "");
        subKeyMap = new HashMap<>();
        subKeyMap.put("a", KeyEvent.VK_A);
        subKeyMap.put("b", KeyEvent.VK_B);
        subKeyMap.put("c", KeyEvent.VK_C);
        subKeyMap.put("d", KeyEvent.VK_D);
        subKeyMap.put("e", KeyEvent.VK_E);
        subKeyMap.put("f", KeyEvent.VK_F);
        subKeyMap.put("g", KeyEvent.VK_G);
        subKeyMap.put("h", KeyEvent.VK_H);
        subKeyMap.put("i", KeyEvent.VK_I);
        subKeyMap.put("j", KeyEvent.VK_J);
        subKeyMap.put("k", KeyEvent.VK_K);
        subKeyMap.put("l", KeyEvent.VK_L);
        subKeyMap.put("m", KeyEvent.VK_M);
        subKeyMap.put("n", KeyEvent.VK_N);
        subKeyMap.put("o", KeyEvent.VK_O);
        subKeyMap.put("p", KeyEvent.VK_P);
        subKeyMap.put("q", KeyEvent.VK_Q);
        subKeyMap.put("r", KeyEvent.VK_R);
        subKeyMap.put("s", KeyEvent.VK_S);
        subKeyMap.put("t", KeyEvent.VK_T);
        subKeyMap.put("u", KeyEvent.VK_U);
        subKeyMap.put("v", KeyEvent.VK_V);
        subKeyMap.put("w", KeyEvent.VK_W);
        subKeyMap.put("x", KeyEvent.VK_X);
        subKeyMap.put("y", KeyEvent.VK_Y);
        subKeyMap.put("z", KeyEvent.VK_Z);
        subKeyMap.put("enter", KeyEvent.VK_ENTER);
        subKeyMap.put("shift", KeyEvent.VK_SHIFT);
        subKeyMap.put("ctrl", KeyEvent.VK_CONTROL);
        subKeyMap.put("[", KeyEvent.VK_OPEN_BRACKET);
        subKeyMap.put("escape", KeyEvent.VK_ESCAPE);
        subKeyMap.put("\\", KeyEvent.VK_BACK_SLASH);
        subKeyMap.put("]", KeyEvent.VK_CLOSE_BRACKET);
        subKeyMap.put("space", KeyEvent.VK_SPACE);
        subKeyMap.put("left", KeyEvent.VK_LEFT);
        subKeyMap.put("up", KeyEvent.VK_UP);
        subKeyMap.put("right", KeyEvent.VK_RIGHT);
        subKeyMap.put("down", KeyEvent.VK_DOWN);
        subKeyMap.put("-", KeyEvent.VK_MINUS);
        subKeyMap.put("=", KeyEvent.VK_EQUALS);
        subKeyMap.put("`", KeyEvent.VK_BACK_QUOTE);
        subKeyMap.put("0", KeyEvent.VK_0);
        subKeyMap.put("1", KeyEvent.VK_1);
        subKeyMap.put("2", KeyEvent.VK_2);
        subKeyMap.put("3", KeyEvent.VK_3);
        subKeyMap.put("4", KeyEvent.VK_4);
        subKeyMap.put("5", KeyEvent.VK_5);
        subKeyMap.put("6", KeyEvent.VK_6);
        subKeyMap.put("7", KeyEvent.VK_7);
        subKeyMap.put("8", KeyEvent.VK_8);
        subKeyMap.put("9", KeyEvent.VK_9);
        subKeyMap.put("tab", KeyEvent.VK_TAB);
        subKeyMap.put(",", KeyEvent.VK_COMMA);
        subKeyMap.put(".", KeyEvent.VK_PERIOD);
        subKeyMap.put("/", KeyEvent.VK_SLASH);
        subKeyMap.put("backspace", KeyEvent.VK_BACK_SPACE);
        subKeyMap.put("delete", KeyEvent.VK_DELETE);
    }

    public static String getShiftKeyForCode(Integer code) {
        if(shiftKeyMap == null)
            init();
        return shiftKeyMap.get(code);
    }

    public static String getSpecialKeyForCode(Integer code) {
        if(specialKeyMap == null)
            init();
        return specialKeyMap.get(code);
    }

    public static Integer getCodeForKey(String text) {
        if(subKeyMap == null)
            init();
        return subKeyMap.get(text);
    }

    public synchronized void keyTyped(KeyEvent e) {
    }

    public synchronized void keyPressed(KeyEvent e) {
//        if(inputPressQueue.peek() == null || inputPressQueue.peek() != e.getKeyCode())
            inputPressQueue.add(e.getKeyCode());

    }

    public synchronized void keyReleased(KeyEvent e) {
//        if(inputReleaseQueue.peek() == null || inputReleaseQueue.peek() != e.getKeyCode())
            inputReleaseQueue.add(e.getKeyCode());
    }
}
