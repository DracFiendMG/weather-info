package com.sreeram.weather.info.repository;

import com.sreeram.weather.info.to.WeatherPK;
import com.sreeram.weather.info.to.WeatherTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface WeatherRepository extends JpaRepository<WeatherTO, WeatherPK> {
    WeatherTO findWeatherTOByPincodeAndForDate(String pincode, Date forDate);
}
