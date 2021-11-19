package Application;

import Graphics.GUI;
import Graphics.Window;
import Menu.BoatView;
import Menu.MainMenu;
import Menu.RaceLoader;
import Menu.RaceStats;

public class App {
  public static void run() {

    GUI.add(new MainMenu(0.f ,0.f ,150.f ,300.f ,"Menu"));
    GUI.add(new BoatView(0.f ,0.f ,350.f ,350.f ,"Boat View"));
    GUI.add(new RaceLoader(0.f ,0.f ,800.f ,600.f ,"Archives de courses"));
    GUI.add(new RaceStats(0.f ,0.f ,300.f ,400.f ,"Statistiques en direct"));
    Window.getInstance().run();

  }
}
