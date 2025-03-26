package ro.uvt.dp.Account;

import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.Transfer;

public class AccountRON extends Account implements Transfer {

	public AccountRON(String accountNumber, double amount) throws InvalidDepositException {
		super(accountNumber, amount);
	}

	public double getInterest() {
		if (amount < 500)
			return 0.03;
		else
			return 0.08;
	}

	@Override
	public String toString() {
		return "Account RON [" + super.toString() + "]";
	}

	@Override
	public void transfer(Account targetAccount, double transferAmount) throws InvalidDepositException, InsufficientFundsException {
		if (transferAmount > 0 && transferAmount <= this.amount) {
			this.retrieve(transferAmount);

			targetAccount.depose(transferAmount);
		} else {
			throw new InvalidDepositException("Invalid transfer amount.");
		}
	}

	public TYPE getType() {
		return TYPE.RON;
	}
}
