package ro.uvt.dp.Bank;

import ro.uvt.dp.BankCommand;
import ro.uvt.dp.Client.Client;

public class AddClientCommand implements BankCommand {
    private Bank bank;
    private Client client;

    public AddClientCommand(Bank bank, Client client) {
        this.bank = bank;
        this.client = client;
    }

    @Override
    public void execute() {
        bank.addClient(client);
    }
}
