package com.sreeram.weather.info.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeocoderServiceExceptionTest {

    @Test
    void testConstructor_WithMessage_ShouldCreateException() {
        String message = "Geocoder service error occurred";

        GeocoderServiceException exception = new GeocoderServiceException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Geocoder service error occurred";
        Throwable cause = new RuntimeException("Root cause");

        GeocoderServiceException exception = new GeocoderServiceException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Root cause", exception.getCause().getMessage());
    }

    @Test
    void testException_IsRuntimeException_ShouldBeUnchecked() {
        GeocoderServiceException exception = new GeocoderServiceException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testException_CanBeThrown_ShouldPropagate() {
        assertThrows(GeocoderServiceException.class, () -> {
            throw new GeocoderServiceException("Test exception");
        });
    }

    @Test
    void testException_WithNullMessage_ShouldStillCreate() {
        GeocoderServiceException exception = new GeocoderServiceException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testException_WithNullCause_ShouldStillCreate() {
        GeocoderServiceException exception = new GeocoderServiceException("Message", null);

        assertNotNull(exception);
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testException_WithChainedCause_ShouldPreserveCauseChain() {
        Throwable rootCause = new IllegalArgumentException("Invalid pincode");
        Throwable intermediateCause = new RuntimeException("API error", rootCause);
        String message = "Geocoder service error";

        GeocoderServiceException exception = new GeocoderServiceException(message, intermediateCause);

        assertEquals(message, exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    void testException_GetStackTrace_ShouldNotBeNull() {
        GeocoderServiceException exception = new GeocoderServiceException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testException_WithDetailedMessage_ShouldStoreCompletely() {
        String detailedMessage = "Failed to geocode pincode 500030: " +
                "API returned 404 Not Found. The pincode may be invalid or not in the database.";

        GeocoderServiceException exception = new GeocoderServiceException(detailedMessage);

        assertEquals(detailedMessage, exception.getMessage());
    }

    @Test
    void testException_MultipleInstances_ShouldBeIndependent() {
        GeocoderServiceException exception1 = new GeocoderServiceException("Error 1");
        GeocoderServiceException exception2 = new GeocoderServiceException("Error 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }
}

