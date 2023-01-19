### ticket-booking
A simple NodeJS module to handle ticket booking

To begin - clone the repo and run `npm install` to install required dependencies

NOTE - REQUIRES NODE VERSION ^16.15.1

To run unit tests - `npm test`

Throws InvalidPurchaseException for:
 - request to book tickets for an invalid account number (including zero and negative integers)
 - request to book tickets without an adult
 - request to book more than 20 tickets in 1 transaction
 - booking requests containing a number of infants more than available adult laps
 - Failure in calls to external payment or seat reservation services

Throws TypeError for:
 - incorrectly formed number of tickets (not an integer)
 - incorrectly formed number of seats (not an integer)