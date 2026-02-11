package com.sreeram.weather.info.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainBO {
    private Double temp;
    private Double tempMin;
    private Double tempMax;
    private Long pressure;
    private Long humidity;
    private Long seaLevel;
}
