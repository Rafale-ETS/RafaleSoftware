package Menu;

import imgui.ImGui;
import imgui.flag.ImGuiCond;

import java.util.Objects;

public class MainMenu extends Menu {
  private static final String OFFLINE = "Hors ligne";
  private static final String ONLINE = "En ligne";
  private static String conStatus = OFFLINE;
  private static String dbStatus = OFFLINE;

  private static final String CONNECT = "Connect";
  private static final String DISCONNECT = "Deconnect";
  private static String btnConnect = CONNECT;

  public MainMenu(Float xRatio,
  Float yRatio,
  Float widthRatio,
  Float heightRatio, String title) {
    super(xRatio, yRatio, widthRatio, heightRatio, title);
  }

  @Override protected void internals() {
    ImGui.text("Status Bateau:");
    ImGui.text(conStatus);
    if(ImGui.button(btnConnect)){
      if(Objects.equals(conStatus, OFFLINE)){
        conStatus = ONLINE;
        btnConnect = DISCONNECT;
      } else {
        conStatus = OFFLINE;
        btnConnect = CONNECT;
      }
    }
    ImGui.separator();
    ImGui.text("Status Data:");
    ImGui.text(dbStatus);
    ImGui.separator();
    if(ImGui.button("Archives")){
      //showLoadOldRace = true;
    }
  }
}
