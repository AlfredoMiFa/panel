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
                    <jsp:param name="M_SELECT" value="C-G-OFICINAS"/>
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
                                    <i class="fa fa-unlock-alt"></i> Oficinas
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
                        <input type="hidden" name="idOficina" id="idOficina" /> 
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
            var wnd;
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/oficinas.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/oficinas.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/oficinas.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/oficinas.do?modo=nuevo",
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
                            id: "idOficina",
                            fields: {
                                idOficina:      { type: "number", editable: false, nullable: false },
                                idEmpresa:      { field: "idEmpresa", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                cpp:            { type: "number", editable: true, nullable: true,validation:{ required: true,maxlength:05, pattern : "\\d{5}" }},
                                idEstado:       { type: "string",editable: true},
                                idCiudad:       { type: "string",editable: true},
                                nombreOficina:  { type: "string", editable: true, nullable: true,validation:{ required: true,maxlength:45 }},
                                calle:          { type: "string", editable: true, nullable: true,validation:{ required: true,maxlength:45 }},
                                numeroOficina:  { type: "number", editable: true, nullable: true,validation:{ required: true,maxlength:20 }},
                                colonia:        { type: "string", editable: true, nullable: true,validation:{ required: true,maxlength:45 }},
                                numeroTelefono: { type: "number", editable: true, nullable: true,validation:{ required: true,maxlength:10, pattern : "\\d{10}" }},
                                rfc:            { type: "string", editable: true, nullable: true,validation:{ required: true,maxlength:13 }}
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
                            { field: "idOficina",hidden:true},
                            { field: "idEmpresa", title:"Empresa",width:"200px",editor:listaDropDownEmpresas,template:"#=idEmpresa.text#"},
                            { field: "nombreOficina", title:"Nombre oficina",encoded: false,width:"150px"},
                            { field: "cpp", title:"Cpp",encoded: false,width:"100px"},
                            { field: "idEstado",       title:"Estado",width:"200px"},
                            { field: "idCiudad", title:"Ciudad",width:"200px"},
                            { field: "calle", title:"Calle", encoded: false,width:"200px"},
                            { field: "numeroOficina", title:"Número",encoded: false,width:"100px"},
                            { field: "colonia", title:"Colonia", encoded: false,width:"200px"},
                            { field: "numeroTelefono", title:"Número de teléfono",encoded: false,width:"180px"},
                            { field: "rfc", title:"RFC",encoded: false,width:"150px"},
                        { command: ["edit"], title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                        
                        detailExpand: function(e) {
                            this.collapseRow(this.tbody.find(' > tr.k-master-row').not(e.masterRow));
			},
                        detailInit: detailInit,
                        
                        edit:function(e){
                            
                            $('input[name = "idEstado"]').attr("disabled", true);
                            $('input[name = "idCiudad"]').attr("disabled", true);
                            
                            $('input[name = "cpp"]').blur(function (){
                                if($.trim($('input[name = "cpp"]').val())!=="")
                                    $.post("../../beaconsAgencias/oficinas.do",{modo:"validaCpp",cpp:$.trim($('input[name = "cpp"]').val())},function(result){
                                        if(!result.success)
                                        {
                                            $('input[name = "cpp"]').val("");
                                            $('input[name = "cpp"]').attr("placeholder",result.msg);
                                            alert(result.msg);
                                        } else {
                                            e.container.find('input[name="idEstado"]').val(result.estado).change();
                                            e.container.find('input[name="idCiudad"]').val(result.ciudad).change();                                            
                                        }
                                    },"json");
                            });
                        }
			
                    });
                    function detailInit(ev) {
			$("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                            dataSource: {
                                transport: {
                                    read:   {
                                        url: "../../beaconsAgencias/departamentos.do",
                                        dataType: "json"
                                    },
                                    update: {
                                        url: "../../beaconsAgencias/departamentos.do?modo=editar",
                                        type: "POST",
                                        dataType: "json",
                                        complete: function(e) {
                                            if (typeof (e.responseText) !== "undefined")
                                                verMensaje($.parseJSON(e.responseText));
                                        }
                                    },
                                    destroy: {
                                         url: "../../beaconsAgencias/departamentos.do?modo=eliminar",
                                         type: "POST",
                                         dataType: "json",
                                         complete: function(e) {
                                            if (typeof (e.responseText) !== "undefined")
                                                verMensaje($.parseJSON(e.responseText));
                                         } 
                                    },
                                    create: {
                                          url: "../../beaconsAgencias/departamentos.do?modo=nuevo",
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
                                        id: "idOficina",
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
                                },
                                filter: { field: "idOficina", operator: "eq", value: ev.data.idOficina }                    
                            },
                            toolbar: [
                                    { text: "Departamentos de la oficina" ,imageClass: "k-icon k-i-note"}
                                    
                                 ],
                            columns: [
                                { field: "idDepartamento",hidden:true},
                                { field: "idOficina",    title:"Oficina",width:"200px",hidden:true,editor:listaDropDownOficinas, template:"#=idOficina.text#"}, 
                                { field: "descripcion",  title:"Nombre departamentos",width:"200px",encoded:false},
                                ],
                            editable: "inline",
                            edit:function(e){
                                e.container.find('input[name = "idOficina"]').attr("disabled", true);
                                e.container.find('input[name="idOficina"]').val(ev.data.idOficina).change();
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
            function listaDropDownMunicipio(container, options){
                 $('<input name="idCiudad" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/Ciudad.do?modo=combo",
                                }
                            }
                        }
                    });
            }
            
            function listaDropDownEmpresas(container, options){
                 $('<input name="idEmpresa" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/empresa.do?modo=combo",
                                }
                            }
                        }
                    });
            }
        </script>  
    </body>
</html>

