package weatherapp;

import java.util.List;

public class CountryCapital {
    private String country;
    private List<String> capitals;

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
