package com.bridgeengineers.weatherapi.service;

import com.bridgeengineers.weatherapi.dto.WeatherRequest;
import com.bridgeengineers.weatherapi.dto.WeatherResponse;
import com.bridgeengineers.weatherapi.model.Weather;
import com.bridgeengineers.weatherapi.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {
    
    @Autowired
    private WeatherRepository weatherRepository;
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.base-url}")
    private String baseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public WeatherResponse getCurrentWeather(String city) {
        System.out.println("Getting current weather for city: " + city);
        
        // First check if we have recent data in database (within last hour)
        var recentWeather = weatherRepository.findTopByCityIgnoreCaseOrderByTimestampDesc(city);
        
        if (recentWeather.isPresent()) {
            Weather weather = recentWeather.get();
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            
            // If data is less than 1 hour old, return cached data
            if (weather.getTimestamp().isAfter(oneHourAgo)) {
                System.out.println("Returning cached weather data for: " + city);
                return convertToDTO(weather);
            }
        }
        
        // If no recent data, fetch from external API
        System.out.println("Fetching fresh weather data from API for: " + city);
        return fetchAndSaveWeatherData(city, null);
    }
    
    @Override
    public WeatherResponse getCurrentWeather(String city, String country) {
        System.out.println("Getting current weather for: " + city + ", " + country);
        return fetchAndSaveWeatherData(city, country);
    }
    
    @Override
    public List<WeatherResponse> getWeatherHistory(String city) {
        System.out.println("Getting weather history for city: " + city);
        List<Weather> weatherHistory = weatherRepository.findByCityIgnoreCaseAndCountryIgnoreCase(city, "");
        
        if (weatherHistory.isEmpty()) {
            // If no history found, try finding by city only
            weatherHistory = weatherRepository.findAll().stream()
                    .filter(w -> w.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }
        
        System.out.println("Found " + weatherHistory.size() + " weather records for: " + city);
        return weatherHistory.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public WeatherResponse saveWeatherData(WeatherRequest request) {
        System.out.println("Saving weather data for: " + request.getCity());
        return fetchAndSaveWeatherData(request.getCity(), request.getCountry());
    }
    
    private WeatherResponse fetchAndSaveWeatherData(String city, String country) {
        try {
            String url = buildApiUrl(city, country);
            System.out.println("Calling Weather API: " + url);
            
            // Call external weather API
            try {
                String jsonResponse = restTemplate.getForObject(url, String.class);
                System.out.println("API Response received: " + (jsonResponse != null ? "Success" : "Empty"));
                
                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    System.out.println("Raw API Response: " + jsonResponse.substring(0, Math.min(200, jsonResponse.length())) + "...");
                }
                
                // For now, create mock data with API confirmation
                // TODO: Parse actual JSON response in next step
                Weather weather = createWeatherDataFromAPI(city, country, jsonResponse);
                
                // Save to database
                Weather savedWeather = weatherRepository.save(weather);
                System.out.println("Successfully saved weather data to database for: " + savedWeather.getCity());
                
                return convertToDTO(savedWeather);
                
            } catch (RestClientException e) {
                System.err.println("API call failed: " + e.getMessage());
                System.out.println("Creating mock weather data due to API failure");
                
                // Fallback to mock data if API fails
                Weather weather = createMockWeatherData(city, country);
                Weather savedWeather = weatherRepository.save(weather);
                
                return convertToDTO(savedWeather);
            }
            
        } catch (Exception e) {
            System.err.println("Error in fetchAndSaveWeatherData: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch weather data for " + city + ": " + e.getMessage());
        }
    }
    
    private String buildApiUrl(String city, String country) {
        String location = city.trim();
        if (country != null && !country.trim().isEmpty()) {
            location += "," + country.trim();
        }
        
        String url = baseUrl + "/weather?q=" + location + "&appid=" + apiKey + "&units=metric";
        
        // Log URL without API key for security
        String logUrl = baseUrl + "/weather?q=" + location + "&appid=***&units=metric";
        System.out.println("Built API URL: " + logUrl);
        
        return url;
    }
    
    private Weather createWeatherDataFromAPI(String city, String country, String apiResponse) {
        // TODO: Parse actual JSON response in next enhancement
        // For now, create enhanced mock data showing API was called
        System.out.println("Creating weather data from API response (mock implementation)");
        
        return new Weather(
            city,
            country != null ? country : "API-Country",
            Math.round((Math.random() * 30 + 5) * 100.0) / 100.0, // Random temp 5-35°C
            Math.round((Math.random() * 40 + 40) * 100.0) / 100.0, // Random humidity 40-80%
            Math.round((Math.random() * 50 + 1000) * 100.0) / 100.0, // Random pressure 1000-1050
            getRandomWeatherDescription(), // Random weather description
            getRandomWeatherIcon(), // Random weather icon
            Math.round((Math.random() * 10 + 1) * 100.0) / 100.0 // Random wind speed 1-11 m/s
        );
    }
    
    private Weather createMockWeatherData(String city, String country) {
        System.out.println("Creating mock weather data for: " + city);
        
        return new Weather(
            city,
            country != null ? country : "Mock-Country",
            25.5, // Fixed temperature for mock
            65.0, // Fixed humidity for mock
            1013.25, // Fixed pressure for mock
            "Clear sky (Mock Data)", // Mock description
            "01d", // Mock icon
            3.5 // Fixed wind speed for mock
        );
    }
    
    private String getRandomWeatherDescription() {
        String[] descriptions = {
            "Clear sky", "Few clouds", "Scattered clouds", "Broken clouds",
            "Light rain", "Overcast clouds", "Partly cloudy", "Sunny"
        };
        return descriptions[(int) (Math.random() * descriptions.length)];
    }
    
    private String getRandomWeatherIcon() {
        String[] icons = {
            "01d", "02d", "03d", "04d", "09d", "10d", "11d", "13d", "50d"
        };
        return icons[(int) (Math.random() * icons.length)];
    }
    
    private WeatherResponse convertToDTO(Weather weather) {
        System.out.println("Converting weather entity to DTO for: " + weather.getCity());
        
        WeatherResponse response = new WeatherResponse();
        response.setCity(weather.getCity());
        response.setCountry(weather.getCountry());
        response.setTemperature(weather.getTemperature());
        response.setHumidity(weather.getHumidity());
        response.setPressure(weather.getPressure());
        response.setDescription(weather.getDescription());
        response.setIcon(weather.getIcon());
        response.setWindSpeed(weather.getWindSpeed());
        response.setTimestamp(weather.getTimestamp());
        
        return response;
    }
}