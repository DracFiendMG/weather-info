package com.sreeram.weather.info.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    public ResponseEntity<String> getWeather() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
