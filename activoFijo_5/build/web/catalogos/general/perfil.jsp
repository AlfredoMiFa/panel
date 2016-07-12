<!DOCTYPE html>
<%@page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - perfiles</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>      
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-CONFIG"/>
                    <jsp:param name="M_SELECT" value="C-G-PERFIL"/>
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
                                    <i class="fa fa-graduation-cap"></i> Perfil
                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row">
                        <div class="col-lg-9 cubierta">                            
                            <div class="row" id="row-bootstrapkendo-wrapper">                           
                                <div id="grid" style="height: 100%;"></div> 
                            </div>
                        </div>
                        <div class="col-lg-3" >
                            <div class="panel panel-default">
                                <div class="panel-heading" style="position: relative;overflow: hidden;height:43px;">
                                    <h3 class="panel-title"><i class="fa fa-unlock"></i> Derechos del menú</h3>
                                    <div id="slide" style="position: absolute;top:0px;right:0px;width:100%;">
                                       <div  class="k-block" style="padding:2px;">
                                           <button class="k-button" style="width:49%;" onclick="cancelarEdicionDerechos();">Cancelar</button>
                                           <button class="k-button" style="width:49%;" onclick="actualizarEdicionDerechos();">Actualizar</button>
                                       </div>
                                   </div>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div style="font-size: 0.9em;">
                                                <jsp:include page="/catalogos/generals/perfil.do">
                                                    <jsp:param name="modo" value="arbolDerechos"/>
                                                </jsp:include>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>                    
            <div style="visibility: hidden;">                                      
                <div id="derechosPopup">                                    
                    <div>
                        <br/>                   
                        <label>Derechos:</label><br/>
                        <input type="hidden" name="corePerfilId" id="corePerfilId" /> 
                        <select id="derechos"></select>
                        <div style="text-align: right;margin-top: 5px;">
                            <a class="k-button k-button-icontext k-grid-update" href="javascript:actualizar();"><span class="k-icon k-update"></span>Actualizar</a>
                            <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelar();"><span class="k-icon k-cancel"></span>Cancelar</a>
                        </div>
                        <br/>
                    </div>
                </div> 
                <div id="reglasPopup">                                    
                    <div>
                        <br/>
                        <label>Reglas seguridad:</label><br/>
                        <input type="hidden" name="reglasSeguridadId" id="reglasSeguridadId" /> 
                        <select id="reglas"></select>
                        <div style="text-align: right;margin-top: 5px;">
                            <a class="k-button k-button-icontext k-grid-update" href="javascript:actualizarReglas();"><span class="k-icon k-update"></span>Actualizar</a>
                            <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelarReglas();"><span class="k-icon k-cancel"></span>Cancelar</a>
                        </div>
                        <br/>
                    </div>
                </div>                   
            </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var wnd,wnd2,treeview;
            var slide = kendo.fx($("#slide")).slideIn("down");
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/perfil.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/perfil.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/perfil.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/perfil.do?modo=nuevo",
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
                            id: "corePerfilId",
                            fields: {
                                corePerfilId: { type: "number",editable: false, nullable: true},
                                perfil: { type: "string", validation: { required: true,maxlength:10 }},
                                descripcion: { type: "string", validation: { required: true,maxlength:150 } },
                                homePage: { type: "string", validation: { required: true,maxlength:45 } },
                                estatus: { type: "string",defaultValue: 'AC'}
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
                    toolbar: ["create",{ text: " Derechos del menu" ,imageClass: "fa fa-unlock"},{ text: " Derechos del sistema" ,imageClass: "fa fa-unlock-alt"},{ text: " Reglas de seguridad" ,imageClass: "fa fa-gavel"},{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"perfil", title: "Perfil", width: "100px"},
                        { field: "descripcion", title:"Descripción", width: "180px" },
                        { field: "homePage", title:"Página de inicio", width: "180px" },
                        { field: "estatus", width: "100px", values: aestatus },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "240px" }],
                    editable: "inline",
                    change: function(e) {
                        var data = this.dataItem(this.select());
                        $.post("../generals/perfil.do", { modo: "perfilDerechos",corePerfilId:data.corePerfilId},function(result) {
                            $("#arbolDerechos [type='checkbox']").each(function () {
                                $(".k-item > div input[name='"+this.name+"']").prop("checked", false);
                                //$("input[name='"+this.name+"']").removeAttr("checked");
                            });
                            if(result.data.indexOf(",")>-1){
                                var aids=result.data.split(",");
                                for(var i=0;i<aids.length;i++){
                                    //$("input[name='CH-"+aids[i]+"']").attr("checked",true);
                                    $(".k-item > div input[name='CH-"+aids[i]+"']").prop("checked", true);
                                }
                            }
                        },'json');
                    },
                    edit:function(e){
                         if(!e.model.isNew()){
                             $('input[name *= "perfil"]').attr("disabled", true);
                         }
                     }
                });
                treeview=$("#arbolDerechos").kendoTreeView().data("kendoTreeView");
                treeview.enable(".k-item",false);
                slide.reverse();
                wnd = $("#derechosPopup")
                    .kendoWindow({
                        title: "Editar derechos del sistema",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 800
                    }).data("kendoWindow");
                $("#derechos").kendoMultiSelect({
                    placeholder: "Seleccionar derecho...",
                    dataTextField: "descripcion",
                    dataValueField: "coreDerechoSistemaId",
                    autoBind: false,
                    dataSource: {
                        serverFiltering: true,
                        transport: {
                            read: {
                                url: "../generals/derechoSistema.do?sort[0][field]=derecho&pageSize=50&page=1&C=8&D=9&E=10",
                                dataType: "json"
                            }
                        },
                         schema: {
                            data: "data",
                            total: "total",
                            model: {
                                id: "coreDerechoSistemaId",
                                fields: {
                                    coreDerechoSistemaId: { type: "number",editable: false, nullable: true},
                                    derecho: { type: "string", validation: { required: true,maxlength:45 }},
                                    descripcion: { type: "string", validation: { required: true,maxlength:100 } }
                                }
                            },
                            error: "errors"
                        },
                        error: function (e) {
                            errorHandler(e);
                        }
                    }
                }); 
                wnd2 = $("#reglasPopup")
                    .kendoWindow({
                        title: "Editar derechos del sistema",
                        modal: true,
                        visible: false,
                        resizable: false,
                        width: 800
                    }).data("kendoWindow");
                $("#reglas").kendoMultiSelect({
                    placeholder: "Seleccionar regla de seguridad...",
                    dataTextField: "nombre",
                    dataValueField: "coreReglaSeguridadId",
                    maxSelectedItems: 1,
                    autoBind: false,
                    dataSource: {
                        serverFiltering: true,
                        transport: {
                            read: {
                                url: "../generals/reglaSeguridad.do?sort[0][field]=nombre&pageSize=100&page=1&C=8&D=9&E=10",
                                dataType: "json"
                            }
                        },
                         schema: {
                            data: "data",
                            total: "total",
                            model: {
                                id: "coreReglaSeguridadId",
                                fields: {
                                    coreReglaSeguridadId: { type: "number",editable: false, nullable: true},
                                    nombre: { type: "string", validation: { required: true,maxlength:45 }}
                                }
                            },
                            error: "errors"
                        },
                        error: function (e) {
                            errorHandler(e);
                        }
                    }
                }); 
                $(".k-grid-Derechosdelsistema").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        $("#corePerfilId").val(row.corePerfilId);
                        wnd.center().open();
                        var multiSelect = $("#derechos").data("kendoMultiSelect");
                        $.post("../generals/perfil.do",{modo:"getDerechosSistema",corePerfilId:row.corePerfilId},function(result){
                            multiSelect.value(result.data.split(","));
                        },"json"); 
                    }else
                        alert("Seleccione un registro");
                }); 
                $(".k-grid-Reglasdeseguridad").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                        $("#corePerfilId").val(row.corePerfilId);
                        wnd2.center().open();
                        var multiSelect = $("#reglas").data("kendoMultiSelect");
                        $.post("../generals/perfil.do",{modo:"getReglaSeguridad",corePerfilId:row.corePerfilId},function(result){
                            multiSelect.value(result.data.split(","));
                        },"json"); 
                    }else
                        alert("Seleccione un registro");
                });                
                $(".k-grid-Derechosdelmenu").click(function(e){
                    var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                    if(row!=null){
                       treeview.enable(".k-item",true);
                       treeview.expand(".k-item");
                        slide.play();
                        var createBtn = $(".cubierta");
                        var div = $("<div id='cubierta'>").css({
                            "position": "absolute",
                            "opacity":"0.4",
                            "filter":"alpha(opacity=40)",
                            "background-color":"#fff",
                            "z-index":"1000",
                            "left": createBtn.offset().left,
                            "top": createBtn.offset().top,
                            "width": createBtn.outerWidth(),
                            "height": createBtn.outerHeight()
                        }).appendTo($("body"));
                    }else
                        alert("Seleccione un registro");
                });

                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function cancelarEdicionDerechos()
            {
                treeview.enable(".k-item",false);
                slide.reverse();
                $('#cubierta').remove();
                var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                $.post("../generals/perfil.do", { modo: "perfilDerechos",corePerfilId:row.corePerfilId},function(result) {
                    $("#arbolDerechos [type='checkbox']").each(function () {
                    });
                    var aids=result.data.split(",");
                    for(var i=0;i<aids.length;i++){
                        $(".k-item > div input[name='CH-"+aids[i]+"']").prop("checked", true);
                    }
                },'json');                    
            }
            function actualizarEdicionDerechos()
            {
                slide.reverse();
                var row=$("#grid").data("kendoGrid").dataItem($("#grid").data("kendoGrid").select());
                var $ids="";
                $('#arbolDerechos input:checked').each(function() {
                    $ids+=$(this).attr("name")+",";
                });
                if($ids.length>0)$ids=$ids.substr(0,$ids.length-1);
                $.post("../generals/perfil.do", { modo: "actualizarPerfilDerechos",corePerfilId:row.corePerfilId,ids:$ids},function(result) {
                     if (result !== null)
                        verMensaje(result);
                    else
                        notification.show({title: "Error",message: "Error al recibir la respuesta"}, "msg-error");
                    treeview.enable(".k-item",false);
                    $('#cubierta').remove();
                },'json');
            }
            function actualizar()
            {
                var multiSelect = $("#derechos").data("kendoMultiSelect");
                $.post("../generals/perfil.do",{modo:"actualizarDerechosSistema",corePerfilId:$("#corePerfilId").val(),ids:multiSelect.value().toString()},function(result){
                    if (result !== null)
                        verMensaje(result);
                    else
                        notification.show({title: "Error",message: "Error al recibir la respuesta"}, "msg-error");
                    wnd.close();
                },"json");
            }
            function cancelar()
            {
                wnd.close();
            }
            function actualizarReglas()
            {
                var multiSelect = $("#reglas").data("kendoMultiSelect");
                $.post("../generals/perfil.do",{modo:"actualizarReglaSeguridad",corePerfilId:$("#corePerfilId").val(),ids:multiSelect.value().toString()},function(result){
                    if (result !== null)
                        verMensaje(result);
                    else
                        notification.show({title: "Error",message: "Error al recibir la respuesta"}, "msg-error");
                    wnd.close();
                },"json");
            }
            function cancelarReglas()
            {
                wnd2.close();
            }
        </script>
        <style>
            #arbolDerechos .k-sprite {
                background-image: url("../../recursos/img/coloricons-sprite.png");
            }

            .rootfolder { background-position: 0 0; }
            .folder     { background-position: 0 -16px; }
            .pdf        { background-position: 0 -32px; }
            .html       { background-position: 0 -48px; }
            .image      { background-position: 0 -64px; }
        </style>
    </body>
</html>
