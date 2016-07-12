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
                    <jsp:param name="M_SELECT" value="C-G-HISTORIAL"/>
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
                                    <i class="fa fa-unlock-alt"></i> Historial Activos
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
                        url: "../../beaconsAgencias/Historial.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Historial.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Historial.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Historial.do?modo=nuevo",
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
                            id: "idHistorialActivo",
                            fields: {
                                idHistorialActivo:      { type: "number", editable: false, nullable: false },
                                oficina:                { type: "string", editable: false, nullable: false },
                                departamento:           { type: "string", editable: false, nullable: false},
                                idIncidencia:           { type: "number", editable: false, nullable: false},
                                incidencia:             { type: "string", editable: false, nullable: false},
                                idActivo:               { type: "number", editable: false, nullable: false},
                                activo:                 { type: "string", editable: false, nullable: false},
                                idUsuario:              { type: "number", editable: false, nullable: false},
                                usuario:                { type: "string", editable: false, nullable: false},
                                actividad:              { type: "string", editable: false, nullable: false},
                                fecha:                  { type: "date"},
                                tipoMovimiento:         { type: "string", editable: false, nullable: false},
                                observaciones:          { type: "string", editable: false, nullable: false},
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
                    pdf: {
                        allPages: true,
                        fileName: "Historial.pdf",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    excel: {
                        allPages: true,
                        fileName: "Historial.xlsx",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                        toolbar: ["pdf","excel",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [      
                            { field: "idHistorialActivo",hidden:true},
                            { field: "oficina",hidden:false, title:"Oficina", width:"150px"},
                            { field: "departamento", title:"Departamento", width:"150px",encoded: false},
                            { field: "idIncidencia", title:"Clave incidencia", width:"100px", encoded: false},
                            { field: "incidencia", title:"Incidencia", width:"150px", encoded: false},
                            { field: "idActivo", title:"Clave Activo", width:"100px", encoded: false},
                            { field: "activo", title:"Activo", width:"150px", encoded: false},
                            { field: "idUsuario", title:"Clave usuario", width:"100px", encoded: false},
                            { field: "usuario", title:"Usuario", width:"150px", encoded: false},
                            { field: "actividad", title:"Actividad realizada", width:"200px"},
                            { field: "fecha", title:"Fecha", format: "{0:dd/MM/yyyy}", filterable: { ui: "datapicker"}, width:"150px"},
                            { field: "tipoMovimiento", title:"Tipo de movimiento", width: "200px", encoded: false},
                            { field: "observaciones",        title:"Observaciones",encoded: false,width:"330px"}, 
                        ],
                        editable: "inline"
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
        </script>
    </body>
</html>

