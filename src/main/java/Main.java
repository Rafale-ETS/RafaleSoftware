import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;

public class Main extends Application {

  @Override
  protected void configure(final Configuration config) {
    config.setTitle("Rafale 3 Base Station");
  }

  @Override
  protected void initImGui(final Configuration config) {
    super.initImGui(config);

    final ImGuiIO io = ImGui.getIO();
    io.setIniFilename(null);                                // We don't want to save .ini file
    io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
    io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
    //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
    io.addConfigFlags(ImGuiConfigFlags.IsTouchScreen);
    io.setConfigViewportsNoTaskBarIcon(true);
    io.setConfigWindowsMoveFromTitleBarOnly(true);
    io.setConfigWindowsResizeFromEdges(false);

    //initFonts(io);
  }

  /**
   * Example of fonts configuration
   * For more information read: https://github.com/ocornut/imgui/blob/33cdbe97b8fd233c6c12ca216e76398c2e89b0d8/docs/FONTS.md
   */
  /*private void initFonts(final ImGuiIO io) {
    io.getFonts().addFontDefault(); // Add default font for latin glyphs

    // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
    // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
    // Here we are using it just to combine all required glyphs in one place
    final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
    rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
    rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
    rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());

    // Font config for additional fonts
    // This is a natively allocated struct so don't forget to call destroy after atlas is built
    final ImFontConfig fontConfig = new ImFontConfig();
    fontConfig.setMergeMode(true);  // Enable merge mode to merge cyrillic, japanese and icons with default font

    final short[] glyphRanges = rangesBuilder.buildRanges();
    io.getFonts().addFontFromMemoryTTF(loadFromResources("Tahoma.ttf"), 14, fontConfig, glyphRanges); // cyrillic glyphs
    io.getFonts().addFontFromMemoryTTF(loadFromResources("NotoSansCJKjp-Medium.otf"), 14, fontConfig, glyphRanges); // japanese glyphs
    io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-regular-400.ttf"), 14, fontConfig, glyphRanges); // font awesome
    io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-solid-900.ttf"), 14, fontConfig, glyphRanges); // font awesome
    io.getFonts().build();

    fontConfig.destroy();
  }*/

  @Override
  public void process() {
    Menu.show();
    RaceStats.show();
    BoatView.show();

    if(Menu.showLoadOldRace){
      RaceLoader.show();
      if(RaceLoader.raceLoaded){
        Menu.showLoadOldRace = false;
        RaceLoader.raceLoaded = false;
      }
    }
  }

  /*private static byte[] loadFromResources(String name) {
    try {
      return Files.readAllBytes(Paths.get(Main.class.getResource(name).toURI()));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }*/

  public static void main(final String[] args) {
    launch(new Main());
    System.exit(0);
  }
}
