<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<html lang="us">
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>Escritorio</title>
        <jsp:include page="/WEB-INF/css-script.jsp"></jsp:include>
    </head>
    <body>    
        <div id="wrapper">
            <!-- Navigation -->
            <nav class="navbar navbar-brand navbar-fixed-top" role="navigation">            
                <jsp:include page="/WEB-INF/header.jsp"></jsp:include>         
                <jsp:include page="/menu.do">
                    <jsp:param name="M_SELECT" value="DASHBOARD"/>
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
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Dashboard <small>información de la aplicación</small>
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="fa fa-dashboard"></i> Dashboard
                                </li>
                            </ol>
                        </div>
                    </div>
                         
                    <jsp:include page="/escritorio.view">
                        <jsp:param name="modo" value="escritorio"/>
                    </jsp:include>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->
        <jsp:include page="/WEB-INF/footer.jsp"></jsp:include>
        <script>
            $(document).ready(function(){
                inicializar();
                $.each(arrayData, function(i, item) {
                    loaddashboard(item.tipo,item.id,item.cveDatasource,item.parametros);
                });
            });
            function loaddashboard(tipo,id,cveDatasource,parametros){
                if(tipo==="TAB"){
                    $.post("../catalogos/generals/datasource.do",{modo:"probarQuery",IDC:id,cveDatasource:cveDatasource},function(result){
                        if(result.errors!=null)
                            errorHandler(result);
                        else
                        {
                            $("#"+tipo+id).html(result.data);
                            $("#gridTAB"+id).kendoGrid({sortable: true,resizable:true});
                        }
                    },"json");
                }else if(tipo==="GRA"){
                    $.post("../catalogos/generals/datasource.do", {modo:'graficar',cveDatasource:cveDatasource,parametros:parametros},function(result){                            
                        if(result.success)
                            createChart(tipo+id,result);
                        else 
                            alert("Se ha generado un error. En realizar la consulta.");                            
                    },'json');
                }
            }
            function createChart(id,result) {
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
                
                $("#"+id).kendoChart({
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
    </body>
</html>