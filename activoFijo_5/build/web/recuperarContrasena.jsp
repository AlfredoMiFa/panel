<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if IE 8]><html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - Recuperar Contraseña</title>
        <link rel="stylesheet" href="recursos/css/foundation/normalize.css" />
        <link rel="stylesheet" href="recursos/css/foundation/foundation.min.css" />
        <script src="recursos/js/foundation/custom.modernizr.js" type="text/javascript"></script>
    </head>
      <body>        
        <div class="row">
            <div class="large-5 large-centered columns">
                <br/><br/><br/>
                <img src="recursos/img/logoActivo1.jpg" alt="Logo administración"/>
                <div class="panel callout radius">
                    <p>
                        Por favor, escribe tu correo electrónico. 
                        Recibirás un enlace para crear la contraseña nueva por correo electrónico.
                    </p>
                </div>
                <div class="panel">
                    <form name="form" id="form" action="recuperarContrasena.do" method="post">
                        <p>
                            <label for="user_login">Correo electrónico<br>
                            <input type="email" name="email" id="email" required="true" size="20"></label>
                        </p>
                        <p class="submit">
                            <a  href="javascript:validaEmail()"  name="submit" id="submit" class="small button">Recuperar contraseña</a>
                            <a href="index.jsp" class="small button secondary" target="_self">Acceder</a>
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
            function validaEmail(){
                if($("#email").valid())
                {
                    $email=$("#email").val();
                    $.post("recuperarContrasena.do",
                        {email:$("#email").val()},
                         function(data) {
                            alert(data.msg);
                        },"json");
                }
            }
            $(document).foundation();
        </script>
    </body>
</html>