package Tools;

import java.time.Instant;

public class Chrono {
  private static final String START = "Start";
  private static final String STOP = "Stop";
  private static String btnChrono = START;
  private static Instant chronoStart = Instant.now();
  private static Instant chronoStop = Instant.now();
  private static boolean chronoOn = false;

}
