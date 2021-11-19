package Menu;

import imgui.ImGui;

import java.time.*;

public class RaceStats extends Menu {

    public RaceStats(Float xRatio,
    Float yRatio,
    Float widthRatio,
    Float heightRatio, String title) {
        super(xRatio, yRatio, widthRatio, heightRatio, title);
    }

    @Override protected void internals() {
        ImGui.columns(2);
        ImGui.text("Date");
        ImGui.nextColumn();
        ImGui.text("" + LocalDate.now(Clock.tickSeconds(ZoneId.systemDefault())));
        ImGui.text("" + LocalTime.now(Clock.tickSeconds(ZoneId.systemDefault())));

        ImGui.nextColumn();
        ImGui.separator();

        ImGui.columns(2);
        ImGui.text("Chrono");
        /**if(ImGui.button()){

        }**/

        ImGui.nextColumn();
        ImGui.separator();
        ImGui.columns(2);
        ImGui.text("Vitesse (SoG)");
        ImGui.text("Direction");
        ImGui.text("Vent");
        ImGui.text("Vent apparent");
        ImGui.text("Position");
        ImGui.nextColumn();
        ImGui.text("15");
        ImGui.text("45");
        ImGui.text("12");
        ImGui.text("22");
        ImGui.text("0.00" + ", " + "0.00");
    }
}
