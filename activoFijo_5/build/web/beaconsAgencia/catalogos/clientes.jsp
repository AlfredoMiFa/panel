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
                    <jsp:param name="M_SELECT" value="C-G-CLIENT"/>
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
                                    <i class="fa fa-unlock-alt"></i> Clientes
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
            var Habilitado= [{"value": "true","text": "HABILITADO"},{"value": "false","text": "DESHABILITADO"}];
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/clientes.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/clientes.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/clientes.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/clientes.do?modo=nuevo",
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
                            id: "idCliente",
                            fields: {
                                idCliente:  { type: "number", editable: false, nullable: false },
                                nombre:     { type: "string", validation: { required: true,maxlength:40 }},
                                apPaterno:  { type: "string", validation: { required: true,maxlength:40 }},
                                apMaterno:  { type: "string", validation: { required: true,maxlength:40 }},
                                contrasena: { type: "string", validation: { required: true,maxlength:15 }},
                                activo:     { type: "string", validation: { required: true,maxlength:1 },defaultValue:'true', values: Habilitado},
                                email:      { type: "string",validation: { required: true,maxlength:45,email:true }},
                                fechaNac:   { type: "date", },
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
                            { field: "idcliente",   hidden:true},                             
                            { field: "nombre",      title:"Nombre",width:"200px"}, 
                            { field: "apPaterno",   title:"Apellido paterno",width:"200px"}, 
                            { field: "apMaterno",   title:"Apelldio materno",width:"200px"}, 
                            { field: "contrasena", title:"Contraseña",filterable: false,sortable:false, width: "120px" ,
                            format: "******",
                            editor: function (container, options) {
                                $('<input data-text-field="' + options.field + '" ' +
                                    'class="k-input k-textbox" ' +
                                    'type="password" ' +
                                    'data-value-field="' + options.field + '" ' +
                                    'data-bind="value:' + options.field + '"/>')
                                    .appendTo(container);
                            }},                            
                            { field: "activo",      title:"Activo",width:"200px", values: Habilitado}, 
                            { field: "email",       title:"Email",width:"250px"},  
                            { field: "fechaNac",    title:"Fecha de nacimiento", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }, width: "150px"}, 
                        
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline"
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
        </script>
    </body>
</html>

