<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if IE 8]><html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - Actualizar Contraseña</title>
        <link rel="stylesheet" href="recursos/css/foundation/normalize.css" />
        <link rel="stylesheet" href="recursos/css/foundation/foundation.min.css" />
        <script src="recursos/js/foundation/custom.modernizr.js" type="text/javascript"></script>
    </head>
      <body>        
        <div class="row">
            <div class="large-5 large-centered columns">
                <br/><br/><br/>
                <img src="recursos/img/logo.png" alt="Logo administración"/>
                <div id="error" class="alert-box alert" style="display: none;">
                    <p>${param.msg}</p>
                </div>
                <div class="panel">
                    <form name="form" id="form" action="actualizarContrasena.do" method="post">
                        <p>
                            <label for="user_login">Nombre de usuario<br>
                            <input type="hidden" name="modo" id="modo" value="login">
                            <input type="text" name="logUsuario" id="logUsuario" value="${param.logUsuario}" class="input" readonly="readonly" required="true" size="20"></label>
                        </p>
                        <p>
                            <label for="user_pass">Contraseña actual<br>
                            <input type="password" name="logContrasenaActual" id="logContrasenaActual" class="input" required="true" size="20"></label>
                        </p>                        
                        <p>
                            <label for="user_pass">Nueva contraseña<br>
                            <input type="password" name="logContrasenaNueva" id="logContrasenaNueva" class="input" required="true" size="20"></label>
                        </p>
                        <p class="submit">
                            <input type="submit" name="wp-submit" id="wp-submit" class="small button" value="Actualizar">
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
                $("#form").validate();
                if("${param.msg}"!="")
                    $("#error").show();
            });
            $(document).foundation();
        </script>
    </body>
</html>