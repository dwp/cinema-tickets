const infantNotGreaterThanAdult = (data) => {
    const { ticketTypeRequests } = data;
    const { Adult: adult, Infant: infant } = ticketTypeRequests;

    const valid = adult >= infant;

    return {
        valid,
        error: valid ? null : 'There must be at least one Adult ticket per Infant ticket',
    }
}

const maxTwentyTicketsAllowed = (data) => {
    const { ticketTypeRequests } = data;
    const totalTickets = Object.values(ticketTypeRequests).reduce((prev, curr) => prev + curr, 0);

    const valid = totalTickets <= 20;

    return {
        valid,
        error: valid ? null : 'Cannot purchase more than 20 tickets at once',
    }
}

export default [
    infantNotGreaterThanAdult,
    maxTwentyTicketsAllowed,
];
