import TicketTypeRequest from "../src/pairtest/lib/TicketTypeRequest.js";

export const requestAdult =  new TicketTypeRequest("ADULT", 1);
export const requestChild = new TicketTypeRequest("CHILD", 3);
export const requestInfant =  new TicketTypeRequest("INFANT",1);
export const requestTwenty = new TicketTypeRequest("ADULT", 20);
export const requestTooManyInfants = new TicketTypeRequest("INFANT",15);

export const goodAccountNum = 1200;
export const badAccountNum = "666";
