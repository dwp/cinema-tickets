package uk.gov.dwp.uc.pairtest.domain;

import java.util.EnumMap;

/**
 * Immutable Object
 */

public class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;
    private final EnumMap<Type, Integer> ticketTypePriceMap;

    private final static int ADULT_TICKET_PRICE = 20;
    private final static int CHILD_TICKET_PRICE = 10;
    private final static int INFANT_TICKET_PRICE = 0;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
        this.ticketTypePriceMap = createTicketTypePriceMap();
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public int getTotalPrice() {
        return ticketTypePriceMap.get(type) * noOfTickets;
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

    public static EnumMap<Type, Integer> createTicketTypePriceMap() {
        EnumMap<Type, Integer> ticketTypePriceMap = new EnumMap<>(Type.class);
        ticketTypePriceMap.put(Type.ADULT, ADULT_TICKET_PRICE);
        ticketTypePriceMap.put(Type.CHILD, CHILD_TICKET_PRICE);
        ticketTypePriceMap.put(Type.INFANT, INFANT_TICKET_PRICE);
        return ticketTypePriceMap;
    }
}
