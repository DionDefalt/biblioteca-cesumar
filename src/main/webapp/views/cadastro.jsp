<%--
    cadastro.jsp — View do formulário de cadastro de projetos via Servlet.
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
    <title>Cadastrar Projeto — Biblioteca DionDefalt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

<!-- CABEÇALHO -->
<header>
    <div class="header-inner">
        <span class="header-icone">📚</span>
        <div class="header-titulo">
            <h1>Biblioteca DionDefalt</h1>
            <p>Catálogo de Projetos</p>
        </div>
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/livros">📋 Projetos</a></li>
            <li><a href="${pageContext.request.contextPath}/livros?acao=form" class="ativo">➕ Cadastrar Projeto</a></li>
        </ul>
    </nav>
</header>

<!-- CONTEÚDO PRINCIPAL -->
<main class="container">

    <h2 class="secao-titulo">➕ Cadastrar Novo Projeto</h2>

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

                <!-- Nome do projeto -->
                <div class="form-grupo full-width">
                    <label for="titulo">Nome do Projeto *</label>
                    <input type="text"
                           id="titulo"
                           name="titulo"
                           placeholder="Ex: Task Manager AWS"
                           value="${param.titulo}"
                           maxlength="200"
                           required>
                </div>

                <!-- Stack / tecnologias -->
                <div class="form-grupo full-width">
                    <label for="autor">Stack / Tecnologias *</label>
                    <input type="text"
                           id="autor"
                           name="autor"
                           placeholder="Ex: Node.js, Docker, ECS, RDS"
                           value="${param.autor}"
                           maxlength="150"
                           required>
                </div>

                <!-- Ano -->
                <div class="form-grupo">
                    <label for="anoPublicacao">Ano *</label>
                    <input type="number"
                           id="anoPublicacao"
                           name="anoPublicacao"
                           placeholder="Ex: 2026"
                           value="${param.anoPublicacao}"
                           min="1000"
                           max="2099"
                           required>
                    <small>Informe um ano entre 1000 e 2099.</small>
                </div>

                <!-- Link do projeto -->
                <div class="form-grupo">
                    <label for="isbn">Link do Projeto *</label>
                    <input type="text"
                           id="isbn"
                           name="isbn"
                           placeholder="Ex: https://taskmanager.diondefalt.dev"
                           value="${param.isbn}"
                           maxlength="500"
                           required>
                    <small>URL completa onde o projeto está publicado.</small>
                </div>

                <!-- Botões -->
                <div class="acoes-form">
                    <button type="submit" class="btn btn-primario">
                        ✅ Cadastrar Projeto
                    </button>
                    <a href="${pageContext.request.contextPath}/livros"
                       class="btn btn-secundario">
                        ← Voltar ao Catálogo
                    </a>
                </div>

            </div>
        </form>
    </div>

</main>

<footer>
    <p>Biblioteca DionDefalt &copy; 2026 — Sistema desenvolvido em
       <span>Java · Servlets · JSP · JSF</span></p>
</footer>

</body>
</html>
