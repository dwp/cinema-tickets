package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTypeRequestTest {
	@Test
	void testTicketTypeRequest_NotNullType() {
	    TicketTypeRequest TypeReq = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
	    assertEquals(TicketTypeRequest.Type.ADULT, TypeReq.getType());
	    assertEquals(1, TypeReq.getNoOfTickets());
	}
}