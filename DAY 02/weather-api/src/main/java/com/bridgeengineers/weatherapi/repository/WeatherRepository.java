package com.bridgeengineers.weatherapi.repository;

import com.bridgeengineers.weatherapi.model.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends MongoRepository<Weather, String> {
    
    // Find latest weather data for a city
    Optional<Weather> findTopByCityIgnoreCaseOrderByTimestampDesc(String city);
    
    // Find weather data by city and country
    List<Weather> findByCityIgnoreCaseAndCountryIgnoreCase(String city, String country);
    
    // Find weather data within a time range
    List<Weather> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Find weather data by city within time range
    @Query("{'city': {$regex: ?0, $options: 'i'}, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<Weather> findByCityAndTimestampRange(String city, LocalDateTime start, LocalDateTime end);
}