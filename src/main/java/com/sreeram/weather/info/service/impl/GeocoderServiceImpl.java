package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.service.GeocoderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeocoderServiceImpl implements GeocoderService {

    @Value("${open.api.host}")
    private String host;

    @Value("${open.api.geocode.path}")
    private String path;

    @Value("${open.api.geocode.country-code}")
    private String countryCode;

    @Value("${open.api.key}")
    private String apiKey;

    private RestTemplate restTemplate;

    public GeocoderServiceImpl(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CoordinatesBO getCoordinates(Long pincode) {
        String URL = host + path;

        String finalURL = UriComponentsBuilder.fromUriString(URL)
                .queryParam("zip", pincode + "," + countryCode)
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            ResponseEntity<CoordinatesBO> response = restTemplate.exchange(
                    finalURL, HttpMethod.GET, null, CoordinatesBO.class
            );

            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException h) {
            h.printStackTrace();
            throw new RuntimeException("Error");
        }
    }
}
