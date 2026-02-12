package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.bo.weather.WeatherDetailsBO;
import com.sreeram.weather.info.repository.CoordinatesRepository;
import com.sreeram.weather.info.repository.WeatherRepository;
import com.sreeram.weather.info.service.GeocoderService;
import com.sreeram.weather.info.service.WeatherService;
import com.sreeram.weather.info.to.CoordinatesTO;
import com.sreeram.weather.info.to.WeatherRequest;
import com.sreeram.weather.info.to.WeatherTO;
import com.sreeram.weather.info.util.TransformUtils;
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
        String pincode = request.getPincode();
        Date forDate = request.getForDate();

        CoordinatesTO coordinatesTO = coordinatesRepository.findCoordinatesTOByPincodeAndForDate(pincode, forDate);

        if (coordinatesTO != null) {
            WeatherTO weather = weatherRepository.findWeatherTOByPincodeAndForDate(pincode, forDate);
            if (weather != null) {
                return weather;
            }
        }

        CoordinatesBO coordinatesBO = geocoderService.getCoordinates(pincode);

        String URL = UriComponentsBuilder.fromUriString(host + path)
                .queryParam("lat", coordinatesBO.getLat())
                .queryParam("lon", coordinatesBO.getLon())
                .queryParam("date", forDate)
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            ResponseEntity<WeatherDetailsBO> response = restTemplate.exchange(
                    URL, HttpMethod.GET, null, WeatherDetailsBO.class
            );

            WeatherTO weather = TransformUtils.transform(pincode, forDate, response.getBody());
            CoordinatesTO coordinates = TransformUtils.transform(pincode, forDate, coordinatesBO);

            coordinatesRepository.save(coordinates);
            weatherRepository.save(weather);

            return weather;
        } catch (HttpServerErrorException | HttpClientErrorException h) {
            h.printStackTrace();
            throw new RuntimeException("Error");
        }
    }
}
