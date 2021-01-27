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
