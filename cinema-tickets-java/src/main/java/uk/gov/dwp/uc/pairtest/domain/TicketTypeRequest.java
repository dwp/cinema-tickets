package uk.gov.dwp.uc.pairtest.domain;

/** Immutable Object */
public class TicketTypeRequest {

  private final int noOfTickets;
  private final Type type;

  public TicketTypeRequest(Type type, int noOfTickets) {
    this.type = type;
    this.noOfTickets = noOfTickets;
  }

  public int getNoOfTickets() {
    return noOfTickets;
  }

  public Type getTicketType() {
    return type;
  }

  public int getTotalPrice() {
    return noOfTickets * type.price;
  }

  public int getTotalSeats() {
    return type.seatRequired ? noOfTickets : 0;
  }

  public enum Type {
    INFANT(0, false),
    CHILD(10, true),
    ADULT(20, true);

    private final int price;
    private final boolean seatRequired;

    Type(int price, boolean seatRequired) {
      this.price = price;
      this.seatRequired = seatRequired;
    }
  }
}
