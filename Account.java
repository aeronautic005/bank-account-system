public class Account {
    private String accountNumber;
    private String accountHolder;
    protected double balance;

    public Account(String accountNumber, String accountHolder, double initialBalance) throws InvalidAmountException {
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative: " + initialBalance);
        }
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero. Given: " + amount);
        }
        this.balance += amount;
        System.out.printf("[SUCCESS] Deposited $%.2f. New Balance: $%.2f%n", amount, this.balance);
    }

    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero. Given: " + amount);
        }
        if (amount > this.balance) {
            double shortfall = amount - this.balance;
            throw new InsufficientFundsException(
                String.format("Withdrawal denied: requested $%.2f exceeds balance $%.2f", amount, this.balance),
                shortfall
            );
        }
        this.balance -= amount;
        System.out.printf("[SUCCESS] Withdrew $%.2f. Remaining Balance: $%.2f%n", amount, this.balance);
    }

    public void displayAccountInfo() {
        System.out.println("----------------------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + accountHolder);
        System.out.printf("Current Balance: $%.2f%n", balance);
        System.out.println("----------------------------------------");
    }
}
