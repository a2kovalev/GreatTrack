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
        budgetDateAndTime dateAndTime = new budgetDateAndTime(current.getHour(), current.getMinute(), current.getYear(), current.getMonthValue() + 1, current.getDayOfMonth());
        BudgetFragment.addToLedger(dateAndTime, money);
    }

    public void subtractMoney (double money) {
        amount = amount - money;
        LocalDateTime current = LocalDateTime.now();
        budgetDateAndTime dateAndTime = new budgetDateAndTime(current.getHour(), current.getMinute(), current.getYear(), current.getMonthValue() + 1, current.getDayOfMonth());
        double minusMoney = money * -1;
        BudgetFragment.addToLedger(dateAndTime, minusMoney);
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