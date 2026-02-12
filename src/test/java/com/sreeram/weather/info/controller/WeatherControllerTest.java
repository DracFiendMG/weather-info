package com.sreeram.weather.info.controller;

import com.sreeram.weather.info.exception.InvalidRequestException;
import com.sreeram.weather.info.exception.WeatherServiceException;
import com.sreeram.weather.info.service.WeatherService;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private WeatherRequest weatherRequest;
    private WeatherTO weatherTO;

    @BeforeEach
    void setUp() {
        weatherRequest = new WeatherRequest();
        ReflectionTestUtils.setField(weatherRequest, "pincode", "500030");
        ReflectionTestUtils.setField(weatherRequest, "forDate", new Date());

        weatherTO = new WeatherTO();
        weatherTO.setPincode("500030");
        weatherTO.setForDate(new Date());
        weatherTO.setTemp(25.0);
        weatherTO.setMain("Clear");
        weatherTO.setDescription("clear sky");
    }

    @Test
    void testGetWeather_WithValidRequest_ShouldReturnWeatherData() {
        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(weatherTO);

        ResponseEntity<WeatherTO> response = weatherController.getWeather(weatherRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("500030", response.getBody().getPincode());
        assertEquals("Clear", response.getBody().getMain());
    }

    @Test
    void testGetWeather_WithInvalidRequest_ShouldThrowException() {
        when(weatherService.getWeather(any(WeatherRequest.class)))
                .thenThrow(new InvalidRequestException("Invalid pincode format"));

        assertThrows(InvalidRequestException.class, () -> weatherController.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenServiceUnavailable_ShouldThrowException() {
        when(weatherService.getWeather(any(WeatherRequest.class)))
                .thenThrow(new WeatherServiceException("Weather API service unavailable"));

        assertThrows(WeatherServiceException.class, () -> weatherController.getWeather(weatherRequest));
    }
}


