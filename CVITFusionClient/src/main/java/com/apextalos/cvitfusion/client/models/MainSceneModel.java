package com.apextalos.cvitfusion.client.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainSceneModel {

	private final DoubleProperty accountBalance;
	private final ObservableList<String> listItems;
	private final ObservableList<ParameterModel> tableItems;

	public MainSceneModel(Double accountBalance) {
		this.accountBalance = new SimpleDoubleProperty(accountBalance);
		this.listItems = FXCollections.observableArrayList();
		this.tableItems = FXCollections.observableArrayList();
	}

	public ObservableList<String> getListItems() {
		return listItems;
	}

	public ObservableList<ParameterModel> getTableItems() {
		return tableItems;
	}

	public DoubleProperty getAccountBalanceProperty() {
		return accountBalance;
	}

	public void deposit(double amount) {
		accountBalance.set(accountBalance.get() + amount);
	}

	public void withdraw(double amount) {
		accountBalance.set(accountBalance.get() - amount);
	}
}
