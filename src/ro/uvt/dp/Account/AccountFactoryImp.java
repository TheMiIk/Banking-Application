package ro.uvt.dp.Account;
import ro.uvt.dp.AccountFactory;

import ro.uvt.dp.Exceptions.InvalidDepositException;

public class AccountFactoryImp implements AccountFactory {
    @Override
    public Account createAccount(Account.TYPE type, String accountNumber, double initialDeposit) throws InvalidDepositException {
        switch (type) {
            case EUR:
                return new AccountEUR(accountNumber, initialDeposit);
            case RON:
                return new AccountRON(accountNumber, initialDeposit);
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}
