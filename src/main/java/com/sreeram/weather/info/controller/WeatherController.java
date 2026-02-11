package com.sreeram.weather.info.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping("/{pincode}")
    public ResponseEntity<String> getWeather(
            @PathVariable Long pincode
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
