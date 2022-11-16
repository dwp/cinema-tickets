const accountIdGreaterThanZero = (data) => {
  const { accountId } = data;

  const valid = accountId > 0;

  return {
    valid,
    error: valid ? null : 'The account ID provided must be greater than 0',
  };
};

const infantNotGreaterThanAdult = (data) => {
  const { adultTickets, infantTickets } = data;

  const valid = adultTickets >= infantTickets;

  return {
    valid,
    error: valid ? null : 'There must be at least one Adult ticket per Infant ticket',
  };
};

const maxTwentyTicketsAllowed = (data) => {
  const { adultTickets, childTickets, infantTickets } = data;
  const totalTicketsCount = Object.values({ adultTickets, childTickets, infantTickets }).reduce((prev, curr) => prev + curr, 0);

  const valid = totalTicketsCount <= 20;

  return {
    valid,
    error: valid ? null : 'Cannot purchase more than 20 tickets at once',
  };
};

const atLeastOneAdult = (data) => {
  const { adultTickets } = data;

  const valid = adultTickets > 0;

  return {
    valid,
    error: valid ? null : 'At least one adult is required for each booking',
  };
};

export default [
  accountIdGreaterThanZero,
  infantNotGreaterThanAdult,
  maxTwentyTicketsAllowed,
  atLeastOneAdult,
];
