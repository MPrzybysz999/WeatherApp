package weatherapp;

import javax.swing.SwingUtilities;

public class WeatherApp {
    private WeatherAppGUI gui;

    public WeatherApp() {
        startGUI();
    }

    private void startGUI() {
        gui = new WeatherAppGUI();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp());
    }
}


