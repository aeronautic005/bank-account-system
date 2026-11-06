public class InsufficientFundsException extends Exception {
    private double shortfall;

    public InsufficientFundsException(String message) {
        super(message);
        this.shortfall = 0.0;
    }

    public InsufficientFundsException(String message, double shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public double getShortfall() {
        return shortfall;
    }
}
