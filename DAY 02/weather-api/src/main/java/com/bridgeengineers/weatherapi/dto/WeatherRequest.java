package com.bridgeengineers.weatherapi.dto;

import jakarta.validation.constraints.NotBlank;

public class WeatherRequest {
    @NotBlank(message = "City name is required")
    private String city;
    
    private String country; // Optional
    
    // Constructors
    public WeatherRequest() {}
    
    public WeatherRequest(String city, String country) {
        this.city = city;
        this.country = country;
    }
    
    // Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}