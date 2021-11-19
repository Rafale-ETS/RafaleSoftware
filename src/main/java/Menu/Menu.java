package Menu;

import imgui.ImGui;
import imgui.flag.ImGuiCond;

public abstract class Menu {

  private  Float xRatio,yRatio,widthRatio,heightRatio;
  private String title;
  public Menu( Float xRatio, Float yRatio, Float widthRatio,
  Float heightRatio, String title){
    this.title  = title;
    this.xRatio = xRatio;
    this.yRatio = yRatio;
    this.widthRatio = widthRatio;
    this.heightRatio = heightRatio;
  }

  public void showUI() {
    ImGui.setNextWindowSize(heightRatio, widthRatio, ImGuiCond.Once);
    ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
    ImGui.begin(title);

    internals();

    ImGui.end();
  }

  protected abstract void internals();

}

