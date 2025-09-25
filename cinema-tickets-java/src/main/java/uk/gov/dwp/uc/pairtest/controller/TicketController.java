/**
 * Jesus TicketController exposes the endpoint for ticket purchasing.
 * Accepts purchase requests via POST, marshals input, and delegates to TicketService.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Governmentt
 */
package uk.gov.dwp.uc.pairtest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

@RestController
@RequestMapping("/tickets")
@CrossOrigin
@Tag(name = "Tickets", description = "Endpoints related to ticket purchasing")
public class TicketController {

    private final TicketService ticketService;

    /**
     * Constructs a TicketController with the given TicketService instance.
     *
     * @param ticketService the injected TicketService implementation
     * @see TicketService
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Handles POST requests for purchasing tickets.
     * Accepts the account ID and one or more ticket type requests in the request body.
     * Delegates purchase handling to the TicketService.
     *
     * @param accountId the account performing the purchase (required)
     * @param ticketTypeRequests the array of ticket type requests as input JSON/XML (required)
     * @throws Exception propagated from TicketService layer on failure
     */
    @Operation(
            summary = "Purchase tickets",
            description = "Allows an account to purchase one or more tickets. Ticket types are configurable via properties file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tickets purchased successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void purchaseTickets(@RequestParam Long accountId, @RequestBody TicketTypeRequest... ticketTypeRequests) throws Exception {
        ticketService.purchaseTickets(accountId, ticketTypeRequests);
    }
}
