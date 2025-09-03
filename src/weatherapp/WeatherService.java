package weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private final String apiKey = "bbf309687aded62894629c21afb1e639";
   

    /**
     * Pobiera temperaturę i opis pogody dla Warszawy
     */
    public String getWeatherForWarsaw() {
        String city = "Warsaw";
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apiKey + "&units=metric&lang=pl";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            String json = content.toString();

            // Wyciągamy sekcję "main"
            String mainPart = json.split("\"main\":\\{")[1].split("\\}")[0];
            String temp = mainPart.split("\"temp\":")[1].split(",")[0];

            // Wyciągamy opis pogody z tablicy "weather"
            String weatherPart = json.split("\"weather\":\\[")[1].split("\\]")[0];
            String description = weatherPart.split("\"description\":\"")[1].split("\"")[0];

            return "Warszawa: " + temp + "°C, " + description;

        } catch (Exception e) {
            e.printStackTrace();
            return "Błąd pobierania pogody";
        }
    }

    public String getWeatherForCity(String city) {
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apiKey + "&units=metric&lang=pl";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            String json = content.toString();

            // Extract main section
            String mainPart = json.split("\"main\":\\{")[1].split("\\}")[0];
            String temp = mainPart.split("\"temp\":")[1].split(",")[0];

            // Extract weather description
            String weatherPart = json.split("\"weather\":\\[")[1].split("\\]")[0];
            String description = weatherPart.split("\"description\":\"")[1].split("\"")[0];

            return city + ": " + temp + "°C, " + description;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not fetch weather data";
        }
    }

    public static void main(String[] args) {
        WeatherService service = new WeatherService();
        System.out.println(service.getWeatherForWarsaw());
    }
}
