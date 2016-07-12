<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - avisos</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>         
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include> 
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-PERS"/>
                    <jsp:param name="M_SELECT" value="C_I_AVISOS"/>                            
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
                                    <a href="#"><i class="fa fa-info"></i> Modulo de información</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-bell"></i> Avisos
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row"> 
                        <div class="col-lg-12" id="row-bootstrapkendo-wrapper">                            
                            <div id="grid" style="height: 100%;"></div> 
                        </div>
                    </div>
                </div>
            </div>                                        
            <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>

            <script>
                var aprioridad = [{"value": "AL","text": "ALTA"},{"value": "ME","text": "MEDIANA"},{"value": "BA","text": "BAJA"}];
                $(document).ready(function(){
                    inicializar();      

                    var dataSource = new kendo.data.DataSource({
                       autoSync: true,
                       transport: {
                         read:   {
                            url: "../generals/aviso.do?tipo=admin",
                            dataType: "json"
                         },
                           parameterMap: function(options, type) {
                                if (type === "read") {
                                    if (options.filter) {
                                        for (var i = 0; i < options.filter.filters.length; i++) {
                                            if (options.filter.filters[i].field === 'fechaCreacion' ) {
                                                options.filter.filters[i].value = kendo.toString(options.filter.filters[i].value, "yyyy-MM-dd");
                                            }
                                        }
                                    }
                                }else  if (type === "create" ||type === "update") {
                                    options.fechaCreacion=kendo.toString(options.fechaCreacion, "dd/MM/yyyy");
                                }
                                return options;
                            }
                         },
                         schema: {
                            data: "data",
                            total: "total",
                            model: {
                                id: "coreAvisoId",
                                fields: {
                                    coreAvisoId: { type: "number",editable: false, nullable: true},
                                    titulo: { type: "string", validation: { required: true,maxlength:45 }},
                                    contenido: { type: "string", validation: { required: true,maxlength:10000 } },
                                    prioridad: { type: "string",editable: false,defaultValue: 'ME'},
                                    vigencia: { type: "number",editable: false},
                                    fechaCreacion: { type: "date",editable: false},
                                    fechaCreacionHora: { type: "string",editable: false}
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
                        toolbar: [{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "coreAvisoId",hidden:true},
                            { field:"titulo", title: "Título",width:"300px"},
                            { field: "contenido", title:"Contenido",encoded: false,width:"400px"}, 
                            { field: "fechaCreacion", title:"Creación",width:"100px", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }},   
                            { field: "fechaCreacionHora",title:"hh:mm:ss",width:"70px",filterable:false,sortable:false},  
                            { field: "prioridad", title:"Prioridad",width:"100px", values: aprioridad},  
                            { field: "vigencia", title:"Vigencia",width:"100px"}]
                    }); 
                    $(".k-grid-Limpiarfiltros").click(function(e){
                        $("#grid").data("kendoGrid").dataSource.filter([]);
                    });
                });
            </script>
        </div>
    </body>
</html>