package org.boring.bitcoinAlerts.smsSender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.domain.User;
import org.boring.bitcoinAlerts.watcher.CoindeskCurrentBtcPrice;
import org.boring.bitcoinAlerts.watcher.TwentyFourHrBtcPrice;
import org.boring.bitcoinAlerts.watcher.TwentyFourHrXacCount;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="verizonSender")
@Scope (value="singleton")
public class VerizonSender implements BtcNetworkMetricListener {
	private static final Logger log = LogManager.getLogger(VerizonSender.class);
	
	
	public void alert(Alert alert) {
		
		String[] numbers = user.getUserPhoneNumbers();
		
		send(alert.getSubject(), alert.getMessage(),numbers);
		log.debug("sent message: {}", alert.getMessage());
	}
	
	@Autowired private CoindeskCurrentBtcPrice coindeskCurrentBtcPrice;
	@Autowired private TwentyFourHrBtcPrice twentyFourHrBtcPrice;
	@Autowired private TwentyFourHrXacCount twentyFourHrXacCount;
	
	
	private Properties mailProperties = new Properties();
	public static final String PROPERTIESFILE = "application.properties";
	
	private User user = new User();
	
	@Value("${mailboxUserEmail}") private String mailboxUserEmail;
	@Value("${emailSessionPassword}") private String emailSessionPassword;
	@Value("${isMsgSenderDisabled}") private boolean isMsgSenderDisabled;
	@Value("${secretKey}") private String secretKey;


	@PostConstruct
	public void init() {
		coindeskCurrentBtcPrice.addListener(this);
		twentyFourHrBtcPrice.addListener(this);
		twentyFourHrXacCount.addListener(this);
		
		
		mailProperties.put("mail.smtp.host", "smtp.gmail.com");
		mailProperties.put("mail.smtp.port", "587");
		mailProperties.put("mail.smtp.auth", "true");
		mailProperties.put("mail.smtp.starttls.enable", "true"); // TLS
		System.out.println("Send email/text disable status="+isMsgSenderDisabled);
		
		try (InputStream input = VerizonSender.class.getClassLoader().getResourceAsStream(PROPERTIESFILE)) {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword(secretKey);
			EncryptableProperties prop = new EncryptableProperties(encryptor);

			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return;
			}
			prop.load(input);
			System.setProperty("emailSessionPassword", prop.getProperty("emailSessionPassword"));
			System.out.println(System.getProperty("emailSessionPassword"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	

	private void send(String subject, String text, String[] numbers) {
		String password = System.getProperty("emailSessionPassword");
		Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(mailboxUserEmail, password);
			}
		});
		try {
			for(String phone : numbers) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailboxUserEmail));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(phone+"@vzwpix.com"));
				message.setSubject(subject);
				message.setText(text);
				transportSend(message);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private void transportSend(Message message) throws MessagingException {
		Transport.send(message);
		
//		if (!isMsgSenderDisabled) {
//			Transport.send(message);
//		}
//		else
//			System.out.println("not sent, message sender is disabled");
			
	}


}
// pa 2023026064
// Shawn 8433436587












