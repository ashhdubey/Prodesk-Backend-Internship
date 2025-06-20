package com.bridgeengineers.weatherapi.dto;

import java.time.LocalDateTime;

public class WeatherResponse {
    private String city;
    private String country;
    private double temperature;
    private double humidity;
    private double pressure;
    private String description;
    private String icon;
    private double windSpeed;
    private LocalDateTime timestamp;
    
    // Constructors
    public WeatherResponse() {}
    
    // Getters and Setters (same as Weather model)
    // ... (add all getters and setters similar to Weather model)
}