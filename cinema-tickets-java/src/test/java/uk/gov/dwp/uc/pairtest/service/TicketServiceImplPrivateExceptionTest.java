//Jesus
package uk.gov.dwp.uc.pairtest.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.SpringContextHolder;

/**
 * Test to cover the catch block in TicketServiceImpl#getSeatReservationService.
 */
public class TicketServiceImplPrivateExceptionTest {

    @Test
    void getSeatReservationServiceThrowsException() throws Exception {
        TicketServiceImpl service = new TicketServiceImpl();
        try (MockedStatic<SpringContextHolder> ctxStatic = Mockito.mockStatic(SpringContextHolder.class)) {
            ctxStatic.when(() -> SpringContextHolder.getBean(SeatReservationService.class))
                    .thenThrow(new RuntimeException("No bean"));
            InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> {
                    Method method = TicketServiceImpl.class.getDeclaredMethod("getSeatReservationService");
                    method.setAccessible(true);
                    method.invoke(service);
                }
            );
            Throwable cause = ex.getCause();
            assertTrue(cause instanceof InvalidPurchaseException);
            assertTrue(cause.getMessage().contains("Failed to load SeatReservationService bean"));
        }
    }
}
