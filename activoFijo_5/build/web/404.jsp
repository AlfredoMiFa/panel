<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<!--[if IE 8]><html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width" />
        <title>${initParam.appName} - página no encontrada</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/recursos/css/foundation/normalize.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/recursos/css/foundation/foundation.min.css" />
        <script src="${pageContext.request.contextPath}/recursos/js/foundation/custom.modernizr.js" type="text/javascript"></script>
    </head>
      <body>        
        <div class="row">
            <div class="large-5 large-centered columns">
                <br/><br/><br/>
                <img src="${pageContext.request.contextPath}/recursos/img/logo.png" alt="Logo administración"/>
                <div class="alert-box alert">
                    <p>
                        La página no ha sido encontrada o no existe, por favor verifique la dirección.
                    </p>
                </div>
                <a href="${pageContext.request.contextPath}/index.jsp" class="small button" target="_self">Volver a inicio</a>
            </div>
        </div>
      <script>
          document.write('<script src=${pageContext.request.contextPath}/recursos/js/foundation/'
            + ('__proto__' in {} ? 'zepto' : 'jquery')
            + '.js><\/script>');
       </script>
       <script type="text/javascript" src="${pageContext.request.contextPath}/recursos/js/foundation/foundation.min.js"></script>
       <script>
            $(document).foundation();
        </script>
    </body>
</html>