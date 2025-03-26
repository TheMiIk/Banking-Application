package ro.uvt.dp.Bank;

import ro.uvt.dp.BankCommand;
import ro.uvt.dp.Client.Client;

public class GetClientCommand implements BankCommand {
    private Bank bank;
    private String clientName;

    public GetClientCommand(Bank bank, String clientName) {
        this.bank = bank;
        this.clientName = clientName;
    }

    @Override
    public void execute() {
        Client client = bank.getClient(clientName);
        if (client != null) {
            System.out.println("Client found: " + client);
        } else {
            System.out.println("Client with name " + clientName + " not found.");
        }
    }
}
