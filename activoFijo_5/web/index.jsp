<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if IE 6]> <div class='alert-box alert' style="padding-5px;text-align:center;"><table><tr><td><b>Nota importante:</b> Te recomendamos actualizar tu navegador, para tener una navegaci&oacute;n correcta del sitio.<br/> 
O puedes descargar la &uacute;ltima versi&oacute;n de: </td><td>
<a href='https://www.google.com/chrome'><img src='${pageContext.request.contextPath}/recursos/img/chrome.png' alt='chrome' style='vertical-align:middle;border:0px;'/></a>
<a href='http://www.apple.com/safari/download/'><img src='${pageContext.request.contextPath}/recursos/img/safari.png' alt='safari' style='vertical-align:middle;border:0px;'/></a>
<a href='http://www.mozilla-europe.org/es/firefox/'><img src='${pageContext.request.contextPath}/recursos/img/firefox.png' alt='FireFox' style='vertical-align:middle;border:0px;'/></td></tr></table></div><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>Acceso</title>
        <link rel="stylesheet" href="recursos/css/foundation/normalize.css" />
        <link rel="stylesheet" href="recursos/css/foundation/foundation.min.css" />
        <script src="recursos/js/foundation/custom.modernizr.js" type="text/javascript"></script>
    </head>
      <body>        
        <div class="row">
            <div class="large-5 large-centered columns">
                <br/><br/><br/>
               <img src="recursos/img/logoActivo1.jpg" alt="Logo administración"/>
                <div id="error" class="alert-box alert" style="display: none;">
                    <p>${param.msg}</p>
                </div>
                <div class="panel">
                    <form name="loginform" id="loginform" action="acceso.do" method="post">
                        <p>
                            <label for="user_login">Nombre de usuario<br>
                            <input type="hidden" name="modo" id="modo" value="login">
                            <input type="text" name="logUsuario" id="logUsuario" class="input" required="true" size="20"></label>
                        </p>
                        <p>
                            <label for="user_pass">Contraseña<br>
                            <input type="password" name="logContrasena" id="logContrasena" class="input" required="true" size="20"></label>
                        </p>
                        <p class="submit">
                            <input type="submit" name="wp-submit" id="wp-submit" class="small button" value="Acceder">
                            <a href="recuperarContrasena.jsp" class="small button secondary" target="_self">Recuperar contraseña</a>
                        </p>
                    </form>
                </div>
            </div>
        </div>
      <!--<script>
          document.write('<script src=recursos/js/foundation/'
            + ('__proto__' in {} ? 'zepto' : 'jquery')
            + '.js><\/script>');
       </script>-->
       <script type="text/javascript" src="recursos/js/foundation/jquery.js"></script>
       <script type="text/javascript" src="recursos/js/foundation/jquery.validate.min.js"></script>
       <script type="text/javascript" src="recursos/js/foundation/foundation.min.js"></script>
       <script>
            $(document).ready(function(){
                $("#loginform").validate();
                if("${param.msg}"!="")
                    $("#error").show();
            });
            $(document).foundation();
        </script>
    </body>
</html>