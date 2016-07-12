<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - exportación query</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-REPORT"/>
                    <jsp:param name="M_SELECT" value="C-G-EXPORT"/>
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
                                    <i class="fa fa-download"></i> Exportación query
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
                <div id="probarQuery"> 
                    <div class="form-group">
                        <textarea id="query" name="query" required style="width:580px;height: 90px;"></textarea>
                    </div>
                    <div class="form-group" style="text-align: right;">
                        <a href="javascript:probar()" class="k-button">Probar</a>
                        <a href="javascript:descargar()" class="k-button">Descargar excel</a>
                    </div>
                    <div>
                        <div id="gridPrueba" style="width:580px;height: 300px;background-color: silver;"></div>
                    </div>
                </div>
            </div>
        </div>
        <div style="visibility: hidden;">
           <form id="formExport" name="formExport" action="../../generals/exportar.do"  method="post">
                <input type="hidden" id="filtro" name="filtro" value=""/>
                <input type="hidden" id="modo" name="modo" value=""/>
                <input type="hidden" id="querystring" name="querystring" value=""/>
                <input type="hidden" id="cmbTipoTool" name="cmbTipoTool" value="XLSX"/>                    
            </form> 
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>         
            var wnd;
            $(document).ready(function(){
                inicializar();                 
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/exportQuery.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/exportQuery.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/exportQuery.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/exportQuery.do?modo=nuevo",
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
                            id: "cveExportQuery",
                            fields: {
                                cveExportQuery: { type: "string",validation: { required: true,maxlength:20 }},
                                campos: { type: "string", validation: { required: true,maxlength:1000 }},
                                tablas: { type: "string", validation:{ required: true,maxlength:400 } },
                                wherec: { type: "string", validation: {maxlength:400 } },
                                orderBy: { type: "string", nullable: true,maxlength:45 },
                                orderc: { type: "string", nullable: true,maxlength:45 },
                                limitc: {  type: "string", nullable: true,maxlength:10 },
                                header: {  type: "string", nullable: true,maxlength:1000 },
                                appendFilter: {  type: "boolean"},
                                conexionDao: { defaultValue: { id: "", text: "Seleccione una opción"},
                                    validation: {
                                        required: true,
                                        custom: function(input){
                                            if (input.attr('data-value-field')==='id'){   
                                                return input.val() > 0;
                                            }
                                            return true;
                                        },
                                        messages:{custom: "Please enter valid value for my custom rule"}
                                    }},
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
                    selectable: "row",
                    sortable: true,
                    resizable: true,
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                    toolbar: ["create",{ text: " Probar query" ,imageClass: "fa fa-external-link-square"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"cveExportQuery",title:"Clave",width:"180px"},
                        { field:"campos", title: "Campos"},
                        { field: "tablas", title:"Tabla(s)", width: "250px"},
                        { field: "appendFilter", title:"Filtro grid",hidden:true,  width: "100px" },
                        { field: "wherec",title: "Where",hidden:true, width: "350px"},
                        { field: "orderBy", title:"Order by",hidden:true,  width: "150px" },
                        { field: "orderc", title:"Orden",hidden:true,  width: "100px" },
                        { field: "limitc",title:"Limite",width: "100px"},
                        { field: "conexionDao", title:"Conexión BD",hidden:true,  width: "300px", editor: listaDropDownEditor2,template: "#=conexionDao.text#" },
                        { field: "estatus", width: "80px", values: aestatus },
                        { field: "header", title:"Header",hidden:true,  width: "100px" },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable  : "popup",
                    edit:function(e){
                         if(!e.model.isNew())
                             $('input[name = "cveExportQuery"]').attr("disabled", true);
                         $('div[data-container-for= "conexionDao"] .k-dropdown').css({width:'300px'});
                     }
                });   
                wnd = $("#probarQuery")
                    .kendoWindow({
                        title: "Probar query",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 600,
                        height:520
                    }).data("kendoWindow");
                $(".k-grid-Probarquery").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        $("#query").val("SELECT "+row.campos+" FROM "+row.tablas+(row.wherec.trim()!==""?" WHERE "+row.wherec:"")
                                +(row.orderBy.trim()!==""?" ORDER BY "+row.orderBy:"")+(row.orderc.trim()!==""?" "+row.orderc:"")
                                +(row.limitc.trim()!==""?" limit "+row.limitc:" limit 1000"));
                        wnd.center().open();
                    }else
                        alert("Seleccione un registro");
                });         
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownEditor2(container, options) {
                $('<input name="conexionDao" data-text-field="text" data-value-field="id"  data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../generals/conexiones.do?modo=combo",
                                }
                            }
                        }
                    });
            } 
            function probar(){
                var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());                
                var validator = $("#probarQuery").kendoValidator().data("kendoValidator");
                if(validator.validateInput($("#query"))){
                    $.post("../generals/exportQuery.do",{modo:"probarQuery",cveExportQuery:row.cveExportQuery,query:$("#query").val()},function(result){
                        if(result.errors!=null)
                            errorHandler(result);
                        else
                        {
                            $("#gridPrueba").html(result.data);
                            $("#grid2").kendoGrid({height: 300,sortable: true,resizable:true});
                        }
                    },"json"); 
                }
            }
            function descargar(){
                var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                var validator = $("#probarQuery").kendoValidator().data("kendoValidator");
                if(validator.validateInput($("#query"))){
                    $("#modo").val(row.cveExportQuery);
                    $("#filtro").val($("#query").val()); 
                    $("#querystring").val($("#query").val());                    
                    $("#formExport").submit(); 
                }
            }
        </script>
        <style>
            .k-edit-form-container{ width: 600px;}
            input[name=campos],input[name=header],input[name=wherec],input[name=tablas],input[name=orderc] ,input[name=limitc] ,input[name=orderBy],input[name=cveExportQuery]  {
                width: 25em;
            }
        </style>
    </body>
</html>

