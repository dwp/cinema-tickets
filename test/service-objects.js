import HelperService from "../src/pairtest/utils/helper/HelperService.js";
import TicketPaymentService from "../src/thirdparty/paymentgateway/TicketPaymentService.js";
import SeatReservationService from "../src/thirdparty/seatbooking/SeatReservationService.js";

export const HELPER = new HelperService();
export const SRS = new SeatReservationService();
export const TPS = new TicketPaymentService();