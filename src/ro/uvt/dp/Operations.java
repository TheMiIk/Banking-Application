package ro.uvt.dp;

import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public interface Operations {
	public double getTotalAmount();

	public double getInterest();

	public void depose(double amount) throws InvalidDepositException;

	public void retrieve(double amount) throws InsufficientFundsException,InvalidDepositException;
}
