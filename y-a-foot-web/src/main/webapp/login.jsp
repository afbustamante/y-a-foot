<%@ page import="net.andresbustamante.yafoot.util.MessagesProperties" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% Locale locale = pageContext.getRequest().getLocale(); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><%= MessagesProperties.getValue("application.title", locale)%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=1"/>
    <link href="<%= config.getServletContext().getContextPath()%>/css/cssLayout.css" type="text/css"/>
    <link href="<%= config.getServletContext().getContextPath()%>/css/default.css" type="text/css"/>
</head>
<body>
<div>
    <form id="login" action="j_security_check" method="post">
        <div>
            <table class="form">
                <tr>
                    <td><label for="j_username"><%= MessagesProperties.getValue("login.username", locale)%>
                    </label></td>
                    <td><input id="j_username" type="text" name="j_username"/></td>
                </tr>
                <tr>
                    <td><label for="j_password"><%= MessagesProperties.getValue("login.password", locale)%>
                    </label></td>
                    <td><input id="j_password" type="password" name="j_password"/></td>
                </tr>
            </table>

            <table>
                <tr>
                    <td>
                        <input type="submit" value="<%= MessagesProperties.getValue("login.validate", locale)%>"
                               onclick="document.forms['login'].submit();"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>
</body>
</html>
