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
                    <jsp:param name="M_ACTIVE" value="C-G-AGEBEA:C-G-CATBEA"/>
                    <jsp:param name="M_SELECT" value="C-G-DEPARTAMENTOS"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Catálogos</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-unlock-alt"></i> Departamentos
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
            <div style="visibility: hidden;"> 
                <div id="perfilesPopup">                                    
                    <div>
                        <br/>
                        <label>Perfiles:</label><br/>
                        <input type="hidden" name="idDepartamento" id="idDepartamento" /> 
                        <select id="perfiles"></select>
                        <div style="text-align: right;margin-top: 5px;">
                            <!--<a class="k-button k-button-icontext k-grid-update" href="javascript:actualizar();"><span class="k-icon k-update"></span>Actualizar</a>
                            <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelar();"><span class="k-icon k-cancel"></span>Cancelar</a>-->
                        </div>
                        <br/>
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
                        url: "../../beaconsAgencias/Departamentos.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Departamentos.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Departamentos.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Departamentos.do?modo=nuevo",
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
                            id: "idDepartamento",
                            fields: {
                                idDepartamento:      { type: "number", editable: false, nullable: false },
                                idOficina:           { field: "idOficina", editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                descripcion:         { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:100 }}
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
                            { field: "idDepartamento",hidden:true},
                            { field: "idOficina",    title:"Oficina",width:"200px",editor:listaDropDownOficinas, template:"#=idOficina.text#"}, 
                            { field: "descripcion",  title:"Nombre departamento",width:"200px",encoded:false},
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
                                        url: "../../beaconsAgencias/personal.do",
                                        dataType: "json"
                                    },
                                    update: {
                                        url: "../../beaconsAgencias/personal.do?modo=editar",
                                        type: "POST",
                                        dataType: "json",
                                        complete: function(e) {
                                            if (typeof (e.responseText) !== "undefined")
                                                verMensaje($.parseJSON(e.responseText));
                                        }
                                    },
                                    destroy: {
                                         url: "../../beaconsAgencias/personal.do?modo=eliminar",
                                         type: "POST",
                                         dataType: "json",
                                         complete: function(e) {
                                            if (typeof (e.responseText) !== "undefined")
                                                verMensaje($.parseJSON(e.responseText));
                                         } 
                                    },
                                    create: {
                                          url: "../../beaconsAgencias/personal.do?modo=nuevo",
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
                                        id: "idDepartamento",
                                        fields: {
                                            idPersonal:         { type: "number", editable: false, nullable: false },
                                            idDepartamento:     { field: "idDepartamento", editable: true, defaultValue: { id: "", text:"Seleccione una opción"}},
                                            idOficina:          { field: "idOficina", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                            idEstado:           { type: "string",editable: true},
                                            idCiudad:           { type: "string",editable: true},
                                            nombre:             { type: "string", validation: { required: true,maxlength:20 }},
                                            apellidoPaterno:    { type: "string", validation: { required: true,maxlength:20 }},
                                            apellidoMaterno:    { type: "string", validation: { required: true,maxlength:20 }},
                                            email:              { type: "string", validation: { required: true,maxlength:30,email: true }},
                                            calle:              { type: "string", validation: { required: true,maxlength:45 }},
                                            colonia:            { type: "string", validation: { required: true,maxlength:45 }},
                                            cpp:                { type: "string", validation: { required: true,maxlength:05, pattern : "\\d{5}" }},
                                            numeroTelefono:     { type: "String", validation: { required: true,maxlength:10, pattern : "\\d{10}" }}

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
                                filter: { field: "idDepartamento", operator: "eq", value: ev.data.idDepartamento }                    
                            },
                            toolbar: [
                                    { text: "Personal laborando" ,imageClass: "k-icon k-i-note"}
                                    
                                 ],
                            columns: [
                                { field: "idOficina",title:"Oficina", width:"200px", hidden:true, editor:listaDropDownOficinas,template:"#=idOficina.text#"},
                                { field: "idPersonal",hidden:true},
                                { field: "idDepartamento",title:"Departamento", width:"200px", hidden:true, editor:listaDropDownDepartamentos,template:"#=idDepartamento.text#"},
                                { field: "idEstado",       title:"Estado",width:"200px"},
                                { field: "idCiudad", title:"Ciudad",width:"200px"},
                                { field: "nombre", title:"Nombre",encoded: false,width:"150px"},
                                { field: "apellidoPaterno", title:"Apellido paterno",encoded: false,width:"150px"},
                                { field: "apellidoMaterno", title:"Apellido materno",encoded: false,width:"150px"},
                                { field: "email", title:"E-mail",encoded: false,width:"150px"},
                                { field: "calle", title:"Calle",encoded: false,width:"200px"},
                                { field: "colonia", title:"Colonia",encoded: false,width:"200px"},
                                { field: "cpp", title:"Cpp",encoded: false,width:"100px"},
                                { field: "numeroTelefono", title:"Número de teléfono",encoded: false,width:"150px"},
                                ],
                            editable: "inline",
                            edit:function(e){
                                e.container.find('input[name = "idDepartamento"]').attr("disabled", true);
                                e.container.find('input[name="idDepartamento"]').val(ev.data.idDepartamento).change();
                             },
                        });
                    }
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownOficinas(container, options){
                 $('<input name="idOficina" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/oficinas.do?modo=combo",
                                }
                            }
                        }
                    });
            }
            function listaDropDownDepartamentos(container, options){
                 $('<input name="idDepartamento" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/Departamentos.do?modo=combo",
                                }
                            }
                        }
                    });
            }
        </script>
    </body>
</html>

