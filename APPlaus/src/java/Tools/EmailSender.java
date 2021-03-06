package Tools;

import APPlausException.InputException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author eirikstadheim
 */
public class EmailSender {
    private static final EmailSender INSTANCE = new EmailSender();
    private final Session session;
    
    private EmailSender() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("evry.applaus@gmail.com", "evryErBest");
            }
        });
    }
    
    public static EmailSender getInstance() {
        return INSTANCE;
    }
    
    public void sendNewPassword(String email, String password)
            throws InputException {
        if(email == null || password == null) {
            throw new InputException("Email or password is null.");
        }
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("APPlaus"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Nytt passord");
            message.setText("Hei!"
                    + "\n\n Du har bedt om å bli tilsendt et nytt passord. "
                    + "Ditt nye passord er: \n\n" + password + "\n\nHa en fin "
                    + "dag!");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
