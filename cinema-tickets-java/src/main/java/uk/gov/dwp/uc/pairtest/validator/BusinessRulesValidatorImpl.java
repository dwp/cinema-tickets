/**
 * Jesus BusinessRulesValidatorImpl.java
 * Implementation of core cinema ticket purchase validations according to defined business rules.
 */
package uk.gov.dwp.uc.pairtest.validator;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.dwp.uc.pairtest.config.TicketProperties;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Implements all central business rules regarding cinema ticket purchases.
 * <ul>
 * <li>Account ID must be provided and > 0.</li>
 * <li>No null or empty ticket requests. No null entries in requests.</li>
 * <li>Total number of tickets must be within allowed range.</li>
 * <li>Child/Infant tickets require at least 1 Adult ticket in same
 * request.</li>
 * <li>Prices are injected from configuration.</li>
 * </ul>
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
public class BusinessRulesValidatorImpl implements BusinessRulesValidatorAndCalculator {
	private final TicketProperties ticketProperties;

	/**
	 * Constructs the validator using provided ticket properties.
	 * 
	 * @param ticketProperties injected ticket price and config.
	 */
	@Autowired
	public BusinessRulesValidatorImpl(TicketProperties ticketProperties) {
		this.ticketProperties = ticketProperties;
	}

	/**
	 * Validates ticket purchase request for business rule violations.
	 * 
	 * @param accountId the account ID making the purchase
	 * @param requests  one or more ticket type requests
	 * @throws InvalidPurchaseException if any rule is violated
	 */
	@Override
	public void validatePurchaseRequest(Long accountId, TicketTypeRequest... requests) {
		if (Objects.isNull(accountId)|| accountId <= 0) {
			throw new InvalidPurchaseException("Invalid accountId: must be non-null and strictly positive");
		}
		if (Objects.isNull(requests) || requests.length == 0) {
			throw new InvalidPurchaseException("At least one ticket request must be provided.");
		}
		assertNoNullTickets(requests);

		// Check seat limits before further rules
		int totalSeats = calculateTotalSeats(requests);
		if (totalSeats > ticketProperties.getMaxTickets()) {
			throw new InvalidPurchaseException(String.format(
					"Cannot purchase more than %d tickets in a single request.", ticketProperties.getMaxTickets()));
		}

		// Accumulate counts by ticket type for business rule checks
		int adultCount = 0;
		int childOrInfantCount = 0;
		for (TicketTypeRequest request : requests) {
			TicketTypeRequest.Type type = request.getType();
			int numberOfTickets = request.getNoOfTickets();

			// Validate type existence in config
			if (!ticketProperties.getPrices().containsKey(type.name())) {
				throw new InvalidPurchaseException("Invalid ticket type: " + type);
			}

			// No zero/negative ticket values
			if (numberOfTickets <= 0) {
				throw new InvalidPurchaseException("Ticket quantity must be greater than zero, for type: " + type);
			}

			// Typewise accumulation
			if (type == TicketTypeRequest.Type.ADULT) {
				adultCount += numberOfTickets;
			} else if (type == TicketTypeRequest.Type.CHILD || type == TicketTypeRequest.Type.INFANT) {
				childOrInfantCount += numberOfTickets;
			}
		}

		/**
		 * If any child/infant tickets are requested, at least one adult ticket is
		 * mandatory. This prevents unsupervised child or infant purchases.
		 */
		if (childOrInfantCount > 0 && adultCount == 0) {
			throw new InvalidPurchaseException(
					"Cannot purchase CHILD or INFANT tickets without at least one ADULT ticket.");
		}

		// Check ticket total and child/infant-adult rule (redundancy/safety
		validate(requests);
	}

	/**
	 * Runs additional validations for individual ticket requests. Enforces max
	 * ticket total and child/infant-adult rule (redundancy/safety).
	 * 
	 * @param requests the individual requests
	 * @throws InvalidPurchaseException if any rule is broken
	 */
	@Override
	public void validate(TicketTypeRequest... requests) throws InvalidPurchaseException {
		if (requests == null || requests.length == 0) {
			throw new InvalidPurchaseException("At least one ticket request must be provided.");
		}
		assertNoNullTickets(requests);
		int totalTickets = Arrays.stream(requests).mapToInt(TicketTypeRequest::getNoOfTickets).sum();

		if (totalTickets > 25) {
			throw new InvalidPurchaseException("Cannot purchase more than 25 tickets");
		}

		boolean hasAdult = Arrays.stream(requests).anyMatch(r -> r.getType() == TicketTypeRequest.Type.ADULT);
		boolean hasChildOrInfant = Arrays.stream(requests)
				.anyMatch(r -> r.getType() == TicketTypeRequest.Type.CHILD
						|| r.getType() == TicketTypeRequest.Type.INFANT);

		// Cross-cutting rule: child/infant always requires adult
		if (hasChildOrInfant && !hasAdult) {
			throw new InvalidPurchaseException("Child or Infant tickets require at least one Adult ticket");
		}
	}

	/**
	 * Calculates the total amount (currency units) for given ticket requests and
	 * config prices.
	 * 
	 * @param requests ticket order requests
	 * @return the computed total amount
	 */
	@Override
	public int calculateTotalAmount(TicketTypeRequest... requests) {
		assertNoNullTickets(requests);
		return Arrays.stream(requests).mapToInt(r -> {
			String type = r.getType().name();
			Integer price = ticketProperties.getPrices().get(type);
			if (Objects.isNull(price)) {
				throw new InvalidPurchaseException("Invalid ticket type or missing price: " + type);
			}
			return price * r.getNoOfTickets();
		}).sum();
	}

	/**
	 * Calculates total physical seats required for purchase. Infants do not require
	 * seats.
	 * 
	 * @param requests ticket requests
	 * @return number of seats needed
	 */
	@Override
	public int calculateTotalSeats(TicketTypeRequest... requests) {
		assertNoNullTickets(requests);
		return Arrays.stream(requests)
				.mapToInt(r -> (r.getType() == TicketTypeRequest.Type.INFANT ? 0 : r.getNoOfTickets())).sum();
	}

	/**
	 * Gets the current configured ticket prices from properties.
	 * 
	 * @return ticket type price map
	 */
	public Map<String, Integer> getTicketPrices() {
		return ticketProperties.getPrices();
	}

	/**
	 * Ensures the ticket array contains no null entries. Prevents
	 * NullPointerException in business logic.
	 * 
	 * @param requests ticket requests
	 * @throws InvalidPurchaseException if any null present
	 */
	private void assertNoNullTickets(TicketTypeRequest... requests) {
		if (Arrays.stream(requests).anyMatch(java.util.Objects::isNull)) {
			throw new InvalidPurchaseException("Ticket request cannot be null.");
		}
	}
}
