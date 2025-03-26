package ro.uvt.dp.Client;

import ro.uvt.dp.Account.Account;
import java.util.ArrayList;
import java.util.List;
import ro.uvt.dp.ClientMediator;

public class ClientMediatorImpl implements ClientMediator {
    private Client client;
    private List<Account> accounts;

    public ClientMediatorImpl(Client client) {
        this.client = client;
        this.accounts = new ArrayList<>();
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }
}