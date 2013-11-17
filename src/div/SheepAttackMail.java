package div;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SheepAttackMail {
	

	/**
	 * Sender mail om angrep på en sau til eieren. 
	 * @param email Epostaddressen til eieren
	 * @param id Id til sau
	 * @param position Posisjon til sau under angrep.
	 */
	public static void sendMail(String email, int id, String position)    {
		final String username = "sheepcontrolit1901@gmail.com";
		final String password = "manuer123";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sheepcontrolit1901@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Sheep attack");
			message.setText("Sheep " + id + " may be under attack. Position: " + position);

			Transport.send(message);
		} catch(MessagingException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}



}
