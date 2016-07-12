<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - cuentas de correo</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>      
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>  
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-CORREO"/>
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
                                    <i class="fa fa-at"></i> Cuentas de correo
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
                            url: "../generals/correo.do",
                            dataType: "json"
                         },
                         update: {
                            url: "../generals/correo.do?modo=editar",
                            type: "POST",
                            dataType: "json",
                            complete: function(e) {
                                if (typeof (e.responseText) !== "undefined")
                                    verMensaje($.parseJSON(e.responseText));
                            }
                         },
                         destroy: {
                             url: "../generals/correo.do?modo=eliminar",
                             type: "POST",
                             dataType: "json",
                             complete: function(e) {
                                if (typeof (e.responseText) !== "undefined")
                                    verMensaje($.parseJSON(e.responseText));
                             } 
                          },
                          create: {
                              url: "../generals/correo.do?modo=nuevo",
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
                                id: "coreCorreoId",
                                fields: {
                                    coreCorreoId: { type: "number",editable: false, nullable: true},
                                    emisor: { type: "string", validation: { required: true,maxlength:45 }},
                                    user: { type: "string", validation: { required: true,email:true,maxlength:45 } },
                                    password: { type: "string", validation: { required: true,maxlength:20 }},
                                    smtpServer: { type: "string", validation: { required: true,maxlength:45 }},
                                    smtpPort: { type: "number",  validation: { required: true,maxlength:5 }}
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
                        selectable: "row",
                        sortable: true,
                        resizable: true,
                        pageable: {
                            refresh: true,
                            input: true,
                            pageSizes: [20, 50, 75],
                            numeric: false
                        },
                        toolbar: ["create",{ text: " Correo de prueba" ,imageClass: "fa fa-external-link-square"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [
                            { field:"emisor", title: "Nombre del Emisor"},
                            { field: "user", title:"Usuario"},
                            { field: "password", title:"Contraseña",sortable: false, width: "100px",filterable: false,
                            format: "******",
                            editor: function (container, options) {
                                $('<input data-text-field="' + options.field + '" ' +
                                        'class="k-input k-textbox" ' +
                                        'type="password" ' +
                                        'data-value-field="' + options.field + '" ' +
                                        'data-bind="value:' + options.field + '"/>')
                                        .appendTo(container);
                            } },
                            { field: "smtpServer",  width: "150px",filterable: false  },
                            { field: "smtpPort", width: "80px",format: "{0}", filterable: false  },
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                        edit:function(e){
                             if(!e.model.isNew()){
                                 $('input[name *= "emisor"]').attr("disabled", true);
                             }
                         }
                    });

                    $(".k-grid-Limpiarfiltros").click(function(e){
                        $("#grid").data("kendoGrid").dataSource.filter([]);
                    });
                    $(".k-grid-Correodeprueba").click(function(e){
                        var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                        if(row!=null){                            
                            $.post("../generals/correo.do", { modo: "probarConexion",coreCorreoId:row.coreCorreoId},function(result) {
                                if (result.success)
                                    $("#grid").data("kendoGrid").dataSource.read();
                                verMensaje(result);
                            },'json');
                        }else
                            alert("Seleccione un registro");
                    });
                });
            </script>
        </div>
    </body>
</html>