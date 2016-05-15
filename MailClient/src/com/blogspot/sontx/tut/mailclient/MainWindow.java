package com.blogspot.sontx.tut.mailclient;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringEscapeUtils;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JMenuItem;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<Message> messageModel;
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

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(mailList, popupMenu);

		JMenuItem mntmDownloadAttachs = new JMenuItem("Download attachs");
		mntmDownloadAttachs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Message message = mailList.getSelectedValue();
				downloadAttachs(message);
			}
		});
		popupMenu.add(mntmDownloadAttachs);

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Message message = mailList.getSelectedValue();
				deleteMail(message);
			}
		});
		popupMenu.add(mntmDelete);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		loadAllMails();
	}

	protected void downloadAttachs(Message message) {
		try {
			List<File> files = MailManager.getInstance().downloadAttachs(message);
			if (files != null && files.size() > 0) {
				JOptionPane.showMessageDialog(this, String.format("Downloaded %d files!", files.size()));
			} else {
				JOptionPane.showMessageDialog(this, "No attachs file in this mail!");
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}

	}

	protected void deleteMail(Message message) {
		try {
			if (JOptionPane.showConfirmDialog(this,
					String.format("Are you sure you want to delete '%s'?", message.getSubject()), "Delete mail",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				message.setFlag(Flags.Flag.DELETED, true);
				messageModel.removeElement(message);
			}
		} catch (HeadlessException | MessagingException e) {
			e.printStackTrace();
		}
	}

	private void loadAllMails() {
		try {
			Message[] messages = MailManager.getInstance().readAllMails();
			messageModel = new DefaultListModel<Message>();
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
				setText(String.format(
						"<html><font size=\"6\">%s</font> <br/> <font size=\"5\">%s</font> <br/> <font size=\"4\">%s</font></html>",
						StringEscapeUtils.escapeXml(value.getSubject()), value.getSentDate(),
						getSendFromString(value.getFrom())));
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

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
