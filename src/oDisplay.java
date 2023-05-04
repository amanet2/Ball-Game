import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Toolkit;

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
        createPanels();
        showFrame();
    }

	public void refreshResolution() {
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
		if(sSettings.show_mapmaker_ui) {
            uiEditorMenus.setupMapMakerWindow();
            xCon.ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", cClientLogic.newprefabname));
        }
		frame.setResizable(false);
        setPreferredSize(new Dimension(sSettings.width,sSettings.height));
        createPanels();
		frame.setContentPane(this);
		frame.pack();
        frame.setLocationRelativeTo(null);
        if(sSettings.displaymode == displaymode_fullscreen) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().setFullScreenWindow(frame);
        }
        else {
            GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().setFullScreenWindow(null);
        }
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
        setBackground(gColors.getColorFromName("clrf_background"));
        int[] od = getContentPaneOffsetDimension();
        dPanel vfxPanel = new dPanel();
        vfxPanel.setBounds(od[0], od[1], sSettings.width, sSettings.height);
        add(vfxPanel, 0, 0);
    }
}
