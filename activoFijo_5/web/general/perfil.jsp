<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - editar perfil</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>   
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>           
                <jsp:include page="/menu.do"></jsp:include>
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
                                <li class="active">
                                    <i class="fa fa-edit"></i> Perfil
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row">  
                        <div class="col-lg-8">
                            <div class="row"> 
                                <form id="form"  role="form" action="../catalogos/generals/usuario.do" >                  
                                    <div class="col-lg-6">
                                        <h4>Datos del usuario</h4>
                                        <input type="hidden" name="modo" value="editarPerfil"/>
                                        <div class="form-group">
                                            <label for="apellidos">Apellidos*</label>
                                            <input id="apellidos" class="form-control" required  name="apellidos" maxlength="45" type="text" value="${va_SESION.usuario.apellidos}" />
                                        </div>
                                        <div class="form-group">
                                            <label for="nombre">Nombre*</label>
                                            <input id="nombre" class="form-control" required  maxlength="45" name="nombre" type="text" value="${va_SESION.usuario.nombre}" />
                                        </div>
                                        <div class="form-group">
                                            <label for="email">Email*</label>
                                            <input type="email" id="email" name="email" class="form-control" maxlength="100" value="${va_SESION.usuario.email}" placeholder="e.g. myname@example.net"  required />
                                        </div>
                                        <div class="form-group">
                                            <label for="telefono">Telefono</label>
                                            <input id="telefono" class="form-control" maxlength="22" name="telefono" type="text" value="${va_SESION.usuario.telefono}" />
                                        </div>
                                    </div>                      
                                    <div class="col-lg-6">                                    
                                        <h4>Información del perfil</h4>
                                        <div class="form-group">
                                            <label for="usuario">Usuario</label>
                                            <input id="usuario" class="form-control" name="usuario"  maxlength="20" type="text" value="${va_SESION.usuario.usuario}" readonly="readonly"/>
                                        </div>
                                        <div class="form-group">                                          
                                            <input id="validarContrasena" name="validarContrasena"  type="hidden" value="false" />
                                            <p class="uneditable-input"><a id="hrefText" href="#">Cambiar Contraseña</a></p>
                                        </div>
                                        <div class="form-group" id="mostrar" style="display: none;">
                                            <label for="contrasena">Nueva contraseña</label>
                                            <input id="contrasena" class="form-control" maxlength="20" name="contrasena" type="password" value="" />
                                        </div>
                                        <div class="form-group">
                                            <!--<label for="estilo">Estilo</label><br/>-->
                                            <input id="estilo"  type="hidden" name="estilo" value="${va_SESION.usuario.estilo}" />
                                        </div>
                                        <div class="form-group">
                                            <div class="label"></div>
                                            <a href="javascript:guardar()" class="k-button">Guardar</a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="col-lg-4">  
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-user"></i> Foto del usuario</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-lg-2"><img id="nfoto" src="../catalogos/generals/usuario.do?modo=getImage" alt="User Picture" width="42" height="42"/></div>
                                        <div class="col-lg-10"><input name="foto" id="foto" type="file"  /></div>
                                    </div>
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <em>Arrastre aquí el archivo a subir</em><br/>
                                            Archivos .jpg, .jpeg y .png son permitidos.<br/>
                                            <p class="uneditable-input"><a id="eliminarFoto" href="javascript:eliminarFoto();">Eliminar foto actual</a></p>
                                        </div>
                                    </div>
                                </div>
                            </div>   
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-user"></i> Cambiar de role</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <form class="navbar-form navbar-left" role="search">
                                            <div class="form-group">    
                                                <input id="userPerfil" name="userPerfil" value="${va_SESION.perfil}" style="width:135px;margin-left:5px; " />
                                                <button class="k-button"  onclick="setPerfilSelected();"><span class="k-icon k-i-refresh"></span></button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->
            <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
            <script>
                $(document).ready(function(){
                    inicializar();
                    /*$("#estilo").kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "value",
                        dataSource: aestilo,
                        index: 0
                    });*/
                    $("#userPerfil").kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "value",
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: approot+"general.do?modo=perfilesUsuario"
                                }
                            }
                        }
                    });
                    $("#foto").kendoUpload({
                        async: {
                            saveUrl: "../catalogos/generals/usuario.do",
                            autoUpload: true
                        },
                        localization:{
                            dropFilesHere:"soltar aquí el archivo de imagen"
                        },
                        complete: onComplete,
                        multiple:false,
                        select: onSelect
                    });
                    $("#hrefText").click(function(){
                        if($("#validarContrasena").val()==="true") {
                            $("#validarContrasena").val("false");
                            $("#hrefText").html("Cambiar Contraseña");
                            $("#mostrar").hide();
                        }else if($("#validarContrasena").val()==="false") {
                            $("#validarContrasena").val("true");
                             $("#contrasena").val("");
                            $("#hrefText").html("Cancelar Cambiar Contraseña");
                            $("#mostrar").show();
                        }
                    });
                });
                function onComplete(e) {
                    $("#nfoto").attr("src", "../catalogos/generals/usuario.do?modo=getImage&timestamp=" + new Date().getTime());
                }
                var onSelect = function(e) {                
                    var html="";
                    $.each(e.files, function(index, value) {
                        if(value.extension.toUpperCase() !== ".PNG" && value.extension.toUpperCase() !== ".JPG" && value.extension.toUpperCase() !== ".JPEG") {
                            e.preventDefault();
                            alert("Archivos permitidos png, jpg y jpeg.");
                        }else if(value.size>100000)
                        {
                           e.preventDefault();
                            alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 100000 bytes."); 
                        }
                    });
                };
                function guardar(){
                    if ($("#form").kendoValidator().data("kendoValidator").validate()) {
                        $.post($('#form').attr( 'action' ), $('#form').serialize(),function(result){                            
                            verMensaje(result);
                        },'json');
                    } 
                }
                
                function eliminarFoto(){
                    $.post("../catalogos/generals/usuario.do",{ modo:'delImage'},function(result){
                        if (result.success)  
                            $("#nfoto").attr("src", "../recursos/img/userDefault.png");
                         else 
                         {                                        
                            $(".success-message").hide();
                            $(".error-message").show();
                        }
                    },'json');
                }
                
                function setPerfilSelected()
                {
                    $.post("/coreappv2/general.do", {modo:"setPerfil",userPerfil:$("#userPerfil").val()},function(result){
                        if (result.success)               
                            location.reload();
                         else 
                            alert("Perfil no encontrado");
                    },'json');
                }
            </script>
    </body>
</html>
