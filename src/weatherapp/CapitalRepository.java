package weatherapp;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CapitalRepository {

    private Map<String, List<CountryCapital>> continents = new HashMap<>();

    public CapitalRepository() {
        loadCapitals();
    }

    private void loadCapitals() {
        try (InputStream is = getClass().getResourceAsStream("capitals_pl.json")) { // plik musi być w tym samym pakiecie co ta klasa
            if (is == null) {
                throw new RuntimeException("Nie znaleziono pliku JSON!");
            }

            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            scanner.close();

            parseJsonManually(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bardzo uproszczone ręczne parsowanie JSON
    private void parseJsonManually(String json) {
        // usuń nawiasy kwadratowe z początku i końca
        json = json.replace("[", "").replace("]", "").trim();

        // podziel na poszczególne obiekty krajów
        String[] entries = json.split("\\},\\s*\\{");

        for (String entry : entries) {
            entry = entry.replace("{", "").replace("}", "").trim();

            // znajdź nazwę kraju
            String country = entry.split("\"country\":")[1]
                                  .split(",")[0]
                                  .replace("\"", "").trim();

            // znajdź stolice
            String capitalsRaw = entry.split("\"capitals\":")[1]
                                      .replaceAll("[\\[\\]\"]", "")
                                      .trim();

            List<String> capitalsList = new ArrayList<>();
            for (String cap : capitalsRaw.split(",")) {
                if (!cap.isBlank()) {
                    capitalsList.add(cap.trim());
                }
            }

            // dodaj do mapy
            continents.computeIfAbsent("ALL", k -> new ArrayList<>())
                      .add(new CountryCapital(country, capitalsList));
        }
    }

    public List<CountryCapital> getCountries() {
        return continents.get("ALL");
    }

    public static class CountryCapital {
        private String country;
        private List<String> capitals;

        public CountryCapital(String country, List<String> capitals) {
            this.country = country;
            this.capitals = capitals;
        }

        public String getCountry() {
            return country;
        }

        public List<String> getCapitals() {
            return capitals;
        }

        @Override
        public String toString() {
            return country + " -> " + capitals;
        }
    }
}
