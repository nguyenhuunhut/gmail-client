package com.blogspot.sontx.tut.mailclient;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringEscapeUtils;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JList<Message> mailList;

	public MainWindow() {
		setTitle("Mail Client");
		setSize(682, 460);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));
		getContentPane().add(panel, BorderLayout.NORTH);

		JButton btnCompose = new JButton("COMPOSE");
		panel.add(btnCompose);
		btnCompose.addActionListener(this);

		mailList = new JList<>();
		mailList.setCellRenderer(new MessageCellRenderer());
		JScrollPane scrollPane = new JScrollPane(mailList);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		loadAllMails();
	}
	
	private void loadAllMails() {
		try {
			Message[] messages = MailManager.getInstance().readAllMails();
			DefaultListModel<Message> messageModel = new DefaultListModel<Message>();
			for (Message message : messages) {
				messageModel.addElement(message);
			}
			mailList.setModel(messageModel);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(this, "Can not read mail: " + e.getMessage(), "Mail Client",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ComposeDialog dialog = new ComposeDialog();
		dialog.setVisible(true);
	}
	
	private static class MessageCellRenderer extends JLabel implements ListCellRenderer<Message> {
		private static final long serialVersionUID = 7285154224115806852L;
		private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public MessageCellRenderer() {
			setOpaque(true);
			setIconTextGap(12);
		}

		private String getSendFromString(Address[] addresses) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < addresses.length - 1; i++) {
				builder.append(addresses[i]);
				builder.append(", ");
			}
			builder.append(addresses[addresses.length - 1]);
			return builder.toString();
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index,
				boolean isSelected, boolean cellHasFocus) {
			try {
				setText(String.format("<html><font size=\"6\">%s</font> <br/> <font size=\"5\">%s</font> <br/> <font size=\"4\">%s</font></html>",StringEscapeUtils.escapeXml(value.getSubject()),
						value.getSentDate(), getSendFromString(value.getFrom())));
			} catch (MessagingException e) {
				setText(e.getMessage());
				setBackground(Color.RED);
				return this;
			}
			if (isSelected) {
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} else {
				setBackground(index % 2 == 0 ? Color.white : Color.lightGray);
				setForeground(Color.black);
			}
			return this;
		}
	}
}
