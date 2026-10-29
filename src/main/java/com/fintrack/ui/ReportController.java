package com.fintrack.ui;

import com.fintrack.controller.FinTracker;
import com.fintrack.exceptions.DataAccessException;
import com.fintrack.utils.Formatter;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Objects;

public class ReportController {

    private final FinTracker finTracker;

    @FXML private Label countLabel;
    @FXML private Label incomeLabel;
    @FXML private Label expensesLabel;
    @FXML private Label balanceLabel;
    @FXML private BarChart<String, Number> barChart;

    public ReportController(FinTracker finTracker) {
        this.finTracker = Objects.requireNonNull(finTracker);
    }

    @FXML
    private void initialize() {
        try {
            BigDecimal income = finTracker.calculateTotalIncome();
            BigDecimal expenses = finTracker.calculateTotalExpenses();
            BigDecimal balance = finTracker.calculateTotalBalance();

            countLabel.setText(String.valueOf(finTracker.getTransactions().size()));
            incomeLabel.setText("+" + Formatter.formatAmount(income));
            expensesLabel.setText("-" + Formatter.formatAmount(expenses));
            balanceLabel.setText(Formatter.formatAmount(balance));
            balanceLabel.getStyleClass().add(balance.signum() >= 0 ? "income-amount" : "expense-amount");

            fillChart(income, expenses);
        } catch (DataAccessException e) {
            Alerts.error("Database error", "Could not load the report.\n\n" + e.getMessage());
        }
    }

    private void fillChart(BigDecimal income, BigDecimal expenses) {
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        incomeSeries.getData().add(new XYChart.Data<String, Number>("Totals", income.doubleValue()));

        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Expenses");
        expensesSeries.getData().add(new XYChart.Data<String, Number>("Totals", expenses.doubleValue()));

        barChart.getData().add(incomeSeries);
        barChart.getData().add(expensesSeries);
    }

    @FXML
    private void onClose() {
        ((Stage) barChart.getScene().getWindow()).close();
    }
}
