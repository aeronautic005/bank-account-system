# Bank Account Hierarchy & Management System

An Object-Oriented Programming (OOP) Java application modeling a banking domain with strict encapsulation, class inheritance, method overriding, and domain-specific custom exceptions.

## Features & Architecture

- **Custom Exceptions:**
  - `InvalidAmountException`: Thrown when negative or zero transaction amounts are supplied.
  - `InsufficientFundsException`: Thrown when a withdrawal exceeds available funds, carrying the exact `shortfall` amount.
- **Base Class:**
  - `Account.java`: Encapsulates account number, holder name, and balance with validation for deposits and withdrawals.
- **Subclasses:**
  - `SavingsAccount.java`: Enforces a minimum maintaining balance and calculates monthly interest.
  - `CheckingAccount.java`: Supports overdraft limits with transaction fee deduction.
- **Driver:**
  - `BankApp.java`: Interactive console application to create accounts, perform transactions, and demonstrate robust exception handling.

## How to Compile & Run

```bash
# Compile all Java files
javac *.java

# Run the application
java BankApp
```
