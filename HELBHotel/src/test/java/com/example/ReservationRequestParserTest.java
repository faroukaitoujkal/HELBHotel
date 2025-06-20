package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ReservationRequestParserTest {

    @Test
    void testParserDoesNotCrash() {
        try {
            ReservationRequestParser parser = new ReservationRequestParser("src/main/java/com/example/reservation.csv");
            List<ReservationRequest> requests = parser.getAllValidRequests();
            assertNotNull(requests);
        } catch (Exception e) {
            fail("Parsing should not throw exception: " + e.getMessage());
        }
    }
}
