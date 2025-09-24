/**
 * Jesus
 */
package uk.gov.dwp.uc.pairtest.service.validator;

/**
 * 
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.BusinessRulesValidatorImpl;

import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(properties = {
        "ticket.prices.ADULT=25",
        "ticket.prices.CHILD=15",
        "ticket.prices.INFANT=0",
        "ticket.max-tickets=25"
})
/**
 * Jesus BusinessRulesValidatorImplTest.java
 * <p>
 * Unit tests for {@link BusinessRulesValidatorImpl}. Covers business rule validation logic and price calculations
 * for ticket purchases, including ticket type constraints, maximum limits, and price combinations.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
public class BusinessRulesValidatorImplTest {

    @Autowired
    private BusinessRulesValidatorImpl businessRulesValidator;

    @Test
    void shouldInjectTicketPricesFromApplicationProperties() {
        Map<String, Integer> prices = businessRulesValidator.getTicketPrices();

        assertThat(prices).isNotNull();
        assertThat(prices)
                .containsEntry("ADULT", 25)
                .containsEntry("CHILD", 15)
                .containsEntry("INFANT", 0);
    }

    @Test
    void testCalculateTotalAmount_WithInvalidType_Throws() {
        TicketTypeRequest bogus = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        Map<String, Integer> prices = new java.util.HashMap<>(businessRulesValidator.getTicketPrices());
        prices.remove("ADULT");
        org.springframework.test.util.ReflectionTestUtils.setField(
                businessRulesValidator, "ticketProperties",
                new uk.gov.dwp.uc.pairtest.config.TicketProperties() {{
                    setPrices(prices);
                }});
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.calculateTotalAmount(bogus));
    }

    // ---- Account ID (Long) validation tests ----
    @Test
    void testValidatePurchaseRequest_accountIdNull() {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(null, req));
    }

    @Test
    void testValidatePurchaseRequest_accountIdZero() {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(0L, req));
    }

    @Test
    void testValidatePurchaseRequest_accountIdNegative() {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(-42L, req));
    }

    @Test
    void testValidatePurchaseRequest_accountIdValidPositiveNoException() {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        // Should not throw
        businessRulesValidator.validatePurchaseRequest(123L, req);
    }
    
    @Test
    void testTicketTypeRequestNull() {
    	assertThrows(InvalidPurchaseException.class,
                () -> businessRulesValidator.validate(null));
    }
    
    // Test: CHILD requires at least one ADULT (cannot relax this rule)
    @Test
    void testBranch_ChildRequiresAdult() {
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, child));
    }

    // Test: INFANT requires at least one ADULT
    @Test
    void testBranch_InfantRequiresAdult() {
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, infant));
    }

    // Test: More than 25 tickets should throw
    @Test
    void testValidate_TooManyTickets() {
        TicketTypeRequest[] requests = java.util.stream.IntStream.range(0, 26)
            .mapToObj(i -> new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1))
            .toArray(TicketTypeRequest[]::new);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validate(requests));
    }

    // Test: Zero or negative number of tickets always throws
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void shouldThrowForZeroOrNegativeNoOfTickets(int invalidCount) {
        TicketTypeRequest req = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, invalidCount);
        InvalidPurchaseException ex = assertThrows(
            InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, req)
        );
        assertThat(ex.getMessage()).containsIgnoringCase("Ticket quantity must be greater than zero, for type");
    }

    @Test
    void shouldThrowForZeroTicketsForChildAndInfant() {
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 0);
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(1L, child));
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(1L, infant));
    }

    @Test
    void shouldThrowForNegativeTicketsForChildAndInfant() {
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, -2);
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, -1);
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(1L, child));
        assertThrows(InvalidPurchaseException.class, () -> businessRulesValidator.validatePurchaseRequest(1L, infant));
    }

    // --------- Price Calculation Combinations Coverage ---------

    @Test
    void testTotalAmount_OneAdult() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThat(businessRulesValidator.calculateTotalAmount(a)).isEqualTo(25);
    }

    @Test
    void testTotalAmount_TwoAdults() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        assertThat(businessRulesValidator.calculateTotalAmount(a)).isEqualTo(50);
    }

    @Test
    void testTotalAmount_AdultAndChild() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        assertThat(businessRulesValidator.calculateTotalAmount(a, c)).isEqualTo(25 + 2 * 15);
    }

    @Test
    void testTotalAmount_AdultAndInfant() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
        assertThat(businessRulesValidator.calculateTotalAmount(a, i)).isEqualTo(2 * 25 + 0);
    }

    @Test
    void testTotalAmount_AdultChildInfant() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThat(businessRulesValidator.calculateTotalAmount(a, c, i)).isEqualTo(25 + 2 * 15 + 0);
    }

    @Test
    void testTotalAmount_MultipleEachType() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        assertThat(businessRulesValidator.calculateTotalAmount(a, c, i)).isEqualTo(4*25 + 3*15 + 0);
    }

    @Test
    void testTotalAmount_OnlyInfantOrOnlyChildThrows() {
        TicketTypeRequest ch = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest inf = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
        // Business rule: these should throw using validatePurchaseRequest, not calculateTotalAmount
        assertThrows(InvalidPurchaseException.class,
                () -> businessRulesValidator.validatePurchaseRequest(1L, ch));
        assertThrows(InvalidPurchaseException.class,
                () -> businessRulesValidator.validatePurchaseRequest(1L, inf));
        // But calculateTotalAmount does not enforce that business rule, only price math:
        assertThat(businessRulesValidator.calculateTotalAmount(ch)).isEqualTo(2 * 15);
        assertThat(businessRulesValidator.calculateTotalAmount(inf)).isEqualTo(0);
    }

    @Test
    void testTotalAmount_LargeValidCombo() {
        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 8);
        TicketTypeRequest i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, -5);
        assertThat(businessRulesValidator.calculateTotalAmount(a, c, i)).isEqualTo(10*25 + 8*15 + 0);
    }
    
    @Test
    void testThrowsWhenRequestsIsNull() {
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validatePurchaseRequest(1L, (TicketTypeRequest[]) null));
    }

    @Test
    void testThrowsWhenRequestsIsEmpty() {
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validatePurchaseRequest(1L));
    }

    @Test
    void testThrowsWhenTotalSeatsExceedMax() {
        TicketTypeRequest[] requests = { new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 100) };
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validatePurchaseRequest(1L, requests));
    }

    @Test
    void testThrowsWhenInvalidTicketType() {
        // Remove a type from prices just for this test if possible
        Map<String, Integer> prices = new java.util.HashMap<>(businessRulesValidator.getTicketPrices());
        prices.remove("CHILD");
        org.springframework.test.util.ReflectionTestUtils.setField(
                businessRulesValidator, "ticketProperties",
                new uk.gov.dwp.uc.pairtest.config.TicketProperties() {{ setPrices(prices); }});
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validatePurchaseRequest(1L, child));
    }

    @Test
    void testThrowsWhenChildOrInfantWithoutAdult() {
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validatePurchaseRequest(1L, child));
    }

    @Test
    void testThrowsWhenNullTicketInRequests() {
        TicketTypeRequest[] requests = { new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1), null };
        assertThrows(InvalidPurchaseException.class, () ->
            businessRulesValidator.validate(requests));
    }
    
    @Test
    void testThrowsWhenTicketTypeIsNotConfigured() {
        Map<String, Integer> prices = new java.util.HashMap<>(businessRulesValidator.getTicketPrices());
        prices.remove("CHILD");
        org.springframework.test.util.ReflectionTestUtils.setField(
                businessRulesValidator, "ticketProperties",
                new uk.gov.dwp.uc.pairtest.config.TicketProperties() {{ setPrices(prices); }});
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, child));
    }

    @Test
    void testValidateWithNullChildOrInfantRequests() {
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validate((TicketTypeRequest[]) null));
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validate()); // empty vararg
    }
    
    @Test
    void testThrowsInvalidPurchaseExceptionWhenTicketTypeIsMissingFromPrices() {
        // Copy and remove a ticket type ("CHILD") from the prices map
        Map<String, Integer> prices = new java.util.HashMap<>(businessRulesValidator.getTicketPrices());
        prices.remove("CHILD");
        org.springframework.test.util.ReflectionTestUtils.setField(
            businessRulesValidator, "ticketProperties",
            new uk.gov.dwp.uc.pairtest.config.TicketProperties() {{ setPrices(prices); }}
        );
        TicketTypeRequest missing = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, missing));
    }
    
    @Test
    void testThrowsWhenChildOrInfantWithoutAdultPerson() {
        // Only child, no adult
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, child));

        // Only infant, no adult
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, infant));

        // Child + infant, still no adult
        TicketTypeRequest[] tickets = {
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, tickets));
    }
    
    @Test
    void testThrowsInvalidPurchaseExceptionWhenTicketTypeNotInPrices() {
        Map<String, Integer> prices = new java.util.HashMap<>(businessRulesValidator.getTicketPrices());
        prices.remove("INFANT");
        org.springframework.test.util.ReflectionTestUtils.setField(
            businessRulesValidator, "ticketProperties",
            new uk.gov.dwp.uc.pairtest.config.TicketProperties() {{ setPrices(prices); }}
        );
        TicketTypeRequest missing = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, missing));
    }

    @Test
    void testThrowsWhenHasChildOrInfantButNoAdult() {
        // Only CHILD, no ADULT
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, child));

        // Only INFANT, no ADULT
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, infant));

        // CHILD + INFANT, still no adult
        TicketTypeRequest[] combo = {
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, combo));
    }
    
    @Test
    void testThrowsWhenHasChildOrInfantButNoAdult1() {
        // Only CHILD, no ADULT
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, child));

        // Only INFANT, no ADULT
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validatePurchaseRequest(1L, infant));

        // CHILD + INFANT, still no adult
        TicketTypeRequest[] combo = {
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };
        assertThrows(InvalidPurchaseException.class,
            () -> businessRulesValidator.validate(combo));
        
    }
}