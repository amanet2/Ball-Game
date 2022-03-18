public class uiMenuItem {
    String text;

    public void doItem(){
        //to override
    }

    public void refreshText() {
        //to override
    }

    public uiMenuItem(String t) {
        text = t;
    }
}