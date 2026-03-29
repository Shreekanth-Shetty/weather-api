package com.project.weather_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.weather_api.dto.AlertResponse;
import com.project.weather_api.dto.WeatherResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Service
public class WeatherService {
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

   @SuppressWarnings("unchecked")
   public WeatherResponse getCurrentWeather(String city, String unit) {
        String url = apiUrl + "/current.json?key=" + apiKey + "&q=" + city + "&aqi=no";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // WeatherAPI.com returns nested objects: location, current
        Map<String, Object> location = (Map<String, Object>) response.get("location");
        Map<String, Object> current  = (Map<String, Object>) response.get("current");
        Map<String, Object> condition = (Map<String, Object>) current.get("condition");

        boolean isFahrenheit = unit.equalsIgnoreCase("FAHRENHEIT");

        WeatherResponse result = new WeatherResponse();
        result.setCity((String) location.get("name"));
        result.setCountry((String) location.get("country"));
        result.setLocalTime((String) location.get("localtime"));
        result.setDescription((String) condition.get("text"));
        result.setHumidity(((Number) current.get("humidity")).intValue());

        if (isFahrenheit) {
            result.setTemperature(((Number) current.get("temp_f")).doubleValue());
            result.setFeelsLike(current.get("feelslike_f") + "°F");
            result.setWindSpeed(((Number) current.get("wind_mph")).doubleValue());
            result.setUnit("°F");
        } else {
            result.setTemperature(((Number) current.get("temp_c")).doubleValue());
            result.setFeelsLike(current.get("feelslike_c") + "°C");
            result.setWindSpeed(((Number) current.get("wind_kph")).doubleValue());
            result.setUnit("°C");
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<AlertResponse> getAlerts(String city) {
    String url = apiUrl + "/forecast.json?key=" + apiKey
               + "&q=" + city + "&days=1&alerts=yes";

    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
    List<AlertResponse> alerts = new ArrayList<>();

    Map<String, Object> alertsData = (Map<String, Object>) response.get("alerts");
    if (alertsData != null) {
        List<Map<String, Object>> alertList = (List<Map<String, Object>>) alertsData.get("alert");
        if (alertList != null) {
            for (Map<String, Object> alert : alertList) {
                alerts.add(new AlertResponse(
                    (String) alert.get("headline"),
                    (String) alert.get("severity"),
                    (String) alert.get("desc"),
                    (String) alert.get("effective"),
                    (String) alert.get("expires")
                ));
            }
        }
    }

    if (alerts.isEmpty()) {
        alerts.add(new AlertResponse(
            "No active alerts",
            "None",
            "No severe weather alerts for this location",
            null,
            null
        ));
    }

    return alerts;
}
}
