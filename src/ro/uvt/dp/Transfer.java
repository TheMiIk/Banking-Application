package ro.uvt.dp;

import ro.uvt.dp.Account.Account;
import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public interface Transfer {
	public void transfer(Account c, double s) throws InsufficientFundsException, InvalidDepositException;
}
