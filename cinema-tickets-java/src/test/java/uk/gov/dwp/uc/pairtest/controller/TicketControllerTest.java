//Jesus
package uk.gov.dwp.uc.pairtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

/**
 * Jesus TicketControllerTest.java
 * <p>
 * Unit tests for {@link TicketController}. Uses MockMvc to verify HTTP API contract for ticket purchase
 * requests and exception handling. Disables security filters for simplified controller testing.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
@WebMvcTest(controllers = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TicketService ticketService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void purchaseTickets_success() throws Exception {
        // Use the JSON with correct field names as per POJO ("ticketType", not "type")
        String body = "[{\"noOfTickets\":2,\"type\":\"ADULT\"},{\"noOfTickets\":1,\"type\":\"CHILD\"}]";

        // Setup: doNothing to avoid unexpected error path/interceptor logic
        org.mockito.Mockito.doNothing().when(ticketService).purchaseTickets(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.any());

        mockMvc.perform(post("/tickets?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void purchaseTickets_invalid_throwsHandled() throws Exception {
        TicketTypeRequest[] requests = new TicketTypeRequest[0];
        String body = MAPPER.writeValueAsString(requests);
        doThrow(new InvalidPurchaseException("Invalid"))
                .when(ticketService).purchaseTickets(1L, requests);

        mockMvc.perform(post("/tickets?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest()); // adjust if your handler maps to different status code
    }
}
