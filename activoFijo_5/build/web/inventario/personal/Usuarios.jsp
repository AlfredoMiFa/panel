<!DOCTYPE html>
<%@page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - derecho sistema</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="C-G-AGEBEA:C-G-CATBEA"/>
                    <jsp:param name="M_SELECT" value="C-G-USUARIOS"/>
                </jsp:include>
            </nav>
            <div id="page-wrapper">
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
                                    <a href="#"><i class="fa fa-gears"></i> Catálogos</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-unlock-alt"></i> Usuarios
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row" id="row-bootstrapkendo-wrapper">                           
                        <div id="grid" style="height: 100%;"></div> 
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/Usuarios.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Usuarios.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Usuarios.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Usuarios.do?modo=nuevo",
                          type: "POST",
                          dataType: "json",
                          complete: function(e) {
                            $("#grid").data("kendoGrid").dataSource.read(); 
                             if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                          }
                       }
                     },
                     schema: {
                        data: "data",
                        total: "total",
                        model: {
                            id: "idUsuario",
                            fields: {
                                idUsuario:          { type: "number", editable: false, nullable: false },
                                usuario:            { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:25 }},
                                contrasenia:        { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:20 }},
                                nombre:             { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:45  }},
                                apellidoPaterno:    { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:45  }},
                                apellidoMaterno:    { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:45 }},
                                email:              { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:30, email: true, placeholder : "e.j. myname@example.net"  }},
                                idEmpresa:          { field: "idEmpresa", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                            }
                        },
                        error: "errors"
                    },
                    serverPaging: true,
                    serverSorting: true,
                    serverFiltering: true,
                     pageSize:20,
                    error: function (e) {
                        errorHandler(e);
                    }
                });
                $("#grid").kendoGrid({
                    dataSource: dataSource,
                    filterable: true,
                    reorderable: true,
                    sortable: true,
                    resizable: true,
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                        toolbar: ["create",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "idUsuario",hidden:true},
                            { field: "usuario",        title:"Usuario",encoded: false,width:"150px"}, 
                            { field: "contrasenia",        title:"Contraseña",encoded: false,width:"150px", format: "******" }, 
                            { field: "nombre",        title:"Nombre",encoded: false,width:"150px"}, 
                            { field: "apellidoPaterno",        title:"Apellido paterno",encoded: false,width:"150px"}, 
                            { field: "apellidoMaterno",        title:"Apellido materno",encoded: false,width:"150px"}, 
                            { field: "email",        title:"E-mail",encoded: false,width:"200px"}, 
                            { field: "idEmpresa", title:"Empresa",width:"200px",editor:listaDropDownEmpresas,template:"#=idEmpresa.text#"},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline"
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            
            function listaDropDownEmpresas(container, options){
                 $('<input name="idEmpresa" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/empresa.do?modo=combo",
                                }
                            }
                        }
                    });
            }
        </script>
    </body>
</html>

