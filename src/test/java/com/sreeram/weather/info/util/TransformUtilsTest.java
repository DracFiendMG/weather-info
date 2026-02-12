package com.sreeram.weather.info.util;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.MainBO;
import com.sreeram.weather.info.bo.weather.WeatherBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.to.CoordinatesTO;
import com.sreeram.weather.info.to.WeatherTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformUtilsTest {

    private WeatherDetailsBO weatherDetailsBO;
    private CoordinatesBO coordinatesBO;
    private String pincode;
    private Date forDate;

    @BeforeEach
    void setUp() {
        pincode = "500030";
        forDate = new Date();

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

        coordinatesBO = new CoordinatesBO();
        coordinatesBO.setLat(17.4065);
        coordinatesBO.setLon(78.4772);
    }

    @Test
    void testTransformWeatherDetailsBO_ShouldReturnWeatherTO() {
        WeatherTO result = TransformUtils.transform(pincode, forDate, weatherDetailsBO);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode());
        assertEquals(forDate, result.getForDate());
        assertEquals(25.0, result.getTemp());
        assertEquals(20.0, result.getTempMin());
        assertEquals(30.0, result.getTempMax());
        assertEquals(1013L, result.getPressure());
        assertEquals(65L, result.getHumidity());
        assertEquals(1013L, result.getSeaLevel());
        assertEquals("Clear", result.getMain());
        assertEquals("clear sky", result.getDescription());
        assertEquals("01d", result.getIcon());
    }

    @Test
    void testTransformCoordinatesBO_ShouldReturnCoordinatesTO() {
        CoordinatesTO result = TransformUtils.transform(pincode, forDate, coordinatesBO);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode());
        assertEquals(forDate, result.getForDate());
        assertEquals(17.4065, result.getLatitude());
        assertEquals(78.4772, result.getLongitude());
    }

    @Test
    void testTransformWeatherDetailsBO_WithNullValues_ShouldHandleGracefully() {
        MainBO mainBO = new MainBO();
        mainBO.setTemp(null);
        weatherDetailsBO.setMain(mainBO);

        WeatherTO result = TransformUtils.transform(pincode, forDate, weatherDetailsBO);

        assertNotNull(result);
        assertNull(result.getTemp());
    }
}

