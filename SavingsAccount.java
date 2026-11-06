public class SavingsAccount extends Account {
    private double interestRate;      // e.g. 0.03 for 3% annual interest
    private double minimumBalance;     // e.g. 500.00 maintaining balance

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance, double interestRate, double minimumBalance) 
            throws InvalidAmountException {
        super(accountNumber, accountHolder, initialBalance);
        
        if (minimumBalance < 0) {
            throw new InvalidAmountException("Minimum balance cannot be negative: " + minimumBalance);
        }
        if (initialBalance < minimumBalance) {
            throw new InvalidAmountException(String.format(
                "Initial balance ($%.2f) cannot be less than the required minimum maintaining balance ($%.2f).", 
                initialBalance, minimumBalance
            ));
        }
        if (interestRate < 0) {
            throw new InvalidAmountException("Interest rate cannot be negative: " + interestRate);
        }

        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero. Given: " + amount);
        }

        double maxAllowedWithdrawal = this.balance - this.minimumBalance;
        if (amount > maxAllowedWithdrawal) {
            double shortfall = amount - maxAllowedWithdrawal;
            throw new InsufficientFundsException(
                String.format(
                    "Withdrawal of $%.2f denied. Must maintain minimum balance of $%.2f. Maximum available to withdraw is $%.2f.", 
                    amount, this.minimumBalance, Math.max(0, maxAllowedWithdrawal)
                ),
                shortfall
            );
        }

        this.balance -= amount;
        System.out.printf("[SUCCESS - SAVINGS] Withdrew $%.2f. Remaining Balance: $%.2f%n", amount, this.balance);
    }

    public void applyMonthlyInterest() {
        double monthlyInterest = this.balance * (this.interestRate / 12.0);
        this.balance += monthlyInterest;
        System.out.printf("[SUCCESS - INTEREST] Added $%.2f (%.2f%% annual). New Balance: $%.2f%n", 
            monthlyInterest, this.interestRate * 100, this.balance);
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("========================================");
        System.out.println("Type           : Savings Account");
        System.out.println("Account Number : " + getAccountNumber());
        System.out.println("Account Holder : " + getAccountHolder());
        System.out.printf("Current Balance: $%.2f%n", getBalance());
        System.out.printf("Min Balance    : $%.2f%n", minimumBalance);
        System.out.printf("Interest Rate  : %.2f%%%n", interestRate * 100);
        System.out.println("========================================");
    }
}
