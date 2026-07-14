<%-- 
    index.jsp — Página inicial que redireciona para a lista de livros.
    Ponto de entrada padrão da aplicação.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% response.sendRedirect(request.getContextPath() + "/livros"); %>
