package ro.uvt.dp.Account;

import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public class TransactionFeeDecorator extends AccountDecorator {
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.01;

    public TransactionFeeDecorator(Account wrappedAccount) throws InvalidDepositException {
        super(wrappedAccount);
    }

    @Override
    public double getInterest() {
        return wrappedAccount.getInterest();
    }

    @Override
    public void depose(double depositAmount) throws InvalidDepositException {
        double fee = depositAmount * TRANSACTION_FEE_PERCENTAGE;
        wrappedAccount.depose(depositAmount - fee);
        logTransaction("Transaction Fee (Deposit): -" + fee);
    }

    @Override
    public void retrieve(double withdrawalAmount) throws InsufficientFundsException, InvalidDepositException {
        double fee = withdrawalAmount * TRANSACTION_FEE_PERCENTAGE;
        wrappedAccount.retrieve(withdrawalAmount + fee);
        logTransaction("Transaction Fee (Withdrawal): -" + fee);
    }

    @Override
    public TYPE getType() {
        return wrappedAccount.getType();
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

}
