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
                    <jsp:param name="M_SELECT" value="C-G-VEHICU"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Empresa</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-unlock-alt"></i> Vehículos
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
       <div style="visibility: hidden;"> 
                <div id="perfilesPopup">                                    
                    <div>
                        <br/>
                        <label>Versiones:</label><br/>
                        <input type="hidden" name="idVersion" id="idCiudad" /> 
                        <select id="perfiles"></select>
                        <div style="text-align: right;margin-top: 5px;">
                            <!--<a class="k-button k-button-icontext k-grid-update" href="javascript:actualizar();"><span class="k-icon k-update"></span>Actualizar</a>
                            <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelar();"><span class="k-icon k-cancel"></span>Cancelar</a>-->
                        </div>
                        <br/>
                    </div>
                </div>           
        </div>                   
           
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var verAMostrar= [{"value": "true","text": "HABILITADO"},{"value": "false","text": "DESHABILITADO"}];
            var estatus= [{"value": "A","text": "HABILITADO"},{"value": "B","text": "DESHABILITADO"}];   
            var wnd,uploadTemplate,wndVisor;
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/vehiculos.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/vehiculos.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/vehiculos.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/vehiculos.do?modo=nuevo",
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
                                idVehiculo:             { type: "number", editable: false, nullable: false },                                                                
                                idSucursal:             { field:"idSucursal",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                nombre:                 { type: "string", editable: true, nullable: false,validation:{ required: true,maxlength:200 }},
                                fechaAlta:              { type: "date", editable: false, nullable: true},
                                estatus:                { type: "string", defaultValue:'A', values:estatus },
                                idVehiculoPlantilla:    { type: "string", editable: true, nullable: true },                                 
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
                        toolbar: ["create",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "idVehiculo",hidden:true},
                            //{ field: "idSucursal",          title:"Sucursal",  encoded: false,width:"200px", editor: listaSucursales,template:"#=idSucursal.text#"}, 
                        { field:"idSucursal",      title:"Sucursal",width:"120px", editor:listaSucursales,template:"#=idSucursal.text#"},                            
                            { field: "nombre",              title:"Nombre",    encoded: false,width:"200px"}, 
                            { field: "fechaAlta",           title:"Fecha alta", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }, width: "100px"},                            
                            { field: "estatus",             title:"Estatus",   encoded: false,width:"150px", defaultValue:'A', values:estatus}, 
                            { field: "idVehiculoPlantilla", title:"Cve. Plantilla",width:"150px"},   
                        { command: ["edit", "destroy"],     title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                   detailExpand: function(e) {
                            this.collapseRow(this.tbody.find(' > tr.k-master-row').not(e.masterRow));
                        },
                    detailInit: detailInit
                        
                    }); 

                function detailInit(ev) {
                    $("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../../beaconsAgencias/versiones.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../../beaconsAgencias/versiones.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../../beaconsAgencias/versiones.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../../beaconsAgencias/versiones.do?modo=nuevo",
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
                                    id: "idVehiculo",
                                    fields: {
                                        idVehiculo:  { type: "string"},
                                        idVersion:   { type: "number",editable: false},
                                        descripcion: { type: "string", validation: { required: true,maxlength:100 } },
                                        precio:      { type: "number", nullable: false,validation:{ min: 0,required: true}},
                                        ano:         { type: "date",  },                                        
                                        verAMostrar: { type: "string",defaultValue:'true', values:verAMostrar },
                                
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
                            filter: { field: "idVehiculo", operator: "eq", value: ev.data.idVehiculo }                    
                        },
                        selectable: "row",
                        toolbar: [
                                    { text: "Versiones" ,imageClass: "k-icon k-i-note"},"create", 
                                    { text: " Adjuntar imagen" ,imageClass: "fa  fa-arrow-circle-up"},
                                    { text: " Visualizar imagen" ,imageClass: "fa  fa-arrow-circle-up"},
                                    { text: " Adjuntar ficha" ,imageClass: "fa  fa-arrow-circle-up"},
                                    { text: " Visualizar ficha" ,imageClass: "fa  fa-arrow-circle-up"}
                                    
                                 ],
                        columns: [
                            { field: "idVehiculo",  filterable:false,sortable:false, width: "50px", hidden:true },
                            { field: "idVersion",   title:"Clave version", width: "100px" },
                            { field: "descripcion", title:"Descripción", width: "200px"},
                            { field: "precio",      title:"Precio", width: "150px"},
                            { field: "ano",         title:"Año", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }, width: "150px"}, 
                            { field: "verAMostrar", title:"Version a mostrar", width: "200px",defaultValue:'true', values:verAMostrar},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" ,filterable:false,sortable:false},
                            ],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "idVehiculo"]').attr("disabled", true);
                            e.container.find('input[name="idVehiculo"]').val(ev.data.idVehiculo).change();
                         },
                         dataBound:function(e){
                             $(".k-grid-Adjuntarimagen").click(function(e){   
                                var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());
                                if(row!=null){
                                    wnd.content(uploadTemplate(row));
                                    wnd.center().open();
                                    $("#archivo").kendoUpload({
                                        async: {
                                            saveUrl: "../../beaconsAgencias/versiones.do?tipoAcc=1",
                                            autoUpload: true
                                        },
                                        success: onSuccess,
                                        multiple:false,
                                        select: onSelect,
                                        upload: function (e) {
                                            e.data = { idVersion: $("#idVersion").val() };
                                        }
                                    });
                                }else
                                    alert("Seleccione un registro");
                            });                               
                             $(".k-grid-Visualizarimagen").click(function(e){
                                var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());                    
                                if(row!=null){
                                    $("#cc2").attr("src","../../beaconsAgencias/versiones.do?modo=getImage&id="+row.idVersion);
                                    wndVisor.center().open();
                                }else
                                    alert("Seleccione un registro");
                            });                                                           
                             $(".k-grid-Adjuntarficha").click(function(e){   
                                var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());
                                if(row!=null){
                                    wnd.content(uploadTemplate(row));
                                    wnd.center().open();
                                    $("#archivo").kendoUpload({
                                        async: {
                                            saveUrl: "../../beaconsAgencias/versiones.do?tipoAcc=2",
                                            autoUpload: true
                                        },
                                        success: onSuccess,
                                        multiple:false,
                                        select: onSelect2,
                                        upload: function (e) {
                                            e.data = { idVersion: $("#idVersion").val() };
                                        }
                                    });
                                }else
                                    alert("Seleccione un registro");
                            }); 
                             $(".k-grid-Visualizarficha").click(function(e){
                                 var row=$("#grid2").data("kendoGrid").dataItem($("#grid2").data("kendoGrid").select());                    
                                 if(row!=null){
                                     $("#cc2").attr("src","../../beaconsAgencias/versiones.do?modo=getPDF&id="+row.idVersion);
                                     wndVisor.center().open();
                                 }else
                                     alert("Seleccione un registro");
                             });                             
                
                         }
                    });
                    
                    wnd = $("#upload")
                       .kendoWindow({
                        title: "Adjuntar archivo",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 420
                    }).data("kendoWindow");   
                    wndVisor = $("#visor")
                        .kendoWindow({
                            title: "Visor de archivos",
                            modal: true,
                            visible: false,
                            resizable: true,
                            width: 600,
                            height:400
                        }).data("kendoWindow");  
                        uploadTemplate = kendo.template($("#template").html()); 
                        
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
                var onSelect2 = function(e) {                
                    var html="";
                    $.each(e.files, function(index, value) {
                        if(value.extension.toUpperCase() !== ".PDF") {
                            e.preventDefault();
                        alert("Archivo permitido PDF.");
                    }else if(value.size>150000)
                        {
                           e.preventDefault();
                        alert("Tamaño del archivo: "+value.size+" bytes, tamaño máximo permitido: 150000 bytes."); 
                        }
                    });
                };                            
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaSucursales(container, options) {
                $('<input name="idSucursal" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/sucursal.do?modo=combo",
                                }
                            }
                        }
                    });
            }             
        </script>
        <script type="text/x-kendo-template" id="template">
            <div class="property extended">
                <br/>
                <label>Archivo:</label> <input type="hidden" name="idVersion" id="idVersion" value="#=idVersion#"/>
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

