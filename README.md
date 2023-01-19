### ticket-booking
A simple NodeJS module to handle ticket booking

To begin - clone the repo and run `npm install` to install required dependencies

NOTE - REQUIRES NODE VERSION ^16.15.1

To run unit tests - `npm test`

SOME NOTES HAVE BEEN LEFT IN FOR DISCUSSION ON UNIT TESTS AS THEY COVER SCENARIOS THAT THE 3RD PARTY CODE ALLOWS THROUGH THAT WOULD VIOLATE THE TEST LOGIC

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

## Containerisation
A Dockerfile has been provided in order to run a containerised version of the application
To run, use the following commands:
- `docker build . -t <name>`
- `docker run -p 5020:8080 -d <name>`

And test by amending the cURL requests given above to use port 5020