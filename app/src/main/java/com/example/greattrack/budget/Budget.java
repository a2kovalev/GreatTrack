package com.example.greattrack.budget;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;

enum BudgetFrequency {
    daily, weekly, monthly
}

public class Budget implements Serializable {
    double amount;
    double originalAmount;
    BudgetFrequency frequency;
    HashMap<LocalDate, Double> budgetLedger = new HashMap<LocalDate, Double>();

    public Budget (double amount, BudgetFrequency frequency) {
        this.amount = amount;
        this.frequency = frequency;
        originalAmount = amount;
    }

    public void addMoney (double money) {
        amount = amount + money;
        LocalDate current = LocalDate.now();
        addToLedger(current, money);
    }

    public void subtractMoney (double money) {
        amount = amount - money;
        LocalDate current = LocalDate.now();
        double minusMoney = money * -1;
        addToLedger(current, minusMoney);
    }

    public double getAmount() {
        DecimalFormat df = new DecimalFormat("0.00");
        amount = Double.parseDouble(df.format(amount));
        return amount;
    }

    public BudgetFrequency getFrequency() {
        return frequency;
    }

    public void addToLedger(LocalDate date, Double amount) {
        budgetLedger.put(date, amount);
    }
}
