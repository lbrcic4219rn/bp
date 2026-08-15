package app;

import gui.IGui;
import gui.swingimp.SwingGui;

import javax.swing.SwingUtilities;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        AppCore core = new AppCore();
        Runtime.getRuntime().addShutdownHook(new Thread(core::shutdown));

        SwingUtilities.invokeLater(
                () -> {
                    IGui gui = new SwingGui(core);
                    core.addSubscriber(gui);
                    gui.start();
                });
    }
}
