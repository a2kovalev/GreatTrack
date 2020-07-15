package com.example.greattrack.budget;

import java.io.Serializable;

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
        return amount;
    }

    public BudgetFrequency getFrequency() {
        return frequency;
    }

}
