export default function calculateSeatReservation (ticketRequest) {
    return ticketRequest['ADULT'] + ticketRequest['CHILD']
}