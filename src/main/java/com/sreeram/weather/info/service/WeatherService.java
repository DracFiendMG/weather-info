package com.sreeram.weather.info.service;

import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;

public interface WeatherService {
    WeatherTO getWeather(WeatherRequest request);
}
