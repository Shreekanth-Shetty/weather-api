package com.project.weather_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AlertResponse {
    private String headline;
    private String severity;
    private String description;
    private String effective;
    private String expires;
}