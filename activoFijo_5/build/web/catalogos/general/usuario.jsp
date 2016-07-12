<!DOCTYPE html>
<%@page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - usuarios</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>      
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>               
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-USUAR"/>
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
                                    <i class="fa fa-users"></i> Usuario
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
                        <label>Perfiles:</label><br/>
                        <input type="hidden" name="coreUsuarioId" id="coreUsuarioId" /> 
                        <select id="perfiles"></select>
                        <div style="text-align: right;margin-top: 5px;">
                            <a class="k-button k-button-icontext k-grid-update" href="javascript:actualizar();"><span class="k-icon k-update"></span>Actualizar</a>
                            <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelar();"><span class="k-icon k-cancel"></span>Cancelar</a>
                        </div>
                        <br/>
                    </div>
                </div>
            </div>
        </div>                    
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/recursos/js/kendo.culture.es-MX.min.js"></script>
        <script>                
            var wnd;
            var cmbaccion=[{"value": "PE","text": "Permitir"},{"value": "BL","text": "Bloquear"}];
            var tipo=[{"value": "SUPER-ADMINISTRADOR", "text":"SUPER-ADMINISTRADOR"},{"value": "ADMINISTRADOR", "text": "ADMINISTRADOR"},{"value": "SUPERVISOR", "text":"SUPERVISOR"},{"value": "USUARIO", "text":"USUARIO"}];
            $(document).ready(function(){
                inicializar();
                kendo.culture("es-MX");
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/usuario.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/usuario.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/usuario.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            $("#grid").data("kendoGrid").dataSource.read(); 
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/usuario.do?modo=nuevo",
                          type: "POST",
                          dataType: "json",
                          complete: function(e) {
                            $("#grid").data("kendoGrid").dataSource.read(); 
                             if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                          }
                       },
                        parameterMap: function(options, type) {
                             if (type === "read") {
                                 if (options.filter) {
                                     for (var i = 0; i < options.filter.filters.length; i++) {
                                         if (options.filter.filters[i].field === 'vigencia') {
                                             options.filter.filters[i].value = kendo.toString(options.filter.filters[i].value, "yyyy-MM-dd");
                                         }
                                     }
                                 }
                             }else  if (type === "create" ||type === "update") {
                                 options.vigencia=kendo.toString(options.vigencia, "dd/MM/yyyy");
                             }
                             return options;
                         }
                     },
                     schema: {
                        data: "data",
                        total: "total",
                        model: {
                            id: "coreUsuarioId",
                            fields: {
                                coreUsuarioId: { type: "number",editable: false, nullable: true},
                                usuario: { type: "string", validation: { required: true,maxlength:20 }},
                                contrasena: { type: "string", validation: { required: true,maxlength:100 } },
                                nombre: { type: "string", validation: { required: true,maxlength:45 } },
                                apellidos: { type: "string", validation: { required: true,maxlength:45 } },
                                email: { type: "string", validation: { required: true,maxlength:100,email:true } },
                                estilo: { type: "string", validation: {maxlength:20 }, defaultValue: 'default' },
                                telefono: { type: "string", validation: {maxlength:22 } },
                                grupo: { type: "string", defaultValue: "USUARIO", values:tipo },
                                esSuper: { type: "string", defaultValue: 'N' },              
                                vigencia: { type: "date" },
                                aplicaBitacora: { type: "boolean", defaultValue: false },  
                                multiSesion: { type: "boolean", defaultValue: true },  
                                ipRestriccion: { type: "boolean", defaultValue: false },              
                                estatus: { type: "string",defaultValue: 'AC'},
                                idEmpresa: { field: "idEmpresa", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idOficina: { field: "idOficina", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}}
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
                    selectable: "row",
                    resizable: true,
                    pageable: {
                        refresh: true,
                        input: true,
                        pageSizes: [20, 50, 75],
                        numeric: false
                    },
                    toolbar: ["create",{ text: "Editar perfiles" ,imageClass: "k-icon k-edit"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                        columns: [
                        { field: "idEmpresa", title:"Empresa",width:"200px",editor:listaDropDownEmpresas,template:"#=idEmpresa.text#"},
                        { field: "idOficina", title:"Oficinas",width:"200px",editor:listaDropDownOficinas,template:"#=idOficina.text#"},
                        { field: "usuario", title:"Usuario", width: "120px" },
                        { field: "contrasena", title:"Contraseña",filterable: false,sortable:false, width: "120px" ,
                        format: "******",
                        editor: function (container, options) {
                            $('<input data-text-field="' + options.field + '" ' +
                                    'class="k-input k-textbox" ' +
                                    'type="password" ' +
                                    'data-value-field="' + options.field + '" ' +
                                    'data-bind="value:' + options.field + '"/>')
                                    .appendTo(container);
                        }},

                        { field:"nombre", title: "Nombre", width: "200px"},
                        { field: "apellidos", title:"Apellidos", width: "200px" },
                        { field: "email", title:"Email", width: "200px" },
                        { field: "telefono", title:"Teléfono", width: "100px" },
                        { field: "grupo", title:"Grupo", width: "150px", defaultValue:"USUARIO", values:tipo },
                        { field: "estilo", width: "150px", title:"Estilo",values: aestilo,hidden:true},
                        { field: "esSuper", width: "100px", title:"Admin",values: aconfirmar},
                        { field: "aplicaBitacora", width: "130px", title:"Bitacora",template:"#if(aplicaBitacora){#<input type='checkbox' checked='checked'/>#}else{#<input type='checkbox'/>#}#"},
                        { field: "multiSesion", width: "130px", title:"Multisesión",template:"#if(multiSesion){#<input type='checkbox' checked='checked'/>#}else{#<input type='checkbox'/>#}#"},
                        { field: "ipRestriccion", width: "100px", title:"Válida IP",template:"#if(ipRestriccion){#<input type='checkbox' checked='checked'/>#}else{#<input type='checkbox'/>#}#"},
                        { field: "estatus", title:"Estatus", width: "90px", values: aestatus },                    
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px"}],
                    editable  :  "inline",
                    detailInit: detailInit,
                    edit:function(e){
                         if(!e.model.isNew())
                             $('input[name = "usuario"]').attr("disabled", true);
                         else{
                            $('input[name = "usuario"]').blur(function (){
                                if($.trim($('input[name = "usuario"]').val())!=="")
                                $.post("../generals/usuario.do",{modo:"verificarUnico",coreUsuarioId:e.model.coreUsuarioId,campo:"usuario",valor:$.trim($('input[name = "usuario"]').val())},function(result){
                                    if(!result.success)  
                                    {
                                        $('input[name = "usuario"]').val("");
                                        $('input[name = "usuario"]').attr("placeholder",result.msg);
                                    }
                                },"json");
                            });
                         }                             
                        $('input[name = "email"]').blur(function (){
                            if($.trim($('input[name = "email"]').val())!=="")
                                $.post("../generals/usuario.do",{modo:"verificarUnico",coreUsuarioId:e.model.coreUsuarioId,campo:"email",valor:$.trim($('input[name = "email"]').val())},function(result){
                                    if(!result.success)  
                                    {
                                        $('input[name = "email"]').val("");
                                        $('input[name = "email"]').attr("placeholder",result.msg);
                                    }
                                },"json");
                        });
                        $('input[name = "coreUsuarioId"]').blur(function (){
                                if($.trim($('input[name = "coreUsuarioId"]').val())!=="")
                                    $.post("../generals/usuario.do",{modo:"empresa",idUsuar:$.trim($('input[name = "coreUsuarioId"]').val())},function(result){
                                        if(!result.success)  
                                        {
                                            $('input[name = "coreUsuarioId"]').val("");
                                            $('input[name = "coreUsuarioId"]').attr("placeholder",result.msg);
                                            alert(result.msg);
                                        }
                                    },"json");
                            });
                        var listaDropDownOficinas;
                        var listaDropDownEmpresas =$('input[name="idEmpresa"]').kendoDropDownList({
                            dataTextField: "text",
                            dataValueField: "id",
                            dataSource: {
                                transport: {
                                    read: {
                                        dataType: "json",
                                        url: "../../beaconsAgencias/empresa.do?modo=combo"
                                    }
                                }
                            },change:function(){
                                listaDropDownOficinas.dataSource.options.transport.read.url="../../beaconsAgencias/oficinas.do?modo=combos&idEmpresa="+listaDropDownEmpresas.value();
                                listaDropDownOficinas.dataSource.read();
                            },dataBound:function(){
                                listaDropDownOficinas=$('input[name="idOficina"]').kendoDropDownList({
                                    dataTextField: "text",
                                    dataValueField: "id",
                                    dataSource: {
                                        transport: {
                                            read: {
                                                dataType: "json",
                                                url: "../../beaconsAgencias/oficinas.do?modo=combos&idEmpresa="+listaDropDownEmpresas.value()
                                            }
                                        }
                                    }
                                }).data("kendoDropDownList");
                            }
                        }).data("kendoDropDownList");
                     }
                });

                function detailInit(ev) {
                    $("<div id='grid2'/>").appendTo(ev.detailCell).kendoGrid({
                        dataSource: {
                            transport: {
                             read:   {
                                url: "../generals/permisosIp.do",
                                dataType: "json"
                             },
                             update: {
                                url: "../generals/permisosIp.do?modo=editar",
                                type: "POST",
                                dataType: "json",
                                complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                }
                             },
                             destroy: {
                                 url: "../generals/permisosIp.do?modo=eliminar",
                                 type: "POST",
                                 dataType: "json",
                                 complete: function(e) {
                                    if (typeof (e.responseText) !== "undefined")
                                        verMensaje($.parseJSON(e.responseText));
                                 } 
                              },
                              create: {
                                  url: "../generals/permisosIp.do?modo=nuevo",
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
                                    id: "corePermisosIpId",
                                    fields: {
                                        coreUsuarioId: { type: "string"},
                                        corePermisosIpId: { type: "number",editable: false, nullable: true},
                                        accion: { type: "string", defaultValue:'PE'},
                                        ip: { type: "string", validation: { required: true,maxlength:15,pattern:"\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b" }},
                                        estatus: { type: "string", defaultValue:'AC'}
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
                            filter: { field: "coreUsuarioId", operator: "eq", value: ev.data.coreUsuarioId }                    
                        },
                        toolbar: [{ text: "Permisos por IP" ,imageClass: "k-icon k-i-note"},"create"],
                        columns: [
                            { field: "coreUsuarioId",filterable:false,sortable:false, width: "100px",hidden:true},
                            { field: "corePermisosIpId",title:"Id", width: "100px"},
                            { field: "accion", title:"Acción", width: "150px",values:cmbaccion},
                            { field: "ip", title:"IP", width: "150px"},
                            { field: "estatus", title:"Estatus",values:aestatus, width: "150px"},
                            { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" ,filterable:false,sortable:false},
                            {}],
                        editable: "inline",
                        edit:function(e){
                            e.container.find('input[name = "coreUsuarioId"]').attr("disabled", true);
                            e.container.find('input[name="coreUsuarioId"]').val(ev.data.coreUsuarioId).change();
                         }
                    });
                }
                wnd = $("#perfilesPopup")
                    .kendoWindow({
                        title: "Editar perfiles",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 500
                    }).data("kendoWindow");
                $("#perfiles").kendoMultiSelect({
                    placeholder: "Seleccionar perfil...",
                    dataTextField: "perfil",
                    dataValueField: "corePerfilId",
                    autoBind: false,
                    dataSource: {
                        serverFiltering: true,
                        transport: {
                            read: {
                                url: "../generals/perfil.do?sort[0][field]=perfil&pageSize=30&page=1&C=8&D=9&E=10&filter[logic]=and&filter[filters][0][operator]=eq&filter[filters][0][field]=estatus&filter[filters][0][value]=AC",
                                dataType: "json"
                            }
                        },
                         schema: {
                            data: "data",
                            total: "total",
                            model: {
                                id: "corePerfilId",
                                fields: {
                                    corePerfilId: { type: "number",editable: false, nullable: true},
                                    perfil: { type: "string", validation: { required: true,maxlength:10 }},
                                    descripcion: { type: "string", validation: { required: true,maxlength:150 } },
                                    estatus: { type: "string",defaultValue: 'AC'}
                                }
                            },
                            error: "errors"
                        },
                        error: function (e) {
                            errorHandler(e);
                        }
                    }
                }); 
                $(".k-grid-Editarperfiles").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        $("#coreUsuarioId").val(row.coreUsuarioId);
                        wnd.center().open();
                        var multiSelect = $("#perfiles").data("kendoMultiSelect");
                        $.post("../generals/perfil.do",{modo:"getPerfilesUsuario",coreUsuarioId:row.coreUsuarioId},function(result){
                            multiSelect.value(result.data.split(","));
                        },"json"); 
                    }else
                        alert("Seleccione un registro");
                });

                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function actualizar()
            {
                var multiSelect = $("#perfiles").data("kendoMultiSelect");
                $.post("../generals/perfil.do",{modo:"actualizarPerfilesUsuario",coreUsuarioId:$("#coreUsuarioId").val(),ids:multiSelect.value().toString()},function(result){
                    if (result !== null)
                        verMensaje(result);
                    else
                    {                            
                        $(".success-message").hide();
                        $(".error-message").show();  
                        $(".error-heading").html(result.msg);
                    }
                    wnd.close();
                },"json");
            }
            function cancelar()
            {
                wnd.close();
            }
            function listaDropDownOficinas(container, options){
                 $('<input required name="idOficina" id="idOficina"/>').appendTo(container);
            }
            
            function listaDropDownEmpresas(container, options){
                $('<input required name="idEmpresa" id="idEmpresa"/>').appendTo(container);
            }
        </script>
        <style>
            #grid2{
                height: 250px;
            }
        </style>
    </body>
</html>