package com.fintrack.controller;

import com.fintrack.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import com.fintrack.exceptions.InvalidInputException;
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

    public void getTransactions() {
        if(transactions.isEmpty()){
            System.out.println("No transactions found!");
        }
        System.out.println("\n========= TRANSACTIONS =========");
        int index = 1;
        for(Transaction transaction : transactions){
            System.out.println(index + ". " + transaction);
            index++;
        }
        System.out.println("\n========= END OF TRANSACTIONS =========");
    }

    public void removeTransaction(int index) throws InvalidInputException {
        if(index < 1 || index > transactions.size()){
            throw new InvalidInputException("Invalid index: " + index);
        }
        Transaction removed =  transactions.remove(index - 1 );
        System.out.println("Transaction removed: "  + removed.getDescription());
    }

    public double calculateTotalBalance(){
        double total = 0;
        for(Transaction transaction : transactions){
            if(transaction.getIsIncome()){
                total += transaction.getAmount();
            }
            else{
                total -= transaction.getAmount();
            }
        }
        return total;
    }
}
