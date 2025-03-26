package ro.uvt.dp.Bank;
import ro.uvt.dp.BankCommand;

public class BankInvoker {
    private BankCommand command;

    public void setCommand(BankCommand command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }
}

