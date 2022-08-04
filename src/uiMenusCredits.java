public class uiMenusCredits extends uiMenu {
    public static uiMenuItem[] getCreditsMenuItems() {
        uiMenuItem[] gameCredits = new uiMenuItem[] {
                new uiMenuItem("Designed and Programmed by Stallion"),
                new uiMenuItem("venmo @StallionUSA")
        };
        return gameCredits;
    }

    public uiMenusCredits() {
        super("Credits", getCreditsMenuItems(), uiMenus.MENU_MAIN);
    }
}
