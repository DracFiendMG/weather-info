package com.sreeram.weather.info.to;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponse_WithValidParameters_ShouldCreateInstance() {
        String message = "Test Error";
        String details = "This is a test error";
        int status = 400;

        ErrorResponse errorResponse = new ErrorResponse(message, details, status);

        assertNotNull(errorResponse);
        assertEquals(message, errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
        assertEquals(status, errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testErrorResponse_TimestampIsSet_ShouldBeRecent() {
        String message = "Test Error";
        String details = "Test Details";
        int status = 500;
        LocalDateTime before = LocalDateTime.now();

        ErrorResponse errorResponse = new ErrorResponse(message, details, status);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getTimestamp().isAfter(before.minusSeconds(1)));
        assertTrue(errorResponse.getTimestamp().isBefore(after.plusSeconds(1)));
    }

    @Test
    void testSetMessage_ShouldUpdateMessage() {
        ErrorResponse errorResponse = new ErrorResponse("Original", "Details", 400);
        String newMessage = "Updated Message";

        errorResponse.setMessage(newMessage);

        assertEquals(newMessage, errorResponse.getMessage());
    }

    @Test
    void testSetDetails_ShouldUpdateDetails() {
        ErrorResponse errorResponse = new ErrorResponse("Message", "Original Details", 400);
        String newDetails = "Updated Details";

        errorResponse.setDetails(newDetails);

        assertEquals(newDetails, errorResponse.getDetails());
    }

    @Test
    void testSetStatus_ShouldUpdateStatus() {
        ErrorResponse errorResponse = new ErrorResponse("Message", "Details", 400);
        int newStatus = 500;

        errorResponse.setStatus(newStatus);

        assertEquals(newStatus, errorResponse.getStatus());
    }

    @Test
    void testSetTimestamp_ShouldUpdateTimestamp() {
        ErrorResponse errorResponse = new ErrorResponse("Message", "Details", 400);
        LocalDateTime newTimestamp = LocalDateTime.of(2026, 2, 12, 10, 30);

        errorResponse.setTimestamp(newTimestamp);

        assertEquals(newTimestamp, errorResponse.getTimestamp());
    }

    @Test
    void testErrorResponse_WithNullMessage_ShouldStillCreateInstance() {
        ErrorResponse errorResponse = new ErrorResponse(null, "Details", 400);

        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
        assertEquals("Details", errorResponse.getDetails());
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void testErrorResponse_WithNullDetails_ShouldStillCreateInstance() {
        ErrorResponse errorResponse = new ErrorResponse("Message", null, 400);

        assertNotNull(errorResponse);
        assertEquals("Message", errorResponse.getMessage());
        assertNull(errorResponse.getDetails());
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void testErrorResponse_WithDifferentStatusCodes_ShouldHandleAll() {
        int[] statusCodes = {200, 400, 401, 403, 404, 500, 503};

        for (int status : statusCodes) {
            ErrorResponse errorResponse = new ErrorResponse("Message", "Details", status);
            assertEquals(status, errorResponse.getStatus());
        }
    }

    @Test
    void testErrorResponse_WithLongMessage_ShouldStoreCompletely() {
        String longMessage = "This is a very long error message that contains multiple sentences. " +
                "It should be stored completely without truncation. " +
                "This tests that the ErrorResponse can handle lengthy messages.";
        String details = "Details";
        int status = 400;

        ErrorResponse errorResponse = new ErrorResponse(longMessage, details, status);

        assertEquals(longMessage, errorResponse.getMessage());
        assertEquals(longMessage.length(), errorResponse.getMessage().length());
    }

    @Test
    void testErrorResponse_WithSpecialCharacters_ShouldHandleCorrectly() {
        String message = "Error with special chars: @#$%^&*()";
        String details = "Details with unicode: \u00A9 \u00AE \u2122";
        int status = 400;

        ErrorResponse errorResponse = new ErrorResponse(message, details, status);

        assertEquals(message, errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void testErrorResponse_MultipleInstances_ShouldBeIndependent() {
        ErrorResponse error1 = new ErrorResponse("Error 1", "Details 1", 400);
        ErrorResponse error2 = new ErrorResponse("Error 2", "Details 2", 500);

        assertNotEquals(error1.getMessage(), error2.getMessage());
        assertNotEquals(error1.getDetails(), error2.getDetails());
        assertNotEquals(error1.getStatus(), error2.getStatus());
    }
}

