package ro.uvt.dp.Bank;

import ro.uvt.dp.Client.Client;
import ro.uvt.dp.Account.Account;
import ro.uvt.dp.BankCommand;
import ro.uvt.dp.Exceptions.InvalidDepositException;

import java.util.Arrays;

public class Bank {

	private static final int MAX_CLIENTS_NUMBER = 100;
	private Client[] clients;
	private int clientsNumber;
	private String bankCode = null;

	private Bank(String bankCode) {
		this.bankCode = bankCode;
		this.clients = new Client[MAX_CLIENTS_NUMBER];
	}

	private static class SingletonHelper {
		private static final Bank INSTANCE = new Bank("Global Bank");
	}

	public static Bank getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public void addClient(Client c) {
		clients[clientsNumber++] = c;
	}

	public Client getClient(String name) {
		for (int i = 0; i < clientsNumber; i++) {
			if (clients[i].getName().equals(name)) {
				return clients[i];
			}
		}
		return null;
	}

	public int getClientsCount() {
		return clientsNumber;
	}

	public Client getClient(int index) {
		if (index >= 0 && index < clientsNumber) {
			return clients[index];
		}
		return null;
	}

	@Override
	public String toString() {
		return "Bank [code=" + bankCode + ", clients=" + Arrays.toString(clients) + "]";
	}

	public static void main(String[] args) throws InvalidDepositException {
		Bank bank = Bank.getInstance();

		Client client1 = new Client.Builder("Ion Ionescu", "Timisoara", null)
				.addAccount(Account.TYPE.EUR, "EUR001", 1000)
				.build();

		BankInvoker invoker = new BankInvoker();

		BankCommand addClientCommand = new AddClientCommand(bank, client1);
		invoker.setCommand(addClientCommand);
		invoker.executeCommand();

		BankCommand getClientCommand = new GetClientCommand(bank, "Ion Ionescu");
		invoker.setCommand(getClientCommand);
		invoker.executeCommand();
	}
}
