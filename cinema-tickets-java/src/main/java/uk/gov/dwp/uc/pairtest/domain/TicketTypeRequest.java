package uk.gov.dwp.uc.pairtest.domain;

import java.util.EnumMap;

/**
 * Immutable Object
 */

public class TicketTypeRequest {

  private final int noOfTickets;
  private final Type type;
  private final EnumMap<Type, Integer> ticketTypePriceMap;
  private final EnumMap<Type, Boolean> ticketTypeSeatReservationMap;

  private final static int ADULT_TICKET_PRICE = 20;
  private final static int CHILD_TICKET_PRICE = 10;
  private final static int INFANT_TICKET_PRICE = 0;

  private final static boolean ADULT_SEAT_RESERVATION = true;
  private final static boolean CHILD_SEAT_RESERVATION = true;
  private final static boolean INFANT_SEAT_RESERVATION = false;

  public TicketTypeRequest(Type type, int noOfTickets) {
    this.type = type;
    this.noOfTickets = noOfTickets;
    this.ticketTypePriceMap = createTicketTypePriceMap();
    this.ticketTypeSeatReservationMap = createTicketTypeSeatReservationMap();
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

  public int getTotalSeatsToReserve() {
    return ticketTypeSeatReservationMap.get(type) ? noOfTickets : 0;
  }

  public enum Type {
    ADULT, CHILD, INFANT
  }

  public static EnumMap<Type, Integer> createTicketTypePriceMap() {
    EnumMap<Type, Integer> ticketTypePriceMap = new EnumMap<>(Type.class);
    ticketTypePriceMap.put(Type.ADULT, ADULT_TICKET_PRICE);
    ticketTypePriceMap.put(Type.CHILD, CHILD_TICKET_PRICE);
    ticketTypePriceMap.put(Type.INFANT, INFANT_TICKET_PRICE);
    return ticketTypePriceMap;
  }

  public static EnumMap<Type, Boolean> createTicketTypeSeatReservationMap() {
    EnumMap<Type, Boolean> ticketTypeSeatReservationMap = new EnumMap<>(Type.class);
    ticketTypeSeatReservationMap.put(Type.ADULT, ADULT_SEAT_RESERVATION);
    ticketTypeSeatReservationMap.put(Type.CHILD, CHILD_SEAT_RESERVATION);
    ticketTypeSeatReservationMap.put(Type.INFANT, INFANT_SEAT_RESERVATION);
    return ticketTypeSeatReservationMap;
  }
}
