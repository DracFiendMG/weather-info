package com.sreeram.weather.info.exception;

public class GeocoderServiceException extends RuntimeException {
    public GeocoderServiceException(String message) {
        super(message);
    }

    public GeocoderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

