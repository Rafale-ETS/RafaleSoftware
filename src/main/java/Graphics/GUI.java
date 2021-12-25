package Graphics;

import Menu.Menu;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GUI {

  private static long windowPID;

  private static long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
  private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
  private static List<Menu> menuList = new ArrayList<Menu>();
  private static void updateMouse(){
    final int imguiCursor = ImGui.getMouseCursor();
    glfwSetCursor(windowPID, mouseCursors[imguiCursor]);
    glfwSetInputMode(windowPID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
  }

  public static void init(){
    //windowPID = Window.getInstance().getwindowPID();
    GUI.initImGui();

  }

  public static void destroy() {
    imGuiGl3.dispose();
    ImGui.destroyContext();
  }

  static private void initImGui() {
    // IMPORTANT!!
    // This line is critical for Dear ImGui to work.
    ImGui.createContext();

    // ImGui provides 3 different color schemas for styling. We will use the classic one here.
    // Try others with ImGui.styleColors*() methods.
    ImGui.styleColorsClassic();

    // Initialize ImGuiIO config
    final ImGuiIO io = ImGui.getIO();

    io.setIniFilename(null); // We don't want to save .ini file
    io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
    io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
    io.setBackendPlatformName("imgui_java_impl_glfw"); // For clarity reasons
    io.setBackendRendererName("imgui_java_impl_lwjgl"); // For clarity reasons

    // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
    final int[] keyMap = new int[ImGuiKey.COUNT];
    keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
    keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
    keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
    keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
    keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
    keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
    keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
    keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
    keyMap[ImGuiKey.End] = GLFW_KEY_END;
    keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
    keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
    keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
    keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
    keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
    keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
    keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
    keyMap[ImGuiKey.A] = GLFW_KEY_A;
    keyMap[ImGuiKey.C] = GLFW_KEY_C;
    keyMap[ImGuiKey.V] = GLFW_KEY_V;
    keyMap[ImGuiKey.X] = GLFW_KEY_X;
    keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
    keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
    io.setKeyMap(keyMap);

    // Mouse cursors mapping
    mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
    mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
    mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
    mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
    mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
    mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

    // ------------------------------------------------------------
    // Here goes GLFW callbacks to update user input in Dear ImGui

    glfwSetKeyCallback(windowPID, (w, key, scancode, action, mods) -> {
      if (action == GLFW_PRESS) {
        io.setKeysDown(key, true);
      } else if (action == GLFW_RELEASE) {
        io.setKeysDown(key, false);
      }

      io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
      io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
      io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
      io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
    });

    glfwSetCharCallback(windowPID, (w, c) -> {
      if (c != GLFW_KEY_DELETE) {
        io.addInputCharacter(c);
      }
    });

    glfwSetMouseButtonCallback(windowPID, (w, button, action, mods) -> {
      final boolean[] mouseDown = new boolean[5];

      mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
      mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
      mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
      mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
      mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

      io.setMouseDown(mouseDown);

      if (!io.getWantCaptureMouse() && mouseDown[1]) {
        ImGui.setWindowFocus(null);
      }
    });

    glfwSetScrollCallback(windowPID, (w, xOffset, yOffset) -> {
      io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
      io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
    });

    io.setSetClipboardTextFn(new ImStrConsumer() {
      @Override
      public void accept(final String s) {
        glfwSetClipboardString(windowPID, s);
      }
    });

    io.setGetClipboardTextFn(new ImStrSupplier() {
      @Override
      public String get() {
        return glfwGetClipboardString(windowPID);
      }
    });

    //LoadFont(io);

    // IMPORTANT!!!
    // Method initializes renderer itself.
    // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
    // ImGui context should be created as well.
    imGuiGl3.init();
  }

  public static void update(int[] winWidth, int[] winHeight, int[] fbWidth,
  int[]fbHeight, double[] mousePosX,double[] mousePosY,double deltaTime ) {
    final ImGuiIO io = ImGui.getIO();
    io.setDisplaySize(winWidth[0], winHeight[0]);
    io.setDisplayFramebufferScale((float) fbWidth[0] / winWidth[0], (float) fbHeight[0] / winHeight[0]);
    io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
    io.setDeltaTime((float) deltaTime);

    GUI.updateMouse();
    GUI.render();
  }

  public static void add(Menu menu) {
    menuList.add(menu);
  }

  private void LoadFont (ImGuiIO io) {
    // ------------------------------------------------------------
    // Fonts configuration

    // -------------------
    // Fonts merge example
/**
    final ImFontAtlas fontAtlas = io.getFonts();

    // First of all we add a default font, which is 'ProggyClean.ttf, 13px'
    fontAtlas.addFontDefault();

    final ImFontConfig fontConfig = new ImFontConfig(); // Keep in mind that creation of the ImFontConfig will allocate native memory
    fontConfig.setMergeMode(true); // All fonts added while this mode is turned on will be merged with the previously added font
    fontConfig.setPixelSnapH(true);
    fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic()); // Additional glyphs could be added like this or in addFontFrom*() methods

    // We merge font loaded from resources with the default one. Thus we will get an absent cyrillic glyphs
    //fontAtlas.addFontFromMemoryTTF(loadFromResources("basis33.ttf"), 16,
    fontConfig);

    // Disable merged mode and add all other fonts normally
    fontConfig.setMergeMode(false);
    fontConfig.setPixelSnapH(false);

    // ------------------------------
    // Fonts from file/memory example

    fontConfig.setRasterizerMultiply(1.2f); // This will make fonts a bit more readable

    // We can add new fonts directly from file
    fontAtlas.addFontFromFileTTF("src/test/resources/DroidSans.ttf", 13, fontConfig);
    fontAtlas.addFontFromFileTTF("src/test/resources/DroidSans.ttf", 14, fontConfig);

    // Or directly from memory
    fontConfig.setName("Roboto-Regular.ttf, 13px"); // This name will be displayed in Style Editor
    fontAtlas.addFontFromMemoryTTF(loadFromResources("Roboto-Regular.ttf"),
    13, fontConfig);
    fontConfig.setName("Roboto-Regular.ttf, 14px"); // We can apply a new config value every time we add a new font
    fontAtlas.addFontFromMemoryTTF(loadFromResources("Roboto-Regular.ttf"),
    14, fontConfig);

    fontConfig.destroy(); // After all fonts were added we don't need this config more
 **/
  }


  public static void render() {

    // IMPORTANT!!
    // Any Dear ImGui code SHOULD go between NewFrame()/Render() methods
    ImGui.newFrame();
    showUi();
    ImGui.render();

    // After ImGui#render call we provide draw data into LWJGL3 renderer.
    // At that moment ImGui will be rendered to the current OpenGL context.
    imGuiGl3.renderDrawData(ImGui.getDrawData());

  }

  private static void showUi() {

    for (Menu menu:menuList){
      menu.showUI();
    }
  }
}
