package uk.gov.dwp.uc.pairtest;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.SpringContextHolder;
import uk.gov.dwp.uc.pairtest.validator.BusinessRulesValidatorAndCalculator;

/**
 * Jesus TicketServiceImpl.java
 * <p>
 * Primary service for processing ticket purchase requests, executing validation, payment, and seat reservation flows.
 * Follows strong error handling and single-responsibility for improved readability and maintainability.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Service
public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
	
	/**
     * Orchestrates the end-to-end ticket purchase transaction, including validation, payment, and seat reservation.
     * Delegates detailed steps to internal private helpers.
     *
     * @param accountId            the user account for purchase
     * @param ticketTypeRequests   list of ticket requests
     * @throws InvalidPurchaseException if validation, payment, or reservation fails
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

    	BusinessRulesValidatorAndCalculator validatorAndCalculator = validateRequest(accountId, ticketTypeRequests);
        int totalAmount = calculateTotalAmount(validatorAndCalculator, ticketTypeRequests);
        int totalSeats = calculateTotalSeats(validatorAndCalculator, ticketTypeRequests);

        TicketPaymentService paymentService = getPaymentService();
        SeatReservationService seatService = getSeatReservationService();
        assertRequiredServicesPresent(paymentService, seatService);

        Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);
        LOGGER.info("Amount for the requested tickets is : "+totalAmount);
        LOGGER.info("Total number of seats to reserve are : "+totalSeats);
        makePayment(paymentService, accountId, totalAmount);
        reserveSeats(seatService, accountId, totalSeats);
    }


    /**
     * Validates account ID and ticket requests, delegating to business rules validator bean.
     * 
     * @param accountId
     * @param ticketTypeRequests
     * @return
     */
    private BusinessRulesValidatorAndCalculator validateRequest(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        if (Objects.isNull(accountId) || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid accountId: must be non-null, positive and greater than zero");
        }
        if (Objects.isNull(ticketTypeRequests) || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket request must be provided.");
        }
        BusinessRulesValidatorAndCalculator validator = getBusinessRulesValidator();
        try {
            validator.validatePurchaseRequest(accountId, ticketTypeRequests);
        } catch (Exception ex) {
            throw (ex instanceof InvalidPurchaseException) ? (InvalidPurchaseException) ex :
                    new InvalidPurchaseException("Validation failed: " + ex.getMessage(), ex);
        }
        return validator;
    }

    /**
     * Calculates the total ticket amount using validated business rules.
     * 
     * @param validator
     * @param ticketTypeRequests
     * @return
     */
    private int calculateTotalAmount(BusinessRulesValidatorAndCalculator validator, TicketTypeRequest... ticketTypeRequests) {
        try {
            return validator.calculateTotalAmount(ticketTypeRequests);
        } catch (Exception ex) {
            throw (ex instanceof InvalidPurchaseException) ? (InvalidPurchaseException) ex :
                    new InvalidPurchaseException("Failed to calculate ticket amount: " + ex.getMessage(), ex);
        }
    }

    /**
     * Calculates the total seats required for the ticket requests.
     * 
     * @param validator
     * @param ticketTypeRequests
     * @return
     */
    private int calculateTotalSeats(BusinessRulesValidatorAndCalculator validator, TicketTypeRequest... ticketTypeRequests) {
        try {
            return validator.calculateTotalSeats(ticketTypeRequests);
        } catch (Exception ex) {
            throw (ex instanceof InvalidPurchaseException) ? (InvalidPurchaseException) ex :
                    new InvalidPurchaseException("Failed to calculate seat count: " + ex.getMessage(), ex);
        }
    }

    /**
     * Finds and returns the business rules validator bean.
     * 
     * @return BusinessRulesValidator
     */
    private BusinessRulesValidatorAndCalculator getBusinessRulesValidator() {
        try {
            return SpringContextHolder.getBean(BusinessRulesValidatorAndCalculator.class);
        } catch (Exception e) {
            throw new InvalidPurchaseException("Failed to load business rules validator bean", e);
        }
    }

    /**
     * Returns TicketPaymentService bean from context.
     * 
     * @return TicketPaymentService
     */
    private TicketPaymentService getPaymentService() {
        try {
            return SpringContextHolder.getBean(TicketPaymentService.class);
        } catch (Exception e) {
            throw new InvalidPurchaseException("Failed to load TicketPaymentService bean", e);
        }
    }

    /**
     * Returns SeatReservationService bean from context.
     * 
     * @return SeatReservationService
     */
    
    private SeatReservationService getSeatReservationService() {
        try {
            return SpringContextHolder.getBean(SeatReservationService.class);
        } catch (Exception e) {
            throw new InvalidPurchaseException("Failed to load SeatReservationService bean", e);
        }
    }

    /**
     * Ensures all required services are loaded properly.
     * 
     * @param paymentService
     * @param seatService
     */
    private void assertRequiredServicesPresent(TicketPaymentService paymentService, SeatReservationService seatService) {
        if (Objects.isNull(paymentService) || Objects.isNull(seatService)) {
            throw new InvalidPurchaseException("Required services are not available in Spring context.");
        }
    }

    /**
     * Attempts to make a payment, throwing an informative exception on failure.
     * 
     * @param paymentService
     * @param accountId
     * @param totalAmount
     */
    private void makePayment(TicketPaymentService paymentService, Long accountId, int totalAmount) {
        try {
            paymentService.makePayment(accountId, totalAmount);
        } catch (Exception ex) {
            throw new InvalidPurchaseException("Payment failed: " + ex.getMessage(), ex);
        }
    }

    /**
     * Attempts to reserve seats, throwing an informative exception on failure.
     * 
     * @param seatService
     * @param accountId
     * @param totalSeats
     */
    private void reserveSeats(SeatReservationService seatService, Long accountId, int totalSeats) {
        try {
            seatService.reserveSeat(accountId, totalSeats);
        } catch (Exception ex) {
            throw new InvalidPurchaseException("Seat reservation failed: " + ex.getMessage(), ex);
        }
    }
}
