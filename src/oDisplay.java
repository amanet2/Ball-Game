import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class oDisplay extends JLayeredPane {
	static int displaymode_windowed = 0;
	static int displaymode_fullscreen = 1;
	static int displaymode_borderless = 2;
	JFrame frame;
    Cursor blankCursor;

    private static oDisplay instance = null;

	public static oDisplay instance() {
		if (instance == null)
			instance = new oDisplay();
		return instance;
	}

	private oDisplay() {
		super();
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        super.setOpaque(true);
	}

	public void refreshResolution() {
        uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[0].text =
                String.format("Resolution: [%dx%d]", sSettings.width, sSettings.height);
        showFrame();
        uiInterface.addListeners();
        clearAndRefresh();
        createPanels();
        gTextures.refreshObjectSprites();
    }

	public void checkDisplay() {
	    int[] sres = new int[]{
	            Integer.parseInt(sVars.get("vidmode").split(",")[0]),
	            Integer.parseInt(sVars.get("vidmode").split(",")[1]),
	            Integer.parseInt(sVars.get("vidmode").split(",")[2])
	    };
	    if(sSettings.width != sres[0] || sSettings.height != sres[1]) {
	        sSettings.width = sres[0];
	        sSettings.height = sres[1];
            refreshResolution();
        }
	    if(sSettings.framerate != sres[2]) {
            sSettings.framerate = sres[2];
            uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[1].text =
                    String.format("Framerate: [%d]",sSettings.framerate);
        }
    }

	public void showFrame() {
        if(frame != null) {
            frame.dispose();
        }
		frame = new JFrame(String.format("%s%s", sVars.get("defaulttitle"),
                sSettings.show_mapmaker_ui ? " -editor" : ""));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                xCon.ex("quit");
            }
        });
        frame.setUndecorated(!sVars.isIntVal("displaymode", displaymode_windowed));
		if(sSettings.show_mapmaker_ui)
			cEditorLogic.setupMapMakerWindow();
		frame.setResizable(false);
        sSettings.width = Integer.parseInt(sVars.get("vidmode").split(",")[0]);
        sSettings.height = Integer.parseInt(sVars.get("vidmode").split(",")[1]);
        setPreferredSize(new Dimension(sSettings.width,sSettings.height));
        setBackground(new Color(
                Integer.parseInt(sVars.get("bgcolor").split(",")[0]),
                Integer.parseInt(sVars.get("bgcolor").split(",")[1]),
                Integer.parseInt(sVars.get("bgcolor").split(",")[2])
        ));
        createPanels();
		frame.setContentPane(this);
		frame.pack();
        frame.setLocationRelativeTo(null);
        if(sVars.isIntVal("displaymode", displaymode_fullscreen))
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
    }

	public int[] getContentPaneOffsetDimension(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double[] screenDims = new double[]{screenSize.getWidth(), screenSize.getHeight()};
        return new int[]{
            sVars.isIntVal("displaymode", oDisplay.displaymode_fullscreen)
                    ? Math.max(0, (int)((screenDims[0]-sSettings.width)/2.0)) : 0,
            sVars.isIntVal("displaymode", oDisplay.displaymode_fullscreen)
                    ? Math.max(0,(int)((screenDims[1]-sSettings.height)/2.0)) : 0
        };
    }

	public void createPanels() {
	    removeAll();
        int ox = getContentPaneOffsetDimension()[0];
        int oy = getContentPaneOffsetDimension()[1];
        int ow = sSettings.width;
        int oh = sSettings.height;
        dPanel vfxPanel = new dPanel();
        vfxPanel.setBounds(ox,oy,ow,oh);
        add(vfxPanel, 0, 0);
        dPanel uiPanel = new dPanel();
        uiPanel.panelLevel = 1;
        uiPanel.setBounds(ox,oy,ow,oh);
        add(uiPanel, 1, 0);
    }

	public void clearAndRefresh() {
        removeAll();
        setBackground(new Color(
                Integer.parseInt(xCon.ex("bgcolor").split(",")[0]),
                Integer.parseInt(xCon.ex("bgcolor").split(",")[1]),
                Integer.parseInt(xCon.ex("bgcolor").split(",")[2])
        ));
	}
}
