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
                    <jsp:param name="M_SELECT" value="C-G-BEAASIG"/>
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
                                    <i class="fa fa-unlock-alt"></i> Asignación de beacons
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
                        url: "../../beaconsAgencias/beaconAsig.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/beaconAsig.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/beaconAsig.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/beaconAsig.do?modo=nuevo",
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
                            id: "idBeaconAsig",
                            fields: {
                                idBeaconAsig:{ type: "number", editable: false, nullable: false },
                                idSucursal:  {  field: "idSucursal",editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                idVehiculo:  {  field: "idVehiculo", editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                idBeacons:   {  field: "idBeacons", editable: true, defaultValue: { id: "", text: "Seleccione una opción"}},
                                descripcion: { type: "string", editable: true, validation: { required: true,maxlength:100 }},                              
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
                            { field: "idBeaconAsig",    hidden:true},                             
                            { field: "idSucursal",      title:"Sucursal",width:"150px", editor:listaDropDownSucursal,template:"#=idSucursal.text#"},
                            { field: "idVehiculo",      title:"Vehículo",width:"150px", editor:listaDropDownVehiculo,template:"#=idVehiculo.text#" },
                            { field: "idBeacons",        title:"Beacon",width:"150px", editor:listaDropDownBeacon,template:"#=idBeacons.text#"},
                            { field: "descripcion",     title:"Descripción",width:"200px"},                                                         
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                        edit:function(e){
                            $('input[name = "idBeacons"]').blur(function (){
                                if($.trim($('input[name = "idBeacons"]').val())!=="")
                                    $.post("../../beaconsAgencias/beaconAsig.do",{modo:"validaBeacon",beacon:$.trim($('input[name = "idBeacons"]').val())},function(result){
                                        if(!result.success)  
                                        {
                                            $('input[name = "idBeacons"]').val("");
                                            $('input[name = "idBeacons"]').attr("placeholder",result.msg);
                                            alert(result.msg);
                                        }
                                    },"json");
                            });                            
                            
                        }
                        
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownVehiculo(container, options) {
                $('<input name="idVehiculo" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/vehiculos.do?modo=combo",
                                }
                            }
                        }
                    });
            }     
            function listaDropDownSucursal(container, options) {
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
            function listaDropDownBeacon(container, options) {
                $('<input name="idBeacons" data-text-field="text" data-value-field="id" required data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({                          
                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../../beaconsAgencias/beacons.do?modo=combo",
                                }
                            }
                        }
                    });
            }             
            
        </script>
    </body>
</html>

