<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - derechos</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>                
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-DERECH"/>
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
                                    <i class="fa fa-unlock"></i> Derechos del menú
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
                        url: "../generals/derecho.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/derecho.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/derecho.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/derecho.do?modo=nuevo",
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
                            id: "coreDerechoId",
                            fields: {
                                coreDerechoId: { type: "number",editable: false, nullable: true},
                                derecho: { type: "string", validation: { required: true,maxlength:10 }},
                                nombreMenu: { type: "string", validation: { required: true,maxlength:45 } },
                                rutaMenu: { type: "string", nullable: true,maxlength:45 },
                                esVisible: { type: "string",defaultValue: 'N' },
                                esEnlace: { type: "string",defaultValue: 'N' },
                                estatus: { type: "string",defaultValue: 'AC'},
                                icon: { type: "string",defaultValue: '', nullable: true},
                                padre: { defaultValue: { id: -1, text: "Seleccione una opción"}},
                                orden: { type: "number", validation: { min: 0, required: true,maxlength:5 } }
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
                        { field:"derecho", title: "Derecho", width: "100px",groupable: false},
                        { field: "nombreMenu", title:"Nombre menú",width: "130px",groupable: false },
                        { field: "rutaMenu", title:"Ruta menú",width: "150px",filterable: false,groupable: false  },
                        { field: "esEnlace", title:"Enlace",  width: "60px", values: aconfirmar,filterable: false  },
                        { field: "esVisible", title:"Visible", width: "60px", values: aconfirmar,filterable: false  },
                        { field: "orden", width: "80px", format: "{0:n0}",groupable: false,filterable: false },
                        { field: "padre", editor: padreDropDownEditor,width: "140px", groupable: false,template: "#=padre.text#" },
                        { field: "estatus", width: "80px", values: aestatus },
                        { field: "icon", title:"Icono", width: "70px",filterable: false,groupable: false,sortable:false  },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "220px" }],
                    editable: "inline",
                    edit:function(e){
                         if(!e.model.isNew()){
                             $('input[name = "derecho"]').attr("disabled", true);
                         }else{
                            $('input[name = "derecho"]').blur(function (){
                                if($.trim($('input[name = "derecho"]').val())!=="")
                                $.post("../generals/derecho.do",{modo:"verificarUnico",coreDerechoId:e.model.coreDerechoId,campo:"derecho",valor:$.trim($('input[name = "derecho"]').val())},function(result){
                                    if(!result.success)  
                                    {
                                        $('input[name = "derecho"]').val("");
                                        $('input[name = "derecho"]').attr("placeholder",result.msg);
                                    }
                                },"json");
                            });
                         }   
                     }
                });
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function padreDropDownEditor(container, options) {
                $('<input required  data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "id",

                        dataSource: {
                            transport: {
                                read: {
                                    dataType: "json",
                                    url: "../generals/derecho.do?modo=combo&filtro="+options.model.coreDerechoId
                                }
                            }
                        }
                    });
            }
        </script>
        <style>
            #row-bootstrapkendo-wrapper{
                font-size: 0.75em; 
            }
        </style>
    </body>
</html>