<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - reportes jasper</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>      
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-REPORT"/>
                    <jsp:param name="M_SELECT" value="CG-REPJASP"/>
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
                                    <i class="fa fa-file-pdf-o"></i> Reportes jasper
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
                <div id="upload"></div>
                <div id="visor">
                    <iframe id="cc2" width="100%" src="" height="99%" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>
                </div>                    
            </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var wnd,uploadTemplate,wndVisor;
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/reporteJasper.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/reporteJasper.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/reporteJasper.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/reporteJasper.do?modo=nuevo",
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
                            id: "coreReporteJasperId",
                            fields: {
                                coreReporteJasperId: { type: "number",editable: false, nullable: true},
                                nombre: { type: "string", validation: { required: true,maxlength:20 }},
                                cvesServicios: { type: "string", validation: {maxlength:45} },
                                nombreArchivo: { type: "string", validation: { required: true,maxlength:45 } },
                                descripcion: { type: "string", nullable: true,maxlength:200 },
                                parametros: { type: "string", nullable: true,maxlength:100 },
                                categoria: {  type: "string",defaultValue: 'RE'},
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
                    sortable: true,
                    selectable: "row",
                    resizable: true,
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                    toolbar: ["create",{ text: " Subir reporte" ,imageClass: "fa  fa-arrow-circle-up"},{ text: " Probar reporte" ,imageClass: "fa fa-external-link-square"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"coreReporteJasperId",title:"ID",width:"90px"},
                        { field:"nombre", title: "Nombre", width: "200px"},
                        { field: "descripcion", title:"Descripción",hidden: true},
                        { field: "parametros",title: "Parametros",filterable: false},
                        { field: "categoria", title:"Categoría",  width: "150px", values: areporte },
                        { field: "conexionDao", title:"Conexión BD",hidden: true,  width: "150px", editor: listaDropDownEditor2,template: "#=conexionDao.text#" },
                        { field: "cvesServicios",title:"Servicios",width: "100px"},
                        { field: "estatus", width: "100px", values: aestatus },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable  : "popup",
                    edit:function(e){
                         if(!e.model.isNew())
                             $('input[name *= "nombre"]').attr("disabled", true);
                         $('div[data-container-for= "categoria"] .k-dropdown').css({width:'300px'});
                         $('div[data-container-for= "conexionDao"] .k-dropdown').css({width:'300px'});
                         $('input[name = "parametros"]').attr("placeholder","variable1,variable2,...||valor1,valor2,...");
                     }
                });
                wnd = $("#upload")
                    .kendoWindow({
                        title: "Subir reporte",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 420
                    }).data("kendoWindow");
                wndVisor = $("#visor")
                    .kendoWindow({
                        title: "Visor reporte",
                        modal: true,
                        visible: false,
                        resizable: true,
                        width: 600,
                        height:400
                    }).data("kendoWindow");
                uploadTemplate = kendo.template($("#template").html());  
                
                $(".k-grid-Probarreporte").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        var aparam=row.parametros.split("||");
                        $("#cc2").attr("src","../../creaReporte.view?id="+row.id+"&nomParametros="+aparam[0]+"&valParametros="+aparam[1]);
                        wndVisor.center().open();
                    }else
                        alert("Seleccione un registro");
                });
                $(".k-grid-Subirreporte").click(function(e){                    
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        wnd.content(uploadTemplate(row));
                        wnd.center().open();
                        $("#archivo").kendoUpload({
                            async: {
                                saveUrl: "../generals/reporteJasper.do",
                                autoUpload: true
                            },
                            success: onSuccess,
                            multiple:false,
                            select: onSelect,
                            upload: function (e) {
                                e.data = { coreReporteJasperId: $("#coreReporteJasperId").val() };
                            }
                        });
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
            var onSelect = function(e) {                
                var html="";
                $.each(e.files, function(index, value) {
                    if(value.extension.toUpperCase() !== ".JASPER") {
                        e.preventDefault();
                        alert("Archivo permitido jasper.");
                    }else if(value.size>100000)
                    {
                       e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 100000 bytes."); 
                    }
                });
            };
            function onSuccess(e) {
                if (typeof (e.response) !== "undefined"){
                    $(".k-upload-files.k-reset").find("li").remove();
                    verMensaje(e.response);
                }
            }
        </script>
        <script type="text/x-kendo-template" id="template">
            <div class="property extended">
                <br/>
                <label>Archivo jasper:</label> <input type="hidden" name="coreReporteJasperId" id="coreReporteJasperId" value="#=coreReporteJasperId#"/>
                <input name="archivo" id="archivo" type="file"  />
                <br/>
            </div>
        </script>
        <style>
            .k-edit-form-container{ width: 700px;}
            input[name=parametros],input[name=descripcion],input[name=nombre] {
                width: 25em;
            }
        </style>
    </body>
</html>
