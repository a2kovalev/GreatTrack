package com.example.greattrack.budget;

import java.io.Serializable;
import java.text.DecimalFormat;

enum BudgetFrequency {
    daily, weekly, monthly
}

public class Budget implements Serializable {
    double amount;
    double originalAmount;
    BudgetFrequency frequency;

    public Budget (double amount, BudgetFrequency frequency) {
        this.amount = amount;
        this.frequency = frequency;
        originalAmount = amount;
    }

    public void addMoney (double money) {
        amount = amount + money;
    }

    public void subtractMoney (double money) {
        amount = amount - money;
    }

    public double getAmount() {
        DecimalFormat df = new DecimalFormat("0.00");
        amount = Double.parseDouble(df.format(amount));
        return amount;
    }

    public BudgetFrequency getFrequency() {
        return frequency;
    }
}
