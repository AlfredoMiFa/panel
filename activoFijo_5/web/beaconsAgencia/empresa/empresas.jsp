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
                    <jsp:param name="M_SELECT" value="C-G-EMPRES"/>
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
                                    <i class="fa fa-unlock-alt"></i> Empresas
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
        <div style="visibility: hidden;"> 
                <div id="perfilesPopup">                                    
                    <div>
                        <br/>
                        <label>Sucursales:</label><br/>
                        <input type="hidden" name="idSucursal" id="udSucursal" /> 
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
            var estatus= [{"value": "A","text": "ACTIVO"},{"value": "B","text": "BAJA"},{"value": "M","text": "MANTENIMIENTO"}];
            var ciudad;
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/empresa.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/empresa.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/empresa.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/empresa.do?modo=nuevo",
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
                            id: "idEmpresa",
                            fields: {
                                idEmpresa:   { type: "number", editable: false, nullable: false },
                                nomEmpresa:  { type: "string", editable: true, nullable: true},
                                idCiudad:    { field: "idCiudad",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                rfc:         { type: "string", editable: true, nullable: true, validation: { required: true,maxlength:13 }},
                                calle:       { type: "calle",  editable: true, nullable: true, validation: { required: true,maxlength:100 }},
                                colonia:     { type: "string", validation: { required: true,maxlength:100 } },
                                telefono:    { type: "string", validation: { required: true,maxlength:10 }},
                                cpp:         { type: "string", validation: { required: true,maxlength:5 }},
                                email:       { type: "string", editable: true, nullable: true,  validation: { required: true,maxlength:100 }},
                                estatus:     { type: "string",editable: true, defaultValue:'A',values:estatus }   
                                                             
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
                            { field: "idEmpresa" ,hidden:true},
                            { field: "nomEmpresa",title:"Nombre empresa",encoded: false,width:"200px"}, 
                            { field: "idCiudad",  title:"Ciudad",width:"200px", editor: listaDropDownEditor, template:"#=idCiudad.text#"}, 
                            { field: "rfc",       title:"RFC",encoded: false,width:"150px"}, 
                            { field: "calle",     title:"Calle",encoded: false,width:"200px"},                              
                            { field: "colonia",   title:"Colonia",encoded: false,width:"250px"}, 
                            { field: "telefono",  title:"Telefono",width:"100px"},  
                            { field: "cpp",       title:"C.P.P",width:"100px"}, 
                            { field: "email",     title:"Email",width:"250px"},                              
                            { field: "estatus",   title:"Estatus",width:"150px", defaultValue:'A', values: estatus},                            
                                                         
                            
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
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
                                url: "../../beaconsAgencias/sucursal.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../../beaconsAgencias/sucursal.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../../beaconsAgencias/sucursal.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../../beaconsAgencias/sucursal.do?modo=nuevo",
                                  type: "POST",
                                  dataType: "json",
                                  complete: function(e) {
                                    $("#grid2").data("kendoGrid").dataSource.read(); 
                                     if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                  }
                               }
                             },
                             schema: {
                                data: "data",
                                total: "total",
                                model: {
                                    id: "idEmpresa",
                                    fields: {
                                        idEmpresa:  { type: "string"},
                                        idSucursal: { type: "number",editable: false, nullable: true},                                                                                
                                        nombre:     { type: "string", validation: { required: true,maxlength:50 }},
                                        idMarca:    { field: "idMarca",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                        idCiudad:   { field: "idCiudad",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                        direccion:  { type: "string", validation: { required: true,maxlength:200 }},
                                        colonia:    { type: "string", validation: { required: true,maxlength:100 }},
                                        cpp:        { type: "string", validation: { required: true,maxlength:5 }},
                                        telefono:   { type: "string", validation: { required: true,maxlength:10 }},
                                        estatus:    { type: "string", validation: { required: true,maxlength:5 }, defaultValue:'A'},
                                        latitud:    { type: "string", validation: { required: true,maxlength:45 }},
                                        longitud:   { type: "string", validation: { required: true,maxlength:45 }}
                                                                              
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
                            filter: { field: "idEmpresa", operator: "eq", value: ev.data.idEmpresa }                    
                        },
                        toolbar: [{ text: "Sucursales" ,imageClass: "k-icon k-i-note"},"create"],
                        columns: [
                            { field: "idEmpresa", title:"Cve. Empresa",filterable:false,sortable:false, width: "90px" },
                            { field: "idSucursal",title:"Cve. Sucursal", filterable:false,sortable:false, width: "90px"},                                                        
                            { field: "nombre",    title:"Nombre", width: "200px"},
                            { field: "idMarca",   title:"Marca", width: "100px", editor: listaDropDownEditorMarca, template:"#=idMarca.text#"},
                            { field: "idCiudad",  title:"Ciudad", width: "100px", editor: listaDropDownEditor, template:"#=idCiudad.text#"},
                            { field: "direccion", title:"Direccion", width: "200px"},
                            { field: "colonia",   title:"Colonia", width: "200px"},
                            { field: "cpp",       title:"C.P.P", width: "100px"},
                            { field: "telefono",  title:"Telefono", width: "100px"},
                            { field: "estatus",   title:"Estatus", width: "100px", defaultValue:'A', values: estatus},   
                            { field: "latitud",   title:"Latitud",   width: "100px"},
                            { field: "longitud",  title:"Longitud", width: "100px"},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" ,filterable:false,sortable:false},
                            ],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "idEmpresa"]').attr("disabled", true);
                            e.container.find('input[name="idEmpresa"]').val(ev.data.idEmpresa).change();
                         }
                    });
                }   
                
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
                $("#idCiudad").kendoDropDownList({
                    dataTextField: "text",
                    dataValueField: "id",
                        dataSource: {
                                serverFiltering: true,
                                transport: {
                                    read: {
                                        dataType: "json",
                                        url: "../../beaconsAgencias/ciudad.do?modo=combo",
                                    }
                                }
                            },
                        index: 0
                });                 
            });
                                  
            function listaDropDownEditor(container, options) {
                $('<input name="idCiudad" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/ciudad.do?modo=combo",
                                }
                            }
                        }
                    });
            }            
            
            function listaDropDownEditorMarca(container, options) {
                $('<input name="idMarca" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/marcas.do?modo=combo",
                                }
                            }
                        }
                    });
            }              
            
             
        </script>
    </body>
</html>

