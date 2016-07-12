<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - mensajes usuario</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>         
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include> 
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-PERS"/>
                    <jsp:param name="M_SELECT" value="C-G-MENSAJ"/>                            
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
                                    <i class="fa fa-envelope-o"></i> Bandeja de mensajes
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row"> 
                        <div class="col-lg-8" id="row-bootstrapkendo-wrapper">                            
                            <div id="grid" style="height: 100%;"></div> 
                        </div>
                        <div class="col-lg-4">
                            <br/>
                            <form id="form" role="form" action="../generals/mensaje.do" >
                                <input type="hidden" name="modo" value="nuevo"/>
                                <div class="form-group">
                                    <label for="titulo">Título:</label>
                                    <input id="titulo" class="form-control" required  name="titulo" maxlength="45" type="text" />
                                </div>
                                <div class="form-group">
                                    <label for="tov">Para:</label>
                                    <input id="tov" required  style="width:330px;" name="tov" maxlength="45" type="text" />
                                </div>
                                <div class="form-group">
                                    <textarea id="texto" name="texto" required maxlength="500"></textarea>   
                                </div>
                                <div class="form-group">
                                    <a id="submit"  href="javascript:guardar()" class="k-button" >Enviar mensaje</a> <span id="words"></span>
                                </div>
                            </form>
                            <div id="details"></div>
                        </div>
                    </div>
                </div>
            </div>                                        
            <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>

            <script>
                var wnd,detailsTemplate;
                $(document).ready(function(){
                    inicializar();     
                    var dataSource = new kendo.data.DataSource({
                       autoSync: true,
                       transport: {
                         read:   {
                            url: "../generals/mensaje.do",
                            dataType: "json"
                         },
                         update: {
                            url: "../generals/mensaje.do?modo=editar",
                            type: "POST",
                            dataType: "json",
                            complete: function(e) {
                                if (typeof (e.responseText) !== "undefined")
                                    verMensaje($.parseJSON(e.responseText));
                            }
                         },
                         destroy: {
                             url: "../generals/mensaje.do?modo=eliminar",
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
                                id: "coreMensajeId",
                                fields: {
                                    coreMensajeId: { type: "number",editable: false, nullable: true},
                                    titulo: { type: "string", validation: { required: true,maxlength:45 },editable: false},
                                    texto: { type: "string", validation: { required: true,maxlength:500 },editable: false },
                                    tov: { type: "string",editable: false},
                                    fechaCreacion: { type: "date",editable: false},
                                    fechaRecepcion: { type: "sring",editable: false, nullable: true},
                                    fechaCreacionHora: { type: "string",editable: false},
                                    grupo: { type: "string",editable: false, nullable: true},
                                    toEstatus: { type: "string",defaultValue: 'AC'},
                                    fromEstatus: { type: "string",defaultValue: 'AC',editable: false},
                                    fromv: { type: "string",editable: false, nullable: true}
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
                        toolbar: [{ text: " Entrada" ,imageClass: "fa fa-long-arrow-down"},{ text: " Salida" ,imageClass: "fa fa-long-arrow-up"}],
                        columns: [    
                            { field: "coreMensajeId",hidden:true},
                            { field: "toEstatus",title:"Estatus", width: "90px", values: aestatusmensaje,template:"#if(toEstatus=='AC'){#<b style='color:red;'>Enviado<\/b>#}else if(toEstatus=='IN'){#<b style='color:green;'>Recibido<\/b>#}#"  },
                            { field: "fromv", title:"De",width:"100px",filterable:false,sortable:false},  
                            { field: "tov", title:"Para",width:"100px",filterable:false,sortable:false},
                            { field: "fechaCreacion", title:"Creación",width:"100px", format: "{0:dd/MM/yyyy}",filterable: {
                                ui: "datepicker"
                            }},   
                            { field: "fechaCreacionHora",title:"hh:mm:ss",width:"70px",filterable:false,sortable:false},  
                            { field: "fechaRecepcion", title:"Recepción",width:"150px",filterable:false,sortable:false},
                            { field:"titulo", title: "Título",width:"200px"},
                            { field: "texto", title:"Texto",encoded: false,width:"300px"},                                  
                            { command: ["destroy",{ text: "Ver detalles", click: showDetails }], title: "&nbsp;", width: "250px" }],
                        editable: true,
                        edit:function(e){
                             if(!e.model.isNew()){
                                 $('input[name *= "titulo"]').attr("disabled", true);
                                 $('input[name *= "texto"]').attr("disabled", true);
                             }
                         }
                    });
                    wnd = $("#details")
                        .kendoWindow({
                            title: "Detalles del mensaje",
                            modal: true,
                            visible: false,
                            resizable: false,
                            width: 800,
                            height:340
                        }).data("kendoWindow");

                    detailsTemplate = kendo.template($("#template").html());
                                       
                    $('#texto').kendoEditor({
                       encoded: false,
                       tools: [
                           "bold","italic","underline","insertImage",
                           "subscript","superscript","viewHtml","strikethrough","fontSize","foreColor","backColor",
                           "outdent","justifyLeft","justifyCenter","justifyRight","justifyFull",
                           "insertUnorderedList","insertOrderedList","indent",
                           "createLink","unlink"
                       ],
                       change:onChange,
                       keyup:editorKeyup
                   });
                       
                    $("#tov").kendoDropDownList({
                        dataTextField: "text",
                        dataValueField: "id",
                        dataSource: {
                                serverFiltering: true,
                                transport: {
                                    read: {
                                        dataType: "json",
                                        url: "../generals/usuario.do?modo=combo"
                                    }
                                }
                            },
                        index: 0
                    }); 
                    $(".k-grid-Entrada").click(function(e){
                        var grid=$("#grid").data("kendoGrid");
                        $("#toolbar-salida").removeClass("k-state-selected");
                         grid.dataSource.filter({
                                 "field"   : "tov",
                                 "operator": "eq",
                                 "value"   : "${va_SESION.usuario.usuario}"
                        });
                    });
                    $(".k-grid-Salida").click(function(e){
                        var grid=$("#grid").data("kendoGrid");
                        $("#toolbar-entrada").removeClass("k-state-selected");
                        grid.dataSource.filter({
                                 "field"   : "fromv",
                                 "operator": "eq",
                                 "value"   : "${va_SESION.usuario.usuario}"
                        });
                    });
                });
            
                function editorKeyup(e) {
                    $("#words").text("Letras: "+this.value().length);
                }
                function onChange(e) {
                    var limit = parseInt($("#texto").attr('maxlength'));   
                    var text = this.value();  
                    var chars = text.length;  
                    if(chars > limit){ 
                        var new_text = text.substr(0, limit); 
                        this.value(new_text); 
                    }
                    $("#words").text("Letras: "+this.value().length);
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
                function showDetails(e) {
                    e.preventDefault();
                    var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
                    wnd.content(detailsTemplate(dataItem));
                    wnd.center().open();
                }
            </script>
            <script type="text/x-kendo-template" id="template">
                <div id="details-container" class="k-block k-info-colored" style="padding-left: 10px;">
                    <h2>#= titulo # </h2>
                    <em >De: #= fromv# &nbsp;&nbsp;Para: #= tov# &nbsp;&nbsp;&nbsp;fecha: #= kendo.toString(fechaCreacion, "dd/MM/yyyy")# </em>
                    <p>#= texto #</p>
                </div>
            </script>   

            <style type="text/css">
                #details-container em
                {
                    color: #8c8c8c;
                }
            </style>
        </div>
    </body>
</html>