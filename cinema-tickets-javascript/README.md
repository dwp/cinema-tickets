# DWP Code Test
## Running the application
### Application build

This project uses [babel](https://babeljs.io/) to compile ES6 code into compatible JavaScript code for the Express server.
The application build can be performed using the following command:

`npm run build`

### Using Docker

The ticket application is runnable as a REST API. The API has been created using Express, which provides a simple solution for creating REST apps.
The application can be run with the following commands:

```shell
docker build . --build-arg PORT=3000 -t cinema-tickets
docker run -d -p 3000:3000 -e PORT=3000 cinema-tickets
```

This will start the application server on port 3000.

### Without Docker

The application can also be executed without Docker using `npm`. To do so, run the following commands:

```shell
npm run build
npm start
```

This will start the application server on the default port (3000).

### Health Check

To confirm the application is running, execute the following command using curl:

```shell
curl http://localhost:3000/ping
```

This should return `pong` in the terminal. The `/ping` route is used as a health check, to confirm the server is running correctly.

### Sending requests

The cinema-tickets API would be used as the backend service for a user interface that would let a user enter their account ID and make their ticket requests.
This information would be transformed into a JSON structure, which would then be sent as a POST request to the `/tickets/purchase` endpoint of the API.
An example JSON request is shown below:

```json
{
  "accountId": 10,
  "tickets": [
    {
      "type": "ADULT",
      "count": 2
    }
  ]
}
```

With the service running, execute the following command to send a sample request to the service:

```shell
curl -X POST http://localhost:3000/tickets/purchase -H 'Content-Type: application/json' -d '{"accountId":10,"tickets":[{"type":"ADULT","count":5}]}' -i
```

This should return a response code of 204 (`No Content`) for the successful request:

```
HTTP/1.1 204 No Content
X-Powered-By: Express
ETag: ...
Date: Wed, 06 Sep 2023 14:39:47 GMT
Connection: keep-alive
Keep-Alive: timeout=5
```

The following sends an invalid request to the server:

```shell
curl -X POST http://localhost:3000/tickets/purchase -H 'Content-Type: application/json' -d '{"accountId":0,"tickets":[{"type":"ADULT","count":5}]}' -i
```

This should return the following response:

```
HTTP/1.1 400 Bad Request
X-Powered-By: Express
Content-Type: text/html; charset=utf-8
Content-Length: 33
ETag: ...
Date: Wed, 06 Sep 2023 14:41:19 GMT
Connection: keep-alive
Keep-Alive: timeout=5

Account ID must be greater than 0
```

Other requests that are invalid for other reasons will return different error messages in the response body, which would be used to inform the user why their request was invalid.

## Testing

This implementation was written following test driven development (TDD) to ensure all business rules were met.
This was particularly important for the `TicketValidator` utility class, to ensure all edge cases and criteria were correctly achieved.

The [mocha][mocha] framework has been used for test execution, along with the [chai][chai] and [sinon-chai][sinon-chai] assertion libraries.
[sinon][sinon] has been used to test that calls to the third-party APIs for seat booking and purchasing were made correctly.
[chai-http][chai-http] has been used to test the server, to assert that correct responses and response codes are returned for different request types.

Run the following command to execute all tests:

`npm run test`

[mocha]: https://mochajs.org/
[chai]: https://www.chaijs.com/
[sinon-chai]: https://www.chaijs.com/plugins/sinon-chai/
[sinon]: https://sinonjs.org/
[chai-http]: https://www.chaijs.com/plugins/chai-http/

## Code quality

ESLint is used to ensure that code is written to a good standard. I have used the DWP style guides (`"@dwp/eslint-config-base` and `@dwp/eslint-config-mocha`).
The lint command can be executed using `npm run lint`. This mimics one of the code quality jobs that would take place in a CI/CD pipeline to ensure that
code matched the departmental standards.
