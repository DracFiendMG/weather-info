package com.sreeram.weather.info.repository;

import com.sreeram.weather.info.to.WeatherTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherTO, Long> {
    WeatherTO findWeatherTOByPincode(Long pincode);
}
