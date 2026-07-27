# FinTrack

A simple console-based personal finance tracker written in Java. FinTrack lets you record income and expenses (including recurring monthly transactions), view your balance, search, and filter your transaction history — all from a text menu.

## Features

- Add transactions (income or expense), including recurring monthly transactions tied to a day of the month
- List all transactions with total income, total expenses, and current balance
- Remove a transaction by its position in the list
- Search transactions by keyword in the description
- Filter transactions by type (income, expenses, or monthly)
- Input validation with clear error messages (empty description, non-positive amount, invalid day of month, etc.)

## Project Structure

```
FinTrackProject/
├── pom.xml
└── src/main/java/com/fintrack/
    ├── app/
    │   └── Menu.java                 # Console entry point / menu loop
    ├── controller/
    │   └── FinTracker.java           # Core logic: add, remove, list, search, filter, totals
    ├── model/
    │   ├── Transaction.java          # Base transaction (description, amount, income flag, date)
    │   └── MonthlyTransaction.java   # Recurring transaction (adds day-of-month)
    ├── exceptions/
    │   └── InvalidInputException.java
    └── utils/
        └── Formatter.java           # Date, amount, and sign formatting helpers
```

## Requirements

- Java 25 (JDK)
- Maven

## Build & Run

From the `FinTrackProject` directory:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.fintrack.app.Menu"
```

Or compile and run directly:

```bash
javac -d target/classes $(find src/main/java -name "*.java")
java -cp target/classes com.fintrack.app.Menu
```

## Usage

On launch, you'll see a menu:

```
1. Add Transaction
2. View Transactions
3. Show current balance
4. Remove Transaction
5. Search Transaction
6. Filter by type (Income / Expenses / Monthly transactions)
7. Exit
```

Enter the number for the action you want and follow the prompts.

## Notes

- Data is stored in memory only; transactions are not persisted between runs.
- All transaction amounts must be greater than 0, and descriptions cannot be empty.
