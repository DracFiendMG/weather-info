package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.exception.GeocoderServiceException;
import com.sreeram.weather.info.exception.InvalidRequestException;
import com.sreeram.weather.info.service.GeocoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocoderServiceImpl implements GeocoderService {

    private static final Logger logger = LoggerFactory.getLogger(GeocoderServiceImpl.class);

    @Value("${open.api.host}")
    private String host;

    @Value("${open.api.geocode.path}")
    private String path;

    @Value("${open.api.geocode.country-code}")
    private String countryCode;

    @Value("${open.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeocoderServiceImpl(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CoordinatesBO getCoordinates(String pincode) {
        if (pincode == null || pincode.trim().isEmpty()) {
            throw new InvalidRequestException("Pincode cannot be null or empty");
        }

        logger.info("Fetching coordinates for pincode: {}", pincode);

        String URL = host + path;

        String finalURL = UriComponentsBuilder.fromUriString(URL)
                .queryParam("zip", pincode + "," + countryCode)
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            ResponseEntity<CoordinatesBO> response = restTemplate.exchange(
                    finalURL, HttpMethod.GET, null, CoordinatesBO.class
            );

            if (response.getBody() == null) {
                logger.error("Geocoder API returned null response for pincode: {}", pincode);
                throw new GeocoderServiceException("Unable to fetch coordinates for pincode: " + pincode);
            }

            logger.info("Successfully fetched coordinates for pincode: {}", pincode);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Client error while fetching coordinates: {}", e.getMessage());
            throw new GeocoderServiceException("Invalid pincode or country code: " + pincode, e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error while fetching coordinates: {}", e.getMessage());
            throw new GeocoderServiceException("Geocoder API service unavailable", e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching coordinates: {}", e.getMessage());
            throw new GeocoderServiceException("Failed to fetch coordinates: " + e.getMessage(), e);
        }
    }
}
