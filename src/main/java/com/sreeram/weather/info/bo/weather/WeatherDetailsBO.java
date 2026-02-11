package com.sreeram.weather.info.bo.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sreeram.weather.info.bo.common.CoordinatesBO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherDetailsBO {
    private CoordinatesBO coord;
    private List<WeatherBO> weather;
    private MainBO main;

    public CoordinatesBO getCoord() {
        return coord;
    }

    public void setCoord(CoordinatesBO coord) {
        this.coord = coord;
    }

    public List<WeatherBO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherBO> weather) {
        this.weather = weather;
    }

    public MainBO getMain() {
        return main;
    }

    public void setMain(MainBO main) {
        this.main = main;
    }
}
