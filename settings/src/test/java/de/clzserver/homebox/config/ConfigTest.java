package de.clzserver.homebox.config;

/**
 * Created by Marc Janﬂen on 06.08.2015.
 */
public class ConfigTest {

    private ConfigTest() {
        PropMngr mgr = new PropMngr();
        mgr.setLocationRelativeTo(null);
        mgr.setVisible(true);
    }

    public static void main(String[] args) {
        new ConfigTest();
    }
}
