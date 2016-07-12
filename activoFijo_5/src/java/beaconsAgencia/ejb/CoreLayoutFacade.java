/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.ejb;

import coreapp.clases.general.FCom;
import beaconsAgencia.clases.general.LayoutChecker;
import beaconsAgencia.entities.CoreLayout;
import beaconsAgencia.entities.CoreParametrosGenerales;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jgonzalezc
 */
@Stateless
public class CoreLayoutFacade extends AbstractFacade<CoreLayout> implements CoreLayoutFacadeLocal {
    @PersistenceContext(unitName = "beaconsAgenciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoreLayoutFacade() {
        super(CoreLayout.class);
    }
    private Object mergeEntity(Object entity) {
        return em.merge(entity);
    }

    private Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    @Override
    public String persistir(CoreLayout layout,String tipo) {
        String mensaje;
        try {
            Object respuesta=null;
            switch (tipo) {
                case "nuevoLayout":
                    respuesta=persistEntity(layout);
                    break;
                case "editarLayout":
                    respuesta=mergeEntity(layout);
                    break;
            }
            if (respuesta != null)
                mensaje = "{\"success\":true,\"title\":\"Operación exitosa.\",\"msg\":\"\"}";
            else
                mensaje = "{\"success\":false,\"title\":\"Operación no encontrada.\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        } catch (ConstraintViolationException e) {              
            mensaje = "{\"success\":false,\"title\":\"Error de persistencia de datos\",\"msg\":\"Favor de comunicarse con el administrador\"}";
        }
        return mensaje;
    }    
       
    @Override
    public String select(int rows, int page, String sort, String order, String search) {
        String json;
        
        int inicio = ((page - 1) * rows);
        try {
            Query queryTotal =em.createNativeQuery("SELECT COUNT(1) FROM core_layout " + search);
            int count=Integer.parseInt(queryTotal.getSingleResult().toString());
        
            String query = "select * from core_layout " + search + " order by " + sort + " " +order;
            String queryPaginado = query + " LIMIT " + inicio + "," + rows;
            Query queryRegistros = em.createNativeQuery(queryPaginado, CoreLayout.class);

            List<CoreLayout> listCatalogo = (List<CoreLayout>)queryRegistros.getResultList();
            json = "{\"data\":[";
            for (CoreLayout catalogo: listCatalogo) {
                json += catalogo.toString()+",";
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
     public String probarLayout(InputStream inputStream,Integer idLayout){
        String mensaje;
        try {
            OPCPackage pkg = OPCPackage.open(inputStream);
            XSSFWorkbook workBook = new XSSFWorkbook(pkg);
            XSSFSheet xssfSheet = workBook.getSheetAt(0);
            int lastRow=xssfSheet.getLastRowNum();
            StringBuilder resultado=new StringBuilder();
            String cadenaEncriptacion=em.find(CoreParametrosGenerales.class,"SEC_ENCRYP").getValor();
            if(lastRow>0){
                CoreLayout coreLayout=find(idLayout);
                if(coreLayout!=null){      
                    int initRow=coreLayout.getRenglonInicio();   
                    int initColumn=coreLayout.getColumnaInicio();                  
                    int lastColumn=coreLayout.getColumnaFin();
                    if(LayoutChecker.compararHeaderLayout(xssfSheet.getRow(initRow),coreLayout)){
                        Map<String,Integer> indexCampos=new HashMap<>();
                        for(int ji=initColumn;ji<lastColumn+1;ji++)
                            indexCampos.put(xssfSheet.getRow(initRow).getCell(ji).getStringCellValue(),new Integer(ji));
                        int nerror=0;
                        int nok=0;
                        for(int i=initRow+1;i<(lastRow+1);i++){
                            coreapp.clases.general.Error error=LayoutChecker.validarRowLayout(xssfSheet.getRow(i),coreLayout,indexCampos,cadenaEncriptacion);
                            if(error.isError())
                                nok++;
                            else{
                                nerror++;
                                resultado.append(" <b>Renglon ").append(i+1).append(":</b> ").append(error.getMensaje()).append("</br>");
                            }
                        }
                        resultado.insert(0, "<br/><br/>");
                        resultado.insert(0, "<br/><b style='color:red;'>Registros incorrectos: "+nerror+"</b>");
                        resultado.insert(0, "<br/><b style='color:green;'>Registros correctos: "+nok+"</b>");
                        resultado.insert(0, "<br/><b>Total de registros: "+(lastRow-initRow)+"</b>");
                        mensaje = "{\"success\":true,\"title\":\"Operación exitosa\",\"msg\":\"\",\"data\":\""+resultado.toString()+"\"}";
                    }else
                        mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"Incorrecta definición del header\"}";
                }else
                    mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"No existe un layout definido\"}";
            }else
                mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"No existe ningun registro a actualizar\"}";            
            pkg.close();
        } catch (IOException ex) {
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"A: Error de persistencia de datos.\"}";
        }catch (OfficeXmlFileException ex) {
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"B: Error de persistencia de datos\"}";
        }catch (InvalidFormatException e){
            mensaje = "{\"success\":false,\"title\":\"Error\",\"msg\":\"C:Error de persistencia de datos\"}";            
        }
         return mensaje;
     }
    
}
