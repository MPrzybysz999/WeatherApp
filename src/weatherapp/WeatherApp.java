package weatherapp;

import javax.swing.SwingUtilities;
import java.util.List;

public class WeatherApp {
    private CapitalRepository repo;
    private WeatherAppGUI gui;

    public WeatherApp() {
        // Initialize repository
        repo = new CapitalRepository();
        initializeData();
    }

    private void initializeData() {
        // Check if repository is empty
        if (repo.getCountries() == null || repo.getCountries().isEmpty()) {
            System.out.println("No data in repository. Check JSON file.");
            return;
        }

        // Display countries and capitals
        System.out.println("All countries and capitals:");
        for (CapitalRepository.CountryCapital cc : repo.getCountries()) {
            System.out.println(cc);
        }

        // Search for specific country
        String countryToFind = "Polska";
        CapitalRepository.CountryCapital polska = null;
        for (CapitalRepository.CountryCapital cc : repo.getCountries()) {
            if (cc.getCountry().equalsIgnoreCase(countryToFind)) {
                polska = cc;
                break;
            }
        }

        if (polska != null) {
            System.out.println("\nCapital of " + countryToFind + ": " + polska.getCapitals());
        } else {
            System.out.println("\nCountry not found: " + countryToFind);
        }
    }

    private void startGUI() {
        gui = new WeatherAppGUI();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        WeatherApp app = new WeatherApp();
        SwingUtilities.invokeLater(() -> app.startGUI());
    }
}


