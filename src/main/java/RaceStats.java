import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.flag.ImGuiCond;

import java.time.*;

public class RaceStats {
    private static final ImNodesContext CONTEXT = new ImNodesContext();

    private static int initialWidth = 300;
    private static int initialHeight = 400;

    private static float trueWindSpeed = 9.4f;
    private static float apparentWindSpeed = 12.3f;
    private static float headding = 9.2f;
    private static float speedOverGround = 11.1f;
    private static float posLatitude = -85.0f;
    private static float posLongitude = 16.3f;

    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static String btnChrono = START;
    private static Instant chronoStart = Instant.now();
    private static Instant chronoStop = Instant.now();
    private static boolean chronoOn = false;

    static {
        ImNodes.createContext();
    }

    public static void show() {
        ImGui.setNextWindowSize(initialWidth, initialHeight, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + ImGui.getMainViewport().getWorkSizeX() - initialWidth, ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        ImGui.begin("Statistiques en direct");

        ImGui.columns(2);
        ImGui.text("Date");
        ImGui.nextColumn();
        ImGui.text("" + LocalDate.now(Clock.tickSeconds(ZoneId.systemDefault())));
        ImGui.text("" + LocalTime.now(Clock.tickSeconds(ZoneId.systemDefault())));

        ImGui.nextColumn();
        ImGui.separator();

        ImGui.columns(2);
        ImGui.text("Chrono");
        if(ImGui.button(btnChrono)){
            if(!chronoOn) {
                chronoStart = Instant.now();
                chronoOn = true;
                btnChrono = STOP;
            } else {
                chronoStop = Instant.now();
                chronoOn = false;
                btnChrono = START;
            }
        }
        ImGui.nextColumn();
        if(chronoOn){
            chronoStop = Instant.now();
        }
        Duration dur =  Duration.between(chronoStart, chronoStop);
        ImGui.text(String.valueOf(LocalTime.of((int) dur.toHours(), dur.toMinutesPart(), dur.toSecondsPart(), dur.toNanosPart())));

        ImGui.nextColumn();
        ImGui.separator();
        ImGui.columns(2);
        ImGui.text("Vitesse (SoG)");
        ImGui.text("Direction");
        ImGui.text("Vent");
        ImGui.text("Vent apparent");
        ImGui.text("Position");
        ImGui.nextColumn();
        ImGui.text(String.valueOf(speedOverGround));
        ImGui.text(String.valueOf(headding));
        ImGui.text(String.valueOf(trueWindSpeed));
        ImGui.text(String.valueOf(apparentWindSpeed));
        ImGui.text(posLatitude + ", " + posLongitude);

        ImGui.end();
    }
}
