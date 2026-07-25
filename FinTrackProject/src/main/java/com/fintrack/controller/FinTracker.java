package com.fintrack.controller;

import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.utils.Formatter;

/**
 * Core service that manages the in-memory list of {@link Transaction}s and
 * provides operations to add, remove, list, search, filter, and summarize
 * them.
 */
public class FinTracker {

    /** All transactions currently tracked, in insertion order. */
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Validates and adds a transaction to the tracked list.
     *
     * @param transaction the transaction to add
     * @throws InvalidInputException if the description is empty/blank or the
     *         amount is not greater than 0
     */
    public void addTransaction(Transaction transaction)  throws InvalidInputException{
        if(transaction.getDescription() == null || transaction.getDescription().isBlank()){
            throw new InvalidInputException("Description cannot be empty!");
        }
        if(transaction.getAmount() <= 0){
            throw new InvalidInputException("Amount must be greater than 0!");
        }
        transactions.add(transaction);
        System.out.println("Transaction added successfully!");
    }

    /**
     * Prints all tracked transactions along with totals for count, income,
     * expenses, and overall balance.
     */
    public void listTransactions() {
        if(transactions.isEmpty()){
            System.out.println("No transactions found!");
        }
        System.out.println("\n========= TRANSACTIONS =========\n");
        int index = 1;
        for(Transaction transaction : transactions){
            System.out.println(index + ". " + transaction);
            index++;
        }
        System.out.println("\n========= END OF TRANSACTIONS =========\n");

        System.out.println("Total number of transactions: " + transactions.size());
        System.out.println("Total income: +" + Formatter.formatAmount(calculateTotalIncome()));
        System.out.println("Total expenses: -" + Formatter.formatAmount(calculateTotalExpenses()));
        System.out.println("Total balance: " + Formatter.formatAmount(calculateTotalBalance()));
        System.out.println("\n==========================================");
    }

    /**
     * Removes the transaction at the given 1-based position in the list.
     *
     * @param index 1-based position of the transaction to remove
     * @throws InvalidInputException if {@code index} is out of range
     */
    public void removeTransaction(int index) throws InvalidInputException {
        if(index < 1 || index > transactions.size()){
            throw new InvalidInputException("Invalid index: " + index);
        }
        Transaction removed =  transactions.remove(index - 1 );
        System.out.println("Transaction removed: "  + removed.getDescription());
    }

    /**
     * Prints transactions filtered by income/expense type.
     *
     * @param income {@code true} to show income transactions, {@code false} to show expenses
     */
    public void filterByType(boolean income) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for(Transaction transaction : transactions){
            if(transaction.isIncome()){
                filteredTransactions.add(transaction);
            }
        }

        if(filteredTransactions.isEmpty()){
            System.out.println("No transactions found!");
            return;
        }

        String type = income ? "INCOME" : "EXPENSES";
        System.out.println("\nTYPE OF TRANSACTIONS: " + type);
        int index = 1;
        for(Transaction transaction : filteredTransactions){
            System.out.println("\n" + index + ". " + transaction);
            index++;
        }
        System.out.println("\n========= END OF TRANSACTIONS BY THE TYPE " + type + " =========\n");
    }

    /**
     * Prints only the transactions that are instances of
     * {@link com.fintrack.model.MonthlyTransaction}.
     */
    public void filterByMonthly(){
        List<Transaction> filteredTransactionsByMonthly = new ArrayList<>();

        for(Transaction transaction : transactions){
            if(transaction instanceof MonthlyTransaction){
                filteredTransactionsByMonthly.add(transaction);
            }
        }

        if(filteredTransactionsByMonthly.isEmpty()){
            System.out.println("No transactions found!");
            return;
        }

        System.out.println("\n===================== MONTHLY TRANSACTIONS ===========");
        int index = 1;
        for(Transaction transaction : filteredTransactionsByMonthly){
            System.out.println(index + ". " + transaction);
            index++;
        }
        System.out.println("========================================================");
    }
    /**
     * Calculates the net balance (total income minus total expenses) across
     * all tracked transactions.
     *
     * @return the current balance
     */
    public double calculateTotalBalance(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(transaction.isIncome()){
                total += transaction.getAmount();
            }
            else{
                total -= transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Sums the amounts of all income transactions.
     *
     * @return the total income
     */
    public double calculateTotalIncome(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(transaction.isIncome()){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Sums the amounts of all expense transactions.
     *
     * @return the total expenses
     */
    public double calculateTotalExpenses(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(!transaction.isIncome()){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Prints all transactions whose description contains the given keyword
     * (case-insensitive).
     *
     * @param keyword the search keyword; if {@code null} or blank, no search is performed
     */
    public void searchTransaction(String keyword){
        if(keyword == null || keyword.isBlank()){
            System.out.println("Invalid keyword!");
            return;
        }

        List<Transaction> results = new ArrayList<>();

        for(Transaction transaction : transactions){
            if(transaction.getDescription().toLowerCase().contains(keyword.toLowerCase())){
                results.add(transaction);
            }
        }

        if(results.isEmpty()){
            System.out.println("No transactions found for: " + keyword);
            return;
        }

        System.out.println("\n============ SEARCH TRANSACTION ==========");
        int index = 1;
        for(Transaction transaction : results){
            System.out.println(index + ". " + transaction);
            index++;
        }
        System.out.println("\n============ END OF SEARCH TRANSACTION ==========");
    }

    /**
     * Returns the live list of all tracked transactions.
     *
     * @return the list of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
