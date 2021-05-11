public class uiMenusCredits extends uiMenu {
    public static uiMenuItem[] getCreditsMenuItems() {
        uiMenuItem[] gameCredits = new uiMenuItem[] {
                new uiMenuItem("Designed and Programmed by Anthony Manetti"),
                new uiMenuItem("github.com/amanet2")
        };
        return gameCredits;
    }

    public uiMenusCredits() {
        super("Credits", getCreditsMenuItems(), uiMenus.MENU_MAIN);
    }
}
