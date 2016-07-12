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
            <!-- navegador -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="C-G-AGEBEA:C-G-LOCBEA"/>
                    <jsp:param name="M_SELECT" value="C-G-ESTADO"/>
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
                                    <a href="#"><i class="fa fa-gears"></i> Localizacion</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-unlock-alt"></i> Estados
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
                <div id="perfilesPopup">                                    
                    <div>
                        <br/>
                        <label>Ciudades:</label><br/>
                        <input type="hidden" name="idCiudad" id="idCiudad" /> 
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
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/estado.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/estado.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/estado.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/estado.do?modo=nuevo",
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
                            id: "idEstado",
                            fields: {
                                idEstado: { type: "number",editable: false, nullable: true},
                                descripcion: { type: "string", validation: { required: true,maxlength:100 }}
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
                        { field:"idEstado", title: "Clave del estado", width: "200px"},
                        { field: "descripcion", title:"Descripción" },
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
                                url: "../../beaconsAgencias/ciudad.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../../beaconsAgencias/ciudad.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../../beaconsAgencias/ciudad.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../../beaconsAgencias/ciudad.do?modo=nuevo",
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
                                    id: "idEstado",
                                    fields: {
                                        idEstado:    { type: "string"},
                                        idCiudad:    { type: "number",editable: false, nullable: true},
                                        descripcion: { type: "string", validation: { required: true,maxlength:100 } }                                        
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
                            filter: { field: "idEstado", operator: "eq", value: ev.data.idEstado }                    
                        },
                        toolbar: [{ text: "Ciudades" ,imageClass: "k-icon k-i-note"},"create"],
                        columns: [
                            { field: "idEstado",filterable:false,sortable:false, width: "50px", hidden:true},
                            { field: "idCiudad",title:"Clave ciudad", width: "100px", hidden:true},
                            { field: "descripcion", title:"Descripción", width: "200px"},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" ,filterable:false,sortable:false},
                            ],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "idEstado"]').attr("disabled", true);
                            e.container.find('input[name="idEstado"]').val(ev.data.idEstado).change();
                         }
                    });
                }   
                
                
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
                
                
                
            });
        </script>     
    </body>
    
</html>
