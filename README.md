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
  
  TODO

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

### Busines logic

Though not explicitly stated in the business logic, I have assumed that the number of infant tickets must not exceed the number of adult tickets, due to the statement 'They [infants] will be sitting on an Adult's lap'.

I have also assumed that the 20 ticket limit is fixed and that infant and adult tickets count towards this total despite only taking up one seat, i.e., a request for 20 adult tickets and 1 infant ticket will fail.

### Thirdparty code

TODO - assuming returns `true` in real world scenario

## Approach

TODO (functional style, code coverage, TDD on helper function unit tests but not handlers etc.)

## 🧐 Challenges 🧐

### Time constraints

The main challenge for me was a lack of time due to my personal circumstances. Though given seven days to complete this, I have only had three (work) days to complete this due to upcoming caring responsibilities. Although this is plenty of time to satisfy the requirements of the task, it meant I had to prioritise. The following list is what I deemed essential to demonstrate:

* satisfy the criteria of the test
* demonstrate my ability to construct a full functioning `express` application
* demonstrate my preferred functional coding style
* demonstrate an ability to write unit and end to end tests

Things that I would have preferred to achieve but had to deprioritise:

* full test coverage (I have tried to show *how* I would test different components, such as helper functions, services and handlers, without duplicating the effort of testing all of them)
* bespoke error handling

### Rewriting to ES Modules

I had written my skeleton `express` app using `CommonJS` (e.g., `const xyz = require('./xyz')`). However, when I came to consume the `thirdparty` services, which cannot be altered as per the instructions, I realised they were exported as `ES Modules`. While it was an easy change to refactor my code to use modules, it was unnecessary time spent. If I was to do this exercise again I would have made sure to double check the configuration of the `thirdparty` modules, especially as they are part of the codebase rather than externally consumed modules.

## Alternative proposal

TODO - lambda fronted by API gateway