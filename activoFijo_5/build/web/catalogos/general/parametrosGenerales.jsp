<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - parametros generales</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body >
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>                
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-A-PARGEN"/>
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
                                <li class="active">
                                    <i class="fa fa-edit"></i> Parametros de configuración
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
                        url: "../generals/parametrosGenerales.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/parametrosGenerales.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/parametrosGenerales.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/parametrosGenerales.do?modo=nuevo",
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
                            id: "clave",
                            fields: {
                                clave: { type: "string", validation: { required: true,maxlength:10 }},
                                descripcion: { type: "string", validation: { required: true,maxlength:300 }},
                                valor: { type: "string", validation: { required: true,maxlength:100 } },
                                esBorrable: { type: "string",defaultValue: 'N'},
                                tipoDato: { type: "string",defaultValue: 'String'},
                                estatus: { type: "string",defaultValue: 'AC'}
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
                        { field: "clave", title:"Clave" , width: "150px"},
                        { field:"descripcion", title: "Descripción",  width: "230px"},
                        { field: "tipoDato", title:"Tipo dato",values:atipodato,  width: "110px"},
                        { field: "valor", title:"Valor",  width: "200px" },
                        { field: "esBorrable",title:"Editable", width: "80px", values: aconfirmar,filterable: false  },
                        { field: "estatus", width: "100px", values: aestatus },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "220px" }],
                    editable: "inline",
                    dataBound:onDataBound
                });
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
                    
            function onDataBound(e) {
                var grid = $("#grid").data("kendoGrid");
                var gridData = grid.dataSource.view();

                for (var i = 0; i < gridData.length; i++) {
                    var currentUid = gridData[i].uid;
                    if (gridData[i].esBorrable !== "S") {
                        var currenRow = grid.table.find("tr[data-uid='" + currentUid + "']");
                        var editButton = $(currenRow).find(".k-grid-edit");
                        var deleteButton = $(currenRow).find(".k-grid-delete");
                        deleteButton.hide();
                        editButton.hide();
                    }
                }
            }
        </script>
    </body>
</html>