export default class InvalidPurchaseException extends Error {
    constructor(message) {
        super(message)  
        // Logging has been added in the calling functions
        // to provide meaningful logs
    }
}
