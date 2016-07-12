<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - bitacora</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>        
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>                
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CAT"/>
                    <jsp:param name="M_SELECT" value="S-C-SISTEM"/>
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
                                    <a href="#"><i class="fa fa-adn"></i> Modulo de auditoría</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-table"></i> Bitacora
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
        <style>
            /*
                Use the DejaVu Sans font for display and embedding in the PDF file.
                The standard PDF fonts have no support for Unicode characters.
            */
            .k-grid {
                font-family: "DejaVu Sans", "Arial", sans-serif;
            }

            /* Hide the Grid header and pager during export */
            .k-pdf-export .k-grid-toolbar,
            .k-pdf-export .k-pager-wrap
            {
                display: none;
            }
        </style>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>

        <!-- Load Pako ZLIB library to enable PDF compression -->
        <script src="${pageContext.request.contextPath}/recursos/js/pako_deflate.min.js"></script>
        <script>
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../auditorias/bitacora.do",
                        dataType: "json"
                     },
                        parameterMap: function(options, type) {
                             if (type === "read") {
                                 if (options.filter) {
                                     for (var i = 0; i < options.filter.filters.length; i++) {
                                         if (options.filter.filters[i].field === 'fechaOperacion') {
                                            options.filter.filters[i].value = kendo.toString(options.filter.filters[i].value, "yyyy-MM-dd");
                                         }
                                     }
                                 }
                             }
                             return options;
                         }
                     },
                     schema: {
                        data: "data",
                        total: "total",
                        model: {
                            id: "bitacoraId",
                            fields: {
                                bitacoraId: { type: "number",editable: false, nullable: true},
                                usuarioId: { type: "number"},
                                tipo: { type: "string" },
                                origen: { type: "string" },
                                operacion: { type: "string" },
                                time: { type: "string" },
                                fechaOperacion: { type: "date"},
                                nombre: { type: "string" }
                            }
                        },
                        error: "errors"
                    },
                    serverPaging: true,
                    serverSorting: true,
                    serverFiltering: true,
                     pageSize:50,
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
                        pageSizes: [50, 100, 250],
                        numeric: false
                    },
                    pdf: {
                        allPages: true,
                        fileName: "Bitacora.pdf",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    excel: {
                        allPages: true,
                        fileName: "Bitacora.xlsx",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    toolbar: ["pdf","excel",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"bitacoraId", title: "Id", width: "80px"},
                        { field: "nombre", title:"Usuario",width: "150px",template:"#:usuarioId#) #:nombre#" },
                        { field: "tipo", title:"Tipo",width: "100px"  },
                        { field: "origen", title:"Origen",  width: "100px" },
                        { field: "operacion", title:"Operación", width: "400px" },
                        { field: "fechaOperacion", title:"Fecha operación", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }, width: "130px"},
                        { field: "time", title:"hh:mm:ss", width: "70px",sortable:false,filterable:false }]
                });
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
        </script>
    </body>
</html>