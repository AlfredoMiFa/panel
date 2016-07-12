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
                    <jsp:param name="M_SELECT" value="C-G-INCI"/>
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
                                    <i class="fa fa-unlock-alt"></i> Incidencia
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
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/Incidencias.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Incidencias.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Incidencias.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Incidencias.do?modo=nuevo",
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
                            id: "idIncidencia",
                            fields: {
                                idIncidencia:   { type:     "number", editable: false, nullable: false },
                                idUsuario:      { type:     "string", editable: true},
                                idOficina:      { field:    "idOficina", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idActivo:       { field:    "idActivo", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                tipoIncidencia: { type:     "string", validation: { required: true,maxlength:100 }},
                                descripcion:    { type:     "string", validation: { required: true,maxlength:45 }},
                                fecha:          { type:     "date"},
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
                            { field: "idIncidencia",hidden:true},
                            { field: "idUsuario", title:"Usuario", width:"150px"},
                            { field: "idOficina", title:"Oficina",width:"200px",editor:listaDropDownOficinas,template:"#=idOficina.text#"},
                            { field: "idActivo", title:"Activo",width:"200px",editor:listaDropDownActivo,template:"#=idActivo.text#"},
                            { field: "tipoIncidencia", title:"Incidencia encontrada",encoded: false,width:"200px"},
                            { field: "descripcion", title: "Descripcion",encoded: false,width:"150px"},
                            { field: "fecha", title:"Fecha",format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"},width:"150px"},
                        { command: ["edit"],     title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                        edit:function(e){
                        $('input[name = "idUsuario"]').attr("disabled", true);
                        var listaDropDownActivo;
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
                                listaDropDownActivo.dataSource.options.transport.read.url="../../beaconsAgencias/Activos.do?modo=combo&idOficina="+listaDropDownOficinas.value();
                                listaDropDownActivo.dataSource.read();
                            },dataBound:function(){
                                listaDropDownActivo=$('input[name="idActivo"]').kendoDropDownList({
                                    dataTextField: "text",
                                    dataValueField: "id",
                                    dataSource: {
                                        transport: {
                                            read: {
                                                dataType: "json",
                                                url: "../../beaconsAgencias/Activos.do?modo=combo&idOficina="+listaDropDownOficinas.value()
                                            }
                                        }
                                    }
                                }).data("kendoDropDownList");
                            }
                        }).data("kendoDropDownList");
                     }
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownOficinas(container, options){
                 $('<input required name="idOficina" id="idOficina"/>').appendTo(container);
            }
            function listaDropDownActivo(container, options){
                 $('<input required name="idActivo" id="idActivo"/>').appendTo(container);
            }
            function listaDropDownUsuario(container, options){
                 $('<input name="idUsuario" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/Usuarios.do?modo=combo",
                                }
                            }
                        }
                    });
            }
        </script>
    </body>
</html>

