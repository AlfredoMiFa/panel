/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import beaconsAgencia.clases.correo.EnviarEmail;
import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.ObjetoSesion;
import beaconsAgencia.entities.CoreCarpeta;
import beaconsAgencia.entities.CoreCorreo;
import beaconsAgencia.entities.CoreDerecho;
import beaconsAgencia.entities.CoreDerechoSistema;
import beaconsAgencia.entities.CoreParametrosGenerales;
import beaconsAgencia.entities.CorePerfil;
import beaconsAgencia.entities.CoreUsuario;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreUsuarioFacade extends AbstractFacade<CoreUsuario> implements CoreUsuarioFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreUsuarioFacade() {
        super(CoreUsuario.class);
    }

    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }

    /**<code>select o fromId CoreUsuario o where o.usuario = :usuario</code>
     * @param usuario
     * @return 
     */
    @Override
    public List<CoreUsuario> queryCoreUsuarioFindByUserName(Object usuario) {
        return em.createNamedQuery("CoreUsuario.findByUsuario").setParameter("usuario", usuario).getResultList();
    }

    /**<code>select o fromId CoreUsuario o where o.email = :email</code>
     * @param email
     * @return 
     */
    @Override
    public List<CoreUsuario> queryCoreUsuarioFindByEmail(Object email) {
        return em.createNamedQuery("CoreUsuario.findByEmail").setParameter("email", email).getResultList();
    }
    

    /**<code>select o fromId CoreUsuario o where o.usuario = :usuario and  o.contrasena = :contrasena </code>
     * @param usuario
     * @param contrasena
     * @return 
     */
    @Override
    public ObjetoSesion queryCoreUsuarioFindByUserContrasena(Object usuario, Object contrasena,String cadenaEncriptacion) {
        ObjetoSesion objetoSesion=new ObjetoSesion();        
        List<CoreUsuario> listCoreUsuario=em.createNamedQuery("CoreUsuario.findByUsuario").setParameter("usuario", usuario).getResultList();
        if(listCoreUsuario.size()>0){ 
            Boolean valida=false;
            valida=FCom.decrypt(listCoreUsuario.get(0).getContrasena(),cadenaEncriptacion).equals(contrasena.toString());
            if(valida){
                if(listCoreUsuario.get(0).getEsSuper().equals("S")) {
                    CorePerfil perfil=new CorePerfil();
                    perfil.setCorePerfilId(0);
                    perfil.setPerfil("SUPER");
                    perfil.setDescripcion("Super usuario");
                    perfil.setEstatus("AC");
                    perfil.setHomePage("general/escritorio.jsp");
                    List<CoreDerecho> coreDerechos=queryCoreDerechoFindMenu();
                    perfil.setCoreDerechoList(coreDerechos);
                    objetoSesion.setSuperAdmin(perfil);
                }
                objetoSesion.setUsuario(listCoreUsuario.get(0));
                if(listCoreUsuario.get(0).getCorePerfilList().size()>0)
                    objetoSesion.setPerfilSelected(listCoreUsuario.get(0).getCorePerfilList().get(0));
                else
                    objetoSesion.setSuperAdministrador(true);
                return objetoSesion;
                
            }else
                return null;
        }
        else
            return  null;
    }
    

    @Override
    public List<CoreDerecho> queryCoreDerechoFindMenu() {
        return em.createNamedQuery("CoreDerecho.findMenu").getResultList();
    }
    
    public List<CoreDerechoSistema> queryCoreDerechoSistemaFindAll() {
        return em.createNamedQuery("CoreDerechoSistema.findAll").getResultList();
    }
    
    @Override
    public String persistirUsuario(CoreUsuario usuario,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevo":
                    respuesta=persistEntity(usuario);
                    CoreCarpeta carpeta=new CoreCarpeta();
                    carpeta.setNombre(usuario.getNombre()+" "+usuario.getApellidos());
                    carpeta.setDescripcion("Carpeta privada");
                    carpeta.setPropietarioId((CoreUsuario)respuesta);
                    carpeta.setEstatus("AC");
                    carpeta.setCoreCarpetaPadreId(null);
                    respuesta=persistEntity(carpeta);
                    break;
                case "editar":
                    respuesta=mergeEntity(usuario);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Esta tipo de operación no es valida.\"}";
        } catch (ConstraintViolationException e) {               
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }    
    
    @Override
    public String selectUsuario(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_usuario " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_usuario " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreUsuario.class);

            List<CoreUsuario> listUsuario = (List<CoreUsuario>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreUsuario usuario: listUsuario) {
                json += usuario.toString()+",";
            }
            if (count > 0)
                json = json.substring(0, json.length() - 1);
            json += "],\"total\":"+count+"}";
        } catch (NumberFormatException e) {
            FCom.printDebug(e.getMessage());
            json = "{\"data\":[],\"total\":0}";
        }
        return json;
    }
    
    @Override
    public String verificarUnico(String campo, String textoBuscar,String id) {
        String mensaje;
        try {
            if (id == null || id.equals(""))
                id = "-1";
            Query query = 
                em.createNativeQuery("select "+campo+" FROM core_usuario WHERE  core_usuario_id!=" + 
                                     id + " and " + campo + "='" +textoBuscar + "'");
            List listRegistros = query.getResultList();
            if (listRegistros.size()>0)
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Ya existe, favor de cambiar.\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\"Disponible\"}";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Error al verificar el dato\"}";
        }
        return mensaje;
    }

    @Override
    public String generaComboUsuario(CoreUsuario coreUsuario) {
        String json = "[{\"id\":\"\",\"text\":\"SELECCIONE UNA OPCION\"},";
        try {
            Query query =em.createNativeQuery("select * from core_usuario where estatus='AC' and CORE_USUARIO_ID!="+coreUsuario.getCoreUsuarioId()+ " and grupo='"+coreUsuario.getGrupo()+"'", CoreUsuario.class);
            List<CoreUsuario> listUsuarios = 
                (List<CoreUsuario>)query.getResultList();
            for (CoreUsuario tbUsuario: listUsuarios)
                json += "{\"id\":" + tbUsuario.getCoreUsuarioId() + ",\"text\":\"" +tbUsuario.getNombre()+" "+tbUsuario.getApellidos() + "\"},";
            json = json.substring(0, json.length() - 1);
            json += "]";
        } catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
    }
    
    @Override
    public String recuperarContrasena(String cadenaEncriptacion,String email,String rutaApp){
        String mensaje;
        CoreParametrosGenerales correoAdministrador=em.find(CoreParametrosGenerales.class,"APP_EMAIL");
        CoreParametrosGenerales nombreAplicacion=em.find(CoreParametrosGenerales.class,"APP_NAME");
        List<CoreCorreo> listaCorreo=em.createNamedQuery("CoreCorreo.findByUser").setParameter("user",correoAdministrador.getValor()).getResultList();
        if(listaCorreo.size()>0){
            List<CoreUsuario> listaCoreUsuario=em.createNamedQuery("CoreUsuario.findByEmail").setParameter("email",email).getResultList();
            if(listaCoreUsuario.size()>0){
                listaCoreUsuario.get(0).setContrasena(FCom.decrypt(listaCoreUsuario.get(0).getContrasena(),cadenaEncriptacion));
                String html="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"> \n" + 
                "<html lang=\"en\"> \n" + 
                "  <head> \n" + 
                "    <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\"> \n" + 
                "    <title> "+nombreAplicacion.getValor()+" </title> \n" +  
                "    <link rel=\"stylesheet\" href=\""+rutaApp+"recursos/css/foundation/normalize.css\" />\n" + 
                "    <link rel=\"stylesheet\" href=\""+rutaApp+"recursos/css/foundation/foundation.min.css\" />" + 
                "  </head> \n" + 
                "  <body> \n" + 
                "    <div class=\"row\">\n" + 
                "       <div class=\"large-6 large-centered columns\">\n" + 
                "          <br/><br/><br/>\n" + 
                "          <img src=\""+rutaApp+"recursos/img/logo.png\" alt=\"Logo administracion\"/>\n" + 
                "          <div class=\"panel callout\">\n" + 
                "              <p>Recuperar contrase&ntilde;a</p>\n" + 
                "          </div>" + 
                "          <div class=\"panel\">\n" + 
                "               <b>"+listaCoreUsuario.get(0).getNombre()+" "+listaCoreUsuario.get(0).getApellidos()+ "</b><br>" +
                "               <p>Sus datos de usuario son: </p> \n" + 
                "               <p><b>Usuario:</b> "+listaCoreUsuario.get(0).getUsuario()+" <br>\n" + 
                "               <p><b>Contrase&ntilde;a: </b>"+FCom.decrypt(listaCoreUsuario.get(0).getContrasena(),cadenaEncriptacion)+" <br>\n" + 
                "          </div>\n" + 
                "          <div id=\"error\" class=\"panel\">\n" + 
                "              <p  style=\"text-align:center;\"><b>"+nombreAplicacion.getValor()+" </b></p>\n" + 
                "          </div>" + 
                "       </div>\n" + 
                "     </div>" + 
                "   </body> \n" + 
                "</html> ";
                if(EnviarEmail.enviar( listaCorreo.get(0),email,listaCorreo.get(0).getEmisor(),"Recuperar password del sistema",html,new ArrayList<InternetAddress>()))
                    mensaje="{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"Se ha enviado un correo electronico con sus datos de usuario.\"}";
                else
                    mensaje="{\"success\":false,\"title\":\"Error\",\"msg\":\"Error al realizar la operacion.\"}";
            }else
                mensaje="{\"success\":false,\"title\":\"Error\",\"msg\":\"No se encuentra registrado el correo.\"}";
        }else
            mensaje="{\"success\":false,\"title\":\"Error\",\"msg\":\"No se encuentra configurado el servicio de correo.\"}";
            
        return mensaje;        
    }

    @Override
    public String validaUsuario(String idUsuario, String tipo) {
        String mensaje ;
        try {
            Query query = em.createNativeQuery("SELECT CORE_USUARIO.CORE_USUARIO_ID, CORE_USUARIO.USUARIO, "
                    + "CORE_USUARIO.CONTRASENA, CORE_USUARIO.NOMBRE, CORE_USUARIO.APELLIDOS, CORE_USUARIO.EMAIL, "
                    + "CORE_USUARIO.TELEFONO, CORE_USUARIO.ESTATUS, CORE_USUARIO.ES_SUPER, CORE_USUARIO.ESTILO, "
                    + "CORE_USUARIO.FOTO, CORE_USUARIO.VIGENCIA, CORE_USUARIO.APLICA_BITACORA, CORE_USUARIO.IS_LOGIN, "
                    + "CORE_USUARIO.MULTI_SESION, CORE_USUARIO.IP_RESTRICCION, CORE_USUARIO.GRUPO, "
                    + "CORE_USUARIO.ID_EMPRESA, CORE_USUARIO.ID_OFICINA"
                    + " FROM BEACONS_AGENCIAS.CORE_USUARIO "
                    + "LEFT OUTER JOIN BEACONS_AGENCIAS.EMPRESA ON(EMPRESA.ID_EMPRESA = CORE_USUARIO.ID_EMPRESA)"
                    + " WHERE CORE_USUARIO.CORE_USUARIO_ID =\""+idUsuario+"\"");
            List listUsuarios = query.getResultList();
            if(listUsuarios.size()>0)
               mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Todo bien.\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"No tiene asignada una empresa, favor de "
                        + "comunicarse con el administrador.\"}";
            /*if(listRegistros.size()>0)
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Puede continuar.\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\"No cuenta con los permisos suficientes"
                        + ", favor de comunicarse con el administrador\"}";*/
        }catch(Exception e) {
            FCom.printDebug(e.getMessage());
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Error al verificar el dato\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String validaOficina(String idEmpresa, String tipo) {
        String mensaje ;
        try {
            Query query = em.createNativeQuery("SELECT OFICINAS.ID_OFICINA, OFICINAS.ID_EMPRESA, "
                    + "OFICINAS.ID_ESTADO, OFICINAS.ID_CIUDAD, OFICINAS.NOMBRE_OFICINA, OFICINAS.CALLE, "
                    + "OFICINAS.NUMERO_OFICINA, OFICINAS.COLONIA, OFICINAS.CPP, "
                    + "OFICINAS.NUMERO_TELEFONO, OFICINAS.RFC "
                    + " FROM BEACONS_AGENCIAS.OFICINAS "
                    + "LEFT OUTER JOIN BEACONS_AGENCIAS.EMPRESA ON(EMPRESA.ID_EMPRESA = OFICINAS.ID_EMPRESA)"
                    + " WHERE OFICINAS.ID_EMPRESA =\""+idEmpresa+"\"");
            List listUsuarios = query.getResultList();
            if(listUsuarios.size()>0)
               mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Todo bien.\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"No tiene asignada una empresa, favor de "
                        + "comunicarse con el administrador.\"}";
            /*if(listRegistros.size()>0)
                mensaje = "{\"success\":false,\"title\":\"\",\"msg\":\"Puede continuar.\"}";
            else
                mensaje = "{\"success\":true,\"title\":\"\",\"msg\":\"No cuenta con los permisos suficientes"
                        + ", favor de comunicarse con el administrador\"}";*/
        }catch(Exception e) {
            FCom.printDebug(e.getMessage());
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Error al verificar el dato\"}";
        }
        return mensaje;
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idUsuarios(String idEmpresa) {
        String json = "";
        try {
            Query query = em.createNativeQuery("SELECT CORE_USUARIO_ID FROM CORE_USUARIO WHERE ID_EMPRESA ="+idEmpresa, CoreUsuario.class);
            List<CoreUsuario> listUsuarios = 
                (List<CoreUsuario>)query.getResultList();
            int count = query.getResultList().size();
            for (CoreUsuario tbUsuario: listUsuarios)
                json +=tbUsuario.getCoreUsuarioId() +",";
            if(count > 0)
                json = json.substring(0, json.length() - 1);
        }catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idUsuariosPorOficina(String idOficina) {
        String json = "";
        try {
            Query query = em.createNativeQuery("SELECT CORE_USUARIO_ID FROM CORE_USUARIO WHERE ID_OFICINA ="+idOficina, CoreUsuario.class);
            List<CoreUsuario> listUsuarios = 
                (List<CoreUsuario>)query.getResultList();
            int count = query.getResultList().size();
            for (CoreUsuario tbUsuario: listUsuarios)
                json +=tbUsuario.getCoreUsuarioId() +",";
            if(count > 0)
                json = json.substring(0, json.length() - 1);
        }catch (Exception e) {
            FCom.printDebug(e.getMessage());
            json = "[{\"id\":\"\",\"text\":\"NO EXISTEN OPCIONES\"}]";
        }
        return json;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
