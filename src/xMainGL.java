import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class xMainGL {
    private static long gameTimeNanos = System.nanoTime();
    private static long tickTimeNanos = gameTimeNanos;
    // The window handle
    private long window;

    public void run() {
        init();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        eManager.configFileSelection = eManager.getFilesSelection("config");
        eManager.prefabFileSelection = eManager.getFilesSelection("prefabs");
        eManager.mapsFileSelection = eManager.getFilesSelection("maps", ".map");
        eManager.winSoundFileSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
        gExecDoableFactory.instance().init();
        cServerVars.instance().init();
        cClientVars.instance().init();
        xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_SERVER);
        xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_CLIENT);
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(sSettings.width, sSettings.height,
                String.format("Ball Game%s", sSettings.show_mapmaker_ui ? " [EDITOR]" : ""), NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS:
                    iKeyboard.inputPressQueueGL.add(key);
                    break;
                case GLFW_RELEASE:
                    iKeyboard.inputReleaseQueueGL.add(key);
                    break;
            }
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);
            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Run the rendering loop until the user has attempted to close
        while ( !glfwWindowShouldClose(window) ) {
            gameTimeNanos = System.nanoTime();
            while(tickTimeNanos < gameTimeNanos) {
                tickTimeNanos += (1000000000/sSettings.rategame);
                iInput.readKeyInputsGL();
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            GL11.glColor3f(0.0f, 1.0f, 0.2f);
            glBegin(GL_LINES);
            double x1 = 100;
            double y1 = sSettings.height-100;
            double x2 = sSettings.width-100;
            double y2 = 100;

            x1 = 2*x1 / sSettings.width - 1;
            y1 = 2*y1 / sSettings.height - 1;

            x2 = 2*x2 / sSettings.width  - 1;
            y2 = 2*y2 / sSettings.height - 1;
            glVertex2d(x1, y1);
            glVertex2d(x2, y2);
            glEnd();
            glTranslatef(gCamera.getX() * 2.0f/sSettings.width, gCamera.getY() * 2.0f/sSettings.height, 0.0f);
            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
//            glfwSetWindowShouldClose(window, true);
        }
    }

    public static void main(String[] args) {
        new xMainGL().run();
    }

}
