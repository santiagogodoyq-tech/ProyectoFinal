package model;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

    private final String correoEmisor = "pointmasterbank@gmail.com";
    private final String claveApp = "ccxp hgrs loxd zczt";

    private Session crearSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");  // Cambia si no usas Gmail
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoEmisor, claveApp);
            }
        });
    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            Message mensaje = new MimeMessage(crearSession());
            mensaje.setFrom(new InternetAddress(correoEmisor));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);

            System.out.println("Correo enviado a: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

