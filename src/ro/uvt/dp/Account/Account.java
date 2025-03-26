package ro.uvt.dp.Account;

import ro.uvt.dp.Operations;
import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.Exceptions.InsufficientFundsException;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Operations {

	protected String accountCode = null;
	protected double amount = 0;

	private List<String> transactionHistory;

	public static enum TYPE {
		EUR, RON
	}

	protected Account(String accountNumber, double initialDeposit) throws InvalidDepositException {
		this.accountCode = accountNumber;
		this.transactionHistory = new ArrayList<>();
		depose(initialDeposit); 
	}

	@Override
	public double getTotalAmount() {
		return amount + amount * getInterest();
	}

	@Override
	public void depose(double depositAmount) throws InvalidDepositException {
		if (depositAmount <= 0) {
			throw new InvalidDepositException("Deposit amount must be positive.");
		}
		this.amount += depositAmount;
		logTransaction("Deposit: +" + depositAmount);
	}

	@Override
	public void retrieve(double withdrawalAmount) throws InsufficientFundsException, InvalidDepositException {
		if (withdrawalAmount <= 0) {
			throw new InvalidDepositException("Withdrawal amount must be positive.");
		}
		if (withdrawalAmount > this.amount) {
			throw new InsufficientFundsException("Insufficient funds for withdrawal.");
		}
		this.amount -= withdrawalAmount;
		logTransaction("Withdrawal: -" + withdrawalAmount);
	}

	protected void logTransaction(String transaction) {
		transactionHistory.add(transaction);
	}

	public List<String> getTransactionHistory() {
		return new ArrayList<>(transactionHistory);
	}

	@Override
	public String toString() {
		return "Account: code=" + accountCode + ", amount=" + amount;
	}

	public String getAccountNumber() {
		return accountCode;
	}

	public abstract TYPE getType();

	public abstract void transfer(Account targetAccount, double transferAmount)
			throws InvalidDepositException, InsufficientFundsException;


}
