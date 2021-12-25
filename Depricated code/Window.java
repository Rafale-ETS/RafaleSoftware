package Graphics;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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
  private int[]  width= new int[1], height = new int[1];
  private int[]  display_width, display_height;
  private double[] mouseY=new double[1],mouseX=new double[1];
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

    double time = 0; // to track our frame delta value

    while ( !glfwWindowShouldClose(windowPID) ){

      final double currentTime = glfwGetTime();
      final double deltaTime = (time > 0) ? (currentTime - time) : 1f / 60f;

      time = currentTime;

      glClearColor(0, 0, 0, 0.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glfwGetWindowSize(windowPID, display_width, display_height);
      glfwGetFramebufferSize(windowPID, width, height);
      glfwGetCursorPos(windowPID, mouseX, mouseY);



      GUI.update(display_width,display_height,width,height,mouseX,mouseY,deltaTime);

      this.update();
    }
  }

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    this.init();
    GUI.init();
    Map map = new Map();
    this.loop();

    GUI.destroy();
    this.destroy();
  }

  private void destroy() {
    glfwFreeCallbacks(windowPID);
    glfwDestroyWindow(windowPID);
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }

  public void init() {

    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    display_width = new int[] {vidmode.width()};
    display_height = new int[] {vidmode.height()};
    fps = vidmode.refreshRate();

    // Create the window
    windowPID = glfwCreateWindow(display_width[0], display_height[0], title,
    NULL,
    NULL);
    if (windowPID == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }


    glfwSetFramebufferSizeCallback(windowPID, (windowPID, width, height) -> {
      this.width[0] = width;
      this.height[0] = height;
      this.setResized(true);
    });


    if (maximized) {
      glfwMaximizeWindow(windowPID);
    }



    // Make the OpenGL context current
    glfwMakeContextCurrent(windowPID);

    // Make the window visible
    glfwShowWindow(windowPID);

    // IMPORTANT!!
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    if (isvSync()) {
      // Enable v-sync
      glfwSwapInterval(1);
    }


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
    return width[0];
  }

  public int getHeight() {
    return height[0];
  }

  public int getDisplay_Width() {
    return display_width[0];
  }

  public int getDisplay_Height() {
    return display_height[0];
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
