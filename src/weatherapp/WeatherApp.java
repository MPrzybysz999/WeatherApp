package weatherapp;
import  weatherapp.CapitalRepository;

public class WeatherApp {

    public static void main(String[] args) {
        // 1. Tworzymy repozytorium
        CapitalRepository repo = new CapitalRepository();

        // 2. Sprawdzenie, czy repozytorium nie jest puste
        if (repo.getCountries() == null || repo.getCountries().isEmpty()) {
            System.out.println("Brak danych w repozytorium. Sprawdź plik JSON.");
            return;
        }

        // 3. Wyświetlamy wszystkie kraje i stolice
        System.out.println("Wszystkie kraje i stolice:");
        for (CapitalRepository.CountryCapital cc : repo.getCountries()) {
            System.out.println(cc);
        }

        // 4. Wyszukujemy konkretny kraj (bez użycia Stream API)
        String countryToFind = "Polska";
        CapitalRepository.CountryCapital polska = null;
        for (CapitalRepository.CountryCapital cc : repo.getCountries()) {
            if (cc.getCountry().equalsIgnoreCase(countryToFind)) {
                polska = cc;
                break;
            }
        }

        if (polska != null) {
            System.out.println("\nStolica " + countryToFind + ": " + polska.getCapitals());
        } else {
            System.out.println("\nNie znaleziono kraju: " + countryToFind);
        }
    }
}


