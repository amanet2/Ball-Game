import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class oDisplay {
	JFrame frame;
    JLayeredPane contentPane;

	public oDisplay() {
		super();
        contentPane = new JLayeredPane();
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
                xMain.shellLogic.console.ex("quit");
            }
        });
        frame.setUndecorated(sSettings.borderless);
		if(sSettings.show_mapmaker_ui) {
            uiEditorMenus.setupMapMakerWindow();
            xMain.shellLogic.console.ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", sSettings.clientNewPrefabName));
        }
		frame.setResizable(false);
        contentPane.setPreferredSize(new Dimension(sSettings.width,sSettings.height));
        createPanels();
		frame.setContentPane(contentPane);
		frame.pack();
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//add listeners
        frame.addKeyListener(iInput.keyboardInput);
        frame.addMouseListener(iInput.mouseInput);
        frame.addMouseMotionListener(iInput.mouseInput);
        frame.addMouseWheelListener(iInput.mouseInput);
        frame.setFocusTraversalKeysEnabled(false);
    }

	public void createPanels() {
	    contentPane.removeAll();
        contentPane.setBackground(gColors.getColorFromName("clrf_background"));
        dPanel vfxPanel = new dPanel();
        vfxPanel.setBounds(0, 0, sSettings.width, sSettings.height);
        contentPane.setOpaque(true);
        contentPane.add(vfxPanel, 0, 0);
    }
}
