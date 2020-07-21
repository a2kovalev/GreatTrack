package com.example.greattrack.budget;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

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
        LocalDateTime current = LocalDateTime.now();
        budgetDateAndTime dateAndTime = new budgetDateAndTime(current.getHour(), current.getMinute(), current.getYear(), current.getMonthValue() + 1, current.getDayOfMonth(), money);
        BudgetFragment.addToLedger(dateAndTime);
    }

    public void subtractMoney (double money) {
        amount = amount - money;
        LocalDateTime current = LocalDateTime.now();
        budgetDateAndTime dateAndTime = new budgetDateAndTime(current.getHour(), current.getMinute(), current.getYear(), current.getMonthValue() + 1, current.getDayOfMonth(), money*-1);
        BudgetFragment.addToLedger(dateAndTime);
    }

    public double getAmount() {
        DecimalFormat df = new DecimalFormat("0.00");
        amount = Double.parseDouble(df.format(amount));
        return amount;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BudgetFrequency getFrequency() {
        return frequency;
    }
}