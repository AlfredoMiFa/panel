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
                    <jsp:param name="M_SELECT" value="C-G-BITAUSU"/>
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
                                    <i class="fa fa-unlock-alt"></i> Bitacora Usuarios
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
            var estatus= [{"value": "Ac","text": "ACTIVO"},{"value": "In","text": "INACTIVO"},{"value": "In","text": "MANTENIMIENTO"},{"value": "In","text": "ALMACEN"},{"value": "In","text": "PROCESO"},{"value": "Ac","text": "ALTA"}];
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/Bitacora.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Bitacora.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Bitacora.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Bitacora.do?modo=nuevo",
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
                            id: "idBitacoraUsuario",
                            fields: {
                                idBitacoraUsuario:          { type: "number", editable: false, nullable: false },
                                idUsuario:                  { type: "number", editable: true,  nullable: false },
                                usuario:                    { type: "string", editable: true,  nullable: false },
                                actividad:                  { type: "string", editable: true,  nullable: false, defaultValue:'In', values:estatus },
                                cantidadActivos:            { type: "number", editable: true,  nullable: false },
                                categoria:                  { type: "string", editable: true,  nullable: false },
                                fecha:                      { type: "date" },
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
                        fileName: "BitacoraUsuarios.pdf",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    excel: {
                        allPages: true,
                        fileName: "BitacoraUsuarios.xlsx",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    toolbar: ["pdf","excel",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "idBitacorUsuario",hidden:true},
                            { field: "idUsuario", title:"Clave Usuario",encoded: false,width:"150px"},
                            { field: "usuario",         title:"Usuario",encoded: false,width:"150px"},
                            { field: "actividad",       title:"Actividad",encoded: false,width:"150px", defaultValue:'In', values:estatus},
                            { field: "cantidadActivos", title:"Cantidad de activos",encoded: false,width:"150px"},
                            { field: "categoria",       title:"Categoria",encoded: false,width:"150px"},
                            { field: "fecha",           title:"Fecha",format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"},width:"150px"},
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

