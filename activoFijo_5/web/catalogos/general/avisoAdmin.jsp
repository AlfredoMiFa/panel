<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - avisos administrador</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>         
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include> 
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-PERS"/>
                    <jsp:param name="M_SELECT" value="C_I_AVIADM"/>                            
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
                                    <a href="#"><i class="fa fa-info"></i> Modulo de información</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-bell-slash"></i> Avisos administrador
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row"> 
                        <div class="col-lg-8" id="row-bootstrapkendo-wrapper">                            
                            <div id="grid" style="height: 100%;"></div> 
                        </div>
                        <div class="col-lg-4">
                            <form id="form" role="form" action="../generals/aviso.do" >
                                <div class="row">
                                    <div class="col-lg-12">
                                        <input type="hidden" name="modo" value="nuevo"/>
                                        <input type="hidden" name="estatus" value="AC"/>
                                        <div class="form-group">
                                            <label for="titulo">Título:</label>
                                            <input id="titulo" class="form-control" required  name="titulo" maxlength="45" type="text" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="prioridad">Prioridad:</label>
                                            <input id="prioridad" required  name="prioridad"  style="width:150px;" maxlength="5" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label for="vigencia">Vigencia:</label>
                                            <input id="vigencia" required name="vigencia" style="width:150px;" maxlength="5" type="text" />
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="perfilDestino">Perfil:</label>
                                    <input id="perfilDestino" required  style="width:330px;" name="perfilDestino" maxlength="5" type="text" />
                                </div>
                                <div class="form-group">
                                    <textarea id="contenido" name="contenido" required maxlength="10000"></textarea>   
                                </div>
                                <div class="form-group">
                                    <a id="submit"  href="javascript:guardar()" class="k-button" >Enviar aviso</a> 
                                </div>
                            </form>
                            <div id="details"></div>
                        </div>
                    </div>
                </div>
            </div>                                        
            <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>

            <script>
                var aprioridad = [{"value": "AL","text": "ALTA"},{"value": "ME","text": "MEDIANA"},{"value": "ME","text": "BAJA"}];
                $(document).ready(function(){
                    inicializar();      

                    var dataSource = new kendo.data.DataSource({
                       autoSync: true,
                       transport: {
                         read:   {
                            url: "../generals/aviso.do?tipo=admin",
                            dataType: "json"
                         },
                         update: {
                            url: "../generals/aviso.do?modo=editar",
                            type: "POST",
                            dataType: "json",
                            complete: function(e) {
                                if (typeof (e.responseText) !== "undefined")
                                    verMensaje($.parseJSON(e.responseText));
                            }
                         },
                         destroy: {
                             url: "../generals/aviso.do?modo=eliminar",
                             type: "POST",
                             dataType: "json",
                             complete: function(e) {
                                if (typeof (e.responseText) !== "undefined")
                                    verMensaje($.parseJSON(e.responseText));
                             } 
                          },
                           parameterMap: function(options, type) {
                                if (type === "read") {
                                    if (options.filter) {
                                        for (var i = 0; i < options.filter.filters.length; i++) {
                                            if (options.filter.filters[i].field === 'fechaCreacion' ) {
                                                options.filter.filters[i].value = kendo.toString(options.filter.filters[i].value, "yyyy-MM-dd");
                                            }
                                        }
                                    }
                                }else  if (type === "create" ||type === "update") {
                                    options.fechaCreacion=kendo.toString(options.fechaCreacion, "dd/MM/yyyy");
                                }
                                return options;
                            }
                         },
                         schema: {
                            data: "data",
                            total: "total",
                            model: {
                                id: "coreAvisoId",
                                fields: {
                                    coreAvisoId: { type: "number",editable: false, nullable: true},
                                    titulo: { type: "string", validation: { required: true,maxlength:45 }},
                                    contenido: { type: "string", validation: { required: true,maxlength:10000 } },
                                    prioridad: { type: "string",editable: false,defaultValue: 'ME'},
                                    vigencia: { type: "number",editable: false},
                                    perfilDestino: { type: "string",editable: false, nullable: true},
                                    fechaCreacion: { type: "date",editable: false},
                                    fechaCreacionHora: { type: "string",editable: false},
                                    estatus: { type: "string",defaultValue: 'AC',editable: false}
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
                        toolbar: [{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [    
                            { field: "coreAvisoId",hidden:true},
                            { field:"titulo", title: "Título",width:"200px"},
                            { field: "contenido", title:"Contenido",encoded: false,width:"300px"}, 
                            { field: "perfilDestino", title:"Perfil",width:"100px"},
                            { field: "fechaCreacion", title:"Creación",width:"100px", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }},   
                            { field: "fechaCreacionHora",title:"hh:mm:ss",width:"70px",filterable:false,sortable:false},  
                            { field: "prioridad", title:"Prioridad",width:"100px", values: aprioridad},  
                            { field: "vigencia", title:"Vigencia",width:"100px"}, 
                            { field: "estatus",title:"Estatus", width: "90px", values: aestatus,template:"#if(estatus=='AC'){#<b style='color:green;'>Activo<\/b>#}else if(estatus=='CA'){#<b style='color:red;'>Cancelado<\/b>#}#"  }],
                        editable: true
                    });              
                    $('#contenido').kendoEditor({
                       encoded: false,
                       tools: [
                           "bold","italic","underline","insertImage",
                           "subscript","superscript","viewHtml","strikethrough","fontSize","foreColor","backColor",
                           "outdent","justifyLeft","justifyCenter","justifyRight","justifyFull",
                           "insertUnorderedList","insertOrderedList","indent",
                           "createLink","unlink",
                       ],
                       change:onChange
                   });
                   $("#prioridad").kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "value",
                        dataSource: aprioridad,
                        index: 0
                    });
                    $("#vigencia").kendoNumericTextBox({
                        min: 1,
                        max: 30,
                        step: 5,
                        format: "# días"
                    });
                       
                    $("#perfilDestino").kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "id",
                        dataSource: {
                                serverFiltering: true,
                                transport: {
                                    read: {
                                        dataType: "json",
                                        url: "../generals/perfil.do?modo=combo2",
                                    }
                                }
                            },
                        index: 0
                    }); 
                    $(".k-grid-Limpiarfiltros").click(function(e){
                        $("#grid").data("kendoGrid").dataSource.filter([]);
                    });
                });
                function onChange(e) {
                    var editor = $("#contenido").data("kendoEditor");
                    var limit = parseInt($("#contenido").attr('maxlength'));   
                    var text = editor.value();  
                    var chars = text.length;  
                    if(chars > limit){ 
                        var new_text = text.substr(0, limit); 
                        editor.value(new_text); 
                    }
                }
                function guardar(){
                    if ($("#form").kendoValidator().data("kendoValidator").validate()) {
                        $.post($('#form').attr( 'action' ), $('#form').serialize(),function(result){
                            if (result.success)  
                            {
                                $("#titulo").val("");                                
                                $("#grid").data("kendoGrid").dataSource.read(); 
                            }                                      
                            verMensaje(result);
                        },'json');
                    } 
                }
            </script>
        </div>
    </body>
</html>