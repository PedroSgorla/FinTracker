package com.fintrack.app;

import com.fintrack.controller.FinTracker;
import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import com.fintrack.utils.Formatter;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Console entry point for the FinTrack application. Presents a text menu
 * that lets the user add, view, remove, search, and filter transactions,
 * and view their current balance.
 */
public class Menu {

    /** Controller used to manage the tracked transactions. */
    private static FinTracker finTracker = new FinTracker();

    /** Scanner used to read user input from the console. */
    private static Scanner input = new Scanner(System.in);

    /**
     * Application entry point. Repeatedly prints the menu and handles the
     * chosen option until the user selects "Exit".
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args ) {
        int option = 0;

        while(option != 7){
            printMenu();
            try{
                option = Integer.parseInt(input.nextLine());

                handleOption(option);
            }
            catch(NumberFormatException e){
                System.out.println("Invalid input option! Please type a number between 1 and 5.");
            }
        }
    }

    /**
     * Prints the main menu options to the console.
     */
    private static void printMenu(){
        System.out.println("\n=============== FINTRACK - YOUR PERSONAL FINANCIAL CONTROL =============");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Transactions");
        System.out.println("3. Show current balance");
        System.out.println("4. Remove Transaction");
        System.out.println("5. Search Transaction");
        System.out.println("6. Filter by type(Income / Expenses / Monthly transactions)");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Dispatches to the appropriate handler based on the chosen menu option.
     *
     * @param option the selected menu option (1-7)
     */
    private static void handleOption(int option){
        switch(option){
            case 1 -> addTransaction();
            case 2 -> finTracker.listTransactions();
            case 3 -> showBalance();
            case 4 -> removeTransaction();
            case 5 -> searchTransaction();
            case 6 -> filterTransactions();
            case 7 -> System.out.println("Goodbye!");
            default ->  System.out.println("Invalid option! Choose again!");
        }
    }

    /**
     * Prompts the user for transaction details, builds either a regular or
     * monthly {@link Transaction}, and adds it via the controller.
     */
    private static void addTransaction(){
        try{
            System.out.print("Description: ");
            String description = input.nextLine();

            System.out.print("Amount: ");
            double amount = Double.parseDouble(input.nextLine());

            System.out.print("Is it an income?(y/n): ");
            boolean isIncome = input.nextLine().equalsIgnoreCase("y");

            System.out.print("Is it monthly transaction?(y/n) ");
            boolean isMonthlyTransaction = input.nextLine().equalsIgnoreCase("y");

            Transaction transaction;

            if(isMonthlyTransaction){
                System.out.print("Day of Month(e.g. 10): ");
                int dayOfMonth = Integer.parseInt(input.nextLine());

                transaction = new MonthlyTransaction(description, amount, isIncome, LocalDate.now(), dayOfMonth);
            }
            else{
                transaction = new Transaction(description, amount, isIncome, LocalDate.now());
            }

            finTracker.addTransaction(transaction);
        }
        catch (NumberFormatException e){
            System.out.println("Invalid number entered! Please try again!");
        }
        catch (InvalidInputException e){
            System.out.println("Error: " + e.getMessage() + "! Please try again!");
        }
    }

    /**
     * Prints the current total balance to the console.
     */
    private static void showBalance(){
        double balance = finTracker.calculateTotalBalance();
        System.out.println("Current balance: " + Formatter.formatAmount(balance));
    }

    /**
     * Lists current transactions, then prompts the user for the position of
     * the one to remove and deletes it.
     */
    private static void removeTransaction(){
        finTracker.listTransactions();
        if(finTracker.getTransactions().isEmpty()){
            return;
        }
        try{
            System.out.print("Enter the number of the transaction you want to remove: ");
            int index  = Integer.parseInt(input.nextLine());
            finTracker.removeTransaction(index);
        }
        catch(NumberFormatException e){
            System.out.println("Invalid number entered! Please try again!");
        }
        catch (InvalidInputException e){
            System.out.println("Error: " + e.getMessage() + "! Please try again!");
        }
    }

    /**
     * Prompts the user for a keyword and prints matching transactions.
     */
    private static void searchTransaction(){
        System.out.print("keyword to search: ");
        String keyword = input.nextLine();
        finTracker.searchTransaction(keyword);
    }

    /**
     * Prompts the user to choose a transaction type (income, expenses, or
     * monthly) and prints the matching transactions.
     */
    private static void filterTransactions(){
        System.out.println("Show only: ");
        System.out.println("1. Income Transaction");
        System.out.println("2. Expenses Transaction");
        System.out.println("3. Monthly Transaction");
        System.out.print("Choose an option: ");

        try{
            int option = Integer.parseInt(input.nextLine());
            switch(option){
                case 1 -> finTracker.filterByType(true);
                case 2 -> finTracker.filterByType(false);
                case 3 -> finTracker.filterByMonthly();
                default -> System.out.println("Invalid option! Choose again!");
            }
        }
        catch (NumberFormatException e){
            System.out.println("Invalid number entered! Please try again!");
        }
    }
}
