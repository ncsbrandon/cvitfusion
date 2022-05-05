package com.apextalos.cvitfusionclient.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HelloModel {
    private final StringProperty accountHolder;
    private final IntegerProperty accountNumber;
    private final DoubleProperty accountBalance;
    private final ObservableList<String> listItems;
    private final ObservableList<KeyValuePair> tableItems;

    public HelloModel(String accountHolder, Integer accountNumber, Double accountBalance) {
        this.accountHolder = new SimpleStringProperty(accountHolder);
        this.accountNumber = new SimpleIntegerProperty(accountNumber);
        this.accountBalance = new SimpleDoubleProperty(accountBalance);
        this.listItems = FXCollections.observableArrayList();
        this.tableItems = FXCollections.observableArrayList();
    }

    public ObservableList<String> getListItems() { return listItems; }

    public ObservableList<KeyValuePair> getTableItems() { return tableItems; }

    public String getAccountHolder() {
        return accountHolder.get();
    }

    public StringProperty accountHolderProperty() {
        return accountHolder;
    }

    public int getAccountNumber() {
        return accountNumber.get();
    }

    public IntegerProperty accountNumberProperty() {
        return accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance.get();
    }

    public DoubleProperty accountBalanceProperty() {
        return accountBalance;
    }

    public void deposit(double amount) {
        accountBalance.set(accountBalance.get() + amount);
    }

    public void withdraw(double amount) {
        accountBalance.set(accountBalance.get() - amount);
    }
}
