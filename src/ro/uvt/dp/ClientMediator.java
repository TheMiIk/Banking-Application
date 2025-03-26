package ro.uvt.dp;

import ro.uvt.dp.Account.Account;
import java.util.List;

public interface ClientMediator {
    void addAccount(Account account);
    Account getAccount(String accountNumber);
    List<Account> getAccounts();
}