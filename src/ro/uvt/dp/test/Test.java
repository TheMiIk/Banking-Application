package ro.uvt.dp.test;

import ro.uvt.dp.Bank.Bank;
import ro.uvt.dp.Bank.BankInvoker;
import ro.uvt.dp.BankCommand;
import ro.uvt.dp.Bank.AddClientCommand;
import ro.uvt.dp.Client.Client;
import ro.uvt.dp.Exceptions.InvalidDepositException;
import ro.uvt.dp.Account.AccountFactoryImp;
import ro.uvt.dp.Account.Account;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

	private JFrame frame;
	private Bank bank;
	private JLabel welcomeLabel;

	private List<String> sessionTransactions = new ArrayList<>();

	public Test(Bank bank) {
		this.bank = bank;
		initialize();
	}

	public void logTransaction(String transaction) {
		sessionTransactions.add(transaction);
	}

	private void initialize() {
		frame = new JFrame("Banking Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new CardLayout());

		JPanel loginPanel = createLoginPanel();
		frame.add(loginPanel, "Login");

		JPanel mainMenuPanel = createMainMenuPanel();
		frame.add(mainMenuPanel, "Main Menu");

		frame.setVisible(true);
	}

	private JPanel createLoginPanel() {
		JPanel loginPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		loginPanel.setBackground(new Color(255, 255, 255));

		Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
		Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(labelFont);
		JTextField nameField = new JTextField(15);
		nameField.setFont(fieldFont);

		JLabel badgeLabel = new JLabel("Badge ID:");
		badgeLabel.setFont(labelFont);
		JTextField badgeField = new JTextField(15);
		badgeField.setFont(fieldFont);

		JButton loginButton = createStyledButton("Login");
		loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

		loginButton.addActionListener(e -> {
			String name = nameField.getText();
			String badge = badgeField.getText();
			if (!name.isEmpty() && !badge.isEmpty()) {
				welcomeLabel.setText("Welcome, " + name);
				CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
				cl.show(frame.getContentPane(), "Main Menu");
			} else {
				JOptionPane.showMessageDialog(frame, "Please enter both name and badge ID.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.gridx = 0; gbc.gridy = 0;
		loginPanel.add(nameLabel, gbc);

		gbc.gridx = 1; gbc.gridy = 0;
		loginPanel.add(nameField, gbc);

		gbc.gridx = 0; gbc.gridy = 1;
		loginPanel.add(badgeLabel, gbc);

		gbc.gridx = 1; gbc.gridy = 1;
		loginPanel.add(badgeField, gbc);

		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
		loginPanel.add(loginButton, gbc);

		return loginPanel;
	}

	private JPanel createMainMenuPanel() {
		JPanel mainMenuPanel = new JPanel(new BorderLayout());
		mainMenuPanel.setBackground(new Color(250, 250, 250));

		welcomeLabel = new JLabel("Welcome, [Name]");
		welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainMenuPanel.add(welcomeLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
		buttonPanel.setBackground(new Color(250, 250, 250));

		JButton viewClientsButton = createStyledButton("View Clients");
		viewClientsButton.addActionListener(e -> showClientsPanel());
		buttonPanel.add(viewClientsButton);

		JButton viewTransactionsButton = createStyledButton("View Transactions");
		viewTransactionsButton.addActionListener(e -> showTransactionsPanel());
		buttonPanel.add(viewTransactionsButton);

		JButton initiateTransactionButton = createStyledButton("Initiate Transaction");
		initiateTransactionButton.addActionListener(e -> showTransactionInitiationPanel());
		buttonPanel.add(initiateTransactionButton);

		JButton exitButton = createStyledButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		buttonPanel.add(exitButton);

		mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

		return mainMenuPanel;
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(0, 123, 255));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(0, 102, 204));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(0, 123, 255));
			}
		});

		return button;
	}

	private void showClientsPanel() {
		StringBuilder clientInfo = new StringBuilder("Clients:\n");

		for (int i = 0; i < bank.getClientsCount(); i++) {
			Client client = bank.getClient(i);

			clientInfo.append("Name: ").append(client.getName()).append("\n");

			for (Account account : client.getAccounts()) {
				clientInfo.append("  ").append(account.toString()).append("\n");
			}

			clientInfo.append("\n");
		}

		JOptionPane.showMessageDialog(null, clientInfo.toString(), "Clients", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showTransactionsPanel() {
		JTextArea transactionsTextArea = new JTextArea();
		transactionsTextArea.setEditable(false);
		transactionsTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		transactionsTextArea.setBackground(new Color(250, 250, 250));
		transactionsTextArea.setLineWrap(true);
		transactionsTextArea.setWrapStyleWord(true);

		if (sessionTransactions.isEmpty()) {
			transactionsTextArea.append("No transactions yet in this session.\n");
		} else {
			for (String transaction : sessionTransactions) {
				transactionsTextArea.append(transaction + "\n");
			}
		}

		JScrollPane scrollPane = new JScrollPane(transactionsTextArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		JOptionPane.showMessageDialog(frame, scrollPane, "Transactions", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showTransactionInitiationPanel() {
		JPanel transactionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		transactionPanel.setBackground(new Color(250, 250, 250));

		JLabel senderLabel = new JLabel("Sender:");
		senderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JTextField senderField = new JTextField(15);
		senderField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JLabel receiverLabel = new JLabel("Receiver:");
		receiverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JTextField receiverField = new JTextField(15);
		receiverField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JLabel currencyLabel = new JLabel("Currency:");
		currencyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		String[] currencies = {"RON", "EUR"};
		JComboBox<String> currencyComboBox = new JComboBox<>(currencies);

		JLabel amountLabel = new JLabel("Amount:");
		amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JTextField amountField = new JTextField(15);
		amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JButton confirmButton = new JButton("Confirm");
		confirmButton.setBackground(new Color(0, 123, 255));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		confirmButton.setFocusPainted(false);

		confirmButton.addActionListener(e -> {
			String sender = senderField.getText();
			String receiver = receiverField.getText();
			String amountText = amountField.getText();
			String selectedCurrency = (String) currencyComboBox.getSelectedItem();

			try {
				double amount = Double.parseDouble(amountText);
				Client senderClient = bank.getClient(sender);
				Client receiverClient = bank.getClient(receiver);

				if (senderClient != null && receiverClient != null) {
					Account senderAccount = null;
					Account receiverAccount = null;

					for (Account account : senderClient.getAccounts()) {
						if (account.getType() == Account.TYPE.valueOf(selectedCurrency)) {
							senderAccount = account;
							break;
						}
					}

					for (Account account : receiverClient.getAccounts()) {
						if (account.getType() == Account.TYPE.valueOf(selectedCurrency)) {
							receiverAccount = account;
							break;
						}
					}

					if (senderAccount != null && receiverAccount != null) {
						senderAccount.transfer(receiverAccount, amount);
						logTransaction("Transferred " + amount + " " + selectedCurrency + " from " + sender + " to " + receiver);
						JOptionPane.showMessageDialog(frame, "Transaction completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frame, "Sender or receiver does not have the selected currency.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Invalid sender or receiver.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Transaction failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0; gbc.gridy = 0;
		transactionPanel.add(senderLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 0;
		transactionPanel.add(senderField, gbc);

		gbc.gridx = 0; gbc.gridy = 1;
		transactionPanel.add(receiverLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 1;
		transactionPanel.add(receiverField, gbc);

		gbc.gridx = 0; gbc.gridy = 2;
		transactionPanel.add(currencyLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 2;
		transactionPanel.add(currencyComboBox, gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		transactionPanel.add(amountLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 3;
		transactionPanel.add(amountField, gbc);

		gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
		transactionPanel.add(confirmButton, gbc);

		JOptionPane.showMessageDialog(frame, transactionPanel, "Initiate Transaction", JOptionPane.PLAIN_MESSAGE);
	}

	public static void main(String[] args) throws InvalidDepositException {
		Bank bank = Bank.getInstance();

		Client client1 = new Client.Builder("Ion Ionescu", "Timisoara", new AccountFactoryImp())
				.addAccount(Account.TYPE.EUR, "EUR001", 1000.0)
				.addAccount(Account.TYPE.RON, "RON001", 500.0)
				.build();

		Client client2 = new Client.Builder("Maria Popescu", "Bucharest", new AccountFactoryImp())
				.addAccount(Account.TYPE.EUR, "EUR002", 1500.0)
				.addAccount(Account.TYPE.RON, "RON002", 750.0)
				.build();

		BankCommand addClientCommand1 = new AddClientCommand(bank, client1);
		BankCommand addClientCommand2 = new AddClientCommand(bank, client2);

		BankInvoker invoker = new BankInvoker();
		invoker.setCommand(addClientCommand1);
		invoker.executeCommand();
		invoker.setCommand(addClientCommand2);
		invoker.executeCommand();

		SwingUtilities.invokeLater(() -> new Test(bank));
	}
}
