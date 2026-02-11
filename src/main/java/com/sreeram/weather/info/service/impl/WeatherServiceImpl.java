package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.service.GeocoderService;
import com.sreeram.weather.info.service.WeatherService;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import com.sreeram.weather.info.util.WeatherUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${open.api.host}")
    private String host;

    @Value("${open.api.weather.path}")
    private String path;

    @Value("${open.api.key}")
    private String apiKey;

    private GeocoderService geocoderService;
    private RestTemplate restTemplate;

    public WeatherServiceImpl(
            GeocoderService geocoderService,
            RestTemplate restTemplate
    ) {
        this.geocoderService = geocoderService;
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherTO getWeather(WeatherRequest request) {
        CoordinatesBO coordinates = geocoderService.getCoordinates(request.getPincode());

        String URL = UriComponentsBuilder.fromUriString(host + path)
                .queryParam("lat", coordinates.getLat())
                .queryParam("lon", coordinates.getLon())
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            ResponseEntity<WeatherDetailsBO> response = restTemplate.exchange(
                    URL, HttpMethod.GET, null, WeatherDetailsBO.class
            );

            return WeatherUtils.transform(response.getBody());
        } catch (HttpServerErrorException | HttpClientErrorException h) {
            h.printStackTrace();
            throw new RuntimeException("Error");
        }
    }
}
