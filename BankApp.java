import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BankApp {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleAccounts();

        boolean running = true;
        System.out.println("=================================================");
        System.out.println("        WELCOME TO THE OOP BANKING SYSTEM        ");
        System.out.println("=================================================");

        while (running) {
            printMenu();
            System.out.print("Enter your choice (1-6): ");
            int choice = readIntChoice();

            switch (choice) {
                case 1:
                    displayAllAccounts();
                    break;
                case 2:
                    handleDeposit();
                    break;
                case 3:
                    handleWithdrawal();
                    break;
                case 4:
                    handleApplyInterest();
                    break;
                case 5:
                    runAutomatedExceptionTestSuite();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nThank you for using OOP Bank. Have a great day!");
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please choose a number between 1 and 6.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void initializeSampleAccounts() {
        try {
            // SavingsAccount: (accountNo, holder, initialBalance, interestRate, minBalance)
            SavingsAccount sa = new SavingsAccount("SA-1001", "Aeron Irasusta", 1500.00, 0.045, 500.00);
            
            // CheckingAccount: (accountNo, holder, initialBalance, overdraftLimit, overdraftFee)
            CheckingAccount ca = new CheckingAccount("CA-2002", "Alex Mercer", 800.00, 300.00, 20.00);

            accounts.add(sa);
            accounts.add(ca);
        } catch (InvalidAmountException e) {
            System.out.println("[ERROR] Failed to initialize sample accounts: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("----------------- MAIN MENU ---------------------");
        System.out.println("1. View All Accounts (Polymorphic Display)");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Apply Monthly Interest (Savings Account)");
        System.out.println("5. Run Automated Exception Test Suite");
        System.out.println("6. Exit");
        System.out.println("-------------------------------------------------");
    }

    private static void displayAllAccounts() {
        System.out.println("\n--- LIST OF REGISTERED ACCOUNTS ---");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("\n[Account Index: " + (i + 1) + "]");
            // Polymorphic call: executes the specific subclass displayAccountInfo()
            accounts.get(i).displayAccountInfo();
        }
    }

    private static void handleDeposit() {
        Account acc = selectAccount();
        if (acc == null) return;

        System.out.print("Enter amount to deposit: $");
        double amount = readDoubleAmount();

        try {
            acc.deposit(amount);
        } catch (InvalidAmountException e) {
            System.out.println("[CAUGHT InvalidAmountException]: " + e.getMessage());
        }
    }

    private static void handleWithdrawal() {
        Account acc = selectAccount();
        if (acc == null) return;

        System.out.print("Enter amount to withdraw: $");
        double amount = readDoubleAmount();

        try {
            acc.withdraw(amount);
        } catch (InvalidAmountException e) {
            System.out.println("[CAUGHT InvalidAmountException]: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("[CAUGHT InsufficientFundsException]: " + e.getMessage());
            if (e.getShortfall() > 0) {
                System.out.printf(">> Deficit / Shortfall amount: $%.2f%n", e.getShortfall());
            }
        }
    }

    private static void handleApplyInterest() {
        System.out.println("\n--- APPLY MONTHLY INTEREST ---");
        boolean foundSavings = false;

        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) acc;
                System.out.println("Applying interest for account: " + sa.getAccountNumber() + " (" + sa.getAccountHolder() + ")");
                sa.applyMonthlyInterest();
                foundSavings = true;
            }
        }

        if (!foundSavings) {
            System.out.println("[INFO] No Savings Accounts found in the system.");
        }
    }

    private static void runAutomatedExceptionTestSuite() {
        System.out.println("\n=================================================");
        System.out.println("       RUNNING AUTOMATED EXCEPTION TEST SUITE    ");
        System.out.println("=================================================");

        try {
            SavingsAccount testSavings = new SavingsAccount("TEST-SA", "Test User", 1000.00, 0.05, 400.00);
            CheckingAccount testChecking = new CheckingAccount("TEST-CA", "Test User", 500.00, 200.00, 15.00);

            // Test Case 1: Negative deposit
            System.out.println("\n[Test 1] Testing negative deposit on SavingsAccount (-$50):");
            try {
                testSavings.deposit(-50.00);
            } catch (InvalidAmountException e) {
                System.out.println("PASS -> Caught expected InvalidAmountException: " + e.getMessage());
            }

            // Test Case 2: Withdrawal violating minimum balance
            System.out.println("\n[Test 2] Testing withdrawal violating minimum balance ($1000 balance, $400 min, withdraw $700):");
            try {
                testSavings.withdraw(700.00);
            } catch (InsufficientFundsException e) {
                System.out.println("PASS -> Caught expected InsufficientFundsException: " + e.getMessage());
                System.out.printf("        Reported shortfall: $%.2f%n", e.getShortfall());
            }

            // Test Case 3: Checking Account overdraft dip and fee
            System.out.println("\n[Test 3] Testing overdraft within limit on CheckingAccount ($500 balance, withdraw $600):");
            testChecking.withdraw(600.00);

            // Test Case 4: Checking Account exceeding overdraft limit
            System.out.println("\n[Test 4] Testing withdrawal beyond overdraft capacity (withdraw $300):");
            try {
                testChecking.withdraw(300.00);
            } catch (InsufficientFundsException e) {
                System.out.println("PASS -> Caught expected InsufficientFundsException: " + e.getMessage());
                System.out.printf("        Reported shortfall: $%.2f%n", e.getShortfall());
            }

            System.out.println("\n=================================================");
            System.out.println("         ALL EXCEPTION TESTS COMPLETED!          ");
            System.out.println("=================================================");
        } catch (Exception e) {
            System.out.println("[ERROR] Exception test suite encountered an unexpected error: " + e.getMessage());
        }
    }

    private static Account selectAccount() {
        if (accounts.isEmpty()) {
            System.out.println("[ERROR] No accounts available.");
            return null;
        }

        System.out.println("\nSelect an Account:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.printf("%d. %s (%s) - Balance: $%.2f%n", 
                (i + 1), accounts.get(i).getAccountNumber(), accounts.get(i).getAccountHolder(), accounts.get(i).getBalance());
        }
        System.out.print("Enter account selection (1-" + accounts.size() + "): ");
        int sel = readIntChoice();

        if (sel < 1 || sel > accounts.size()) {
            System.out.println("[ERROR] Selection out of bounds.");
            return null;
        }
        return accounts.get(sel - 1);
    }

    private static int readIntChoice() {
        try {
            int val = scanner.nextInt();
            scanner.nextLine(); // clear buffer
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // clear invalid input
            return -1;
        }
    }

    private static double readDoubleAmount() {
        try {
            double val = scanner.nextDouble();
            scanner.nextLine(); // clear buffer
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // clear invalid input
            return -1;
        }
    }
}
