/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.correo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author jgonzalezc
 */
public class CorreoSMTP {
    protected HashMap toAddresses = new HashMap();
    protected HashMap ccAddresses = new HashMap();
    protected HashMap bccAddresses = new HashMap();
    protected HashMap replyToAddresses = new HashMap();

    protected String nombreRemitente = null;
    protected String correoElectronicoRemitente = null;
    protected String tituloCorreo = null;
    protected String contenidoMensaje = null;


    public CorreoSMTP() {
    }

    public CorreoSMTP(String nombreRemitente,String correoElectronicoRemitente, String tituloCorreo) {
        this.nombreRemitente = nombreRemitente;
        this.correoElectronicoRemitente = correoElectronicoRemitente;
        this.tituloCorreo = tituloCorreo;
    }

    private InternetAddress[] obtenDirecciones(HashMap direcciones) {
        ArrayList listaDirecciones = new ArrayList();
        try {
            String nombre = null;
            String correoElectronico = null;
            Iterator i = direcciones.keySet().iterator();

            while (i.hasNext()) {
                nombre = (String)i.next();
                correoElectronico = (String)direcciones.get(nombre);

                try {
                    listaDirecciones.add(new InternetAddress(correoElectronico,nombre));
                } catch (Exception ex) {
                    System.out.println("Error al agregar la direccion a la lista de direcciones en el metodo obtenDirecciones de la clase CorreoSMTP");
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error en el metodo obtenDirecciones de la clase CorreoSMTP");
            ex.printStackTrace();
        }

        if (listaDirecciones.isEmpty())
            return null;

        InternetAddress[] direccionesCorreo = 
            new InternetAddress[listaDirecciones.size()];
        direccionesCorreo = (InternetAddress[])listaDirecciones.toArray(direccionesCorreo);
        return direccionesCorreo;
    }

    public void addToAddresses(String nombre, String correoElectronico) {
        this.toAddresses.put(nombre, correoElectronico);        
    }

    public void addccAddresses(String nombre, String correoElectronico) {
        this.ccAddresses.put(nombre, correoElectronico);        
    }

    public void clearToAddresses() {
        this.toAddresses.clear();
    }

    //Metodos getters y setters

    public void setContenidoMensaje(String valor) {
        this.contenidoMensaje = valor;        
    }

    public String getContenidoMensaje() {
        return this.contenidoMensaje;
    }

    public void setNombreRemitente(String valor) {
        this.nombreRemitente = valor;
    }

    public String getNombreRemitente() {
        return this.nombreRemitente;
    }

    public void setTituloCorreo(String valor) {
        this.tituloCorreo = valor;
    }

    public String getTituloCorreo() {
        return this.tituloCorreo;
    }

    public void setCorreoElectronicoRemitente(String valor) {
        this.correoElectronicoRemitente = valor;
    }

    public InternetAddress getCorreoElectronicoRemitente() {
        InternetAddress temp = null;
        try {
            temp = 
new InternetAddress(this.correoElectronicoRemitente, this.nombreRemitente);
        } catch (Exception ex) {
            System.out.println("Error en el metodo getCorreoElectronicoRemitente de la clase CorreoSMTP");
            ex.printStackTrace();
        }
        return temp;
    }

    public InternetAddress[] getToAddresses() {
        return this.obtenDirecciones(this.toAddresses);
    }

    public InternetAddress[] getCCAddresses() {
        return this.obtenDirecciones(this.ccAddresses);
    }

    public InternetAddress[] getBCCAddresses() {
        return this.obtenDirecciones(this.bccAddresses);
    }

    public InternetAddress[] getReplyToAddresses() {
        return this.obtenDirecciones(this.replyToAddresses);
    }
}
