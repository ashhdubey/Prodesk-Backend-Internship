package com.bridgeengineers.weatherapi.controller;

import com.bridgeengineers.weatherapi.dto.WeatherRequest;
import com.bridgeengineers.weatherapi.dto.WeatherResponse;
import com.bridgeengineers.weatherapi.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getCurrentWeather(
            @RequestParam String city,
            @RequestParam(required = false) String country) {
        
        WeatherResponse weather = country != null ? 
            weatherService.getCurrentWeather(city, country) : 
            weatherService.getCurrentWeather(city);
            
        return ResponseEntity.ok(weather);
    }
    
    @GetMapping("/history/{city}")
    public ResponseEntity<List<WeatherResponse>> getWeatherHistory(@PathVariable String city) {
        List<WeatherResponse> history = weatherService.getWeatherHistory(city);
        return ResponseEntity.ok(history);
    }
    
    @PostMapping("/save")
    public ResponseEntity<WeatherResponse> saveWeatherData(@Valid @RequestBody WeatherRequest request) {
        WeatherResponse weather = weatherService.saveWeatherData(request);
        return ResponseEntity.ok(weather);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather API is running!");
    }
}