package com.fintrack.ui;

import com.fintrack.controller.FinTracker;
import com.fintrack.exceptions.DataAccessException;
import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.IntStream;

public class TransactionFormController {

    private final FinTracker finTracker;

    private final Transaction editing;

    @FXML private TextField descriptionField;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private RadioButton incomeRadio;
    @FXML private RadioButton expenseRadio;
    @FXML private CheckBox monthlyCheckBox;
    @FXML private ComboBox<Integer> dayComboBox;
    @FXML private Label errorLabel;

    public TransactionFormController(FinTracker finTracker, Transaction editing) {
        this.finTracker = Objects.requireNonNull(finTracker);
        this.editing = editing;
    }

    @FXML
    private void initialize() {
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());
        errorLabel.setVisible(false);

        dayComboBox.getItems().setAll(IntStream.rangeClosed(1, 31).boxed().toList());
        dayComboBox.setValue(1);
        dayComboBox.disableProperty().bind(monthlyCheckBox.selectedProperty().not());

        datePicker.setValue(LocalDate.now());

        if (editing != null) {
            fillFrom(editing);
        }
    }

    private void fillFrom(Transaction transaction) {
        descriptionField.setText(transaction.getDescription());
        amountField.setText(transaction.getAmount().toPlainString());
        datePicker.setValue(transaction.getDate());
        incomeRadio.setSelected(transaction.isIncome());
        expenseRadio.setSelected(!transaction.isIncome());
        if (transaction instanceof MonthlyTransaction monthly) {
            monthlyCheckBox.setSelected(true);
            dayComboBox.setValue(monthly.getDayOfMonth());
        }
    }

    @FXML
    private void onSave() {
        try {
            Transaction transaction = buildTransaction();
            if (editing == null) {
                finTracker.addTransaction(transaction);
            } else {
                finTracker.updateTransaction(transaction);
            }
            close();
        } catch (InvalidInputException e) {
            showError(e.getMessage());
        } catch (DataAccessException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private Transaction buildTransaction() {
        BigDecimal amount = parseAmount(amountField.getText());
        boolean income = incomeRadio.isSelected();

        Transaction transaction = monthlyCheckBox.isSelected()
                ? new MonthlyTransaction(descriptionField.getText(), amount, income,
                        datePicker.getValue(), dayComboBox.getValue())
                : new Transaction(descriptionField.getText(), amount, income, datePicker.getValue());

        if (editing != null) {
            transaction.setId(editing.getId());
        }
        return transaction;
    }

    private static BigDecimal parseAmount(String text) {
        if (text == null || text.isBlank()) {
            throw new InvalidInputException("Amount cannot be empty!");
        }
        try {
            return new BigDecimal(text.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Amount must be a number, for example 150.50");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void close() {
        ((Stage) descriptionField.getScene().getWindow()).close();
    }
}
