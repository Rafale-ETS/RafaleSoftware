import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.flag.ImGuiCond;

public class RaceLoader {
    private static final ImNodesContext CONTEXT = new ImNodesContext();

    private static int initialWidth = 800;
    private static int initialHeight = 600;

    public static boolean raceLoaded = false;

    static {
        ImNodes.createContext();
    }

    public static void show() {
        ImGui.setNextWindowSize(initialWidth, initialHeight, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getCenterX() - (initialWidth/2.0f), ImGui.getMainViewport().getCenterY() - (initialHeight/2.0f), ImGuiCond.Once);
        ImGui.begin("Archives de courses");
        if(ImGui.button("Load")){
            raceLoaded = true;
        }


        ImGui.text("TODO");

        ImGui.end();
    }
}
