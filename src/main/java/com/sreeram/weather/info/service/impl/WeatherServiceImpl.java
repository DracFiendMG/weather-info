package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.exception.InvalidRequestException;
import com.sreeram.weather.info.exception.WeatherServiceException;
import com.sreeram.weather.info.repository.CoordinatesRepository;
import com.sreeram.weather.info.repository.WeatherRepository;
import com.sreeram.weather.info.service.GeocoderService;
import com.sreeram.weather.info.service.WeatherService;
import com.sreeram.weather.info.to.CoordinatesTO;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import com.sreeram.weather.info.util.TransformUtils;
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

import java.util.Date;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${open.api.host}")
    private String host;

    @Value("${open.api.weather.path}")
    private String path;

    @Value("${open.api.key}")
    private String apiKey;

    private final GeocoderService geocoderService;
    private final RestTemplate restTemplate;
    private final CoordinatesRepository coordinatesRepository;
    private final WeatherRepository weatherRepository;

    public WeatherServiceImpl(
            GeocoderService geocoderService,
            RestTemplate restTemplate,
            CoordinatesRepository coordinatesRepository,
            WeatherRepository weatherRepository
    ) {
        this.geocoderService = geocoderService;
        this.restTemplate = restTemplate;
        this.coordinatesRepository = coordinatesRepository;
        this.weatherRepository = weatherRepository;
    }

    @Override
    public WeatherTO getWeather(WeatherRequest request) {
        validateWeatherRequest(request);

        String pincode = request.getPincode();
        Date forDate = request.getForDate();

        logger.info("Fetching weather for pincode: {} and date: {}", pincode, forDate);

        CoordinatesTO coordinatesTO = coordinatesRepository.findCoordinatesTOByPincodeAndForDate(pincode, forDate);

        if (coordinatesTO != null) {
            WeatherTO weather = weatherRepository.findWeatherTOByPincodeAndForDate(pincode, forDate);
            if (weather != null) {
                logger.info("Weather data found in cache for pincode: {}", pincode);
                return weather;
            }
        }

        logger.info("Weather data not found in cache, fetching from API for pincode: {}", pincode);

        CoordinatesBO coordinatesBO = geocoderService.getCoordinates(pincode);

        if (coordinatesBO == null) {
            logger.error("Failed to get coordinates for pincode: {}", pincode);
            throw new WeatherServiceException("Unable to fetch coordinates for pincode: " + pincode);
        }

        String URL = UriComponentsBuilder.fromUriString(host + path)
                .queryParam("lat", coordinatesBO.getLat())
                .queryParam("lon", coordinatesBO.getLon())
                .queryParam("date", forDate)
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            ResponseEntity<WeatherDetailsBO> response = restTemplate.exchange(
                    URL, HttpMethod.GET, null, WeatherDetailsBO.class
            );

            if (response.getBody() == null) {
                logger.error("Weather API returned null response for pincode: {}", pincode);
                throw new WeatherServiceException("Weather API returned empty response");
            }

            WeatherTO weather = TransformUtils.transform(pincode, forDate, response.getBody());
            CoordinatesTO coordinates = TransformUtils.transform(pincode, forDate, coordinatesBO);

            coordinatesRepository.save(coordinates);
            weatherRepository.save(weather);

            logger.info("Successfully fetched and saved weather data for pincode: {}", pincode);
            return weather;
        } catch (HttpClientErrorException e) {
            logger.error("Client error while fetching weather data: {}", e.getMessage());
            throw new WeatherServiceException("Invalid request to weather API: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error while fetching weather data: {}", e.getMessage());
            throw new WeatherServiceException("Weather API service unavailable: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching weather data: {}", e.getMessage());
            throw new WeatherServiceException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }

    private void validateWeatherRequest(WeatherRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Weather request cannot be null");
        }
        if (request.getPincode() == null || request.getPincode().trim().isEmpty()) {
            throw new InvalidRequestException("Pincode cannot be null or empty");
        }
        if (request.getForDate() == null) {
            throw new InvalidRequestException("Date cannot be null");
        }
        if (!request.getPincode().matches("\\d{5,6}")) {
            throw new InvalidRequestException("Invalid pincode format. Expected 5-6 digits");
        }
    }
}
