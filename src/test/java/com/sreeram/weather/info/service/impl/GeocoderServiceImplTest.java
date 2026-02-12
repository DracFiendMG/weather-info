package com.sreeram.weather.info.service.impl;

import com.sreeram.weather.info.bo.common.CoordinatesBO;
import com.sreeram.weather.info.exception.GeocoderServiceException;
import com.sreeram.weather.info.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeocoderServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeocoderServiceImpl geocoderService;

    private CoordinatesBO coordinatesBO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geocoderService, "host", "https://api.openweathermap.org");
        ReflectionTestUtils.setField(geocoderService, "path", "/geo/1.0/zip");
        ReflectionTestUtils.setField(geocoderService, "countryCode", "IN");
        ReflectionTestUtils.setField(geocoderService, "apiKey", "test-api-key");

        coordinatesBO = new CoordinatesBO();
        coordinatesBO.setLat(17.4065);
        coordinatesBO.setLon(78.4772);
    }

    @Test
    void testGetCoordinates_WithValidPincode_ShouldReturnCoordinates() {
        String pincode = "500030";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class)))
                .thenReturn(new ResponseEntity<>(coordinatesBO, HttpStatus.OK));

        CoordinatesBO result = geocoderService.getCoordinates(pincode);

        assertNotNull(result);
        assertEquals(17.4065, result.getLat());
        assertEquals(78.4772, result.getLon());
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class));
    }

    @Test
    void testGetCoordinates_WhenPincodeIsNull_ShouldThrowInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> geocoderService.getCoordinates(null));
    }

    @Test
    void testGetCoordinates_WhenPincodeIsEmpty_ShouldThrowInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> geocoderService.getCoordinates(""));
    }

    @Test
    void testGetCoordinates_WhenPincodeIsBlank_ShouldThrowInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> geocoderService.getCoordinates("   "));
    }

    @Test
    void testGetCoordinates_WhenAPIReturnsNull_ShouldThrowGeocoderServiceException() {
        String pincode = "500030";
        ResponseEntity<CoordinatesBO> nullResponse = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class)))
                .thenReturn(nullResponse);

        assertThrows(GeocoderServiceException.class, () -> geocoderService.getCoordinates(pincode));
    }

    @Test
    void testGetCoordinates_WhenHttpClientError_ShouldThrowGeocoderServiceException() {
        String pincode = "999999";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        GeocoderServiceException exception = assertThrows(
                GeocoderServiceException.class,
                () -> geocoderService.getCoordinates(pincode)
        );
        assertTrue(exception.getMessage().contains("Invalid pincode"));
    }

    @Test
    void testGetCoordinates_WhenHttpServerError_ShouldThrowGeocoderServiceException() {
        String pincode = "500030";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        GeocoderServiceException exception = assertThrows(
                GeocoderServiceException.class,
                () -> geocoderService.getCoordinates(pincode)
        );
        assertTrue(exception.getMessage().contains("Geocoder API service unavailable"));
    }

    @Test
    void testGetCoordinates_WhenUnexpectedError_ShouldThrowGeocoderServiceException() {
        String pincode = "500030";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CoordinatesBO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(GeocoderServiceException.class, () -> geocoderService.getCoordinates(pincode));
    }
}


