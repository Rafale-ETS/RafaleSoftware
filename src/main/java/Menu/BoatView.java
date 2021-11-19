package Menu;

import imgui.ImGui;
import imgui.flag.ImGuiCond;

public class BoatView extends Menu{

    public BoatView(Float xRatio,
    Float yRatio,
    Float widthRatio,
    Float heightRatio, String title) {
        super(xRatio, yRatio, widthRatio, heightRatio, title);
    }

    @Override protected void internals() {
        ImGui.text("TODO");
    }
}
