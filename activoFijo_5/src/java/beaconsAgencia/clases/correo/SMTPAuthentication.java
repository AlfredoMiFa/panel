/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.correo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author jgonzalezc
 */
public class SMTPAuthentication extends Authenticator {
    private String username = "", password = "";
    public SMTPAuthentication(String acorreo,String apassword) {
        username=acorreo;
        password=apassword;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}



