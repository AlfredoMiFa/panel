<%@page contentType="text/html;charset=UTF-8"%>
        <span id="notification" style="display:none;"></span>
        <div id="relogin" style="display: none;">
            <form id="logform" name="logform" action="${pageContext.request.contextPath}/acceso.do">
                <input type="hidden" name="modo" value="login" />
                <div class="logforms k-block">
                    <li><input type="text" class="k-textbox"  id="logUsuario" name="logUsuario"  placeholder="Nombre de usuario"  required /></li>
                    <li><input type="password"  id="logContrasena" name="logContrasena" class="k-textbox"  placeholder="Contraseña"  required /></li>
                    <li><a href="javascript:relogin()" class="k-button">Autenticar</a></li>
                </div>
            </form>
        </div>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/jquery.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/jszip.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/kendo.all.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/kendo.culture.es-MX.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/kendo.messages.es-ES.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/css/bootstrap/bootstrap.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/recursos/js/core.js"></script>
        <script id="infoTemplate" type="text/x-kendo-template">
            <div class="msg-info">
                <img src="${pageContext.request.contextPath}/recursos/img/envelope.png" />
                <h3>#= title #</h3>
                <p>#= message #</p>
            </div>
        </script>

        <script id="errorTemplate" type="text/x-kendo-template">
            <div class="msg-error">
                <img src="${pageContext.request.contextPath}/recursos/img/error-icon.png" />
                <h3>#= title #</h3>
                <p>#= message #</p>
            </div>
        </script>

        <script id="successTemplate" type="text/x-kendo-template">
            <div class="msg-success">
                <img src="${pageContext.request.contextPath}/recursos/img/success-icon.png" />
                <h3>#= title #</h3>
                <p>#= message #</p>
            </div>
        </script>