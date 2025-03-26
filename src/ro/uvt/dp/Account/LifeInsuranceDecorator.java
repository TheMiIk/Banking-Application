package ro.uvt.dp.Account;

import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.Exceptions.InsufficientFundsException;


public class LifeInsuranceDecorator extends AccountDecorator {
    private static final double LIFE_INSURANCE_COST = 100.0;

    public LifeInsuranceDecorator(Account wrappedAccount) throws InvalidDepositException {
        super(wrappedAccount);
    }

    @Override
    public double getInterest() {
        return wrappedAccount.getInterest();
    }

    public void addLifeInsurance() {
        if (wrappedAccount.getTotalAmount() >= LIFE_INSURANCE_COST) {
            try {
                wrappedAccount.retrieve(LIFE_INSURANCE_COST);
                logTransaction("Life Insurance Added: -" + LIFE_INSURANCE_COST);
            } catch (InvalidDepositException | InsufficientFundsException e) {
                System.err.println("Error adding life insurance: " + e.getMessage());
            }
        } else {
            System.err.println("Insufficient funds to add life insurance.");
        }
    }

    @Override
    public TYPE getType() {
        return wrappedAccount.getType();
    }

    @Override
    public void transfer(Account targetAccount, double transferAmount) throws InvalidDepositException, InsufficientFundsException {
        if (transferAmount <= 0) {
            throw new InvalidDepositException("Transfer amount must be positive.");
        }
        if (transferAmount > this.amount) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
        }


        this.amount -= transferAmount;


        targetAccount.depose(transferAmount);


        logTransaction("Transfer to " + targetAccount.getAccountNumber() + ": -" + transferAmount);
    }

}
