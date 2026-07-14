# FinTrack

A simple command-line personal finance tracker built in Java. Add income and expense transactions, list them, check your current balance, and remove entries — all from a text menu.

## Features

- Add transactions with description, amount, type (income/expense), and date
- Support for recurring monthly transactions (with a fixed day of month)
- List all transactions
- Calculate current balance (income minus expenses)
- Remove a transaction by its list position
- Input validation with custom exceptions for invalid descriptions, amounts, and indexes

## Tech stack

- Java 25
- Maven

## Project structure

```
FinTrackProject/
├── pom.xml
└── src/main/java/com/fintrack/
    ├── app/
    │   └── Menu.java                  # CLI entry point and menu loop
    ├── controller/
    │   └── FinTracker.java            # Core logic: add/list/remove transactions, balance
    ├── model/
    │   ├── Transaction.java           # Base transaction model
    │   └── MonthlyTransaction.java    # Recurring transaction (extends Transaction)
    └── exceptions/
        └── InvalidInputException.java # Custom exception for validation errors
```

## Getting started

### Prerequisites

- JDK 25
- Maven

### Build

```bash
cd FinTrackProject
mvn compile
```

### Run

```bash
mvn exec:java -Dexec.mainClass="com.fintrack.app.Menu"
```

Or compile and run directly:

```bash
javac -d target/classes -sourcepath src/main/java src/main/java/com/fintrack/app/Menu.java
java -cp target/classes com.fintrack.app.Menu
```

## Usage

On launch, you'll see a menu with the following options:

```
1. Add Transaction
2. View Transactions
3. Show current balance
4. Remove Transaction
5. Exit
```

When adding a transaction, you'll be prompted for a description, amount, whether it's income, and whether it recurs monthly (and if so, on which day).

## Roadmap ideas

- Persist transactions to a file or database
- Filter/search transactions by date or category
- Automated tests for `FinTracker` and models
