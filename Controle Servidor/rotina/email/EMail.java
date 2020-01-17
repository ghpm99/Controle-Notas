/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rotina.Email;

import BancoDados.Conexao;
import BancoDados.GerenciadorBanco;
import Configuracao.Referencia;
import GUI.principal.Principal;
import Util.Sobre;
import com.sun.mail.util.MailSSLSocketFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

/**
 *
 * @author guilherme.machado
 */
public class EMail {

    ArrayList<Conexao> bancos = new ArrayList<>();

    /**
     *
     */
    public void iniciar() {

        String host = "imap-mail.outlook.com";
        String mailStoreType = "imaps";

        fetch(host, mailStoreType, Referencia.getInstancia().getEmail(), Referencia.getInstancia().getSenhaemail());

    }

    private void fetch(String pop3Host, String storeType, String user, String password) {

        try {

            Properties properties = new Properties();
            MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
            socketFactory.setTrustAllHosts(true);
            properties.put("mail.imap.ssl.socketFactory", socketFactory);
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imap.host", pop3Host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.disabletop", "true");
            Session emailSession = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);//To change body of generated methods, choose Tools | Templates.
                }

            });

            Store store = emailSession.getStore("imaps");

            store.connect(pop3Host, user, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Flags seen = new Flags(Flags.Flag.FLAGGED);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message[] messages = emailFolder.search(unseenFlagTerm);

            Principal.setStatus("Numero de E-Mails:" + messages.length);

            GerenciadorBanco.INSTANCIA.getNomeBancos().forEach(s -> {
                Conexao a = GerenciadorBanco.INSTANCIA.getConexao(s);
                if (a.pmWeb) {
                    bancos.add(a);
                }
            });

            for (int i = 0; i < messages.length; i++) {
                Principal.getInstancia().progressoBarra.setValue((int) (((double) 100 / messages.length) * i));
                Status temp = new Status();
                Message message = messages[i];
                writePart(message, temp);
                bancos.forEach(s -> {
                    BancoDados.modulo.ModuloNota.getInstancia().workflow(s, temp, 0);
                });
                message.setFlag(Flags.Flag.FLAGGED, true);
            }

            emailFolder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException | GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    private void writePart(Part p, Status status) {

        try {
            if (p instanceof Message) {
                Message m = (Message) p;

                // SUBJECT
                if (m.getSubject() != null) {
                    String temp = m.getSubject().toUpperCase();
                    if (temp.contains("approved".toUpperCase())) {
                        status.setStatus("approved");
                    } else if (temp.contains("Withdrawal".toUpperCase())) {
                        status.setStatus("Withdrawal");
                    } else if (temp.contains("Return".toUpperCase())) {
                        status.setStatus("Return");
                    } else if (temp.contains("Rejection".toUpperCase())) {
                        status.setStatus("Rejection");
                    } else {
                        return;
                    }
                    //Withdrawal
                    //Return
                    //Rejection
                    //approved

                } else {
                    return;
                }

            }
            //check if the content is plain text
            if (p.isMimeType("text/plain")) {
                separar((String) p.getContent(), status);
            } //check if the content has attachment
            else if (p.isMimeType("multipart/*")) {

                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    writePart(mp.getBodyPart(i), status);
                }

            } //check if the content is a nested message
            else if (p.isMimeType("message/rfc822")) {

                writePart((Part) p.getContent(), status);
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void separar(String arg, Status status) {
        String[] aa = arg.split("<");
        for (String a : aa) {            
            if (a.startsWith("https://pmw.tishmanspeyer.com/pmweb/CostManagementProgressInvoices.aspx?")) {                
                String[] b = a.split(">");
                String c = b[0].replaceAll("https://pmw.tishmanspeyer.com/pmweb/CostManagementProgressInvoices.aspx?", "");
                String[] d = c.split("&");
                status.setId(Integer.valueOf(d[0].replaceAll("[^0-9]", "")));
            }
        }
    }

    /**
     *
     * @param avaliacao
     * @param assunto
     * @param campo
     * @param usuario
     */
    public void enviarSugestao(int avaliacao, String assunto, String campo, String usuario) {
        try {
            String para = "ghpm99@hotmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "smtp-mail.outlook.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Referencia.getInstancia().getEmail()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(para));
            message.setSubject("Avaliação syscon: " + assunto);
            message.setText("Avaliação: " + avaliacao
                    + "\nMensagem:" + campo
                    + "\nUsuario: " + usuario
                    + "\nVersao do servidor: " + Sobre.getMensagem());
            Transport.send(message, Referencia.getInstancia().getEmail(), Referencia.getInstancia().getSenhaemail());
        } catch (AddressException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
