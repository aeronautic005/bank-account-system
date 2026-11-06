public class CheckingAccount extends Account {
    private double overdraftLimit;  // e.g. 200.00 (allowed to go negative down to -$200.00)
    private double overdraftFee;    // e.g. 15.00 charged when withdrawal dips below $0

    public CheckingAccount(String accountNumber, String accountHolder, double initialBalance, double overdraftLimit, double overdraftFee)
            throws InvalidAmountException {
        super(accountNumber, accountHolder, initialBalance);

        if (overdraftLimit < 0) {
            throw new InvalidAmountException("Overdraft limit cannot be negative: " + overdraftLimit);
        }
        if (overdraftFee < 0) {
            throw new InvalidAmountException("Overdraft fee cannot be negative: " + overdraftFee);
        }

        this.overdraftLimit = overdraftLimit;
        this.overdraftFee = overdraftFee;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public double getOverdraftFee() {
        return overdraftFee;
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero. Given: " + amount);
        }

        double maxAllowedWithdrawal = this.balance + this.overdraftLimit;
        if (amount > maxAllowedWithdrawal) {
            double shortfall = amount - maxAllowedWithdrawal;
            throw new InsufficientFundsException(
                String.format(
                    "Withdrawal of $%.2f denied. Exceeds overdraft capacity ($%.2f limit). Maximum available to withdraw: $%.2f.",
                    amount, this.overdraftLimit, maxAllowedWithdrawal
                ),
                shortfall
            );
        }

        boolean willOverdraft = (amount > this.balance);
        this.balance -= amount;
        System.out.printf("[SUCCESS - CHECKING] Withdrew $%.2f. New Balance: $%.2f%n", amount, this.balance);

        if (willOverdraft) {
            this.balance -= this.overdraftFee;
            System.out.printf("[OVERDRAFT FEE] Account went negative. A fee of $%.2f was deducted. Remaining Balance: $%.2f%n",
                this.overdraftFee, this.balance);
        }
    }

    @Override
    public void displayAccountInfo() {
        System.out.println("========================================");
        System.out.println("Type           : Checking Account");
        System.out.println("Account Number : " + getAccountNumber());
        System.out.println("Account Holder : " + getAccountHolder());
        System.out.printf("Current Balance: $%.2f%n", getBalance());
        System.out.printf("Overdraft Limit: $%.2f%n", overdraftLimit);
        System.out.printf("Overdraft Fee  : $%.2f%n", overdraftFee);
        double availableCredit = Math.max(0, this.balance + this.overdraftLimit);
        System.out.printf("Total Spending Power: $%.2f%n", availableCredit);
        System.out.println("========================================");
    }
}
