<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - catálogo general</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>   
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include> 
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-CATGEN"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Modulo de configuración</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-briefcase"></i> Catálogo general
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row" id="row-bootstrapkendo-wrapper">                           
                        <div id="grid" style="height: 100%;"></div> 
                    </div>
                </div>
            </div>
            <div style="visibility: hidden;">                                               
                <div id="upload">                                
                    <input name="modo" id="modo" type="hidden" value="cargaCsv" />
                    <input name="archivo" id="archivo" type="file"  />   
                </div>
                <div id="ayuda"> 
                    <div class="k-block k-info-colored" style="padding-left: 10px;">                                        
                        <h3> Detalle del Error:</h3>
                        <ul>
                            <li class="msg-info">
                            </li>
                        </ul>
                    </div>
                </div>
                <form id="formExport" name="formExport" action="../../generals/exportar.do"  method="post" style='float:left;'>
                    <input type="hidden" id="filtro" name="filtro" value=""/>
                    <input type="hidden" id="total" name="total" value=""/>
                    <input type="hidden" id="modo" name="modo" value="EXPORT_CATAL_GENERAL"/> 
                    <input type="hidden"  id="cmbTipoTool" name="cmbTipoTool" value="XLSX"/>                
                </form>
            </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var wnd,wndVisor;
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/catalogoGeneral.do",
                        dataType: "json",
                        complete: function(e) {
                           $("#filtro").val($.parseJSON(e.responseText).filter);
                           $("#total").val($.parseJSON(e.responseText).total);
                        }
                     },
                     update: {
                        url: "../generals/catalogoGeneral.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/catalogoGeneral.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/catalogoGeneral.do?modo=nuevo",
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
                            id: "coreCatalogoGeneralId",
                            fields: {
                                coreCatalogoGeneralId: { type: "number",editable: false, nullable: true},
                                dominio: { type: "string", validation: { required: true,maxlength:30,pattern:"^[\\w_]{1,30}$" }},
                                valor: { type: "string", validation: { required: true,maxlength:100 } },
                                atributo1: { type: "string",validation: {maxlength:100}},
                                atributo2: { type: "string",validation: {maxlength:100}},
                                esBorrable: { type: "string",defaultValue: 'N'},
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
                    selectable: "row",
                    resizable: true,
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                    toolbar: ["create",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"},{ text: ' Importar excel',imageClass: "fa fa-arrow-circle-up"},{ text: ' Exportar excel',imageClass: "fa fa-file-excel-o"}],
                    columns: [
                        { field:"dominio", title: "Dominio", width: "200px"},
                        { field: "valor", title:"Valor",groupable: false },
                        { field: "atributo1", title:"Atributo 1",filterable: false,groupable: false  },
                        { field: "atributo2", title:"Atributo 2",groupable: false,filterable: false  },
                        { field: "esBorrable",title:"Editable", width: "80px", values: aconfirmar,filterable: false  },
                        { field: "estatus",title:"Estatus", width: "100px", values: aestatus },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable: "inline",
                    dataBound:onDataBound
                });
                $(".k-grid-Importarexcel").click(function(){
                    if(confirm("Esto puede demorar algunos minutos, ¿Desea continuar?"))
                        subirCsv();
                });
                $(".k-grid-Exportarexcel").click(function(){ 
                    if($("#total").val()<1000000){
                        $("#cmbTipoTool").val("XLSX");
                        $("#formExport").submit(); 
                    }
                    else{
                        if(confirm("El formato XLSX solo soporta 1000000 de registros. ¿Desea descargar csv separado por pipes(|)?")){
                            $("#cmbTipoTool").val("CSV");
                            $("#formExport").submit(); 
                        }
                    }
                });   
                wnd = $("#upload")
                    .kendoWindow({
                        title: "Subir csv",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 420
                    }).data("kendoWindow");
                    wndVisor = $("#ayuda").kendoWindow({
                        title: "Información de ayuda",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 800,
                        height:340
                    }).data("kendoWindow");
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
            function subirCsv() {
                wnd.center().open();
                $("#archivo").kendoUpload({
                    async: {
                        saveUrl: "../generals/catalogoGeneral.do",
                        autoUpload: true
                    },
                    success: onSuccess,
                    multiple:false,
                    select: onSelect
                });
            }
            var onSelect = function(e) {   
                $.each(e.files, function(index, value) {
                    if(value.extension.toUpperCase() !== ".CSV") {
                        e.preventDefault();
                        alert("Archivo permitido csv separado por pipe(|).");
                    }else if(value.size>100000)
                    {
                       e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 1000000 bytes."); 
                    }
                });
            };
            function onSuccess(e) {
                if (typeof (e.response) !== "undefined")
                {
                    verMensaje(e.response);
                    wnd.close();
                    $("#grid").data("kendoGrid").dataSource.read(); 
                    $(".msg-info").html(e.response.msg);
                    if(e.response.msg.indexOf("error")>-1)
                        wndVisor.center().open();
                }
            }
        </script>
    </body>
</html>
