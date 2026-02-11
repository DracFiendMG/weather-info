package com.sreeram.weather.info.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherDetailsBO {
    private CoordinatesBO coord;
    private List<WeatherBO> weather;
    private MainBO main;
}
