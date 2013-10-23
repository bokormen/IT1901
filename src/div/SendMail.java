package div;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static boolean sendMailTo(String email, String newPassword)
			throws Exception {

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
			message.setSubject("New password for your sheepcontrol user");
			message.setText("Yo dude, here is your new password: "
					+ newPassword);

			Transport.send(message);
			return true;

		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}
	}
}