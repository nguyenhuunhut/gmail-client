package com.blogspot.sontx.tut.mailclient;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailManager {
	private static MailManager instance = new MailManager();

	public static MailManager getInstance() {
		return instance;
	}

	private static final String SENDING_HOST = "smtp.gmail.com";
	private static final int SENDING_PORT = 465;
	private static final String RECEIVING_HOST = "imap.gmail.com";

	private String userName;
	private String password;
	private String from;

	public void initializeGMailAccount(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.from = String.format("%s@gmail.com", userName);
	}

	private Properties getSendingMailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", String.valueOf(465));
		props.put("mail.smtp.user", this.userName);
		props.put("mail.smtp.password", this.password);
		props.put("mail.smtp.auth", "true");
		return props;
	}

	private Properties getReceivingMailProperties() {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		return props;
	}

	public void sendMail(String to, String subject, String content) throws MessagingException {
		Session sendingMailSession = Session.getDefaultInstance(getSendingMailProperties());
		Message simpleMessage = new MimeMessage(sendingMailSession);
		InternetAddress fromAddress = new InternetAddress(from);
		InternetAddress toAddress = new InternetAddress(to);

		simpleMessage.setFrom(fromAddress);
		simpleMessage.setRecipient(RecipientType.TO, toAddress);
		simpleMessage.setSubject(subject);
		simpleMessage.setText(content);

		Transport transport = sendingMailSession.getTransport("smtps");
		transport.connect(SENDING_HOST, SENDING_PORT, this.userName, this.password);
		transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
		transport.close();
	}

	private Message[] messages;
	private Folder folder;
	private Store store;

	public void beginReadMails() throws MessagingException {
		Session receivingingMailSession = Session.getDefaultInstance(getReceivingMailProperties(), null);
		receivingingMailSession.setDebug(true);
		store = receivingingMailSession.getStore("imaps");
		store.connect(RECEIVING_HOST, userName, password);
		folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		messages = folder.getMessages();
	}

	public Message[] readAllMails() throws MessagingException {
		return messages;
	}

	public void endReadMails() throws MessagingException {
		folder.close(false);
		store.close();
		messages = null;
		folder = null;
		store = null;
	}
}
