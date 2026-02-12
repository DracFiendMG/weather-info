package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.MainBO;
import com.sreeram.weather.info.bo.weather.WeatherBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.exception.InvalidRequestException;
import com.sreeram.weather.info.exception.WeatherServiceException;
import com.sreeram.weather.info.repository.CoordinatesRepository;
import com.sreeram.weather.info.repository.WeatherRepository;
import com.sreeram.weather.info.service.GeocoderService;
import com.sreeram.weather.info.to.CoordinatesTO;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private GeocoderService geocoderService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CoordinatesRepository coordinatesRepository;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private WeatherRequest weatherRequest;
    private CoordinatesBO coordinatesBO;
    private WeatherDetailsBO weatherDetailsBO;
    private WeatherTO weatherTO;
    private CoordinatesTO coordinatesTO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "host", "https://api.openweathermap.org");
        ReflectionTestUtils.setField(weatherService, "path", "/data/2.5/weather");
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");

        weatherRequest = new WeatherRequest();
        ReflectionTestUtils.setField(weatherRequest, "pincode", "500030");
        ReflectionTestUtils.setField(weatherRequest, "forDate", new Date());

        coordinatesBO = new CoordinatesBO();
        coordinatesBO.setLat(17.4065);
        coordinatesBO.setLon(78.4772);

        weatherTO = new WeatherTO();
        weatherTO.setPincode("500030");
        weatherTO.setForDate(new Date());
        weatherTO.setTemp(25.0);
        weatherTO.setMain("Clear");

        coordinatesTO = new CoordinatesTO();
        coordinatesTO.setPincode("500030");
        coordinatesTO.setForDate(new Date());
        coordinatesTO.setLatitude(17.4065);
        coordinatesTO.setLongitude(78.4772);

        weatherDetailsBO = new WeatherDetailsBO();
        MainBO mainBO = new MainBO();
        mainBO.setTemp(25.0);
        mainBO.setTempMin(20.0);
        mainBO.setTempMax(30.0);
        mainBO.setPressure(1013L);
        mainBO.setHumidity(65L);
        mainBO.setSeaLevel(1013L);

        WeatherBO weatherBO = new WeatherBO();
        weatherBO.setMain("Clear");
        weatherBO.setDescription("clear sky");
        weatherBO.setIcon("01d");

        List<WeatherBO> weatherList = new ArrayList<>();
        weatherList.add(weatherBO);

        weatherDetailsBO.setMain(mainBO);
        weatherDetailsBO.setWeather(weatherList);
    }

    @Test
    void testGetWeather_WhenDataExistsInCache_ShouldReturnCachedData() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(coordinatesTO);
        when(weatherRepository.findWeatherTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(weatherTO);

        WeatherTO result = weatherService.getWeather(weatherRequest);

        assertNotNull(result);
        assertEquals("500030", result.getPincode());
        verify(coordinatesRepository).findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class));
        verify(weatherRepository).findWeatherTOByPincodeAndForDate(anyString(), any(Date.class));
        verify(geocoderService, never()).getCoordinates(anyString());
        verify(restTemplate, never()).exchange(anyString(), any(), any(), eq(WeatherDetailsBO.class));
    }

    @Test
    void testGetWeather_WhenDataNotInCache_ShouldFetchFromAPI() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(null);
        when(geocoderService.getCoordinates(anyString())).thenReturn(coordinatesBO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(WeatherDetailsBO.class)))
                .thenReturn(new ResponseEntity<>(weatherDetailsBO, HttpStatus.OK));
        when(coordinatesRepository.save(any(CoordinatesTO.class))).thenReturn(coordinatesTO);
        when(weatherRepository.save(any(WeatherTO.class))).thenReturn(weatherTO);

        WeatherTO result = weatherService.getWeather(weatherRequest);

        assertNotNull(result);
        verify(geocoderService).getCoordinates("500030");
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(WeatherDetailsBO.class));
        verify(coordinatesRepository).save(any(CoordinatesTO.class));
        verify(weatherRepository).save(any(WeatherTO.class));
    }

    @Test
    void testGetWeather_WhenRequestIsNull_ShouldThrowInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> weatherService.getWeather(null));
    }

    @Test
    void testGetWeather_WhenPincodeIsNull_ShouldThrowInvalidRequestException() {
        ReflectionTestUtils.setField(weatherRequest, "pincode", null);

        assertThrows(InvalidRequestException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenPincodeIsEmpty_ShouldThrowInvalidRequestException() {
        ReflectionTestUtils.setField(weatherRequest, "pincode", "");

        assertThrows(InvalidRequestException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenDateIsNull_ShouldThrowInvalidRequestException() {
        ReflectionTestUtils.setField(weatherRequest, "forDate", null);

        assertThrows(InvalidRequestException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenPincodeFormatIsInvalid_ShouldThrowInvalidRequestException() {
        ReflectionTestUtils.setField(weatherRequest, "pincode", "abc");

        assertThrows(InvalidRequestException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenCoordinatesAreNull_ShouldThrowWeatherServiceException() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(null);
        when(geocoderService.getCoordinates(anyString())).thenReturn(null);

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenAPIReturnsNull_ShouldThrowWeatherServiceException() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(null);
        when(geocoderService.getCoordinates(anyString())).thenReturn(coordinatesBO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(WeatherDetailsBO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenHttpClientError_ShouldThrowWeatherServiceException() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(null);
        when(geocoderService.getCoordinates(anyString())).thenReturn(coordinatesBO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(WeatherDetailsBO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(weatherRequest));
    }

    @Test
    void testGetWeather_WhenHttpServerError_ShouldThrowWeatherServiceException() {
        when(coordinatesRepository.findCoordinatesTOByPincodeAndForDate(anyString(), any(Date.class)))
                .thenReturn(null);
        when(geocoderService.getCoordinates(anyString())).thenReturn(coordinatesBO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(WeatherDetailsBO.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(weatherRequest));
    }
}


