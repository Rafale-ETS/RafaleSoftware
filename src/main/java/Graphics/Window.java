package Graphics;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;


public class Window extends JFrame {
  //Singleton
  private static Window window = null;

  public static Window getInstance() {
    if(window == null){
      window = new Window();
    }

    return window;
  }
    /**
     * Create the frame.
     */
    public Window() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 450, 300);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu mnNewMenu = new JMenu("New menu");
      menuBar.add(mnNewMenu);

      JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
      mnNewMenu.add(mntmNewMenuItem);

      JSeparator separator = new JSeparator();
      mnNewMenu.add(separator);

      JRadioButtonMenuItem rdbtnmntmNewRadioItem = new JRadioButtonMenuItem("New radio item");
      mnNewMenu.add(rdbtnmntmNewRadioItem);

      JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("New check item");
      mnNewMenu.add(checkBoxMenuItem);

      this.getContentPane().add(new Map());
      this.show();

    }

  }

