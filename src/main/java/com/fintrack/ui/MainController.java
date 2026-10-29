package com.fintrack.ui;

import com.fintrack.controller.FinTracker;
import com.fintrack.exceptions.DataAccessException;
import com.fintrack.exceptions.InvalidInputException;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import com.fintrack.utils.Formatter;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController {

    private enum Filter {
        ALL("All transactions"),
        INCOME("Income"),
        EXPENSES("Expenses"),
        MONTHLY("Monthly");

        private final String label;

        Filter(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final FinTracker finTracker;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Filter> filterComboBox;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expensesLabel;

    @FXML
    private Label balanceLabel;

    public MainController(FinTracker finTracker) {
        this.finTracker = Objects.requireNonNull(finTracker);
    }

    @FXML
    private void initialize() {
        configureColumns();

        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        transactionTable.setPlaceholder(new Label("No transactions found."));
        transactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onEdit();
            }
        });

        editButton.disableProperty().bind(transactionTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(transactionTable.getSelectionModel().selectedItemProperty().isNull());

        filterComboBox.getItems().setAll(Filter.values());
        filterComboBox.setValue(Filter.ALL);
        filterComboBox.setOnAction(event -> refresh());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refresh());

        // Load the data after the window is shown, so an error dialog can appear on top of it.
        Platform.runLater(this::refresh);
    }

    private void configureColumns() {
        dateColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDate()));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : Formatter.formatDate(date));
            }
        });

        descriptionColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getDescription()));

        typeColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(describeType(cell.getValue())));

        // Income is shown as a positive number and expenses as a negative one,
        // so the column also sorts correctly.
        amountColumn.setCellValueFactory(cell -> {
            Transaction transaction = cell.getValue();
            BigDecimal signed = transaction.isIncome()
                    ? transaction.getAmount()
                    : transaction.getAmount().negate();
            return new ReadOnlyObjectWrapper<>(signed);
        });
        amountColumn.setCellFactory(column -> new AmountCell());
    }

    private static String describeType(Transaction transaction) {
        if (transaction instanceof MonthlyTransaction monthly) {
            return "Monthly (day " + monthly.getDayOfMonth() + ")";
        }
        return "Regular";
    }

    private void refresh() {
        try {
            List<Transaction> transactions = loadTransactions();
            transactionTable.setItems(FXCollections.observableArrayList(transactions));

            incomeLabel.setText("Income: +" + Formatter.formatAmount(finTracker.calculateTotalIncome()));
            expensesLabel.setText("Expenses: -" + Formatter.formatAmount(finTracker.calculateTotalExpenses()));
            balanceLabel.setText("Balance: " + Formatter.formatAmount(finTracker.calculateTotalBalance()));
        } catch (DataAccessException e) {
            Alerts.error("Database error",
                    "Could not load the transactions. Is MySQL running?\n\n" + e.getMessage());
        }
    }

    private List<Transaction> loadTransactions() {
        List<Transaction> base = switch (filterComboBox.getValue()) {
            case ALL -> finTracker.getTransactions();
            case INCOME -> finTracker.filterByType(true);
            case EXPENSES -> finTracker.filterByType(false);
            case MONTHLY -> finTracker.filterByMonthly();
        };

        String keyword = searchField.getText();
        if (keyword == null || keyword.isBlank()) {
            return base;
        }

        Set<Long> matchingIds = finTracker.searchTransaction(keyword).stream()
                .map(Transaction::getId)
                .collect(Collectors.toSet());
        return base.stream()
                .filter(transaction -> matchingIds.contains(transaction.getId()))
                .toList();
    }

    @FXML
    private void onNew() {
        openForm(null);
    }

    @FXML
    private void onEdit() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openForm(selected);
        }
    }

    @FXML
    private void onDelete() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        boolean confirmed = Alerts.confirm("Delete transaction",
                "Delete \"" + selected.getDescription() + "\"? This cannot be undone.");
        if (!confirmed) {
            return;
        }
        try {
            finTracker.removeTransaction(selected.getId());
            refresh();
        } catch (InvalidInputException | DataAccessException e) {
            Alerts.error("Could not delete the transaction", e.getMessage());
        }
    }

    @FXML
    private void onReport() {
        openDialog("/com/fintrack/view/report-view.fxml", "Report", new ReportController(finTracker));
    }

    private void openForm(Transaction toEdit) {
        String title = toEdit == null ? "New transaction" : "Edit transaction";
        openDialog("/com/fintrack/view/transaction-form.fxml", title,
                new TransactionFormController(finTracker, toEdit));
        refresh();
    }

    private void openDialog(String fxmlPath, String title, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            loader.setControllerFactory(type -> controller);

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/com/fintrack/css/style.css")).toExternalForm());

            Stage dialog = new Stage();
            dialog.initOwner(transactionTable.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle(title);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            Alerts.error("Could not open the window", e.getMessage());
        }
    }

    private static class AmountCell extends TableCell<Transaction, BigDecimal> {

        @Override
        protected void updateItem(BigDecimal amount, boolean empty) {
            super.updateItem(amount, empty);
            getStyleClass().removeAll("income-amount", "expense-amount");
            if (empty || amount == null) {
                setText(null);
                return;
            }
            boolean income = amount.signum() >= 0;
            setText(Formatter.formatSign(income) + Formatter.formatAmount(amount.abs()));
            getStyleClass().add(income ? "income-amount" : "expense-amount");
        }
    }
}
