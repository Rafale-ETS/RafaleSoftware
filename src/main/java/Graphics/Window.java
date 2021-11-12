package Graphics;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
  //Singleton
  private static Window window = null;

  //PID of the OpenGL window
  private long windowPID;


  private boolean vSync = true;
  private boolean resized = false;
  private boolean maximized = true;
  private String title = "Rafale|ETS";
  private GLFWVidMode vidmode;
  private int width, height;
  private int display_width, display_height;
  private int fps;

  public Window(){

  }
  public static Window getInstance() {
    if(window == null){
      window = new Window();
    }

    return window;
  }

  private void loop() {


    while ( !glfwWindowShouldClose(windowPID) ){

      try (MemoryStack stack = stackPush()) {
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);

        glfwGetWindowSize(windowPID, w, h);
        width = w.get(0);
        height = h.get(0);

        glfwGetFramebufferSize(windowPID, w, h);
        display_width = w.get(0);
        display_height = h.get(0);
      }

      newFrame();

      Menu.render();

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

      GUI.getInstance().render();

      glfwSwapBuffers(windowPID); // swap the color buffers
    }
  }

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(windowPID);
    glfwDestroyWindow(windowPID);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  public void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    //




    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

    vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    display_width = vidmode.width();
    display_height = vidmode.height();
    fps = vidmode.refreshRate();

    // Create the window
    windowPID = glfwCreateWindow(display_width, display_height, title, NULL, NULL);
    if (windowPID == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }


    glfwSetFramebufferSizeCallback(windowPID, (windowPID, width, height) -> {
      this.width = width;
      this.height = height;
      this.setResized(true);
    });

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(windowPID, (windowPID, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(windowPID,
        true); // We will detect this in the rendering loop
      }
    });

    if (maximized) {
      glfwMaximizeWindow(windowPID);
    }

    // Make the OpenGL context current
    glfwMakeContextCurrent(windowPID);

    if (isvSync()) {
      // Enable v-sync
      glfwSwapInterval(1);
    }

    // Make the window visible
    glfwShowWindow(windowPID);

    GL.createCapabilities();

    //Turn debug on
    Callback debugProc = GLUtil.setupDebugMessageCallback();

    GUI.getInstance().setupWindow(windowPID);
    GLFWErrorCallback.createPrint(System.err).set();
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  private void newFrame() {
    try (MemoryStack stack = stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);

      glfwGetWindowSize(windowPID, w, h);
      width = w.get(0);
      height = h.get(0);

      glfwGetFramebufferSize(windowPID, w, h);
      display_width = w.get(0);
      display_height = h.get(0);
    }

    GUI.getInstance().input(windowPID);
    glfwPollEvents();

  }

  public long getwindowPID() {
    return windowPID;
  }

  public String getWindowTitle() {
    return title;
  }

  public void setWindowTitle(String title) {
    glfwSetWindowTitle(windowPID, title);
  }

  public void setClearColor(float r, float g, float b, float alpha) {
    glClearColor(r, g, b, alpha);
  }

  public boolean isKeyPressed(int keyCode) {
    return glfwGetKey(windowPID, keyCode) == GLFW_PRESS;
  }

  public boolean windowShouldClose() {
    return glfwWindowShouldClose(windowPID);
  }

  public String getTitle() {
    return title;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getDisplay_Width() {
    return display_width;
  }

  public int getDisplay_Height() {
    return display_height;
  }

  public boolean isResized() {
    return resized;
  }

  public void setResized(boolean resized) {
    this.resized = resized;
  }

  public boolean isvSync() {
    return vSync;
  }

  public void setvSync(boolean vSync) {
    this.vSync = vSync;
  }

  public void update() {
    glfwSwapBuffers(windowPID);
    glfwPollEvents();
  }


}
