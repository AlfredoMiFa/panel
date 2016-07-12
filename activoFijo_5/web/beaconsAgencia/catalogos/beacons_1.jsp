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
                    <jsp:param name="M_SELECT" value="C-G-BEACON"/>
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
                                    <i class="fa fa-unlock-alt"></i> Beacons
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
            var sensorHab= [{"value": "true","text": "HABILITADO"},{"value": "false","text": "DESHABILITADO"}];
            var estatus= [{"value": "A","text": "ACTIVO"},{"value": "B","text": "BAJA"},{"value": "M","text": "MANTENIMIENTO"}];
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/beacons.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/beacons.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/beacons.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/beacons.do?modo=nuevo",
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
                            id: "idBeacons",
                            fields: {
                                idBeacons:      { type: "number", editable: false, nullable: false },
                                uuid:           { type: "string", editable: true, nullable: true,validation:{  required: true,maxlength:40 }},
                                majorId:        { type: "number", editable: true, nullable: true,validation:{ min: 0, max:65000, required: true,maxlength:6 }},
                                minorId:        { type: "number", editable: true, nullable: true,validation:{ min: 0, max:65000, required: true,maxlength:6 }},
                                descripcion:    { type: "string", validation: { required: true,maxlength:100 } },
                                macadress:      { type: "string", validation: { required: true,maxlength:20 ,pattern:"\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}\\b" }},
                                nombre:         { type: "string", validation: { required: true,maxlength:100 }},
                                potencia:       { type: "number", editable: true, nullable: true,validation: { min: 0, required: true,maxlength:3 }},
                                frecuencia:     { type: "number", editable: true, nullable: true,validation: { min: 0, required: true,maxlength:3 }},
                                carga:          { type: "string", validation: { required: true,maxlength:100 }},
                                os:             { type: "string", validation: { required: true,maxlength:50 }},
                                sensorHabMov:   { type: "string",editable: true, defaultValue:'true', values: sensorHab},
                                estatus:        { type: "string",editable: true, defaultValue:'A', values:estatus }                                
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
                            { field: "idBeacons",hidden:true},
                            { field: "uuid",        title:"UUID",encoded: false,width:"330px"}, 
                            { field: "majorId",     title:"Major Id",encoded: false,width:"200px",format: "{0:n0}"}, 
                            { field: "minorId",     title:"Minor Id",encoded: false,width:"200px",format: "{0:n0}"},                              
                            { field: "descripcion", title:"Descripción",encoded: false,width:"250px"}, 
                            { field: "macadress",   title:"MacAdress",width:"170px",},  
                            { field: "nombre",      title:"Nombre",width:"200px"}, 
                            { field: "potencia",    title:"Potencia",width:"100px",format: "{0:n0}"},
                            { field: "frecuencia",  title:"Frecuencia",width:"100px",format: "{0:n0}"},                             
                            { field: "carga",       title:"Carga",width:"200px"},                              
                            { field: "os",          title:"Sistema operativo",width:"200px"},                                                         
                            { field: "sensorHabMov",title:"Sensor de mov. Habilitado",width:"190px", defaultValue:'true',values: sensorHab },  
                            { field: "estatus",    title:"Estatus",width:"150px", defaultValue:'A', values: estatus},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline"
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
        </script>
    </body>
</html>

