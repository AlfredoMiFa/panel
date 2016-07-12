<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - validador layout</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>       
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-REPORT"/>
                    <jsp:param name="M_SELECT" value="C-G-LAYOUT"/>
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
                                    <i class="fa fa-building"></i> Layout de carga
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
                <div id="ayuda"><div id="contenidoAyuda"></div></div>
            </div>
        </div>    
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var wnd,uploadTemplate,uploadTemplateExcel,wndAyuda;
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                    read:   {
                       url: "../generals/layout.do?modo=seleccionarLayout",
                       dataType: "json"
                    },
                    update: {
                       url: "../generals/layout.do?modo=editarLayout",
                       type: "POST",
                       dataType: "json",
                       complete: function(e) {
                           if (typeof (e.responseText) !== "undefined")
                               verMensaje($.parseJSON(e.responseText));
                       }
                    },
                    destroy: {
                        url: "../generals/layout.do?modo=eliminarLayout",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                           if (typeof (e.responseText) !== "undefined")
                               verMensaje($.parseJSON(e.responseText));
                        } 
                     },
                     create: {
                         url: "../generals/layout.do?modo=nuevoLayout",
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
                           id: "coreLayoutId",
                           fields: {
                                coreLayoutId: { type: "number",editable: false, nullable: true},
                                tabla: { type: "string", validation: { required: true,maxlength:25 }},
                                nombre: { type: "string", validation: { required: true,maxlength:45 }},
                                columnaInicio: { type: "number",min:0},
                                columnaFin: { type: "number",min:0},
                                renglonInicio: { type: "number",min:0},
                                activo: { type: "boolean"}
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
                    selectable: "row",
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                    toolbar: ["create",{ text: " Probar layout" ,imageClass: "fa fa-external-link-square"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        {field:"coreLayoutId",title:"Id",hidden:true},
                        { field: "tabla", title:"Tabla", width: "120px"   },
                        { field: "nombre",title:"Nombre", width: "200px" },
                        { field: "columnaInicio",title:"Columna inicio", format: "{0:n0}", width: "100px" },
                        { field: "columnaFin",title:"Columna fin", format: "{0:n0}", width: "90px" },
                        { field: "renglonInicio",title:"Renglon inicio", format: "{0:n0}", width: "100px" },
                        { field: "activo", width: "100px"},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "190px" }],
                    editable: "inline",
                    detailInit: detailInit
                });
                function detailInit(ev) {
                    var detailRow = ev.detailRow;
                    $("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../generals/layout.do?modo=seleccionarCamposLayout",
                                dataType: "json"
                             },
                             update: {
                                url: "../generals/layout.do?modo=editarCamposLayout",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../generals/layout.do?modo=eliminarCamposLayout",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../generals/layout.do?modo=nuevoCamposLayout",
                                  type: "POST",
                                  dataType: "json",
                                  complete: function(e) {
                                    detailRow.find("#grid2").data("kendoGrid").dataSource.read(); 
                                     if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                  }
                               }
                             },
                             schema: {
                                data: "data",
                                total: "total",
                                model: {
                                    id: "coreCamposLayoutId",
                                    fields: {
                                        coreLayoutId: { type: "string", validation: {required: true,max:20 }},
                                        coreCamposLayoutId: { type: "number",editable: false, nullable: true},
                                        nombreCampo: { type: "string", validation: { required: true,maxlength:45 }},
                                        nombreVariable: { type: "string", validation: { required: true,maxlength:45 }},
                                        orden: { type: "number", validation: { min: 0, required: true,maxlength:5 } },
                                        validar: { type: "boolean",defaultValue: true},
                                        nullable: { type: "boolean",defaultValue: true},
                                        tipoDato: { type: "string",defaultValue: 'String'}
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
                            },
                            filter: [{ field: "coreLayoutId", operator: "eq", value: ev.data.coreLayoutId }]
                        },
                        filterable: {extra: false},
                        sortable: true,
                        reorderable: true,
                        resizable:true,
                        pageable: {
                            refresh: true,
                            input: true,
                            numeric: false
                        },
                        toolbar: [{text: "Campos layout"},"create"],
                        columns: [
                            { field: "coreLayoutId",hidden:true},
                            { field: "nombreCampo", title:"Nombre campo", width: "150px"},
                            { field: "nombreVariable", title:"Nombre variable", width: "150px" },
                            { field: "validar",title:"Validar", width: "80px"},
                            { field: "tipoDato", values: atipodato, width: "100px"  },
                            { field: "orden", width: "80px", format: "{0:n0}",filterable: false },
                            { field: "nullable",title:"Nullable", width: "90px"},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "210px" }],
                        editable: "inline",
                        detailInit: detailInit2,
                        edit:function(e){
                            e.container.find('input[name = "coreLayoutId"]').attr("disabled", true);
                            e.container.find("input[name=coreLayoutId]").val(ev.data.coreLayoutId).change(); 
                         }
                    });
                }  
                function detailInit2(ev) {
                    var detailRow = ev.detailRow;
                    $("<div id='grid3'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../generals/layout.do?modo=seleccionarValidador",
                                dataType: "json"
                             },
                             update: {
                                url: "../generals/layout.do?modo=editarValidador",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../generals/layout.do?modo=eliminarValidador",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../generals/layout.do?modo=nuevoValidador",
                                  type: "POST",
                                  dataType: "json",
                                  complete: function(e) {
                                     if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                    detailRow.find("#grid3").data("kendoGrid").dataSource.read(); 
                                  }
                               }
                             },
                             schema: {
                                data: "data",
                                total: "total",
                                model: {
                                    id: "coreValidadorId",
                                    fields: {
                                        coreCamposLayoutId: { type: "string", validation: {required: true,max:20 }},
                                        coreValidadorId: { type: "number",editable: false, nullable: true},
                                        tipo: { type: "string",defaultValue:"REGEX"},
                                        nombreClase: { type: "string", validation: { maxlength:45 }},
                                        nombreMetodo: { type: "string", validation: { maxlength:45 } },
                                        regex: { type: "string", validation: { maxlength:100 } },
                                        valorInicial: { type: "string", validation: { maxlength:45 } },
                                        valorFinal: { type: "string", validation: { maxlength:45 } },
                                        sqlSentence: { type: "string", validation: { maxlength:200 } },
                                        conexionDao: { field: "conexionDao", defaultValue: { id: "", text: "Seleccione una opción"}},
                                        mensajeError: { type: "string", validation: { required: true,maxlength:45 } },
                                        parametros: { type: "string", validation: { maxlength:100 } },
                                        activo: { type: "boolean",defaultValue:true }
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
                            },
                            filter: [{ field: "coreCamposLayoutId", operator: "eq", value: ev.data.coreCamposLayoutId }]
                            
                        },
                        filterable: {extra: false},
                        sortable: true,
                        reorderable: true,
                        resizable:true,
                        pageable: {
                            refresh: true,
                            input: true,
                            numeric: false
                        },
                        toolbar: [{text: "Validador"},"create",{ text: " Subir clase" ,imageClass: "fa  fa-arrow-circle-up"}],
                        columns: [
                            { field: "coreCamposLayoutId", title:"Campo layout",hidden:true},
                            { field: "tipo", width: "100px", title:"Tipo validación", values: atipovalidacion },
                            { field: "nombreClase", title:"Clase",filterable: false,hidden:true },
                            { field: "nombreMetodo",title:"Método", width: "80px",filterable: false,hidden:true},
                            { field: "valorInicial", title:"Valor Inicial",filterable: false,hidden:true},
                            { field: "valorFinal", title:"Valor Final",filterable: false,hidden:true},
                            { field: "conexionDao", title:"Conexión BD",filterable: false,hidden:true, editor: listaDropDownEditor2  },
                            { field: "mensajeError", title:"Mensaje error", width: "200px"  },
                            { field: "sqlSentence", title:"SQL",filterable: false,hidden:true },
                            { field: "parametros", title:"Parametros",filterable: false, width: "100px",hidden:true },
                            { field: "regex", title:"REGEX",filterable: false, width: "150px"  },
                            { field: "activo", width: "60px" },
                            { command: ["edit", "destroy",{ text: "Subir class", click: subirClase }], title: "&nbsp;", width: "260px" }],
                        editable: "popup",
                        edit:function(e){
                             e.container.find('div[data-container-for= "conexionDao"] .k-dropdown').css({width:'250px'});
                            e.container.find('input[name = "coreCamposLayoutId"]').attr("disabled", true);
                            e.container.find("input[name=coreCamposLayoutId]").val(ev.data.coreCamposLayoutId).change(); 
                         }
                    });
                }
                    
                wnd = $("#upload").kendoWindow({
                    title: "Subir",
                    modal: true,
                    visible: false,
                    resizable: false,
                    width: 420
                }).data("kendoWindow");
                
                wndAyuda = $("#ayuda")
                    .kendoWindow({
                        title: "Reporte de carga de excel",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 800,
                        height:500
                    }).data("kendoWindow");
                uploadTemplate = kendo.template($("#template").html()); 
                uploadTemplateExcel = kendo.template($("#templatee").html());  

                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
                $(".k-grid-Probarlayout").click(function(){
                    subirExcel();
                });
                $(".k-grid-Subirclase").click(function(){
                    subirClase();
                });
            });
                
            
            var onSelect = function(e) {  
                $.each(e.files, function(index, value) {
                    if(value.extension.toUpperCase() !== ".CLASS" && value.extension.toUpperCase() !== ".JAR") {
                        e.preventDefault();
                        alert("Archivo permitido CLASS.");
                    }else if(value.size>100000)
                    {
                       e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 100000 bytes."); 
                    }
                });
            };

            function listaDropDownEditor2(container, options) {
                $('<input name="conexionDao" data-text-field="text" data-value-field="id"  data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../generals/conexiones.do?modo=combo"
                                }
                            }
                        }
                    });
            }function subirClase(e) {
                e.preventDefault();
                var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
                wnd.title("Subir clase");
                wnd.content(uploadTemplate(dataItem));
                wnd.center().open();
                $("#archivo").kendoUpload({
                    async: {
                        saveUrl: "../generals/layout.do",
                        autoUpload: true
                    },
                    success: onSuccess,
                    multiple:false,
                    select: onSelect,
                    upload: function (e) {
                        e.data = { coreValidadorId: $("#coreValidadorId").val() };
                    }
                });
            }
            function subirExcel() {
                var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                if(row!=null){
                    wnd.title("Subir excel");
                    wnd.content(uploadTemplateExcel(row));
                    wnd.center().open();
                    $("#archivoe").kendoUpload({
                        async: {
                            saveUrl: "../generals/layout.do",
                            autoUpload: true
                        },
                        success: onSuccess2,
                        multiple:false,
                        select: onSelect2,
                        upload: function (e) {
                            e.data = { coreLayoutId: $("#coreLayoutId").val() };
                        }
                    });
                }else
                    alert("Seleccionar un registro");
            }
            var onSelect2 = function(e) {   
                $.each(e.files, function(index, value) {
                    if(value.extension.toUpperCase() !== ".XLSX") {
                        e.preventDefault();
                        alert("Archivo permitido excel(xlsx).");
                    }else if(value.size>5000000)
                    {
                       e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 5000000 bytes."); 
                    }
                });
            };
            function onSuccess(e) {
                if (typeof (e.response) !== "undefined"){
                    $(".k-upload-files.k-reset").find("li").remove();
                    verMensaje(e.response);
                }
            }
            function onSuccess2(e) {
                if (typeof (e.response) !== "undefined"){
                    $(".k-upload-files.k-reset").find("li").remove();
                    verMensaje(e.response); 
                    if(e.response.success){
                        $("#contenidoAyuda").html(e.response.data);
                        wndAyuda.center().open();
                    }
                }
            }
        </script>
        <script type="text/x-kendo-template" id="template">
            <div class="property extended">
                <br/>
                <label>Archivo class</label>
                <input type="hidden" name="coreValidadorId" id="coreValidadorId" value="#=coreValidadorId#"/> 
                <input name="modo" id="modo" type="hidden" value="cargaClass" /> 
                <input name="archivo" id="archivo" type="file"  />
                <br/>
            </div>
        </script>
        <script type="text/x-kendo-template" id="templatee">
            <div class="property extended">
                <br/>
                <label>Archivo excel</label>
                <input type="hidden" name="coreLayoutId" id="coreLayoutId" value="#=coreLayoutId#"/> 
                <input name="modo" id="modo" type="hidden" value="cargaExcel" /> 
                <input name="archivoe" id="archivoe" type="file"  />
                <br/>
            </div>
        </script>
        <style>
            .k-popup-edit-form .k-edit-form-container{ width: 500px;}
            input[name=regex],input[name=sqlSentence],input[name=parametros],input[name=mensajeError] {
                width: 250px;
            }
        </style>
    </body>
</html>

