package com.sreeram.weather.info.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceExceptionTest {

    @Test
    void testConstructor_WithMessage_ShouldCreateException() {
        String message = "Weather service error occurred";

        WeatherServiceException exception = new WeatherServiceException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Weather service error occurred";
        Throwable cause = new RuntimeException("Root cause");

        WeatherServiceException exception = new WeatherServiceException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Root cause", exception.getCause().getMessage());
    }

    @Test
    void testException_IsRuntimeException_ShouldBeUnchecked() {
        WeatherServiceException exception = new WeatherServiceException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testException_CanBeThrown_ShouldPropagate() {
        assertThrows(WeatherServiceException.class, () -> {
            throw new WeatherServiceException("Test exception");
        });
    }

    @Test
    void testException_WithNullMessage_ShouldStillCreate() {
        WeatherServiceException exception = new WeatherServiceException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testException_WithNullCause_ShouldStillCreate() {
        WeatherServiceException exception = new WeatherServiceException("Message", null);

        assertNotNull(exception);
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testException_WithChainedCause_ShouldPreserveCauseChain() {
        Throwable rootCause = new IllegalArgumentException("Root");
        Throwable intermediateCause = new RuntimeException("Intermediate", rootCause);
        String message = "Weather service error";

        WeatherServiceException exception = new WeatherServiceException(message, intermediateCause);

        assertEquals(message, exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    void testException_GetStackTrace_ShouldNotBeNull() {
        WeatherServiceException exception = new WeatherServiceException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testException_WithLongMessage_ShouldStoreCompletely() {
        String longMessage = "This is a very long error message " +
                "that describes a complex weather service failure scenario " +
                "with multiple contributing factors and detailed context";

        WeatherServiceException exception = new WeatherServiceException(longMessage);

        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    void testException_MultipleInstances_ShouldBeIndependent() {
        WeatherServiceException exception1 = new WeatherServiceException("Error 1");
        WeatherServiceException exception2 = new WeatherServiceException("Error 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }
}

