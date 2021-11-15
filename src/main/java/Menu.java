import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.flag.ImGuiCond;

import java.util.Objects;

public class Menu {
    private static final ImNodesContext CONTEXT = new ImNodesContext();

    private static final String OFFLINE = "Hors ligne";
    private static final String ONLINE = "En ligne";
    private static String conStatus = OFFLINE;
    private static String dbStatus = OFFLINE;

    private static final String CONNECT = "Connect";
    private static final String DISCONNECT = "Deconnect";
    private static String btnConnect = CONNECT;

    public static boolean showLoadOldRace = false;

    static {
        ImNodes.createContext();
    }

    public static void show() {
        ImGui.setNextWindowSize(150, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        ImGui.begin("Menu");

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
            showLoadOldRace = true;
        }

        ImGui.end();
    }
}
