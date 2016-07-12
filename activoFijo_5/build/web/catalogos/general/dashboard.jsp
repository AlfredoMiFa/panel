<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - construir dashboard</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>    
                <jsp:include page="/menu.do">
                    <jsp:param name="M_ACTIVE" value="M-CONFIG:S-C-REPORT"/>
                    <jsp:param name="M_SELECT" value="C-G-DASH"/>
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
                                    <i class="fa fa-wrench"></i> Construir dashboard
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
            var wnd;
            var cmbtipo=[{"value": "TABLA","text": "Tabla"},{"value": "GRAFICA","text": "Gráfica"},{"value": "TEXTO","text": "Texto"}];
            var cmbcolumnas=[{"value": "1","text": "1 Columna"},{"value": "2","text": "2 Columnas"},{"value": "3","text": "3 Columnas"}];
            var cmbbloques=[{"value": "A","text": "A"},{"value": "B","text": "B"},{"value": "C","text": "C"},{"value": "D","text": "D"},{"value": "E","text": "E"},{"value": "F","text": "F"}];
            var yesNoDropDownDataSource = new kendo.data.DataSource({ data: [{ Value: "true", Text: "Si" }, { Value: "false", Text: "No" }]});
            var defaultTools = kendo.ui.Editor.defaultTools;
            $(document).ready(function(){
                inicializar(); 
                defaultTools["viewHtml"].options.shift = true;
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../generals/dashboard.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../generals/dashboard.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../generals/dashboard.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../generals/dashboard.do?modo=nuevo",
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
                            id: "coreDashboardId",
                            fields: {
                                coreDashboardId: { type: "number", nullable: true,editable:false},
                                corePerfilId: { field: "corePerfilId", defaultValue: { id: "", text: "Seleccione una opción"}},
                                cveDatasource: { field: "cveDatasource", defaultValue: { id: "", text: "Seleccione una opción"}},
                                tipo: { type: "string",defaultValue:"GRAFICA" },
                                parametros: { type: "string", nullable: true,maxlength:200 },
                                titulo: { type: "string", nullable: true,maxlength:100 },
                                columnas: { type: "string",defaultValue:"1" },
                                bloque: { type: "string",defaultValue:"A" },
                                contenido: {  type: "string", nullable: true,maxlength:3000 },
                                activo: {  type: "boolean"}
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
                    toolbar: ["create",{ text: "Limpiar filtros",imageClass: "k-icon k-delete"}],
                    columns: [
                        { field:"coreDashboardId",title:"Id",width:"90px"},
                        { field:"corePerfilId", title: "Perfil",template:"#=corePerfilId.text#", width: "150px",filterable: {field:"corePerfilId.text"}},
                        { field: "cveDatasource", title:"Datasource",template:"#=cveDatasource.text#", width: "200px",filterable: {field:"cveDatasource.text"}},
                        { field: "contenido", title:"Contenido",hidden:true,  width: "100px" },
                        { field: "parametros", title:"Parametros",hidden:true,  width: "100px" },
                        { field: "titulo", title:"titulo",hidden:true,  width: "100px" },
                        { field: "columnas",title: "Columnas",values:cmbcolumnas, width: "100px"},
                        { field: "tipo", title:"Tipo",values:cmbtipo,  width: "100px" },
                        { field: "bloque", title:"Bloque",values:cmbbloques,  width: "100px" },
                        { field: "activo",title:"Activo",width: "100px"},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                    editable  : {
                        mode: "popup",
                        template: kendo.template($("#popup-editor").html())
                      },
                    edit:function(e){
                        var editor = $("#contenido").data("kendoEditor");
                        editor.bind("change", onChange);
                        editor.bind("keyup", editorKeyup);
                        var tipolistbox = $("#tipo").data("kendoDropDownList");
                        var tabs = $("#tabs").data("kendoTabStrip");
                        tabs.select(tipolistbox.select());
                        tabs.disable(tabs.tabGroup.children());
                        $("#parametros").focus(function(){
                            if($(this).val().trim()==="")
                                $(this).val("categoria:xxx|field:xxx|group:xxx|sort:xxx|tipo:xxx|titulo:xxx");
                        });
                        tipolistbox.bind("change", onChangeTipo);
                        tabsselect();
                     }
                });      
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            var datasourceDS = new kendo.data.DataSource({
                transport: {
                    read: {
                        dataType: "json",
                        url: "../generals/datasource.do?modo=combo",
                    }
                }
            }); 
            var perfilDS = new kendo.data.DataSource({
                transport: {
                    read: {
                        dataType: "json",
                        url: "../generals/perfil.do?modo=combo",
                    }
                }
            });
            
            function editorKeyup(e) {
                $("#words").text("Letras: "+this.value().length);
            }
            function onChange(e) {
                var limit = parseInt($("#contenido").attr('maxlength'));   
                var text = this.value();  
                var chars = text.length;  
                if(chars > limit){ 
                    var new_text = text.substr(0, limit); 
                    this.value(new_text); 
                }
                $("#words").text("Letras: "+this.value().length);
            }
            function tabsselect(){
                var tabs = $("#tabs").data("kendoTabStrip");
                var tipolistbox = $("#tipo").data("kendoDropDownList");
                if(tipolistbox.value()==="TABLA"){
                    var datasourcelistbox = $("#cveDatasource").data("kendoDropDownList");
                    if(datasourcelistbox.select()>0){
                        $.post("../generals/datasource.do",{modo:"probarQuery",cveDatasource:datasourcelistbox.value()},function(result){
                            if(result.errors!=null)
                                errorHandler(result);
                            else
                            {
                                $("#gridPrueba").html(result.data);
                                $("#grid2").kendoGrid({sortable: true,resizable:true});
                            }
                        },"json");
                    }
                    tabs.select(0);
                }else if(tipolistbox.value()==="GRAFICA"){
                    var datasourcelistbox = $("#cveDatasource").data("kendoDropDownList");
                    if(datasourcelistbox.select()>0){
                        $.post("../generals/datasource.do", {modo:'graficar',cveDatasource:datasourcelistbox.value(),parametros:$("#parametros").val()},function(result){                            
                            if(result.success)
                                createChart(result);
                            else 
                                alert("Se ha generado un error. En realizar la consulta.");                            
                        },'json');
                    }
                    tabs.select(1);
                }else if(tipolistbox.value()==="TEXTO"){
                    tabs.select(2);
                }
            }
            function onChangeTipo(e){                
                tabsselect();
            }
            function createChart(result) {
                var dataSourced;
                if(result.tipoGrafica==="pie" || result.tipoGrafica==="donut" || result.tipoGrafica==="NAN"){
                    dataSourced= new kendo.data.DataSource({
                        data: result.data,
                        sort: {field: result.sort,dir: "desc"}
                    });
                }else{
                    dataSourced= new kendo.data.DataSource({
                        data: result.data,
                        group: {field: result.group},
                        sort: {field: result.sort,dir: "desc"}
                    });
                }
                
                $("#chart").kendoChart({
                    dataSource:dataSourced,                   
                    title: {
                        text: result.titulo
                    },
                    legend: {
                        position: "top"
                    },
                    seriesDefaults: {
                        type: result.tipoGrafica,
                        visibleInLegend: result.count < 100
                    },
                    series: [{
                            field: result.field,
                            categoryField: result.categoria
                        }],
                    tooltip: {
                        visible: true,
                        template: "#= dataItem."+result.categoria+" # - #= value #|#= kendo.format('{0:P}', percentage)# "+(result.group!=="NAN"?"(#= dataItem."+result.group+" #)":"")
                    }
                });
            }
        </script>
        
        <script id="popup-editor" type="text/x-kendo-template">
            <table>
                <tr>
                    <td width='250px;' valign='top'>
                        <ul class="forms">
                            <li style="text-align:right;"><input id="coreDashboardId" name="coreDashboardId" type="text" class="k-textbox" data-bind="value:coreDashboardId" readonly="readonly"  style="width:100px;"/></li>
                            <li><label for="titulo">Titulo: </label><input id="titulo" name="titulo" type="text" class="k-textbox" data-bind="value:titulo"  /></li>
                            <li><label for="corePerfilId">Perfil: </label><input id="corePerfilId" data-value-field="id" data-text-field="text" data-source="perfilDS" data-role="dropdownlist"  name="corePerfilId" type="text" data-bind="value:corePerfilId" required /></li>
                            <li><label for="cveDatasource">Datasource: </label><input id="cveDatasource" data-value-field="id" data-text-field="text" data-source="datasourceDS" data-role="dropdownlist" name="cveDatasource" type="text" data-bind="value:cveDatasource" required /></li>
                            <li><label for="tipo">Tipo: </label><input id="tipo"  data-value-field="value" data-text-field="text" data-source="cmbtipo" data-role="dropdownlist"  name="tipo" type="text" data-bind="value:tipo" style="width:300px;"/>
                                                                <a href="javascript:tabsselect();" class="k-button" style="width:40px;"><i class="fa fa-rotate-left"></i></a></li>
                            <li><label for="columnas">Columnas: </label><input id="columnas"  data-value-field="value" data-text-field="text" data-source="cmbcolumnas" data-role="dropdownlist"  name="columnas" type="text" data-bind="value:columnas"/></li>
                            <li><label for="bloque">Bloque: </label><input id="bloque" data-value-field="value" data-text-field="text" data-source="cmbbloques" data-role="dropdownlist"  name="bloque" type="text" data-bind="value:bloque"/></li>
                            <li><label for="parametros">Parametros gráfica: </label><input id="parametros" name="parametros" type="text" class="k-textbox" data-bind="value:parametros"  placeholder="categoria:xxx|field:xxx|group:xxx|sort:xxx|tipo:xxx|titulo:xxx" /></li>
                            <li><label for="activo">Activo: </label><input name="activo" data-bind="value:activo" data-value-field="Value"  data-text-field="Text" data-source="yesNoDropDownDataSource" data-role="dropdownlist" /></li>
                        </ul>
                    </td>
                    <td width='350px;' valign='top'>
                        <div id="tabs" data-role="tabstrip" style="width:370px;margin-left:10px;">
                            <ul>
                                <li class="k-state-active">
                                    Tabla
                                </li>
                                <li>
                                    Gráfica
                                </li>
                                <li>
                                    Texto
                                </li>
                            </ul>
                            <div>
                                <div class="TABLA" style="height:455px;">
                                    <div id="gridPrueba" style="width:100%;height:100%;background-color: silver;"></div>
                                </div>
                           </div>
                            <div>
                                <div class="GRAFICA" style="height:455px;">
                                    <div id="chart" style="height: 100%;"></div>
                                </div>
                           </div>
                            <div>
                                <div class="TEXTO" style="height:455px;">
                                    <label for="contenido">Contenido (3000 max): </label><textarea id="contenido" data-role="editor" data-tools="['bold','italic','underline','insertImage',
                                       'subscript','superscript','viewHtml','strikethrough','outdent','justifyLeft','justifyCenter','justifyRight','justifyFull',
                                       'insertUnorderedList','insertOrderedList','indent','createLink','unlink']" data-encode="false" maxlength="3000" name="contenido" data-bind="value:contenido" style='height:420px;'></textarea></li>
                                        <div id="words"></div>
                                </div>
                           </div>
                        </div>
                    </td>
                </tr>
            </table>
        </script>
        <style>
            .k-popup-edit-form .k-edit-form-container{ width: 800px;height:580px;}
            .forms {
                float: left;
            }

            .forms li {
                margin-bottom: 5px;
                list-style: none;
            }

            .forms li > * {
                width: 350px;
            }
        </style>
    </body>
</html>

