package weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
            // URL encode the city name
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + encodedCity + "&appid=" + apiKey + "&units=metric&lang=pl";
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
            String feelsLike = mainPart.split("\"feels_like\":")[1].split(",")[0];
            String pressure = mainPart.split("\"pressure\":")[1].split(",")[0];
            String humidity = mainPart.split("\"humidity\":")[1].split(",")[0];

            // Extract weather description
            String weatherPart = json.split("\"weather\":\\[")[1].split("\\]")[0];
            String description = weatherPart.split("\"description\":\"")[1].split("\"")[0];

            // Extract wind data
            String windPart = json.split("\"wind\":\\{")[1].split("\\}")[0];
            String windSpeed = windPart.split("\"speed\":")[1].split(",")[0];
            // Fix wind degree parsing to handle cases when "gust" comes before "deg"
            String windDeg = "0";
            if (windPart.contains("\"deg\":")) {
                windDeg = windPart.split("\"deg\":")[1].split("[,}]")[0];
            }

            return String.format("%s|%s|%s|%s|%s|%s|%s|%s", 
                city, temp, description, pressure, humidity, feelsLike, windSpeed, windDeg);

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
