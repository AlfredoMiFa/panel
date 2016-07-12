/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.listener;

import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.ejb.BitacoraFacadeLocal;
import beaconsAgencia.ejb.CoreUsuarioFacadeLocal;
import beaconsAgencia.entities.Bitacora;
import beaconsAgencia.entities.CoreUsuario;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author jgonzalezc
 */
public class CloseSesionListener implements HttpSessionListener {
    @EJB
    CoreUsuarioFacadeLocal usuarioEJBLocal;
    @EJB
    BitacoraFacadeLocal bitacoraEJBLocal;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ObjetoSesion objetoSesion=(ObjetoSesion)session.getAttribute(session.getServletContext().getInitParameter("vsi")+"SESION");        
        CoreUsuario coreUsuario=usuarioEJBLocal.find(objetoSesion.getUsuario().getCoreUsuarioId());
        coreUsuario.setIsLogin(false);
        
        usuarioEJBLocal.persistirUsuario(coreUsuario, "editar");
        if(objetoSesion.getUsuario().getAplicaBitacora())
        {
            Bitacora bitacora=new Bitacora();
            bitacora.setFechaOperacion(new Date());
            bitacora.setOperacion("LOGOUT");
            bitacora.setOrigen("");
            bitacora.setTipo("ACCESO");
            bitacora.setUsuarioId(coreUsuario.getCoreUsuarioId());
            bitacoraEJBLocal.persistir(bitacora, "nuevo");            
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
