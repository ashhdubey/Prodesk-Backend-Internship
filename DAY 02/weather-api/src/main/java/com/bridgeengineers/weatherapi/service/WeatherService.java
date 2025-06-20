package com.bridgeengineers.weatherapi.service;

import com.bridgeengineers.weatherapi.dto.WeatherRequest;
import com.bridgeengineers.weatherapi.dto.WeatherResponse;
import java.util.List;

public interface WeatherService {
    WeatherResponse getCurrentWeather(String city);
    WeatherResponse getCurrentWeather(String city, String country);
    List<WeatherResponse> getWeatherHistory(String city);
    WeatherResponse saveWeatherData(WeatherRequest request);
}