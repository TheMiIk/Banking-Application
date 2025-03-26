package ro.uvt.dp;
import ro.uvt.dp.Account.Account;
import ro.uvt.dp.Exceptions.InvalidDepositException;

public interface AccountFactory {
    Account createAccount(Account.TYPE type, String accountNumber, double initialDeposit) throws InvalidDepositException;
}
