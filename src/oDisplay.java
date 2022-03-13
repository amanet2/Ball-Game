import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class oDisplay extends JLayeredPane {
	static int displaymode_windowed = 0;
	static int displaymode_borderless = 1;
	static int displaymode_fullscreen = 2;
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

    public void refreshDisplaymode() {
        uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[2].text = String.format("Borderless [%s]",
                sSettings.displaymode == oDisplay.displaymode_borderless ? "X" : "  ");
        oDisplay.instance().createPanels();
        oDisplay.instance().showFrame();
    }

	public void refreshResolution() {
        uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[0].text =
                String.format("Resolution: [%dx%d]", sSettings.width, sSettings.height);
        showFrame();
        createPanels();
        gTextures.refreshObjectSprites();
    }

	public void showFrame() {
        if(frame != null)
            frame.dispose();
		frame = new JFrame(String.format("Ball Game%s", sSettings.show_mapmaker_ui ? " [EDITOR]" : ""));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                xCon.ex("quit");
            }
        });
        frame.setUndecorated(sSettings.displaymode != displaymode_windowed);
		if(sSettings.show_mapmaker_ui)
			uiEditorMenus.setupMapMakerWindow();
		frame.setResizable(false);
//        sSettings.width = Integer.parseInt(cClientVars.instance().get("vidmode").split(",")[0]);
//        sSettings.height = Integer.parseInt(cClientVars.instance().get("vidmode").split(",")[1]);
        setPreferredSize(new Dimension(sSettings.width,sSettings.height));
        setBackground(gColors.getFontColorFromName("background"));
        createPanels();
		frame.setContentPane(this);
		frame.pack();
        frame.setLocationRelativeTo(null);
        if(sSettings.displaymode == displaymode_fullscreen)
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		//add listeners
        frame.addKeyListener(iInput.keyboardInput);
        frame.addMouseListener(iInput.mouseInput);
        frame.addMouseMotionListener(iInput.mouseMotion);
        frame.addMouseWheelListener(iInput.mouseWheelInput);
        frame.setFocusTraversalKeysEnabled(false);
    }

	public int[] getContentPaneOffsetDimension(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double[] screenDims = new double[]{screenSize.getWidth(), screenSize.getHeight()};
        return new int[]{
            sSettings.displaymode == oDisplay.displaymode_fullscreen
                    ? Math.max(0, (int)((screenDims[0]-sSettings.width)/2.0)) : 0,
            sSettings.displaymode == oDisplay.displaymode_fullscreen
                    ? Math.max(0,(int)((screenDims[1]-sSettings.height)/2.0)) : 0
        };
    }

	public void createPanels() {
	    removeAll();
        setBackground(gColors.getFontColorFromName("background"));
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
}
