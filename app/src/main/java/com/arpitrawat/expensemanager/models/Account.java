package com.arpitrawat.expensemanager.models;

public class Account {
    private double account_amount;
    private String accountName;

public Account(){

}

    public Account(double accountAmount, String accountName) {
        account_amount = accountAmount;
        this.accountName = accountName;
    }

    public double getAccount_amount() {
        return account_amount;
    }

    public void setAccount_amount(double account_amount) {
        this.account_amount = account_amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
