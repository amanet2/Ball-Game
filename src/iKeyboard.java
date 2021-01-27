import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class iKeyboard implements KeyListener {
    static Queue<Integer> inputReleaseQueue = new LinkedList();
    static Queue<Integer> inputPressQueue = new LinkedList();
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

    static String[] keyCodeSubTexts = new String[] {
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z",
        "enter",
        "shift",
        "ctrl",
        "[",
        "escape",
        "\\",
        "]",
        "space",
        "left",
        "up",
        "right",
        "down",
        "-",
        "=",
        "`",
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "tab",
        ",",
        ".",
        "/",
        "backspace",
        "delete"
    };

    static Integer[] keyCodeSubCodes = new Integer[] {
        KeyEvent.VK_A,
        KeyEvent.VK_B,
        KeyEvent.VK_C,
        KeyEvent.VK_D,
        KeyEvent.VK_E,
        KeyEvent.VK_F,
        KeyEvent.VK_G,
        KeyEvent.VK_H,
        KeyEvent.VK_I,
        KeyEvent.VK_J,
        KeyEvent.VK_K,
        KeyEvent.VK_L,
        KeyEvent.VK_M,
        KeyEvent.VK_N,
        KeyEvent.VK_O,
        KeyEvent.VK_P,
        KeyEvent.VK_Q,
        KeyEvent.VK_R,
        KeyEvent.VK_S,
        KeyEvent.VK_T,
        KeyEvent.VK_U,
        KeyEvent.VK_V,
        KeyEvent.VK_W,
        KeyEvent.VK_X,
        KeyEvent.VK_Y,
        KeyEvent.VK_Z,
        KeyEvent.VK_ENTER,
        KeyEvent.VK_SHIFT,
        KeyEvent.VK_CONTROL,
        KeyEvent.VK_OPEN_BRACKET,
        KeyEvent.VK_ESCAPE,
        KeyEvent.VK_BACK_SLASH,
        KeyEvent.VK_CLOSE_BRACKET,
        KeyEvent.VK_SPACE,
        KeyEvent.VK_LEFT,
        KeyEvent.VK_UP,
        KeyEvent.VK_RIGHT,
        KeyEvent.VK_DOWN,
        KeyEvent.VK_MINUS,
        KeyEvent.VK_EQUALS,
        KeyEvent.VK_BACK_QUOTE,
        KeyEvent.VK_0,
        KeyEvent.VK_1,
        KeyEvent.VK_2,
        KeyEvent.VK_3,
        KeyEvent.VK_4,
        KeyEvent.VK_5,
        KeyEvent.VK_6,
        KeyEvent.VK_7,
        KeyEvent.VK_8,
        KeyEvent.VK_9,
        KeyEvent.VK_TAB,
        KeyEvent.VK_COMMA,
        KeyEvent.VK_PERIOD,
        KeyEvent.VK_SLASH,
        KeyEvent.VK_BACK_SPACE,
        KeyEvent.VK_DELETE
    };

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
