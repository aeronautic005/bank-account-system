public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super("Transaction amount must be greater than zero.");
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
