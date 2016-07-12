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
                    <jsp:param name="M_SELECT" value="C-G-PERSON"/>
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
                                    <i class="fa fa-unlock-alt"></i> Personal
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
                            <input type="hidden" name="idPersonal" id="idPersonal" /> 
                            <select id="perfiles"></select>
                            <div style="text-align: right;margin-top: 5px;">
                                <!--<a class="k-button k-button-icontext k-grid-update" href="javascript:actualizar();"><span class="k-icon k-update"></span>Actualizar</a>
                                <a class="k-button k-button-icontext k-grid-cancel" href="javascript:cancelar();"><span class="k-icon k-cancel"></span>Cancelar</a>-->
                            </div>
                            <br/>
                        </div>
                    </div>
                </div>
        </div>
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            var ciudad;
            var estado;
            $(document).ready(function(){
                inicializar();    
                var dataSource = new kendo.data.DataSource({
                   transport: {
                     read:   {
                        url: "../../beaconsAgencias/Personal.do",
                        dataType: "json"
                     },
                     update: {
                        url: "../../beaconsAgencias/Personal.do?modo=editar",
                        type: "POST",
                        dataType: "json",
                        complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                        }
                     },
                     destroy: {
                         url: "../../beaconsAgencias/Personal.do?modo=eliminar",
                         type: "POST",
                         dataType: "json",
                         complete: function(e) {
                            if (typeof (e.responseText) !== "undefined")
                                verMensaje($.parseJSON(e.responseText));
                         } 
                      },
                      create: {
                          url: "../../beaconsAgencias/Personal.do?modo=nuevo",
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
                            id: "idPersonal",
                            fields: {
                                idPersonal:         { type: "number", editable: false, nullable: false },
                                idEstado:           { type: "string",editable: true},
                                idCiudad:           { type: "string",editable: true},
                                idOficina:          { field: "idOficina", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                idDepartamento:     { field: "idDepartamento", editable: true, defaultvalue: { id: "", text: "Seleccione una opción"}},
                                nombre:             { type: "string", validation: { required: true,maxlength:20 }},
                                apellidoPaterno:    { type: "string", validation: { required: true,maxlength:20 }},
                                apellidoMaterno:    { type: "string", validation: { required: false,maxlength:20 }},
                                email:              { type: "string", validation: { required: true,maxlength:30,email: true }},
                                calle:              { type: "string", validation: { required: true,maxlength:45 }},
                                colonia:            { type: "string", validation: { required: true,maxlength:45 }},
                                cpp:                { type: "number", validation: { required: true,maxlength:05, pattern : "\\d{5}" }},
                                numeroTelefono:     { type: "number", validation: { required: true,maxlength:10, pattern : "\\d{10}" }}
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
                            { field: "idPersonal",hidden:true},
                            { field: "idOficina",       title:"Oficina",width:"200px",editor:listaDropDownOficinas,template:"#=idOficina.text#"},
                            { field: "idDepartamento",  title:"Departamento",width:"200px",editor:listaDropDownDepartamento,template:"#=idDepartamento.text#"},
                            { field: "cpp",             title:"Cpp",encoded: false,width:"100px"},
                            { field: "idEstado",        title:"Estado",width:"150px"},
                            { field: "idCiudad",        title:"Ciudad",width:"150px"},
                            { field: "nombre",          title:"Nombre",encoded: false,width:"150px"},
                            { field: "apellidoPaterno", title:"Apellido paterno",encoded: false,width:"150px"},
                            { field: "apellidoMaterno", title:"Apellido materno",encoded: false,width:"150px"},
                            { field: "email",           title:"E-mail",encoded: false,width:"200px"},
                            { field: "calle",           title:"Calle",encoded: false,width:"200px"},
                            { field: "colonia",         title:"Colonia",encoded: false,width:"200px"},
                            { field: "numeroTelefono",  title:"Número de teléfono",encoded: false,width:"200px"},
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
                        editable: "inline",
                        detailExpand: function(e) {
                            this.collapseRow(this.tbody.find(' > tr.k-master-row').not(e.masterRow));
                        },
                        edit:function(e){
                            var listaDropDownDepartamento;
                            var listaDropDownOficinas =$('input[name="idOficina"]').kendoDropDownList({
                                dataTextField: "text",
                                dataValueField: "id",
                                dataSource: {
                                    transport: {
                                        read: {
                                            dataType: "json",
                                            url: "../../beaconsAgencias/oficinas.do?modo=combo"
                                        }
                                    }
                                },change:function(){
                                    listaDropDownDepartamento.dataSource.options.transport.read.url="../../beaconsAgencias/Departamentos.do?modo=combo&idOficina="+listaDropDownOficinas.value();
                                    listaDropDownDepartamento.dataSource.read();
                                },dataBound:function(){
                                    listaDropDownDepartamento=$('input[name="idDepartamento"]').kendoDropDownList({
                                        dataTextField: "text",
                                        dataValueField: "id",
                                        dataSource: {
                                            transport: {
                                                read: {
                                                    dataType: "json",
                                                    url: "../../beaconsAgencias/Departamentos.do?modo=combo&idOficina="+listaDropDownOficinas.value()
                                                }
                                            }
                                        }
                                    }).data("kendoDropDownList");
                                }
                            }).data("kendoDropDownList");
                            $('input[name = "idEstado"]').attr("disabled", true);
                            $('input[name = "idCiudad"]').attr("disabled", true);
                            
                            $('input[name = "cpp"]').blur(function (){
                                if($.trim($('input[name = "cpp"]').val())!=="")
                                    $.post("../../beaconsAgencias/Personal.do",{modo:"validaCpp",cpp:$.trim($('input[name = "cpp"]').val())},function(result){
                                        if(!result.success)  
                                        {
                                            $('input[name = "cpp"]').val("");
                                            $('input[name = "cpp"]').attr("placeholder",result.msg);
                                            alert(result.msg);
                                        } else {
                                            e.container.find('input[name="idEstado"]').val(result.estado).change();
                                            e.container.find('input[name="idCiudad"]').val(result.ciudad).change();
                                        }
                                    },"json");
                            });
                            
                        }
                        
                    }); 
                $(".k-grid-Limpiarfiltros").click(function(e){
                    $("#grid").data("kendoGrid").dataSource.filter([]);
                });
            });
            function listaDropDownOficinas(container, options){
                 $('<input required name="idOficina" id="idOficina"/>').appendTo(container);
            }
            function listaDropDownDepartamento(container, options){
                 $('<input required name="idDepartamento" id="idDepartamento"/>').appendTo(container);
            }
        </script>
    </body>
</html>

