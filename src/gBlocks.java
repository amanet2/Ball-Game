public class gBlocks {
    static final int CUBE = 0;
    static final int FLOOR = 1;
    static final int CORNERUR = 2;
    static final int CORNERLR = 3;
    static final int CORNERLL = 4;
    static final int CORNERUL = 5;

    static String[] titles = new String[]{
            "BLOCK_CUBE",
            "BLOCK_FLOOR",
            "BLOCK_CORNERUR",
            "BLOCK_CORNERLR",
            "BLOCK_CORNERLL",
            "BLOCK_CORNERUL"
    };

    static int getCodeForTitle(String title) {
        for(int i = 0; i < titles.length; i++) {
            if(title.equalsIgnoreCase(titles[i]))
                return i;
        }
        return -1;
    }

    private static String getTitleForCode(int code) {
        return titles[code];
    }

    public static String getTitleForBlock(gBlock block) {
        return getTitleForCode(block.getInt("code"));
    }
}
