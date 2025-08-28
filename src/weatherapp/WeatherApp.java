package weatherapp;

import javax.swing.SwingUtilities;

public class WeatherApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGUI gui = new WeatherAppGUI();
            gui.setVisible(true);
        });
    }
}
