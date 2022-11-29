# Objective

This is a coding exercise which will allow you to demonstrate how you code and your approach to a given problem. 

You will be assessed on: 
- Your ability to write clean, well-tested and reusable code.
- How you have ensured the following business rules are correctly met.

# Business Rules

- There are 3 types of tickets i.e. Infant, Child, and Adult.
- The ticket prices are based on the type of ticket (see table below).
- The ticket purchaser declares how many and what type of tickets they want to buy.
- Multiple tickets can be purchased at any given time.
- Only a maximum of 20 tickets that can be purchased at a time.
- Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.
- Child and Infant tickets cannot be purchased without purchasing an Adult ticket.

|   Ticket Type    |     Price   |
| ---------------- | ----------- |
|    INFANT        |    £0       |
|    CHILD         |    £10      |
|    ADULT         |    £20      |

- There is an existing `TicketPaymentService` responsible for taking payments.
- There is an existing `SeatReservationService` responsible for reserving seats.

## Constraints

- The JavaScript code in the `thirdparty` folder CANNOT be modified.
- The `TicketTypeRequest` SHOULD be an immutable object.

## Assumptions

You can assume:
- All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.
- The `TicketPaymentService` implementation is an external provider with no defects. You do not need to worry about how the actual payment happens.
- The payment will always go through once a payment request has been made to the `TicketPaymentService`.
- The `SeatReservationService` implementation is an external provider with no defects. You do not need to worry about how the seat reservation algorithm works.
- The seat will always be reserved once a reservation request has been made to the `SeatReservationService`.

## Your Task

Provide a working implementation of a `TicketService` that:
- Considers the above objective, business rules, constraints & assumptions.
- Calculates the correct amount for the requested tickets and makes a payment request to the `TicketPaymentService`.  
- Calculates the correct no of seats to reserve and makes a seat reservation request to the `SeatReservationService`.  
- Rejects any invalid ticket purchase requests. It is up to you to identify what should be deemed as an invalid purchase request.

## Notes for the examiner from the candidate

1. I was debating whether to test Class `TicketService` s private methods by using a getter method to access the private method and make it available within the jest testing suite.
- I have decided not to do this and to instead test the outputs of `TicketService.purchaseTickets()` after consulting the developer community and also in order to keep all Class `TicketService` methods private. Getters are a public method and would violate this constraint.

2. The business rules state that `Infants do not pay for a ticket and are not allocated a seat. They will be sitting on an Adult's lap.`.
- This rule does not state how many infants can sit on an adults lap.
- I have assumed that an infinite number of infants can sit on an adult's lap. Making ticket requests valid for infant's if an adult is also present.
- I have decided this as the number of infant's which can fit on each Adult's lap is not defined. So rather than deciding an arbitrary number of children can sit on each Adult's lap (1, 2, 4???) I have settled on allowing any number of infants if an adult is present.
-If only a finite number of infants can fit on an Adult's lap we could create a Class `TicketService` private method to check that the number of infant's is equal or lesser than the number of adult's.

3. `TicketService.purchaseTickets()` makes requests to `SeatReservationService.reserveSeat()` and `TicketPaymentService.makePayment()` to model ticket seat reservations and purchases respectively..
- However, these methods do not return anything!
- I have returned an object representing a successful request as to allow me to test that `TicketService.purchaseTickets()` functions as expected.
- A more ideal solution would be to have `SeatReservationService.reserveSeat()` and `TicketPaymentService.makePayment()` act like a asynchornous method and return a promise which is reolved on a successful request or rejected if the request fails.

