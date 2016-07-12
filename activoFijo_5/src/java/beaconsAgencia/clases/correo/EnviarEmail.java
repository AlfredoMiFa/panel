/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.correo;

import java.util.List;
import javax.mail.internet.InternetAddress;
import beaconsAgencia.entities.CoreCorreo;

/**
 *
 * @author jgonzalezc
 */
public class EnviarEmail {
    public static boolean enviar(CoreCorreo smtpCuenta,String email,String nombreUsuario,String titulo, String mensaje,List<InternetAddress> listacc) {
        try {
             SesionSMTP sesion = new SesionSMTP(smtpCuenta.getSmtpServer(), Integer.parseInt(smtpCuenta.getSmtpPort()), smtpCuenta.getUser(), smtpCuenta.getPassword());
             CorreoSMTP correo = new CorreoSMTP(smtpCuenta.getEmisor(), smtpCuenta.getUser(), titulo); 
             correo.addToAddresses(nombreUsuario , email);
             if(listacc.size()!=0) {                
                for(int i=0;i<listacc.size();i++)
                 correo.addccAddresses(listacc.get(i).getPersonal(),listacc.get(i).getAddress());
             }
             correo.setTituloCorreo(titulo);
             correo.setContenidoMensaje(mensaje);
             sesion.enviaCorreoHTML(correo);
             return true;
        }catch(Exception e) {
            return false;
        }
    }
    
}
