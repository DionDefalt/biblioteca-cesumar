<%--
    cadastro.jsp — View do formulário de cadastro de livros via Servlet.
    Os dados são enviados via POST para o LivroServlet.
    Camada VIEW do padrão MVC.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastrar Livro — Biblioteca Cesumar</title>
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
            <li><a href="${pageContext.request.contextPath}/livros">📋 Acervo</a></li>
            <li><a href="${pageContext.request.contextPath}/livros?acao=form" class="ativo">➕ Cadastrar Livro</a></li>
            <li><a href="${pageContext.request.contextPath}/cadastro.xhtml">🖊 Cadastro JSF</a></li>
        </ul>
    </nav>
</header>

<!-- CONTEÚDO PRINCIPAL -->
<main class="container">

    <h2 class="secao-titulo">➕ Cadastrar Novo Livro</h2>

    <!-- Exibe mensagem de erro de validação, se houver -->
    <c:if test="${not empty erro}">
        <div class="mensagem mensagem-erro">
            ❌ ${erro}
        </div>
    </c:if>

    <!-- Card do formulário -->
    <div class="card">

        <%-- Formulário envia POST para o LivroServlet com acao=cadastrar --%>
        <form method="post"
              action="${pageContext.request.contextPath}/livros"
              novalidate>

            <input type="hidden" name="acao" value="cadastrar">

            <div class="form-grid">

                <!-- Título -->
                <div class="form-grupo full-width">
                    <label for="titulo">Título do Livro *</label>
                    <input type="text"
                           id="titulo"
                           name="titulo"
                           placeholder="Ex: O Senhor dos Anéis"
                           value="${param.titulo}"
                           maxlength="200"
                           required>
                </div>

                <!-- Autor -->
                <div class="form-grupo full-width">
                    <label for="autor">Autor *</label>
                    <input type="text"
                           id="autor"
                           name="autor"
                           placeholder="Ex: J.R.R. Tolkien"
                           value="${param.autor}"
                           maxlength="150"
                           required>
                </div>

                <!-- Ano de publicação -->
                <div class="form-grupo">
                    <label for="anoPublicacao">Ano de Publicação *</label>
                    <input type="number"
                           id="anoPublicacao"
                           name="anoPublicacao"
                           placeholder="Ex: 1954"
                           value="${param.anoPublicacao}"
                           min="1000"
                           max="2099"
                           required>
                    <small>Informe um ano entre 1000 e 2099.</small>
                </div>

                <!-- ISBN -->
                <div class="form-grupo">
                    <label for="isbn">ISBN *</label>
                    <input type="text"
                           id="isbn"
                           name="isbn"
                           placeholder="Ex: 978-8533615540"
                           value="${param.isbn}"
                           maxlength="20"
                           required>
                    <small>ISBN com 10 ou 13 dígitos (hifens são aceitos).</small>
                </div>

                <!-- Botões -->
                <div class="acoes-form">
                    <button type="submit" class="btn btn-primario">
                        ✅ Cadastrar Livro
                    </button>
                    <a href="${pageContext.request.contextPath}/livros"
                       class="btn btn-secundario">
                        ← Voltar ao Acervo
                    </a>
                </div>

            </div>
        </form>
    </div>

    <!-- Informação sobre o formulário JSF -->
    <div class="card" style="background: #eef3f9; border-left: 4px solid #1a3a5c;">
        <p style="color: #1a3a5c; font-size: 0.9rem;">
            🖊 <strong>Prefere usar o JSF?</strong>
            <a href="${pageContext.request.contextPath}/cadastro.xhtml"
               style="color: #c8a96e; font-weight: 600; text-decoration: none;">
               Clique aqui para o formulário JSF
            </a>
            — ambos realizam o mesmo cadastro.
        </p>
    </div>

</main>

<footer>
    <p>Biblioteca Cesumar &copy; 2025 — Sistema desenvolvido em
       <span>Java · Servlets · JSP · JSF</span></p>
</footer>

</body>
</html>
