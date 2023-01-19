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

## API
A simple NodeJS/Express API has been included for testing purposes
To run API - `npm start` then 
- run the following command to test success
```
curl --location --request POST 'http://localhost:8080' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountid": 123,
    "ticketRequests": [
        {
            "ticketType": "ADULT",
            "noOfTickets": 1
        }
    ]
}'
```

- run the following command to view error response (make changes to the data-raw parameters to visualise error scenarios)

```
curl --location --request POST 'http://localhost:8080' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountid": "MYACC",
    "ticketRequests": [
        {
            "ticketType": "ADULT",
            "noOfTickets": 1
        }
    ]


}'
```