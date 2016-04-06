package com.blogspot.sontx.tut.mailclient;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Font;

public class ConnectionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private Account account = null;

	public Account getAccount() {
		return account;
	}

	public ConnectionDialog() {
		setTitle("Mail Client - Login");
		setResizable(false);
		getContentPane().setLayout(null);

		JLabel lblMailAddress = new JLabel("User name:");
		lblMailAddress.setBounds(10, 63, 71, 15);
		getContentPane().add(lblMailAddress);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 92, 71, 15);
		getContentPane().add(lblPassword);

		usernameField = new JTextField();
		usernameField.setBounds(91, 61, 227, 19);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(91, 89, 227, 19);
		getContentPane().add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(227, 163, 91, 25);
		btnLogin.addActionListener(this);
		getContentPane().add(btnLogin);
		
		JLabel lblWelcomeToGmail = new JLabel("Welcome to Gmail Client");
		lblWelcomeToGmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblWelcomeToGmail.setBounds(10, 11, 195, 14);
		getContentPane().add(lblWelcomeToGmail);

		setSize(332, 228);
		setModal(true);
		
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		if (username == null || (username = username.trim()).length() == 0) {
			usernameField.requestFocus();
		} else if (password.length() == 0) {
			passwordField.requestFocus();
		} else {
			account = new Account();
			account.setUsername(username);
			account.setPassword(password);
			dispose();
		}
	}

	public static class Account {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
