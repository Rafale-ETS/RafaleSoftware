package Menu;

import imgui.ImGui;

public class RaceLoader extends Menu{

    public RaceLoader(Float xRatio,
    Float yRatio,
    Float widthRatio,
    Float heightRatio, String title) {
        super(xRatio, yRatio, widthRatio, heightRatio, title);
    }

    @Override protected void internals() {
        if(ImGui.button("Load")){
            //raceLoaded = true;
        }
        ImGui.text("TODO");
    }
}
