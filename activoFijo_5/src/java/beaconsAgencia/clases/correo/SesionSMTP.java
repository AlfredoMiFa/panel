/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.correo;

import javax.mail.internet.*;
import javax.mail.*;

import java.util.*;

/**
 *
 * @author jgonzalezc
 */
public class SesionSMTP {

    //Atributos de la clase
    private String servidor;
    private String usuario;
    private String password;
    private int puerto;
    private boolean debug = true;

    public SesionSMTP(String servidor, int puerto, String usuario,String passwd) {
        this.servidor = servidor;
        this.usuario = usuario;
        this.password = passwd;
        this.puerto = puerto;
    }

    /**
     *Metodo que se encarga de enviar los correos electronicos
     * @param correo Objeto de la clase CorreoSMTP con el contenido e información del correo electronico
     */
    public void enviaCorreoHTML(CorreoSMTP correo) throws Exception{
        try {
            //Creamos las propiedades
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.host", this.servidor);
            propiedades.setProperty("mail.smtp.auth", "true");
            propiedades.setProperty("mail.debug", "false");
            propiedades.setProperty("mail.smtp.ssl.enable", "true");


            SMTPAuthentication auth = new SMTPAuthentication(usuario,password);
            Session sesion = Session.getInstance(propiedades, auth);
            sesion.setDebug(debug);

            //Creamos un mensaje de correo de javamail
            MimeMessage mensajeCorreo = new MimeMessage(sesion);
            //Preparamos el mensaje de correo con los datos de nuestra clase CorreoSMTP pasados en el objeto correo            
            this.preparaMensajeHTML(mensajeCorreo, correo);

            //Finalmente nos conectamos y enviamos el correo electronico
            Transport transportador = sesion.getTransport("smtp");
            transportador.connect(this.usuario, this.password);
            //transportador.connect(this.servidor, this.usuario, this.password);
            transportador.send(mensajeCorreo);
            transportador.close();
        } catch (Exception ex) {
            System.out.println("Error en el metodo enviaCorreo de la clase SesionSMTP");
            ex.printStackTrace();
            throw ex;
        }        
    }

    public void enviaCorreo(CorreoSMTP correo) {
        try {
            //Creamos las propiedades
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.host", this.servidor);
            propiedades.setProperty("mail.smtp.auth", "true");

            SMTPAuthentication auth = new SMTPAuthentication(usuario,password);
            Session sesion = Session.getInstance(propiedades, auth);
            sesion.setDebug(debug);

            //Creamos un mensaje de correo de javamail
            MimeMessage mensajeCorreo = new MimeMessage(sesion);
            //Preparamos el mensaje de correo con los datos de nuestra clase CorreoSMTP pasados en el objeto correo            
            this.preparaMensaje(mensajeCorreo, correo);

            //Finalmente nos conectamos y enviamos el correo electronico
            Transport transportador = sesion.getTransport("smtp");
            transportador.send(mensajeCorreo);
            transportador.close();
        } catch (Exception ex) {
            System.out.println("Error en el metodo enviaCorreo de la clase SesionSMTP");
            ex.printStackTrace();
        }
    }

    /**
     *Metodo que se encarga de hacer la traduccion de nuestro objeto con la información necesaria para enviar el correo a una forma aceptable por
     * javamail
     * @param mensaje Mensaje de correo de javamail
     * @param correo Objeto con la información necesaria para enviar el correo
     * @return 
     */
    private MimeMessage preparaMensajeHTML(MimeMessage mensaje, 
                                           CorreoSMTP correo) {
        try {
            //Asignamos los datos del mensaje
            mensaje.setFrom(correo.getCorreoElectronicoRemitente());
            mensaje.setSubject(correo.getTituloCorreo());
            mensaje.setSentDate(new java.util.Date());
            Multipart html = new MimeMultipart();
            BodyPart bp1 = new MimeBodyPart();

            bp1.setContent(correo.getContenidoMensaje(), "text/html");
            html.addBodyPart(bp1);

            mensaje.setContent(html);
            mensaje.setContentLanguage(new String[] { "es", "en" });
            //mensaje.setContentLanguage(new String[] {"es-MX"});
            //mensaje.setText(correo.getContenidoMensaje());

            //Ponemos las direcciones de los usuarios a los que se les enviara el correo electronico
            mensaje.setRecipients(Message.RecipientType.TO,correo.getToAddresses());
            mensaje.setRecipients(Message.RecipientType.CC,correo.getCCAddresses());

            mensaje.saveChanges();
        } catch (Exception ex) {
            System.out.println("Error en el metodo preparaMensaje de la clase SesionSMTP");
            ex.printStackTrace();
        }

        return mensaje;
    }

    /**
     *Metodo que se encarga de hacer la traduccion de nuestro objeto con la información necesaria para enviar el correo a una forma aceptable por
     * javamail
     * @param mensaje Mensaje de correo de javamail
     * @param correo Objeto con la información necesaria para enviar el correo
     * @return 
     */
    private MimeMessage preparaMensaje(MimeMessage mensaje, 
                                       CorreoSMTP correo) {
        try {
            //Asignamos los datos del mensaje
            mensaje.setFrom(correo.getCorreoElectronicoRemitente());
            mensaje.setSubject(correo.getTituloCorreo(),"utf-8");
            mensaje.setSentDate(new java.util.Date());
            
            mensaje.setContent(correo.getContenidoMensaje(), "text/html; charset=utf-8");
            mensaje.setHeader("Content-Type","text/html; charset=utf-8");
            mensaje.setRecipients(Message.RecipientType.TO,correo.getToAddresses());
            mensaje.setRecipients(Message.RecipientType.CC,correo.getCCAddresses());
            mensaje.saveChanges();
        } catch (Exception ex) {
            System.out.println("Error en el metodo preparaMensaje de la clase SesionSMTP");
            ex.printStackTrace();
        }

        return mensaje;
    }

    //Metodos getters y setters

    public void setServidor(String valor) {
        this.servidor = valor;
    }

    public String getServidor() {
        return this.servidor;
    }

    public void setPuerto(int valor) {
        this.puerto = valor;
    }

    public int getPuerto() {
        return this.puerto;
    }

    public void setUsuario(String valor) {
        this.usuario = valor;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setPassword(String valor) {
        this.password = valor;
    }

    public String getPassword() {
        return this.password;
    }


}



