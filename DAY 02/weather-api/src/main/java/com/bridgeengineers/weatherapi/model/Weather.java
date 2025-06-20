package com.bridgeengineers.weatherapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "weather_data")
public class Weather {
    @Id
    private String id;
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
    public Weather() {}
    
    public Weather(String city, String country, double temperature, 
                  double humidity, double pressure, String description, 
                  String icon, double windSpeed) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.description = description;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
    
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}