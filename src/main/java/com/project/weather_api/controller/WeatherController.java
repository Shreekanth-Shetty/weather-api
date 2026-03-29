package com.project.weather_api.controller;

import com.project.weather_api.dto.WeatherResponse;
import com.project.weather_api.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather", description = "Get weather data and alerts")
@SecurityRequirement(name = "bearerAuth")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current")
    @Operation(summary = "Get current weather", description = "Returns current weather for a city")
    public ResponseEntity<?> getCurrentWeather(@RequestParam String city,
                                               @RequestParam(defaultValue = "CELSIUS") String unit) {
        WeatherResponse weather = weatherService.getCurrentWeather(city, unit);
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get weather alerts", description = "Returns active weather alerts for a city")
    public ResponseEntity<?> getAlerts(@RequestParam String city) {
        return ResponseEntity.ok(weatherService.getAlerts(city));
}
}