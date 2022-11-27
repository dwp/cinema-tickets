export default class InvalidPurchaseException extends Error {
  constructor(subType, message) {
    super(message);
    this.subType = subType;
    this.name = "InvalidPurchaseException";
  }
}
