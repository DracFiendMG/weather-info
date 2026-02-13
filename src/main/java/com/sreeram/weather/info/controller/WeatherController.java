package com.sreeram.weather.info.controller;

import com.sreeram.weather.info.service.WeatherService;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(
            WeatherService weatherService
    ) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherTO getWeather(
            @RequestBody WeatherRequest weatherRequest
    ) {
        return weatherService.getWeather(weatherRequest);
    }
}
