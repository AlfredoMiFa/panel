/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beaconsAgencia.clases.general;

import coreapp.clases.general.Error;
import coreapp.clases.general.FCom;
import coreapp.clases.general.SimpleClassLoader;
import coreapp.clases.general.Validador;
import beaconsAgencia.ejb.CoreConexionesDaoFacadeLocal;
import beaconsAgencia.entities.CoreConexionesDao;
import beaconsAgencia.entities.CoreLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import javax.ejb.EJB;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 *
 * @author jgonzalezc
 */
public class LayoutChecker {
    @EJB
    static CoreConexionesDaoFacadeLocal coreConexionesEJBLocal;
    public static boolean compararHeaderLayout(XSSFRow headerRow,CoreLayout layout){
         int noCeldas=headerRow.getLastCellNum()-layout.getColumnaInicio();
         int noCampos=layout.getCoreCamposLayoutList().size();
         if(noCeldas==noCampos)
         {
             for(int i=0;i<noCeldas;i++){
                if(!headerRow.getCell(i+layout.getColumnaInicio()).getStringCellValue().equals(layout.getCoreCamposLayoutList().get(i).getNombreCampo()))
                    return false;
             }
             return true;
         }else
            return false;
     }
    public static Error validarRowLayout(XSSFRow row,CoreLayout layout,Map indexCampos,String cadenaEncriptacion){
         int noCeldas=row.getLastCellNum()-layout.getColumnaInicio();
         int noCampos=layout.getCoreCamposLayoutList().size();
         if(noCeldas==noCampos)
         {
             StringBuilder error=new StringBuilder();
             for(int i=0;i<noCeldas;i++){
                if(layout.getCoreCamposLayoutList().get(i).getValidar()){
                    for(int j=0;j<layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().size();j++){
                        if(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getActivo()){
                            String tipo=layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getTipo();
                            Cell cel = row.getCell(i+layout.getColumnaInicio());
                            String value=null;
                            if(cel!=null){
                                switch(cel.getCellType()) {
                                    case 0: value=cel.getNumericCellValue()+ "";
                                        break;
                                    case 1:value=cel.getStringCellValue()+"";
                                        break;
                                    case 4: value=cel.getBooleanCellValue()+"";
                                        break;
                                    default:
                                        value=null;
                                }
                            }
                            if(!(layout.getCoreCamposLayoutList().get(i).isNullable() && (value==null || value.trim().equals(""))))                                
                            {
                                if(value==null)
                                    error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - Es obligatoria. | ");
                                else{
                                    if(tipo.equals("REGEX")){
                                        if(!value.matches(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getRegex()))                                        
                                            error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - ").append(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getMensajeError()).append(" | ");
                                    }else if(tipo.equals("RANGO")){
                                        try{
                                            Double valueDouble=Double.parseDouble(value);
                                            Double valorInicial=Double.parseDouble(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getValorInicial());
                                            Double valorFinal=Double.parseDouble(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getValorFinal());
                                            if((valorInicial> valueDouble) || (valueDouble>valorFinal))
                                                error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> -  no esta en rango("+valorInicial+","+valorFinal+") | ");          
                                        }catch(Exception e){
                                            error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> -  debe ser númerica | ");  
                                        }
                                    }else if(tipo.equals("CLASE")){
                                        try{
                                            if(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getClass1()!=null){
                                                String parametros=layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getParametros();
                                                String[] aparametros=parametros.split("\\|");
                                                String[] anombre=aparametros[1].split(",");
                                                if(anombre.length>0)
                                                    parametros+="|";
                                                for (String nombre_campo : anombre) {
                                                    parametros += row.getCell((int) indexCampos.get(nombre_campo)).getStringCellValue()+",";
                                                }
                                                if(anombre.length>0)
                                                    parametros=parametros.substring(0, parametros.length() - 1);
                                                SimpleClassLoader loader=new SimpleClassLoader();
                                                
                                                Object o; 
                                                try{
                                                    //Class clase=loader.simpleLoadClass(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getNombreClase(),layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getClass1());
                                                    Class clase=loader.simpleLoadClass(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getNombreClase(),"C:\\Users\\jgonzalezc\\Documents\\NetBeansProjects\\ValidadorLibrary\\build\\classes\\coreapp\\clases\\general\\ValidadorA.class");
                                                    o = (clase).newInstance();
                                                    boolean resultado=((Validador)o).validar(parametros);
                                                    //boolean resultado=(Boolean)clase.getMethod(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getNombreMetodo(),String.class).invoke(o,parametros);
                                                    if(!resultado)
                                                        error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - ").append(layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getMensajeError()).append(" | ");
                                                }catch(ClassNotFoundException | NoClassDefFoundError e){
                                                    System.out.println("Valio");
                                                }
                                                catch(Exception e){
                                                    System.out.println("No se instancio");
                                                }
                                            }else
                                                error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - clase no cargada. | ");
                                        }catch(Exception e){
                                            error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - error al utilizar validación por clase. | ");
                                        }

                                    }else if(tipo.equals("SQL")){
                                        Connection conn= null;
                                        ResultSet rs = null;
                                        Statement st=null;
                                        try{                  
                                            CoreConexionesDao conexion=layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getConexionDao();
                                            if(conexion!=null)
                                            {
                                                Class.forName(conexion.getDriver());
                                                try{
                                                   String query=layout.getCoreCamposLayoutList().get(i).getCoreValidadorList().get(j).getSqlSentence().replace("##=value#", value);
                                                    conn =  DriverManager.getConnection(conexion.getServidor(),conexion.getUsuario(),FCom.decrypt(conexion.getContrasena(),cadenaEncriptacion));
                                                   st = conn.createStatement();
                                                   rs=st.executeQuery(query);
                                                   if(!rs.next())
                                                        error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - la validación por SQL ha sido negativa. | ");
                                                }catch (Exception e) {
                                                    error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - error en la validación SQL. | ");
                                                }finally{
                                                    if (st != null)
                                                        st.close();
                                                    if (conn != null) 
                                                        conn.close();
                                                }
                                            }
                                        }catch(Exception e1) {
                                            error.append("<i style='color:green;'>").append(layout.getCoreCamposLayoutList().get(i).getNombreCampo()).append("</i> - error en la validación SQL. | ");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
             }
             if(error.toString().equals(""))
                return new Error(true,null);
             else
                return new Error(false,error.toString());
         }else
            return new Error(false,"El número de celdas("+noCeldas+") no corresponde al número de campos del layout ("+noCampos+");");
     }
}
