# DWP Code Test
## Testing

This implementation was written following test driven development (TDD) to ensure all business rules were met.
This was particularly important for the `TicketValidator` utility class, to ensure all edge cases and criteria were correctly achieved.

The `mocha` framework has been used for test execution, along with the `chai` and `sinon-chai` assertion libraries.
`sinon` has been used to test that calls to the third-party APIs for seat booking and purchasing were made correctly.

Run the following command to execute the unit tests:

`npm run test`

## Code quality

ESLint is used to ensure that code is written to a good standard. I have used the DWP style guides (`"@dwp/eslint-config-base` and `@dwp/eslint-config-mocha`).
The lint command can be executed using `npm run lint`. This mimics one of the code quality jobs that would take place in a CI/CD pipeline to ensure that
code matched the departmental standards.
