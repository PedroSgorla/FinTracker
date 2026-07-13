package com.fintrack.app;

import com.fintrack.controller.FinTracker;
import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;

import java.time.LocalDate;
import java.util.Scanner;

public class Menu {

    private static FinTracker finTracker = new FinTracker();
    private static Scanner input = new Scanner(System.in);

    public static void main( String[] args ) {
        int option = 0;

        while(option != 5){
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

    public static void printMenu(){
        System.out.println("\n=============== FINTRACK - YOUR PERSONAL FINANCIAL CONTROL =============");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Transactions");
        System.out.println("3. Show current balance");
        System.out.println("4. Remove Transaction");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    public static void handleOption(int option){
        switch(option){
            case 1 -> addTransaction();
            case 2 -> finTracker.listTransactions();
            case 3 -> showBalance();
            case 4 -> removeTransaction();
            case 5 -> System.out.println("Goodbye!");
            default ->  System.out.println("Invalid option! Choose again!");
        }
    }

    public static void addTransaction(){
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

    public static void showBalance(){
        double balance = finTracker.calculateTotalBalance();
        System.out.printf("Current balance: %.2f%n" + balance);
    }

    public static void removeTransaction(){
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
}
