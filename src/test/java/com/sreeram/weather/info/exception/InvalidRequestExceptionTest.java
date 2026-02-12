package com.sreeram.weather.info.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidRequestExceptionTest {

    @Test
    void testConstructor_WithMessage_ShouldCreateException() {
        String message = "Invalid request parameters";

        InvalidRequestException exception = new InvalidRequestException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_WithMessageAndCause_ShouldCreateException() {
        String message = "Invalid request parameters";
        Throwable cause = new IllegalArgumentException("Validation failed");

        InvalidRequestException exception = new InvalidRequestException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Validation failed", exception.getCause().getMessage());
    }

    @Test
    void testException_IsRuntimeException_ShouldBeUnchecked() {
        InvalidRequestException exception = new InvalidRequestException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testException_CanBeThrown_ShouldPropagate() {
        assertThrows(InvalidRequestException.class, () -> {
            throw new InvalidRequestException("Test exception");
        });
    }

    @Test
    void testException_WithNullMessage_ShouldStillCreate() {
        InvalidRequestException exception = new InvalidRequestException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testException_WithNullCause_ShouldStillCreate() {
        InvalidRequestException exception = new InvalidRequestException("Message", null);

        assertNotNull(exception);
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testException_WithValidationMessage_ShouldStoreProperly() {
        String message = "Pincode cannot be null or empty";

        InvalidRequestException exception = new InvalidRequestException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testException_WithChainedCause_ShouldPreserveCauseChain() {
        Throwable rootCause = new NumberFormatException("Invalid number format");
        Throwable intermediateCause = new IllegalArgumentException("Bad input", rootCause);
        String message = "Invalid request";

        InvalidRequestException exception = new InvalidRequestException(message, intermediateCause);

        assertEquals(message, exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    void testException_GetStackTrace_ShouldNotBeNull() {
        InvalidRequestException exception = new InvalidRequestException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testException_WithDetailedValidationMessage_ShouldStoreCompletely() {
        String detailedMessage = "Invalid pincode format. Expected 5-6 digits, but got: 'abc123'";

        InvalidRequestException exception = new InvalidRequestException(detailedMessage);

        assertEquals(detailedMessage, exception.getMessage());
    }

    @Test
    void testException_MultipleInstances_ShouldBeIndependent() {
        InvalidRequestException exception1 = new InvalidRequestException("Error 1");
        InvalidRequestException exception2 = new InvalidRequestException("Error 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }

    @Test
    void testException_ForDifferentValidationErrors_ShouldHandleAll() {
        String[] validationErrors = {
                "Pincode cannot be null",
                "Date cannot be null",
                "Invalid pincode format",
                "Pincode must be 5-6 digits"
        };

        for (String error : validationErrors) {
            InvalidRequestException exception = new InvalidRequestException(error);
            assertEquals(error, exception.getMessage());
        }
    }
}

