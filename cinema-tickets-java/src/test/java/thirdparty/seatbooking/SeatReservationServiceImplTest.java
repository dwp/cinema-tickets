package thirdparty.seatbooking;

import org.junit.jupiter.api.Test;

/**
 * Minimal test to achieve coverage for SeatReservationServiceImpl.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
public class SeatReservationServiceImplTest {
    @Test
    void testReserveSeat_noop() {
        new SeatReservationServiceImpl().reserveSeat(101L, 3);
    }
}
