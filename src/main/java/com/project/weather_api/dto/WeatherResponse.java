package com.project.weather_api.dto;

import lombok.Data;

@Data
public class WeatherResponse {
    private String city;
    private String country;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;
    private String unit;
    private String feelsLike;
    private String localTime;
}