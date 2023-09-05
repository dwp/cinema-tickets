import { MAX_NUMBER_TICKETS } from './Constants.js';

export const INVALID_ACCOUNT_ID = 'Account ID must be greater than 0';
export const TOO_MANY_TICKETS = `Total number of tickets must be less than ${MAX_NUMBER_TICKETS}`;
export const NO_ADULT_TICKET = 'Cannot purchase child or infant tickets without purchasing an adult ticket';
export const MORE_INFANTS_THAN_ADULTS = 'Cannot purchase more infant tickets than adult tickets';
