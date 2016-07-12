<!DOCTYPE html>
<%@page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - reglas contraseña</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>   
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-REGLAS"/>
                </jsp:include>
            </nav>
            <div id="page-wrapper" style="min-height: 600px;">
                <div class="container-fluid">
                    <noscript>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="alert alert-warning alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <i class="fa fa-info-circle"></i>  <b>Alerta:</b> Es necesario tener habilitado el uso de javascript de su navegador.
                                </div>
                            </div>
                        </div>
                    </noscript>
                    <div class="row">
                        <div class="col-lg-12">
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="escritorio.jsp">Dashboard</a>
                                </li>
                                <li>
                                    <a href="#"><i class="fa fa-gears"></i> Modulo de configuración</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-key"></i> Reglas contraseña
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row">  
                        <form id="form"  role="form"  action="../generals/reglasContrasena.do" > 
                            <div class="col-lg-2"></div>
                            <div class="col-lg-10">
                                <div class="form-group">
                                    <input type="hidden" name="modo" id="modo" value="editar"/>
                                    <input type="hidden" name="coreSegContrasenaId" id="coreSegContrasenaId" required/>
                                    <label for="longitudMinimaUsuario">Longitud mínima para nombre de usuario</label><br/>
                                    <input type="text" name="longitudMinimaUsuario"  id="longitudMinimaUsuario" required/>
                                </div>
                                <div class="form-group">
                                    <label for="longitudMinimaContrasena">Longitud mínima contraseña</label><br/>
                                    <input type="text" name="longitudMinimaContrasena"   id="longitudMinimaContrasena" required/>
                                </div>
                                <div class="form-group">
                                    <label for="diasVigencia">Días de vigencia de cuenta de usuario</label><br/>
                                    <input type="text" name="diasVigencia" id="diasVigencia" required/>
                                </div>
                                <div class="form-group">
                                    <input type="checkbox" id="chkNumero" name="chkNumero"/>
                                    <label for="chkNumero">Requiere un número de contraseña (0...9)</label>
                                </div>
                                <div class="form-group">
                                    <input type="checkbox" id="chkCaracterEspecial" name="chkCaracterEspecial"/><label for="chkCaracterEspecial">Requiere caracter especial ($,%,!,¡,@,#,/,?,¿)</label>
                                </div>
                                <div class="form-group">
                                    <input type="checkbox" id="chkVerificacion" name="chkVerificacion"/> <label for="chkVerificacion">Casilla de verificación</label>
                                </div>
                                <div class="form-group">
                                    <b style="color:red;margin-left: 20px;">Nota: estas reglas se utilizan para todos los usuarios del sistema</b>
                                </div>
                                <div class="form-group">
                                     <a href="javascript:guardar()" class="k-button">Guardar</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/recursos/js/kendo.culture.es-MX.min.js"></script>
        <script>     
            $(document).ready(function(){
                inicializar();
                kendo.culture("es-MX"); 

                $.post("../generals/reglasContrasena.do",{modo:"find"},function(result){
                     if (result!=null)  
                     {
                         $("#coreSegContrasenaId").val(result.coreSegContrasenaId);
                         $("#longitudMinimaContrasena").val(result.longcontrasena);
                         $("#longitudMinimaUsuario").val(result.longusuario);
                         $("#diasVigencia").val(result.vigencia);
                         $("#longitudMinimaUsuario").kendoNumericTextBox({maxlength: 2,format:"##",decimals:0,min:4,max:15});
                        $("#longitudMinimaContrasena").kendoNumericTextBox({maxlength: 2,format:"##",decimals:0,min:4,max:15});
                        $("#diasVigencia").kendoNumericTextBox({maxlength: 2,format:"##",decimals:0,min:5,step:5,max:10000});
                        if(result.recnum)
                            $("#chkNumero").attr('checked',true);
                        if(result.reccar)
                            $("#chkCaracterEspecial").attr('checked',true);

                     }
                     else 
                     {  
                        $("#longitudMinimaContrasena").attr("readonly","readonly");
                        notification.show({title: "Ha ocurrido un error",message: "Favor de comunicarse con el administrador."}, "msg-error");   
                     }
                 },'json');
             });
            function guardar(){
                if ($("#form").kendoValidator().data("kendoValidator").validate()) {
                    $.post($('#form').attr( 'action' ), $('#form').serialize(),function(result){
                        verMensaje(result);
                    },'json');
                } 
            }
        </script>
    </body>
</html>