var approot="/beaconsAgencia/";
var notification;
function inicializar()
{
    resizePageWrapper();
     kendo.culture("es-MX");
    notification = $("#notification").kendoNotification({
        position: {
            pinned: true,
            top: 30,
            right: 30
        },
        stacking: "down",
        templates: [{
            type: "msg-info",
            template: $("#infoTemplate").html()
        }, {
            type: "msg-error",
            template: $("#errorTemplate").html()
        }, {
            type: "msg-success",
            template: $("#successTemplate").html()
        }]
    }).data("kendoNotification");
    
                  
    $("#side-menu").kendoPanelBar({
        expandMode: "single"
    });

    $("#relogin").kendoWindow({
        height: "180px",
        title: "Su sesion a caducado",
        modal: true,
        resizable:false,
        visible:false,
        width: "230px"
    }).data("kendoWindow");
    
    
    $.post(approot+"catalogos/generals/mensaje.do",{ modo:'nmensajes'},function(result){
        if (result.success){
            var html="";
            if(result.total>0){
                $("#asBuzon").css("color","red");
                $.each(result.data, function( index, value ) {                    
                    html+="<li class='message-preview'><a href='#'><div class='media'>"+
                            "<h5 class='media-heading'><i class='fa fa-clock-o'></i> <strong>"+value.fecha+"</strong></h5>"+
                                "<p>"+value.titulo+"</p></div></a></li>";
                 });
            }
            html+="<li class='message-footer'><a href='"+approot+"catalogos/general/mensaje.jsp'>Leer todos los mensajes</a></li>";
            $("#caBuzon").html(html);
        }else
            verMensaje(result)
    },'json');
    $.post(approot+"catalogos/generals/aviso.do",{ modo:'navisos'},function(result){
        if (result.success){
            var html="";
            if(result.total>0){
                $("#asAvisos").css("color","red");
                $.each(result.data, function( index, value ) {   
                    var clase="default";
                    if(value.prioridad==='ME')
                        clase="warning";
                    else if(value.prioridad==='ME')
                        clase="danger";
                    html+="<li><a href='#'><span class='label label-"+clase+"'>"+
                            ""+value.titulo+"</span></a></li>";
                 });
            }
            html+="<li><a href='"+approot+"catalogos/general/aviso.jsp'>Leer todos los avisos</a></li>";
            $("#caAvisos").html(html);
        }else
            verMensaje(result)
    },'json');
}
function resizePageWrapper(){    
    if(window.innerHeight-128>300)
        $("#row-bootstrapkendo-wrapper").height(window.innerHeight-128);
}

$(window).bind("resize", function() {
    resizePageWrapper();
    if($("#grid").length>0){    
        resizeGrid("#grid");
    }    
    if($("#tabstripauto").length>0){
        var tabStripElement = $("#tabstrip").kendoTabStrip();
        expandContentDivs(tabStripElement.children(".k-content"),tabStripElement); 
    }
});

function resizeGrid(gridSelector) {
    var element = $(gridSelector),
        dataArea = element.find('.k-grid-content'),
        elementHeight = element.innerHeight(),
        otherElements = element.children().not('.k-grid-content'),
        otherElementsHeight = 0;
    otherElements.each(function () {
        otherElementsHeight += $(this).outerHeight();
    });
    dataArea.height(elementHeight - otherElementsHeight);
}
var expandContentDivs = function(divs,tabStripElement) { 
    divs.height(tabStripElement.innerHeight() - tabStripElement.children(".k-tabstrip-items").outerHeight() - 16);
} 

var aestatus = [{"value": "AC","text": "Activo"},{"value": "IN","text": "Inactivo"}];
var aestatusmensaje = [{"value": "AC","text": "Enviado"},{"value": "IN","text": "Recibido"}];
var aestatusc = [{"value": "LI","text": "Listo"},{"value": "PE","text": "Pendiente"}];
var atipovalidacion = [{"value": "CLASE","text": "Método de clase"},{"value": "REGEX","text": "Regex"},{"value": "RANGO","text": "Rango"},{"value": "SQL","text": "SQL query"}];
var acategoria = [{"value": "FTP","text": "Ftp"},{"value": "VNC","text": "VNC"},{"value":"CAMARA","text":"CAMARA"}];
var aestatusp = [{"value": "PR","text": "Pruebas"},{"value": "PD","text": "Produccion"},{"value": "IN","text": "Inactivo"}];
var aconfirmar = [{"value": "S","text": "Si"},{"value": "N","text": "No"}];
var amenu = [{"value": "SIDE","text": "Menu lateral"},{"value": "TOP","text": "Menu superior"}];
var areporte = [{"value": "RE","text": "Reporte"}];
var ametodo = [{"value": "POST","text": "POST"},{"value": "GET","text": "GET"}];
var atipodato = [{"value": "String","text": "String"},{"value": "Integer","text": "Integer"},{"value": "Double","text": "Double"},{"value": "Boolean","text": "Boolean"},{"value": "Date","text": "Date"}];
var aestilo = [{"value": "black","text": "black"},{"value": "blueopal","text": "blueopal"},{"value": "bootstrap","text": "bootstrap"},{"value": "default","text": "default"},{"value": "flat","text": "flat"},
{"value": "highcontrast","text": "highcontrast"},{"value": "material","text": "material"},{"value": "materialblack","text": "materialblack"},{"value": "metro","text": "metro"},{"value": "metroblack","text": "metroblack"},
{"value": "moonlight","text": "moonlight"},{"value": "silver","text": "silver"},{"value": "uniform","text": "uniform"}];
var atarget=[{"value": "_blank","text": "_blank"},{"value": "_self","text": "_self"}];


function verMensaje(response)
{
    if(response.errors!=null)
        errorHandler(response);
    else{
        if(response.success)
            notification.show({title: response.title,message: response.msg}, "msg-success"); 
        else      
            notification.show({title: response.title,message: response.msg}, "msg-error");    
    }
}

function errorHandler(e)
{
    if(e.errors=="901")
    {                        
        var winlog = $("#relogin").data("kendoWindow");
        winlog.center();
        winlog.open();
    }
    else
        notification.show({title: "Error: ",message: "Error en la capa del controlador"}, "msg-error");
}
function relogin()
{
    if($("#logform").kendoValidator().data("kendoValidator").validate())
    {
        $.post($('#logform').attr( 'action' ), $('#logform').serialize(),function(result){
            if (result.success)               
                location.reload();
             else                 
                notification.show({title: "New E-mail",message: result.msg}, "msg-error");
        },'json');
    }
}
function disabledBackSpace()
{      
    document.onkeydown = function(){ 
        //116->f5
        //122->f11
        if (window.event && (window.event.keyCode == 122 || window.event.keyCode == 116)){
            window.event.keyCode = 505; 
        }
        
        if (window.event.keyCode == 505){ 
            return false; 
        } 
        
        if (window.event && (window.event.keyCode == 8))
        {
            valor = document.activeElement.value;
            if (valor==undefined) { return false; } //Evita Back en página.
            else
            {
                if (document.activeElement.getAttribute('type')=='select-one')
                    { return false; } //Evita Back en select.
                if (document.activeElement.getAttribute('type')=='button')
                    { return false; } //Evita Back en button.
                if (document.activeElement.getAttribute('type')=='radio')
                    { return false; } //Evita Back en radio.
                if (document.activeElement.getAttribute('type')=='checkbox')
                    { return false; } //Evita Back en checkbox.
                if (document.activeElement.getAttribute('type')=='file')
                    { return false; } //Evita Back en file.
                if (document.activeElement.getAttribute('type')=='reset')
                    { return false; } //Evita Back en reset.
                if (document.activeElement.getAttribute('type')=='submit')
                    { return false; } //Evita Back en submit.
                else //Text, textarea o password
                {
                    if (document.activeElement.value.length==0)
                        { return false; } //No realiza el backspace(largo igual a 0).
                    else
                        { document.activeElement.value.keyCode = 8; } //Realiza el backspace.
                }
            }
        }
    }
//end desabilitar teclas
}
$( document ).ajaxStart(function() {
    var createBtn = $("#page-wrapper");
    $("<div id='loading-gn'>").css({
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
    kendo.ui.progress($("#loading-gn"), true);
});
$( document ).ajaxStop(function() {
    $('#loading-gn').remove();
});


function inspeccionar(obj)
{
  var msg = '';
  for (var property in obj)
  {
    if (typeof obj[property] == 'function')
    {
      var inicio = obj[property].toString().indexOf('function');
      var fin = obj[property].toString().indexOf(')')+1;
      var propertyValue=obj[property].toString().substring(inicio,fin);
      msg +=(typeof obj[property])+' '+property+' : '+propertyValue+' ; - ';
    }
    else if (typeof obj[property] == 'unknown')
    {
      msg += 'unknown '+property+' : unknown ; - ';
    }
    else
    {
      msg +=(typeof obj[property])+' '+property+' : '+obj[property]+' ; - ';
    }
  }
  return msg;
}