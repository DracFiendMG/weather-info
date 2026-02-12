package com.sreeram.weather.info.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataNotFoundExceptionTest {

    @Test
    void testConstructor_WithMessage_ShouldCreateException() {
        String message = "Data not found in database";

        DataNotFoundException exception = new DataNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Data not found in database";
        Throwable cause = new RuntimeException("Database query returned no results");

        DataNotFoundException exception = new DataNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Database query returned no results", exception.getCause().getMessage());
    }

    @Test
    void testException_IsRuntimeException_ShouldBeUnchecked() {
        DataNotFoundException exception = new DataNotFoundException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testException_CanBeThrown_ShouldPropagate() {
        assertThrows(DataNotFoundException.class, () -> {
            throw new DataNotFoundException("Test exception");
        });
    }

    @Test
    void testException_WithNullMessage_ShouldStillCreate() {
        DataNotFoundException exception = new DataNotFoundException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testException_WithNullCause_ShouldStillCreate() {
        DataNotFoundException exception = new DataNotFoundException("Message", null);

        assertNotNull(exception);
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testException_WithSpecificDataMessage_ShouldStoreProperly() {
        String message = "Weather data not found for pincode 500030 and date 2026-02-12";

        DataNotFoundException exception = new DataNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testException_WithChainedCause_ShouldPreserveCauseChain() {
        Throwable rootCause = new RuntimeException("Connection timeout");
        Throwable intermediateCause = new RuntimeException("Database error", rootCause);
        String message = "Data not found";

        DataNotFoundException exception = new DataNotFoundException(message, intermediateCause);

        assertEquals(message, exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    void testException_GetStackTrace_ShouldNotBeNull() {
        DataNotFoundException exception = new DataNotFoundException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testException_WithDetailedMessage_ShouldStoreCompletely() {
        String detailedMessage = "No weather records found for the specified criteria. " +
                "Pincode: 500030, Date: 2026-02-12. Please try fetching from the API.";

        DataNotFoundException exception = new DataNotFoundException(detailedMessage);

        assertEquals(detailedMessage, exception.getMessage());
    }

    @Test
    void testException_MultipleInstances_ShouldBeIndependent() {
        DataNotFoundException exception1 = new DataNotFoundException("Error 1");
        DataNotFoundException exception2 = new DataNotFoundException("Error 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }

    @Test
    void testException_ForDifferentDataTypes_ShouldHandleAll() {
        String[] dataNotFoundMessages = {
                "Weather data not found",
                "Coordinates data not found",
                "No cached data available",
                "Record does not exist"
        };

        for (String message : dataNotFoundMessages) {
            DataNotFoundException exception = new DataNotFoundException(message);
            assertEquals(message, exception.getMessage());
        }
    }
}

