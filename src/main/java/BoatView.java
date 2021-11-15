import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.flag.ImGuiCond;

public class BoatView {
    private static final ImNodesContext CONTEXT = new ImNodesContext();

    private static int initialWidth = 350;
    private static int initialHeight = 350;

    static {
        ImNodes.createContext();
    }

    public static void show() {
        ImGui.setNextWindowSize(initialWidth, initialHeight, ImGuiCond.Once);
        ImGui.setNextWindowPos(
                ImGui.getMainViewport().getPosX() + ImGui.getMainViewport().getWorkSizeX() - initialWidth,
                ImGui.getMainViewport().getPosY() + ImGui.getMainViewport().getWorkSizeY() - initialHeight,
                ImGuiCond.Once);
        ImGui.begin("Moth");
        ImGui.text("TODO");
        ImGui.end();
    }
}
