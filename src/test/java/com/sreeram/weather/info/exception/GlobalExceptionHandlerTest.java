package com.sreeram.weather.info.exception;

import com.sreeram.weather.info.to.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleWeatherServiceException_ShouldReturn503() {
        String message = "Weather API is unavailable";
        WeatherServiceException exception = new WeatherServiceException(message);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleWeatherServiceException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Weather Service Error", response.getBody().getMessage());
        assertEquals(message, response.getBody().getDetails());
        assertEquals(503, response.getBody().getStatus());
    }

    @Test
    void testHandleGeocoderServiceException_ShouldReturn503() {
        String message = "Geocoder API is unavailable";
        GeocoderServiceException exception = new GeocoderServiceException(message);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeocoderServiceException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Geocoder Service Error", response.getBody().getMessage());
        assertEquals(message, response.getBody().getDetails());
        assertEquals(503, response.getBody().getStatus());
    }

    @Test
    void testHandleInvalidRequestException_ShouldReturn400() {
        String message = "Invalid pincode format";
        InvalidRequestException exception = new InvalidRequestException(message);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidRequestException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid Request", response.getBody().getMessage());
        assertEquals(message, response.getBody().getDetails());
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    void testHandleDataNotFoundException_ShouldReturn404() {
        String message = "Weather data not found for pincode 500030";
        DataNotFoundException exception = new DataNotFoundException(message);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataNotFoundException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Data Not Found", response.getBody().getMessage());
        assertEquals(message, response.getBody().getDetails());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void testHandleGlobalException_ShouldReturn500() {
        String message = "Unexpected error occurred";
        Exception exception = new RuntimeException(message);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getMessage());
        assertEquals(message, response.getBody().getDetails());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    void testHandleWeatherServiceException_WithNullMessage_ShouldStillReturn503() {
        WeatherServiceException exception = new WeatherServiceException(null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleWeatherServiceException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getDetails());
    }

    @Test
    void testHandleInvalidRequestException_WithLongMessage_ShouldHandleCorrectly() {
        String longMessage = "Invalid request parameters: pincode must be 5-6 digits, " +
                "date cannot be null, date must be in the future, and all fields are required";
        InvalidRequestException exception = new InvalidRequestException(longMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidRequestException(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(longMessage, response.getBody().getDetails());
    }

    @Test
    void testAllExceptionHandlers_ShouldHaveTimestampSet() {
        WeatherServiceException weatherEx = new WeatherServiceException("Weather error");
        GeocoderServiceException geocoderEx = new GeocoderServiceException("Geocoder error");
        InvalidRequestException invalidEx = new InvalidRequestException("Invalid error");
        DataNotFoundException notFoundEx = new DataNotFoundException("Not found error");
        Exception globalEx = new Exception("Global error");

        ResponseEntity<ErrorResponse> weatherResponse = globalExceptionHandler.handleWeatherServiceException(weatherEx, webRequest);
        ResponseEntity<ErrorResponse> geocoderResponse = globalExceptionHandler.handleGeocoderServiceException(geocoderEx, webRequest);
        ResponseEntity<ErrorResponse> invalidResponse = globalExceptionHandler.handleInvalidRequestException(invalidEx, webRequest);
        ResponseEntity<ErrorResponse> notFoundResponse = globalExceptionHandler.handleDataNotFoundException(notFoundEx, webRequest);
        ResponseEntity<ErrorResponse> globalResponse = globalExceptionHandler.handleGlobalException(globalEx, webRequest);

        assertNotNull(weatherResponse.getBody().getTimestamp());
        assertNotNull(geocoderResponse.getBody().getTimestamp());
        assertNotNull(invalidResponse.getBody().getTimestamp());
        assertNotNull(notFoundResponse.getBody().getTimestamp());
        assertNotNull(globalResponse.getBody().getTimestamp());
    }

    @Test
    void testExceptionHandlers_ShouldReturnDifferentStatusCodes() {
        WeatherServiceException weatherEx = new WeatherServiceException("Error");
        InvalidRequestException invalidEx = new InvalidRequestException("Error");
        DataNotFoundException notFoundEx = new DataNotFoundException("Error");
        Exception globalEx = new Exception("Error");

        ResponseEntity<ErrorResponse> weatherResponse = globalExceptionHandler.handleWeatherServiceException(weatherEx, webRequest);
        ResponseEntity<ErrorResponse> invalidResponse = globalExceptionHandler.handleInvalidRequestException(invalidEx, webRequest);
        ResponseEntity<ErrorResponse> notFoundResponse = globalExceptionHandler.handleDataNotFoundException(notFoundEx, webRequest);
        ResponseEntity<ErrorResponse> globalResponse = globalExceptionHandler.handleGlobalException(globalEx, webRequest);

        assertEquals(503, weatherResponse.getBody().getStatus());
        assertEquals(400, invalidResponse.getBody().getStatus());
        assertEquals(404, notFoundResponse.getBody().getStatus());
        assertEquals(500, globalResponse.getBody().getStatus());
    }

    @Test
    void testExceptionHandlers_ShouldReturnDifferentMessages() {
        WeatherServiceException weatherEx = new WeatherServiceException("Error");
        GeocoderServiceException geocoderEx = new GeocoderServiceException("Error");
        InvalidRequestException invalidEx = new InvalidRequestException("Error");
        DataNotFoundException notFoundEx = new DataNotFoundException("Error");
        Exception globalEx = new Exception("Error");

        ResponseEntity<ErrorResponse> weatherResponse = globalExceptionHandler.handleWeatherServiceException(weatherEx, webRequest);
        ResponseEntity<ErrorResponse> geocoderResponse = globalExceptionHandler.handleGeocoderServiceException(geocoderEx, webRequest);
        ResponseEntity<ErrorResponse> invalidResponse = globalExceptionHandler.handleInvalidRequestException(invalidEx, webRequest);
        ResponseEntity<ErrorResponse> notFoundResponse = globalExceptionHandler.handleDataNotFoundException(notFoundEx, webRequest);
        ResponseEntity<ErrorResponse> globalResponse = globalExceptionHandler.handleGlobalException(globalEx, webRequest);

        assertEquals("Weather Service Error", weatherResponse.getBody().getMessage());
        assertEquals("Geocoder Service Error", geocoderResponse.getBody().getMessage());
        assertEquals("Invalid Request", invalidResponse.getBody().getMessage());
        assertEquals("Data Not Found", notFoundResponse.getBody().getMessage());
        assertEquals("Internal Server Error", globalResponse.getBody().getMessage());
    }

    @Test
    void testHandleGlobalException_WithDifferentExceptionTypes_ShouldHandleAll() {
        Exception[] exceptions = {
                new NullPointerException("NPE"),
                new IllegalArgumentException("IAE"),
                new IllegalStateException("ISE"),
                new RuntimeException("RE")
        };

        for (Exception ex : exceptions) {
            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(ex, webRequest);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Internal Server Error", response.getBody().getMessage());
        }
    }
}

