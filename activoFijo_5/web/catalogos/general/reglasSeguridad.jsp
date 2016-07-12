<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - reglas de seguridad</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>       
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G_REGSEG"/>
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
                                    <i class="fa fa-gavel"></i> Reglas de seguridad
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
            var atiporegla = [{"value": "PERMITIR","text": "PERMITIR"},{"value": "RESTRINGIR","text": "RESTRINGIR"}];
            $(document).ready(function(){
                inicializar();
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/reglaSeguridad.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/reglaSeguridad.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/reglaSeguridad.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/reglaSeguridad.do?modo=nuevo",
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
                            id: "coreReglaSeguridadId",
                            fields: {
                                coreReglaSeguridadId: { type: "number",editable: false, nullable: true},
                                nombre: { type: "string", validation: { required: true,maxlength:45}}
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
                        { field:"nombre", title: "Campo"},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable: "inline",
                    detailInit:detailInit
                });
                function detailInit(ev) {
                    var detailRow = ev.detailRow;
                    $("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../generals/reglaSeguridadDetalle.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../generals/reglaSeguridadDetalle.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../generals/reglaSeguridadDetalle.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../generals/reglaSeguridadDetalle.do?modo=nuevo",
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
                                    id: "coreReglaSeguridadDetalleId",
                                    fields: {
                                        coreReglaSeguridadId: { type: "string"},
                                        coreReglaSeguridadDetalleId: { type: "number",editable: false, nullable: true},
                                        nombre: { type: "string", validation: { required: true,maxlength:45}},
                                        catalogo: { type: "string",defaultValue: '', validation: { required: true,maxlength:45 } },
                                        valorIni: { type: "string",validation: {required: true,maxlength:45}},
                                        valorFin: { type: "string",validation: {required: true,maxlength:45}},
                                        tipo: { type: "string",required: true,defaultValue: 'PERMITIR'}
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
                            filter: { field: "coreReglaSeguridadId", operator: "eq", value: ev.data.coreReglaSeguridadId }                    
                        },
                        toolbar: ["create"],
                        columns: [
                            { field: "coreReglaSeguridadId", width: "50px",hidden:true},
                            { field: "coreReglaSeguridadDetalleId",title:"Id", width: "90px"},
                            { field:"nombre", title: "Campo", width: "140px"},
                            { field: "catalogo", title:"Catálogo", width: "220px", editor: listaDropDownEditor},
                            { field: "valorIni", title:"Valor ini", width: "100px",filterable: false,groupable: false  },
                            { field: "valorFin", title:"Valor fin", width: "100px",groupable: false,filterable: false  },
                            { field: "tipo",title:"Tipo", width: "130px", values: atiporegla},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "240px" ,filterable:false,sortable:false}],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "coreReglaSeguridadId"]').attr("disabled", true);
                            e.container.find('input[name="coreReglaSeguridadId"]').val(ev.data.coreReglaSeguridadId).change();

                         }
                    });
                }  
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownEditor(container, options) {
                $('<input name="catalogo" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../generals/catalogoGeneral.do?modo=combo1&filtro=CATALOGO_RS",
                                }
                            }
                        }
                    });
            }
        </script>
        <style>
            #grid2{
                height: 250px;
            }
        </style>
    </body>
</html>

