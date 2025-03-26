package ro.uvt.dp.Client;

import ro.uvt.dp.Account.Account;
import ro.uvt.dp.AccountFactory;
import ro.uvt.dp.Account.Account.TYPE;
import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.ClientMediator;

import java.util.ArrayList;
import java.util.List;

public class Client {
	private String name;
	private String address;
	private ClientMediator mediator;

	private Client(Builder builder) {
		this.name = builder.name;
		this.address = builder.address;
		this.mediator = new ClientMediatorImpl(this);

		for (Account account : builder.accounts) {
			mediator.addAccount(account);
		}
	}

	public static class Builder {
		private String name;
		private String address;
		private List<Account> accounts = new ArrayList<>();
		private AccountFactory accountFactory;

		public Builder(String name, String address, AccountFactory accountFactory) {
			this.name = name;
			this.address = address;
			this.accountFactory = accountFactory;
		}

		public Builder addAccount(TYPE type, String accountNumber, double amount) throws InvalidDepositException {
			Account account = accountFactory.createAccount(type, accountNumber, amount);
			accounts.add(account);
			return this;
		}

		public Client build() {
			return new Client(this);
		}
	}

	public Account getAccount(String accountCode) {
		return mediator.getAccount(accountCode);
	}

	@Override
	public String toString() {
		return "\n\tClient [name=" + name + ", address=" + address + ", accounts=" + mediator.getAccounts() + "]";
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void addAccount(Account account) {
		mediator.addAccount(account);
	}

	public List<Account> getAccounts() {
		return mediator.getAccounts();
	}
}