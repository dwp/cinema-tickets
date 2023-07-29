package uk.gov.dwp.uc.pairtest.domain;

/**
 * <p>
 * Immutable Object used to create a request for ticket purchases.
 * </p>
 *
 * @since   1.0.0
 *
 * @author  Hansaka Weerasingha
 *
 * @version $Id$
 */
public class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(final Type type, final int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(20.0),
        CHILD(10.0),
        INFANT(0);

        private double price;

        /**
         * The default constructor which takes ticket price as an input.
         *
         * @param price Ticket price
         */
        Type(final double price) {
            this.price = price;
        }

        /**
         * Getter for price.
         *
         * @return the price
         */
        public double getPrice() {
            return price;
        }

        /**
         * Check if the ticket type is an adult.
         *
         * @return True if ticket type is Adult
         */
        public boolean isAdult() {
            return this == ADULT;
        }

        /**
         * Check if a seat is required.
         *
         * @return True if a seat is required
         */
        public boolean isSeatRequired() {
            return this == INFANT ? false : true;
        }
    }

}
