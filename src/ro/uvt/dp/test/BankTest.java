package ro.uvt.dp.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ro.uvt.dp.Account.Account;
import ro.uvt.dp.Account.AccountEUR;
import ro.uvt.dp.Account.AccountRON;
import ro.uvt.dp.AccountFactory;
import ro.uvt.dp.Bank.Bank;
import ro.uvt.dp.Client.Client;
import ro.uvt.dp.Account.Account.TYPE;
import ro.uvt.dp.Exceptions.InsufficientFundsException;
import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.Account.AccountFactoryImp;
import ro.uvt.dp.Account.AccountDecorator;
import ro.uvt.dp.Operations;
import ro.uvt.dp.BankCommand;
import ro.uvt.dp.Bank.AddClientCommand;
import ro.uvt.dp.Bank.BankInvoker;
import ro.uvt.dp.Client.ClientMediatorImpl;
import ro.uvt.dp.ClientMediator;


public class BankTest {

    private Bank bcr;
    private Bank bank;
    private Client cl1;
    private Client cl2;
    private AccountFactory accountFactory;

    @Before
    public void setUp() throws InvalidDepositException {
        bcr = Bank.getInstance();  // singleton
        accountFactory = new AccountFactoryImp();

        cl1 = new Client.Builder("Ionescu Ion", "Timisoara", accountFactory)
                .addAccount(Account.TYPE.EUR, "EUR124", 200.9)
                .addAccount(Account.TYPE.RON, "RON1234", 400)
                .build();

        bcr.addClient(cl1);

        cl2 = new Client.Builder("Marinescu Marin", "Timisoara", accountFactory)
                .addAccount(Account.TYPE.RON, "RON126", 100)
                .build();

        bcr.addClient(cl2);
    }

    @Test
    public void testTransactionFeeDecorator() {

        double initialAmount = 200.9;
        double depositAmount = 100;
        double fee = depositAmount * 0.01;
        double expectedAmountAfterDeposit = initialAmount + (depositAmount - fee);

        double actualAmountAfterDeposit = initialAmount + (depositAmount - fee);

        assertEquals(expectedAmountAfterDeposit, actualAmountAfterDeposit, 0.01);

        double withdrawalAmount = 50;
        double withdrawalFee = withdrawalAmount * 0.01;
        double expectedAmountAfterWithdrawal = expectedAmountAfterDeposit - (withdrawalAmount + withdrawalFee);

        double actualAmountAfterWithdrawal = expectedAmountAfterDeposit - (withdrawalAmount + withdrawalFee);

        assertEquals(expectedAmountAfterWithdrawal, actualAmountAfterWithdrawal, 0.01);
    }

    @Test
    public void testLifeInsuranceDecorator() {

        double initialAmount = 400;
        double lifeInsuranceCost = 100;
        double expectedAmountAfterInsurance = initialAmount - lifeInsuranceCost;

        double actualAmountAfterInsurance = initialAmount - lifeInsuranceCost;

        assertEquals(expectedAmountAfterInsurance, actualAmountAfterInsurance, 0.01);
    }

    @Test
    public void testTransactionFeeAndLifeInsuranceTogether() {

        double initialAmount = 100;
        double depositAmount = 200;
        double transactionFee = depositAmount * 0.01;
        double amountAfterDeposit = initialAmount + (depositAmount - transactionFee);

        double lifeInsuranceCost = 100;
        double amountAfterInsurance = amountAfterDeposit - lifeInsuranceCost;

        double finalAmount = amountAfterInsurance;

        assertEquals(finalAmount, amountAfterInsurance, 0.01);
    }

    @Test
    public void testAccountFactoryCreatesEuroAccount() throws InvalidDepositException {
        Account euroAccount = accountFactory.createAccount(Account.TYPE.EUR, "EUR129", 500);
        assertNotNull(euroAccount);
        assertTrue(euroAccount instanceof AccountEUR);
        assertEquals("EUR129", euroAccount.getAccountNumber());
        double expectedAmount = 500 + (500 * 0.01);
        assertEquals(expectedAmount, euroAccount.getTotalAmount(), 0.01);
    }

    @Test
    public void testAccountFactoryCreatesRonAccount() throws InvalidDepositException {
        Account ronAccount = accountFactory.createAccount(Account.TYPE.RON, "RON129", 300);
        assertNotNull(ronAccount);
        assertTrue(ronAccount instanceof AccountRON);
        assertEquals("RON129", ronAccount.getAccountNumber());
        double expectedAmount = 300 + (300 * 0.03);
        assertEquals(expectedAmount, ronAccount.getTotalAmount(), 0.01);
    }

    @Test
    public void testAddClient() {
        assertNotNull(cl1);
        assertNotNull(cl2);

        assertEquals("Ionescu Ion", cl1.getName());
        assertEquals("Marinescu Marin", cl2.getName());

        assertEquals("Timisoara", cl1.getAddress());
        assertEquals("Timisoara", cl2.getAddress());
    }

    @Test
    public void testDeposit() throws InvalidDepositException {
        Account account = cl2.getAccount("RON126");
        assertNotNull(account);

        account.depose(400);

        double expectedAmount = 500;
        double expectedAmountWithInterest = expectedAmount + (expectedAmount * 0.08);
        assertEquals(expectedAmountWithInterest, account.getTotalAmount(), 0.01);
    }

    @Test
    public void testRetrieve() throws InvalidDepositException, InsufficientFundsException {
        Account account = cl2.getAccount("RON126");
        assertNotNull(account);

        account.retrieve(50);

        double expectedAmount = 50;
        double expectedAmountWithInterest = expectedAmount + (expectedAmount * 0.03);
        assertEquals(expectedAmountWithInterest, account.getTotalAmount(), 0.01);
    }

    @Test
    public void testGetClientByName() {
        Client retrievedClient = bcr.getClient("Ionescu Ion");
        assertNotNull(retrievedClient);
        assertEquals("Ionescu Ion", retrievedClient.getName());
    }

    @Test
    public void testClientHasMultipleAccounts() {
        assertNotNull(cl1.getAccount("EUR124"));
        assertNotNull(cl1.getAccount("RON1234"));
    }

    @Test
    public void testSingletonPattern() {
        Bank bankInstance1 = Bank.getInstance();
        Bank bankInstance2 = Bank.getInstance();

        assertSame(bankInstance1, bankInstance2);
    }

    @Test
    public void testExecuteBankCommand() {
        BankInvoker invoker = new BankInvoker();
        BankCommand addClientCommand = new AddClientCommand(bcr, cl1);
        invoker.setCommand(addClientCommand);
        invoker.executeCommand();
    }

    @Test
    public void testMediatorClientHasAccounts() throws InvalidDepositException {
        ClientMediator mediator = new ClientMediatorImpl(cl1);

        Account account1 = accountFactory.createAccount(TYPE.EUR, "EUR125", 100);
        Account account2 = accountFactory.createAccount(TYPE.RON, "RON125", 300);

        mediator.addAccount(account1);
        mediator.addAccount(account2);

        assertNotNull(mediator.getAccount("EUR125"));
        assertNotNull(mediator.getAccount("RON125"));

        assertEquals("EUR125", mediator.getAccount("EUR125").getAccountNumber());
        assertEquals("RON125", mediator.getAccount("RON125").getAccountNumber());
    }

    @Test
    public void testMediatorMultipleAccounts() throws InvalidDepositException {
        ClientMediator mediator = new ClientMediatorImpl(cl1);

        Account account1 = accountFactory.createAccount(TYPE.EUR, "EUR128", 100);
        Account account2 = accountFactory.createAccount(TYPE.RON, "RON128", 500);

        mediator.addAccount(account1);
        mediator.addAccount(account2);

        assertTrue(mediator.getAccounts().size() > 1);
    }
}