package com.blogspot.sontx.tut.mailclient;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import com.blogspot.sontx.tut.mailclient.ConnectionDialog.Account;

public class Program {

	public static void main(String[] args) {
		ConnectionDialog dialog = new ConnectionDialog();
		dialog.setVisible(true);
		Account account = dialog.getAccount();
		if (account != null) {
			MailManager.getInstance().initializeGMailAccount(account.getUsername(), account.getPassword());
			try {
				MailManager.getInstance().beginReadMails();
				MainWindow window = new MainWindow();
				window.setVisible(true);
				MailManager.getInstance().endReadMails();
			} catch (MessagingException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		} else {
			System.exit(0);
		}
	}

}
