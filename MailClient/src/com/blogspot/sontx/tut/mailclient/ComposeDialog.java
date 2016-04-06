package com.blogspot.sontx.tut.mailclient;

import javax.swing.JDialog;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.mail.MessagingException;
import javax.swing.JButton;

public class ComposeDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField subjectField;
	private JTextField toField;
	private JTextArea contentField;

	public ComposeDialog() {
		setTitle("Mail Client- Compose");
		setSize(514, 424);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.NORTH);
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblSubjet = new JLabel("Subject:  ");
		panel.add(lblSubjet, BorderLayout.WEST);

		subjectField = new JTextField();
		panel.add(subjectField, BorderLayout.CENTER);
		subjectField.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblTo = new JLabel("To:          ");
		panel_2.add(lblTo, BorderLayout.WEST);

		toField = new JTextField();
		toField.setColumns(10);
		panel_2.add(toField);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_3.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton btnSend = new JButton("Send");
		panel_4.add(btnSend, BorderLayout.EAST);
		btnSend.addActionListener(this);

		contentField = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(contentField);
		panel_3.add(scrollPane, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		
		setModal(true);
	}

	private MailHolder getMailHolder() {
		String subject = subjectField.getText();
		String to = toField.getText();
		String content = contentField.getText();
		if (subject == null || ((subject = subject.trim()).length() == 0)) {
			subjectField.requestFocus();
			return null;
		}
		if (to == null || ((to = to.trim()).length() == 0)) {
			toField.requestFocus();
			return null;
		}
		if (content == null || ((content = content.trim()).length() == 0)) {
			contentField.requestFocus();
			return null;
		}
		return new MailHolder(subject, to, content);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MailHolder holder = getMailHolder();
		if (holder == null) {
			JOptionPane.showMessageDialog(this, "Enter valid info into this field!");
		} else {
			try {
				MailManager.getInstance().sendMail(holder.to, holder.subject, holder.content);
				JOptionPane.showMessageDialog(this, "Mail sent successfully...", "Mail Client",
						JOptionPane.PLAIN_MESSAGE);
			} catch (MessagingException e1) {
				JOptionPane.showMessageDialog(this, "Mail sent fail: " + e1.getMessage(), "Mail Client",
						JOptionPane.PLAIN_MESSAGE);
			}
			dispose();
		}
	}

	private static class MailHolder {
		String subject;
		String to;
		String content;

		public MailHolder(String subject, String to, String content) {
			this.subject = subject;
			this.to = to;
			this.content = content;
		}
	}
}
