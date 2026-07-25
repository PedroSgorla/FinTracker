package com.fintrack.controller;

import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.utils.Formatter;

public class FinTracker {

    private List<Transaction> transactions = new ArrayList<>();

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

    public void removeTransaction(int index) throws InvalidInputException {
        if(index < 1 || index > transactions.size()){
            throw new InvalidInputException("Invalid index: " + index);
        }
        Transaction removed =  transactions.remove(index - 1 );
        System.out.println("Transaction removed: "  + removed.getDescription());
    }

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

    public double calculateTotalIncome(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(transaction.isIncome()){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public double calculateTotalExpenses(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(!transaction.isIncome()){
                total += transaction.getAmount();
            }
        }
        return total;
    }

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

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
