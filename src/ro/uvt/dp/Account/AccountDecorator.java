package ro.uvt.dp.Account;

import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public abstract class AccountDecorator extends Account {
    protected Account wrappedAccount;

    public AccountDecorator(Account wrappedAccount) throws InvalidDepositException {
        super(wrappedAccount.getAccountNumber(), 0);
        this.wrappedAccount = wrappedAccount;
    }

    @Override
    public double getTotalAmount() {
        return wrappedAccount.getTotalAmount();
    }

    @Override
    public void depose(double depositAmount) throws InvalidDepositException {
        wrappedAccount.depose(depositAmount);
    }

    @Override
    public void retrieve(double withdrawalAmount) throws InsufficientFundsException, InvalidDepositException {
        wrappedAccount.retrieve(withdrawalAmount);
    }

    @Override
    public String toString() {
        return wrappedAccount.toString();
    }

    @Override
    public String getAccountNumber() {
        return wrappedAccount.getAccountNumber();
    }
}

