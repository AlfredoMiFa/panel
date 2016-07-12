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
                    <jsp:param name="M_ACTIVE" value="C-G-AGEBEA:C-G-EMPBEA"/>
                    <jsp:param name="M_SELECT" value="C-G-ACTI"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Oficina</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-unlock-alt"></i> Activo
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
        <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/recursos/js/kendo.culture.es-MX.min.js"></script>
        <script>  
            var estatus= [{"value": "Ac","text": "ALTA"},{"value": "I1","text": "MANTENIMIENTO"},{"value": "I2","text": "ALMACEN"},{"value": "I3","text": "PROCESO"}];
            var wnd,uploadTemplate,wndVisor;
            $(document).ready(function(){
                inicializar();
                kendo.culture("es-MX");
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/Activos.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Activos.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Activos.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Activos.do?modo=nuevo",
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
                            id: "idActivo",
                            fields: {                                
                                idActivo:           { type: "number", editable: false, nullable: false },
                                idOficina:          { field: "idOficina", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idDepartamento:     { field: "idDepartamento", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idPersonal:         { field: "idPersonal", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idCategoria:        { field: "idCategoria", editable: true, defaultvalue: { id: "", text: "Seleccione una opcion"}},
                                numeroInventario:   { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:45 }},
                                denominacion:       { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:60 }},
                                numeroSerie:        { type: "string", editable: true, nullable: false },
                                descripcion:        { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:100 }},
                                marca:              { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:30 }},
                                modelo:             { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:30 }},
                                precio:             { type: "number",  editable: true, nullable: false},
                                notaCredito:        { type: "string", editable: true, nullable: false,validation:{ required: true, maxlength:20 }},
                                estado:             { type: "char",   editable: true, defaultValue:'Ac', values:estatus},                             
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
                    pdf: {
                        allPages: true,
                        fileName: "Activos.pdf",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                    excel: {
                        allPages: true,
                        fileName: "Activos.xlsx",
                        proxyURL: "http://demos.telerik.com/kendo-ui/service/export"
                    },
                        toolbar: ["pdf","excel","create",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "idActivo",hidden:true, title:"Id Activo"},
                            { field: "idOficina",        title:"Oficina",width:"200px",editor:listaDropDownOficinas,template:"#=idOficina.text#"},
                            { field: "idDepartamento",   title:"Departamento",width:"200px",editor:listaDropDownDepartamento,template:"#=idDepartamento.text#"},
                            { field: "idPersonal",       title:"Personal encargado",width:"200px",editor:listaDropDownPersonal,template:"#=idPersonal.text#"},
                            { field: "idCategoria",      title:"Categoria",width:"200px",editor:listaDropDownCategorias,template:"#=idCategoria.text#"},
                            { field: "numeroInventario", title:"Número de inventario", encoded: false, width:"200px"},
                            { field: "denominacion",     title:"Denominación", encoded: false, width:"200px"},
                            { field: "numeroSerie",      title:"Numero de serie",encoded: false,width:"150px"},
                            { field: "descripcion",      title:"Descripción", encoded: false, width:"200px"},
                            { field: "marca",            title:"Marca",encoded: false,width:"100px"},
                            { field: "modelo",           title:"Modelo",encoded: false,width:"100px"},
                            { field: "precio",           title:"Precio",encoded: false,width:"100px"},
                            { field: "notaCredito",      title:"Nota de crédito", encoded: false, width:"200px"},
                            { field: "estado",           title:"Estatus",width:"150px",defaultValue:'Ac', values: estatus},
                        { command: ["edit"],     title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                   detailExpand: function(e) {
                            this.collapseRow(this.tbody.find(' > tr.k-master-row').not(e.masterRow));
                        },
                    detailInit: detailInit,
                    edit:function(e){
                        var listaDropDownDepartamento;
                        var listaDropDownPersonal;
                            var listaDropDownOficinas =$('input[name="idOficina"]').kendoDropDownList({
                                dataTextField: "text",
                                dataValueField: "id",
                                dataSource: {
                                    transport: {
                                        read: {
                                            dataType: "json",
                                            url: "../../beaconsAgencias/oficinas.do?modo=combo"
                                        }
                                    }
                                },change:function(){
                                    listaDropDownDepartamento.dataSource.options.transport.read.url="../../beaconsAgencias/Departamentos.do?modo=combo&idOficina="+listaDropDownOficinas.value();
                                    listaDropDownDepartamento.dataSource.read();
                                },dataBound:function(){
                                    listaDropDownDepartamento=$('input[name="idDepartamento"]').kendoDropDownList({
                                        dataTextField: "text",
                                        dataValueField: "id",
                                        dataSource: {
                                            transport: {
                                                read: {
                                                    dataType: "json",
                                                    url: "../../beaconsAgencias/Departamentos.do?modo=combo&idOficina="+listaDropDownOficinas.value()
                                                }
                                            }
                                        },change:function(){
                                            listaDropDownPersonal.dataSource.options.transport.read.url="../../beaconsAgencias/Personal.do?modo=combo&idDepartamento="+listaDropDownDepartamento.value();
                                            listaDropDownPersonal.dataSource.read();
                                        },dataBound:function(){
                                            listaDropDownPersonal=$('input[name="idPersonal"]').kendoDropDownList({
                                                dataTextField: "text",
                                                dataValueField: "id",
                                                dataSource: {
                                                    transport: {
                                                        read: {
                                                            dataType: "json",
                                                            url: "../../beaconsAgencias/Personal.do?modo=combo&idDepartamento="+listaDropDownDepartamento.value()
                                                        }
                                                    }
                                                }
                                            }).data("kendoDropDownList");
                                        }
                                    }).data("kendoDropDownList");
                                }
                            }).data("kendoDropDownList");
                     }
                    }); 

                function detailInit(ev) {
                    $("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../../beaconsAgencias/Imagen.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../../beaconsAgencias/Imagen.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../../beaconsAgencias/Imagen.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../../beaconsAgencias/Imagen.do?modo=nuevo",
                                  type: "POST",
                                  dataType: "json",
                                  complete: function(e) {
                                    $("#grid2").data("kendoGrid").dataSource.read(); 
                                     if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                  }
                               },
                               
                                parameterMap: function(options, type) {
                                        if (type === "create" ||type === "update"){
                                           options.ano=kendo.toString(options.ano, "dd/MM/yyyy");}
                                     return options;
                                 }                               
                               
                             },
                             schema: {
                                data: "data",
                                total: "total",
                                model: {
                                    id: "idActivo",
                                    fields: {
                                        idImagenes: { type: "number",editable: false},
                                        idActivo:   { type: "string"},
                                        descripcion:{ type: "string", validation:{ required: true,maxlength:45}},
                                
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
                            filter: { field: "idActivo", operator: "eq", value: ev.data.idActivo }                    
                        },
                        selectable: "row",
                        toolbar: [
                                    { text: "Imagenes" ,imageClass: "k-icon k-i-note"},"create", 
                                    { text: " Adjuntar imagen" ,imageClass: "fa  fa-arrow-circle-up"},
                                    { text: " Visualizar imagen" ,imageClass: "fa  fa-arrow-circle-up"}
                                    
                                 ],
                        columns: [
                             { field: "idActivo", filterable: false,sortable: false, width:"50px", hidden: true, title:"Id Activo"},
                            { field: "idImagenes", title:"Clave imagen",width:"100px"},
                            { field: "descripcion", title:"Descripcion",width:"200px"},
                            { command: ["edit"], title: "&nbsp;", width: "250px" ,filterable:false,sortable:false},
                            ],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "idActivo"]').attr("disabled", true);
                            e.container.find('input[name="idActivo"]').val(ev.data.idActivo).change();
                         },
                         dataBound:function(e){
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
                                var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());
                                if(row!=null){
                                    wnd.content(uploadTemplate(row));
                                    wnd.center().open();
                                    $("#archivo").kendoUpload({
                                        async: {
                                            saveUrl: "../../beaconsAgencias/Imagen.do",
                                            autoUpload: true
                                        },
                                        success: onSuccess,
                                        multiple:false,
                                        select: onSelect,
                                        upload: function (e) {
                                            e.data = { idImagenes: $("#idImagenes").val() };
                                        }
                                    });
                                }else
                                    alert("Seleccione un registro");
                            });                               
                             $(".k-grid-Visualizarimagen").click(function(e){
                                var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());                    
                                if(row!=null){
                                    $("#cc2").attr("src","../../beaconsAgencias/Imagen.do?modo=getImage&id="+row.idImagenes);
                                    wndVisor.center().open();
                                }else
                                    alert("Seleccione un registro");
                            });                            
                
                         }
                    });
                        
                }  
                
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
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownOficinas(container, options){
                 $('<input required name="idOficina" id="idOficina"/>').appendTo(container);
            }
            
            function listaDropDownDepartamento(container, options){
                $('<input required name="idDepartamento" id="idDepartamento"/>').appendTo(container);
            }
    
            function listaDropDownPersonal(container, options){
                $('<input required name="idPersonal" id="idPersonal"/>').appendTo(container);
            }
            
            function listaDropDownCategorias(container, options){
                 $('<input name="idCategoria" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/categorias.do?modo=combo",
                                }
                            }
                        }
                    });
            }
        </script>
        <script type="text/x-kendo-template" id="template">
            <div class="property extended">
                <br/>
                <label>Archivo:</label> <input type="hidden" name="idImagenes" id="idImagenes" value="#=idImagenes#"/>
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

