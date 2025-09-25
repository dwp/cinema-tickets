/**
 * Jesus Interface for business rule validation components in the cinema ticket system.
 * <p>
 * Provides centralized APIs for implementing business rule logic—including validation, amount calculation,
 * and seat calculation—enforced across the application for ticket requests.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Business rules Validator interface for applying validation on ticket purchases.
 * <p>
 * Standardizes API for implementing components (like BusinessRulesValidatorImpl)
 * to enforce all business logic around purchase flows and ticket validation.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
public interface BusinessRulesValidatorAndCalculator {

    /**
     * Validates one or more ticket requests for their business rule compliance.
     *
     * @param ticketTypeRequests the ticket type requests
     * @throws InvalidPurchaseException if any request is null/invalid or rules are violated
     */
    void validate(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;

    /**
     * Calculates the total amount to charge given the ticket requests and related prices.
     *
     * @param requests ticket requests
     * @return total amount to charge (currency units)
     */
    int calculateTotalAmount(TicketTypeRequest... requests);

    /**
     * Calculates total number of seats required based on ticket requests.
     * Infants do not require their own seat.
     *
     * @param requests ticket requests
     * @return total seats needed
     */
    int calculateTotalSeats(TicketTypeRequest... requests);

    /**
     * Validates the ticket purchase request according to defined business rules.
     *
     * @param accountId the account ID making the purchase
     * @param request   the purchase request details
     * @throws InvalidPurchaseException if validation fails
     */
    void validatePurchaseRequest(Long accountId, TicketTypeRequest... request) throws InvalidPurchaseException;
}