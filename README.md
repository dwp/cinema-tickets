# cinema-tickets

This repository is a code test to satisfy the problem defined in [task.md](./TASK.md).

## 🏃 Running the application 🏃

<details>

  <summary>Click to expand running instructions</summary>

  There are two options for running this application locally.

  Prior to both, clone the repo to your machine and open a terminal window in the root of the project.

  ### 💻 NodeJS 💻

  Run the application using `node`.

  #### Prerequisites

  - `node` version `16.11.1` or higher (though this has only been tested on major version `16`)
  - [Postman](https://www.postman.com/company/about-postman/) to run end-to-end tests

  #### Commands

  ```sh
  npm i
  npm start
  ```

  This will run the following commands in order:

  ```sh
  npm run test:lint && npm run test:unit && node src/index.js
  ```

  If either of the test commands fail, the application will not start.

  If you wish to make any changes to the application, hot reloading is enabled by running:

  ```sh
  npm run dev
  ```

  This bypasses the `npm test` commands.

  ### 🐳 Docker 🐳

  #### Prerequisites

  - Docker

  #### Commands

  ```sh
  npm run docker:start
  ```

  ### Verifying

  You can check the application is running by opening another terminal window and hitting:

  ```sh
  curl localhost:3000/healthcheck
  ```

  ### Testing
  
  There are two types of tests included with the repository - unit tests and end-to-end (e2e) tests.
  
  The unit tests run isolated logic tests against each file, while the e2e tests make calls to the service and make assertions against the result.

  The unit tests are written using the `mocha` testing framework and the `chai` assertion library and the e2e tests are in the format of a Postman collection.

  Also included under the `test` scripts are a `test: coverage` and `test: lint` script. The coverage script uses the [c8](https://github.com/bcoe/c8) tool to provide test coverage (I chose `c8` over `nyc` as it seems to be able to handle Module JS better) and the `lint` script uses `standard`. These two commands are included in the `npm test` command and they will run prior to the unit tests.

  #### Unit tests

  To run the unit tests (as well as the linting and coverage scripts), the application does not need to be running. From the root of the directory in the terminal of your choice (having previously run `npm i`), simply run:

  ```sh
  npm test
  ```

  to view a report in the console of the test suit, including a list of all passing tests logically grouped by service and expected behaviour, and a table showing test coverage.

  #### e2e tests

  > Note: the application must be running in order to run the e2e tests. It can be running locally or in Docker, though both cannot be running at the same time as they use the same port.

  To run the e2e tests, [import the collection](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman) into Postman by selecting the `Upload Files` option and uploading the `test/e2e/cinema-tickets.postman_collection.json` file.

  Hover over the `cinema-tickets` collection and click the hamburger menu, then `Run collection`. This opens a `Runner` tab with an option to `Run cinema-tickets`.

  This runs through the Postman collection which covers multiple scenarios, testing for both valid and invalid requests.

</details>

## 🤔 Assumptions 🤔

I have made some assumptions as I go due to the limited brief.

### Request Structure

I have assumed the request is coming from an external service with the following simple shape:

```json
{
    "accountId": 39,
    "ticketsRequested": {
        "adult": 2,
        "child": 1,
        "infant": 1
    }
}
```

### Business logic

Though not explicitly stated in the business logic, I have assumed that the number of infant tickets must not exceed the number of adult tickets, due to the statement 'They [infants] will be sitting on an Adult's lap'.

I have also assumed that the 20 ticket limit is fixed and that infant and adult tickets count towards this total despite only taking up one seat, i.e., a request for 20 adult tickets and 1 infant ticket will fail.

### Thirdparty code

As I cannot change the `thirdparty` code which returns nothing and just throws an error if basic validation fails, I have assumed they would return `true` in a real world scenario and so this is what I return from my `ticketService` if there are no errors thrown. This is obviously oversimplifying things but works for this small application.

## 🧑‍🔬 Approach 🧑‍🔬

### Coding style

I prefer a functional coding style whereby all functions are initialised with any dependencies, then return the function as it is to be called.

As a very basic example, if I was defining a function to lookup a constant from a provided `key`, instead of the following:

```js
import C from './constants'

export default ({ key }) => C[key]
```

I would define it as follows:

```js
export default ({ C }) => ({ key }) => C[key]
```

and instantiate it like so:

```js
import C from './constants'
import initLookupConstant from './lookupConstant.js'
const lookupConstant = ({ C })

// can now call with key
const key = 'Jeff'
const jeffConstant = lookupConstant({ key })
```

Whilst I find that this style is neat and readable (though can take some getting used to), the main benefit is in testing, as you have total control over dependencies and can mock, stub and spy on anything at all without having to use any complicated dependency injection tools.

I also prefer to define function signatures as objects:

```js
const iDontDoThis = (a, b, c)

const iDoThis = ({ a, b, c })
```

I find this makes functions less brittle as you don't have to worry about calling them with parameters in order.

### Code structure

I was working on a very limited timescale but started by building a skeleton `express` application in a functional style, with a `/healthcheck` route.

I then expanded this to add a `/tickets` route with a basic handler, which I then fleshed out to call a `ticketService`, which in turn calls the `thirdparty` 'gateway' services (seat reservation and payment).

The `handler` is injected with initialised helper functions, including a request validation function, and the initialised `ticketService`. The service is initialised with the third party services.

The program flow is as follows:

1. Request made to `/tickets` route
2. Request forwarded to `ticketHandler`
3. Pass request to request validation helper
  1. If validation fails, return list of errors to client
  2. If validation succeeds, call helper functions to calculate total amount to pay and number of seats to reserve
  3. Create formatted object with the above
4. Call `ticketService` with formatted request
5. Call both third party services
  1. If validation fails, throw an error to handler (it should never fail this validation due to the previous validation in the handler)
  2. If validation succeeds, return `true` to handler to allow handler to proceed
6. Service returns result (either `true` or `Error`) to handler
7. Handler returns result to client

### Testing

Due to time constraints I have not attempted to achieved 100% code coverage. As the `index.js` file mainly deals with importing dependencies this is tricky to test in the functional style. I have not tested `src/routes/index.js` or `src/app.js` simply because I have already demonstrated the testing approach in other files and I ran out of time.

For helper functions where the functionality is known and limited I have practiced TDD (writing tests first and then source code to satisfy the tests) but in the interests of getting up and running quickly I didn't fully TDD the handler or service, which were frequently changing. As functionality evolved, I did practice some TDD in these files.

### CI

I have a very basic [Github workflow](https://github.com/aNerdInTheHand/cinema-tickets/actions) that runs on every commit and audits my dependencies and runs the unit tests.

## 🧐 Challenges 🧐

### Time constraints

The main challenge for me was a lack of time due to my personal circumstances. Although I have had time to satisfy the requirements of the task, I probably sacrificed some planning for the sake of completing the task. I hope that I have managed to provide a decent overview of my programming ability.

### Rewriting to ES Modules

I had written my skeleton `express` app using `CommonJS` (e.g., `const xyz = require('./xyz')`). However, when I came to consume the `thirdparty` services, which cannot be altered as per the instructions, I realised they were exported as `ES Modules`. While it was an easy change to refactor my code to use modules, it was unnecessary time spent. If I was to do this exercise again I would have made sure to double check the configuration of the `thirdparty` modules, especially as they are part of the codebase rather than externally consumed modules.

## 💡 What would I change? 💡

### Validation 

If I undertook this test again, the main thing I would have changed is my error handling. This would have been a good use case for middleware in validating every request. As I developed this quickly and with only one relevant route (not counting the `/healthcheck` route) I developed the validation (which throws the errors) as a helper function and have not had time to refactor.

I have also been inconsistent in adding validation to my `calculateSeatsToReserve` and `calculateTotalPayment` helper functions. In theory no invalid requests should make it to these functions so instead of half testing for invalid requests in one function, and not at all in another, I would have had faith in my initial validation and not validated in subsequent helpers as it add unneccesary bulk to the code, especially the test code, and is covered by e2e tests anyway.

### Logging

I would probably have used a third party logger like [bunyan](https://github.com/trentm/node-bunyan) instead of hijacking `console` for my logging!

### Alternative proposal

If time allowed I would have taken steps to show how this could have been a good use case for an AWS lambda function fronted by API Gateway. However, this would have made more sense if the `thirdparty` code had been hosted in external APIs rather than bundled with the code, so I would have only documented how I would have done it rather than actually implementing it.

### Frontend service

If time had been no issue it would have been cool to build a separate frontend service to call this service.