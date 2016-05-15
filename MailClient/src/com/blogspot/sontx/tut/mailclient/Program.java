package com.blogspot.sontx.tut.mailclient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import com.blogspot.sontx.tut.mailclient.ConnectionDialog.Account;

public class Program extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			MailManager.getInstance().endReadMails();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ConnectionDialog dialog = new ConnectionDialog();
		dialog.setVisible(true);
		Account account = dialog.getAccount();
		if (account != null) {
			MailManager.getInstance().initializeGMailAccount(account.getUsername(), account.getPassword());
			try {
				MailManager.getInstance().beginReadMails();
				MainWindow window = new MainWindow();
				window.addWindowListener(new Program());
				window.setVisible(true);
			} catch (MessagingException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		} else {
			System.exit(0);
		}
	}

}
