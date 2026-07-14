<%--
    lista.jsp — View de listagem dos livros cadastrados.
    Recebe o atributo "livros" (List<Livro>) do LivroServlet.
    Camada VIEW do padrão MVC.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acervo — Biblioteca Cesumar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

<!-- CABEÇALHO -->
<header>
    <div class="header-inner">
        <span class="header-icone">📚</span>
        <div class="header-titulo">
            <h1>Biblioteca Cesumar</h1>
            <p>Sistema de Gerenciamento do Acervo</p>
        </div>
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/livros" class="ativo">📋 Acervo</a></li>
            <li><a href="${pageContext.request.contextPath}/livros?acao=form">➕ Cadastrar Livro</a></li>
            <li><a href="${pageContext.request.contextPath}/cadastro.xhtml">🖊 Cadastro JSF</a></li>
        </ul>
    </nav>
</header>

<!-- CONTEÚDO PRINCIPAL -->
<main class="container">

    <!-- Mensagens de feedback ao usuário -->
    <c:if test="${param.sucesso == 'cadastrado'}">
        <div class="mensagem mensagem-sucesso">
            ✅ Livro cadastrado com sucesso no acervo!
        </div>
    </c:if>

    <c:if test="${param.sucesso == 'excluido'}">
        <div class="mensagem mensagem-sucesso">
            🗑️ Livro removido do acervo com sucesso.
        </div>
    </c:if>

    <c:if test="${param.erro == 'naoEncontrado'}">
        <div class="mensagem mensagem-erro">
            ❌ Livro não encontrado. Verifique o ID ou ISBN informado.
        </div>
    </c:if>

    <!-- Título da seção com contador -->
    <h2 class="secao-titulo">
        📋 Acervo de Livros
        <span class="badge-contador">${fn:length(livros)} livro(s)</span>
    </h2>

    <!-- Card principal com a tabela -->
    <div class="card">

        <c:choose>
            <c:when test="${empty livros}">
                <!-- Estado vazio -->
                <div class="tabela-vazia">
                    <p>📭 Nenhum livro cadastrado no acervo.</p>
                    <br>
                    <a href="${pageContext.request.contextPath}/livros?acao=form"
                       class="btn btn-primario">➕ Cadastrar primeiro livro</a>
                </div>
            </c:when>

            <c:otherwise>
                <!-- Tabela de livros -->
                <div class="tabela-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Título</th>
                                <th>Autor</th>
                                <th>Ano</th>
                                <th>ISBN</th>
                                <th>Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="livro" items="${livros}">
                                <tr>
                                    <td class="td-id">${livro.id}</td>
                                    <td><strong>${livro.titulo}</strong></td>
                                    <td>${livro.autor}</td>
                                    <td>${livro.anoPublicacao}</td>
                                    <td class="td-isbn">${livro.isbn}</td>
                                    <td>
                                        <%-- Formulário de exclusão por ID --%>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/livros"
                                              onsubmit="return confirm('Deseja remover o livro \'${livro.titulo}\' do acervo?');">
                                            <input type="hidden" name="acao" value="excluir">
                                            <input type="hidden" name="id"   value="${livro.id}">
                                            <button type="submit" class="btn btn-perigo">
                                                🗑️ Excluir
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Botão de ação -->
    <div style="margin-top: 8px;">
        <a href="${pageContext.request.contextPath}/livros?acao=form"
           class="btn btn-primario">➕ Cadastrar Novo Livro</a>
    </div>

</main>

<footer>
    <p>Biblioteca Cesumar &copy; 2025 — Sistema desenvolvido em
       <span>Java · Servlets · JSP · JSF</span></p>
</footer>

</body>
</html>
