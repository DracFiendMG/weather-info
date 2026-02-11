package com.sreeram.weather.info.service;

import com.sreeram.weather.info.bo.common.CoordinatesBO;

public interface GeocoderService {
    CoordinatesBO getCoordinates(Long pincode);
}
