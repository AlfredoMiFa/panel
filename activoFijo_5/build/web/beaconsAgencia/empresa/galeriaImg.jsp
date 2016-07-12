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
                    <jsp:param name="M_ACTIVE" value="C-G-AGEBEA:C-G-EMPBEA"/>
                    <jsp:param name="M_SELECT" value="C-G-GALIMG"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Empresas</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file-pdf-o"></i> Galéria de imagenes
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
            var tipoImg= [{"value": "V","text": "VERSION"},{"value": "C","text": "COLORES"}];
            var wnd,uploadTemplate,wndVisor;
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/galeriaImg.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/galeriaImg.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/galeriaImg.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/galeriaImg.do?modo=nuevo",
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
                            id: "idVehiculo",
                            fields: {
                                idVehiculo: { field:"idVehiculo",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}}, 
                                idGaleriaImagen: { type: "number",editable: false, nullable: true},                                
                                descripcion: { type: "string", validation: { required: true,maxlength:20 }},
                                tipoGaleria: { type: "string", defaultValue:"V", values:tipoImg }
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
                    toolbar: ["create",{ text: " Adjuntar imagen" ,imageClass: "fa  fa-arrow-circle-up"},{ text: " Visualizar imagen" ,imageClass: "fa  fa-arrow-circle-up"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"idVehiculo",      title:"Clave vehículo",width:"120px", editor:listaDropDownVehiculo,template:"#=idVehiculo.text#"},
                        { field:"idGaleriaImagen", title: "Clave imagen", width: "150px"},
                        { field: "descripcion",    title:"Descripción"},
                        { field: "tipoGaleria",    title: "Tipo de galeria",filterable: false, values:tipoImg},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable:"inline"
                });
                wnd = $("#upload")
                    .kendoWindow({
                        title: "Adjuntar imagen",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 420
                    }).data("kendoWindow");
                wndVisor = $("#visor")
                    .kendoWindow({
                        title: "Visor de imagenes",
                        modal: true,
                        visible: false,
                        resizable: true,
                        width: 600,
                        height:400
                    }).data("kendoWindow");
                uploadTemplate = kendo.template($("#template").html());               
                $(".k-grid-Adjuntarimagen").click(function(e){                    
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        wnd.content(uploadTemplate(row));
                        wnd.center().open();
                        $("#archivo").kendoUpload({
                            async: {
                                saveUrl: "../../beaconsAgencias/galeriaImg.do",
                                autoUpload: true
                            },
                            success: onSuccess,
                            multiple:false,
                            select: onSelect,
                            upload: function (e) {
                                e.data = { idGaleriaImagen: $("#idGaleriaImagen").val() };
                            }
                        });
                    }else
                        alert("Seleccione un registro");
                });                
                $(".k-grid-Visualizarimagen").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());                    
                    if(row!=null){
                        //var aparam=row.parametros.split("||");
                        $("#cc2").attr("src","../../beaconsAgencias/galeriaImg.do?modo=getImage&id="+row.idGaleriaImagen);
                        wndVisor.center().open();
                    }else
                        alert("Seleccione un registro");
                });                 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
                
                
            });
            var onSelect = function(e) {                
                var html="";
                $.each(e.files, function(index, value) {
                    if(value.extension.toUpperCase() !== ".JPG") {
                        e.preventDefault();
                        alert("Archivo permitido jpg.");
                    }else if(value.size>150000)
                    {
                       e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 150000 bytes."); 
                    }
                });
            };
            function onSuccess(e) {
                if (typeof (e.response) !== "undefined"){
                    $(".k-upload-files.k-reset").find("li").remove();
                    verMensaje(e.response);
                }
            }
            
            function listaDropDownVehiculo(container, options) {
                $('<input name="idVehiculo" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/vehiculos.do?modo=combo",
                                }
                            }
                        }
                    });
            }               
        </script>
        <script type="text/x-kendo-template" id="template">
            <div class="property extended">
                <br/>
                <label>Imagen:</label> <input type="hidden" name="idGaleriaImagen" id="idGaleriaImagen" value="#=idGaleriaImagen#"/>
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
