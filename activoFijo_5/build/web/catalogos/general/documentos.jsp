<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - documentos</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>        
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>                
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-PERS"/>
                    <jsp:param name="M_SELECT" value="C_I_GESDOC"/>
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
                                    <i class="fa fa-files-o"></i> Gestor documental
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row">                           
                        <div class="col-lg-8" id="row-bootstrapkendo-wrapper">                            
                            <div id="grid" style="height: 100%;"></div> 
                        </div>
                        <div class="col-lg-4"> 
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-files-o"></i> Carpetas</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div style="margin: 5px;">
                                            <input id="appendNodeText" value="" class="k-textbox" style="width:100%;" placeholder="Nombre de la carpeta"/>
                                        </div>
                                        <div style="text-align: center;">
                                            <button class="k-button" id="appendNodeToSelected">Agregar carpeta</button>
                                            <button class="k-button" id="removeNode">Borrar carpeta</button>
                                        </div><br/>
                                        <div id="treeview"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-arrow-circle-up"></i> Subir archivos</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <input type="hidden" id="idCarpeta" name="idCarpeta" value="" readonly="readonly"/>
                                            <em>Archivos permitidos:</em> <br/> .docx, .xlsx, .doc, .xls, .pdf, .txt .jpeg y .png<br/>
                                            <em>Arrastre aquí los archivos a subir</em>
                                            <input name="archivo" id="archivo" type="file"/>
                                        </div>
                                    </div>
                                </div>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>
            <iframe id="cc2" width="0" src="" height="0" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
             var dataSource,wndVisor;
            $(document).ready(function(){
                inicializar();
                kendo.culture("es-MX");                     
                $("#split-container-horizontal2").kendoSplitter({
                    panes: [                        
                        { collapsible: false},
                        { collapsible: false,resizable:false,size:"300px" }
                    ]
                }); 
                $.getJSON("../../general/documentos.do",{modo:"arbolCarpetas"}, function (data) {
                    $("#treeview").kendoTreeView({
                        dataSource: data,
                        select: onSelect
                    });
                }) ;    
                function onSelect(e) {
                    var item = this.dataItem(e.node);
                    $("#path").html(item.text);
                    $("#idCarpeta").val(item.id);
                    $("#grid").data("kendoGrid").dataSource.filter({ field: "coreCarpetaId", operator: "eq", value: item.id });
                    $("#grid").data("kendoGrid").dataSource.read();
                }
                $("#appendNodeToSelected").click(function(){
                    if($("#appendNodeText").val()!==''){
                        var treeview = $("#treeview").data("kendoTreeView");
                        var selectedNode = treeview.select();
                        var item=treeview.dataItem(selectedNode);
                        if (selectedNode.length > 0){
                            $.getJSON("../../general/documentos.do",{modo:"agregarCarpeta",idPadre:item.id,nombre:$("#appendNodeText").val()}, function (data) {
                                if(data.success){
                                    treeview.append({
                                    id:data.id,
                                    text: $("#appendNodeText").val(),
                                    spriteCssClass:"folder"}, selectedNode);
                                }else
                                    alert(data.msg);
                            }) ; 
                        }else
                            alert("Seleccione una carpeta");
                    }else
                        alert("Escriba el nombre de la carpeta");
                });

                $("#removeNode").click(function() {
                    var treeview = $("#treeview").data("kendoTreeView");
                    var selectedNode = treeview.select();
                    var item=treeview.dataItem(selectedNode);
                    if(!item.items)
                    {
                        if(confirm("¿Esta seguro de borrar esta carpeta?"))
                            $.getJSON("../../general/documentos.do",{modo:"borrarCarpeta",id:item.id}, function (data) {
                                if(data.success){
                                    treeview.remove(selectedNode);
                                }else
                                    alert(data.msg);
                            });
                    }else
                        alert("La carpeta no esta vacia");                        
                });                    
                $("#archivo").kendoUpload({
                    async: {
                        saveUrl: "../../general/documentos.do?modo=upload",
                        autoUpload: true
                    },
                    localization:{
                        dropFilesHere:"soltar aquí el archivo"
                    },
                    success: onSuccess,
                    select: onSelectU,
                    upload: function (e) {
                        e.data = { idCarpeta: $("#idCarpeta").val() };
                    }
                });
                dataSource = new kendo.data.DataSource({
                    transport: {
                        read: {
                            url: "../../general/documentos.do?modo=listaDocumentos",
                            dataType: "json"
                        },
                        update: {
                           url: "../../general/documentos.do?modo=editarDocumento",
                           type: "POST",
                           dataType: "json",
                           complete: function(e) {
                                $("#grid").data("kendoGrid").dataSource.read(); 
                               if (typeof (e.responseText) !== "undefined")
                                   verMensaje($.parseJSON(e.responseText));
                           }
                        },
                        destroy: {
                            url: "../../general/documentos.do?modo=eliminarDocumento",
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
                            id: "coreDocumentoId",
                            fields: {
                                coreDocumentoId: { editable: false, nullable: true },
                                nombreArchivo: { editable: false,type: "string" },
                                tipomime: { editable: false,type: "string" },
                                descripcion: { type: "string",validation:{required:true,maxlenght:200} },
                                coreCarpetaId: { editable: false,type: "number" }
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
                        pageSizes: [50, 100, 250],
                        numeric: false
                    },
                    toolbar: [{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field: "coreDocumentoId", title:"Id",  width: "50px",hidden:true },
                        { field: "tipomime", title:"Tipo", width: "70px",template:"<a href='javascript:descargar(#:coreDocumentoId#);'><img src='../../recursos/img/#:tipomime#.png' alt='#: coreDocumentoId # image' style='width:40px;' /></a>" },
                        { field: "nombreArchivo", title:"Nombre archivo", width: "160px"},
                        { field: "descripcion", title:"Descripción", width: "160px" },
                        { field: "coreCarpetaId",title:"Carpeta", width: "150px",hidden:true  },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable: "inline",
                    edit:function(e){
                         if(!e.model.isNew()){
                             $('input[name = "nombreArchivo"]').attr("disabled", true);
                         }
                     }
                }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });    
            var onSelectU = function(e) {  
                if($("#idCarpeta").val()!==""){
                    $.each(e.files, function(index, value) {
                        if(!existeArchivo(value.name)){
                            if(  value.extension.toUpperCase() !== ".PNG" && value.extension.toUpperCase() !== ".JPG" && value.extension.toUpperCase() !== ".GIF"
                             && value.extension.toUpperCase() !== ".DOCX" && value.extension.toUpperCase() !== ".XLSX" && value.extension.toUpperCase() !== ".DOC"
                             && value.extension.toUpperCase() !== ".XLS" && value.extension.toUpperCase() !== ".PDF" && value.extension.toUpperCase() !== ".TXT") {
                                e.preventDefault();
                                alert("Archivos permitidos .docx, .xlsx, .doc, .xls, .pdf, .txt .jpg y .png");
                            }else if(value.size>8000000){
                               e.preventDefault();
                                alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 8000000 bytes."); 
                            }
                        }else{
                            if(confirm("El archivo ya existe, ¿desea reescribirlo?.")){
                                if(  value.extension.toUpperCase() !== ".PNG" && value.extension.toUpperCase() !== ".JPG" && value.extension.toUpperCase() !== ".GIF"
                                && value.extension.toUpperCase() !== ".DOCX" && value.extension.toUpperCase() !== ".XLSX" && value.extension.toUpperCase() !== ".DOC"
                                && value.extension.toUpperCase() !== ".XLS" && value.extension.toUpperCase() !== ".PDF" && value.extension.toUpperCase() !== ".TXT") {
                                   e.preventDefault();
                                   alert("Archivos permitidos .docx, .xlsx, .doc, .xls, .pdf, .txt .jpg y .png");
                               }else if(value.size>8000000){
                                  e.preventDefault();
                                   alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 8000000 bytes."); 
                               }
                           }else
                              e.preventDefault();                                   
                        }
                    });
                }else{
                    e.preventDefault();
                    alert("Seleccione una carpeta.");
                }
            };
            function existeArchivo(archivo){
                for(var i=0; i < dataSource.view().length; i++)
                {
                    if(dataSource.view()[i].nombreArchivo === archivo)
                        return true;
                }
                return false;
            }
            function onSuccess(e) {
                if (typeof (e.response) !== "undefined"){
                    verMensaje(e.response);
                    $(".k-upload-files.k-reset").find("li").remove();
                    $("#grid").data("kendoGrid").dataSource.read();             
                }
            }
            function descargar(idDocumento){ 
                $("#cc2").attr("src","../../general/documentos.do?coreDocumentoId="+idDocumento+"&modo=descargarDocumento");
            }
            $(".k-grid-Limpiarfiltros").click(function(e){
                $("#grid").data("kendoGrid").dataSource.filter([]);
            });

        </script>
        <style>
            #treeview {
                margin: 0 auto;
            }

            #treeview .k-sprite {
                background-image: url("../../recursos/img/coloricons-sprite.png");
            }

            .rootfolder { background-position: 0 0; }
            .folder     { background-position: 0 -16px; }
            
            .k-dropzone{
                height: 100px;
            }
        </style>
    </body>
</html>