package ro.uvt.dp.Account;

import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public class AccountEUR extends Account {

	public AccountEUR(String accountNumber, double amount) throws InvalidDepositException {
		super(accountNumber, amount);
	}

	public double getInterest() {
		return 0.01;
	}

	@Override
	public String toString() {
		return "Account EUR [" + super.toString() + "]";
	}

	@Override
	public void transfer(Account targetAccount, double transferAmount) throws InvalidDepositException, InsufficientFundsException {
		if (transferAmount <= 0) {
			throw new InvalidDepositException("Transfer amount must be positive.");
		}
		if (transferAmount > this.amount) {
			throw new InsufficientFundsException("Insufficient funds for transfer.");
		}

		this.amount -= transferAmount;

		targetAccount.depose(transferAmount);

		logTransaction("Transfer to " + targetAccount.getAccountNumber() + ": -" + transferAmount);
	}

	public TYPE getType() {
		return TYPE.EUR;
	}



}
