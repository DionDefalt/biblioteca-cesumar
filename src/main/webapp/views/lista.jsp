<%--
    lista.jsp — View de listagem dos projetos cadastrados.
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
    <title>Catálogo de Projetos — Biblioteca DionDefalt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <style>
        .header-inner { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px; }
        .login-box { display: flex; align-items: center; gap: 8px; margin-left: auto; }
        .login-form-mini { display: flex; align-items: center; gap: 6px; }
        .login-form-mini input {
            padding: 6px 10px; border-radius: 6px; border: 1px solid #ccc;
            font-size: 13px; width: 110px;
        }
        .btn-pequeno { padding: 6px 12px; font-size: 13px; }
        .login-erro-mini { color: #ff8080; font-size: 12px; margin-left: 8px; }
        .login-status { font-size: 13px; color: #fff; opacity: 0.9; }
        .td-somente-leitura { color: #999; }
        .contato-box { margin-top: 20px; text-align: center; padding: 24px; }
        .contato-box h3 { margin-bottom: 6px; }
        .contato-box p { color: #666; margin-bottom: 14px; }
    </style>
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

        <!-- Mini-formulário de login / status de sessão, sempre visível no cabeçalho -->
        <div class="login-box">
            <c:if test="${sessionScope.autenticado != true}">
                <form method="post" action="${pageContext.request.contextPath}/login" class="login-form-mini">
                    <input type="text" name="usuario" placeholder="Usuário" required>
                    <input type="password" name="senha" placeholder="Senha" required>
                    <button type="submit" class="btn btn-primario btn-pequeno">Entrar</button>
                </form>
                <c:if test="${param.erroLogin == '1'}">
                    <span class="login-erro-mini">❌ Usuário ou senha inválidos</span>
                </c:if>
            </c:if>
            <c:if test="${sessionScope.autenticado == true}">
                <span class="login-status">👤 ${sessionScope.usuarioLogado}</span>
                <a href="${pageContext.request.contextPath}/login?acao=logout" class="btn btn-secundario btn-pequeno">🔓 Sair</a>
            </c:if>
        </div>
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/livros" class="ativo">📋 Projetos</a></li>
            <c:if test="${sessionScope.autenticado == true}">
                <li><a href="${pageContext.request.contextPath}/livros?acao=form">➕ Cadastrar Projeto</a></li>
            </c:if>
        </ul>
    </nav>
</header>

<!-- CONTEÚDO PRINCIPAL -->
<main class="container">

    <!-- Mensagens de feedback ao usuário -->
    <c:if test="${param.sucesso == 'cadastrado'}">
        <div class="mensagem mensagem-sucesso">
            ✅ Projeto cadastrado com sucesso no catálogo!
        </div>
    </c:if>

    <c:if test="${param.sucesso == 'excluido'}">
        <div class="mensagem mensagem-sucesso">
            🗑️ Projeto removido do catálogo com sucesso.
        </div>
    </c:if>

    <c:if test="${param.erro == 'naoEncontrado'}">
        <div class="mensagem mensagem-erro">
            ❌ Projeto não encontrado. Verifique o ID ou link informado.
        </div>
    </c:if>

    <!-- Título da seção com contador -->
    <h2 class="secao-titulo">
        📋 Catálogo de Projetos
        <span class="badge-contador">${fn:length(livros)} projeto(s)</span>
    </h2>

    <!-- Card principal com a tabela -->
    <div class="card">

        <c:choose>
            <c:when test="${empty livros}">
                <!-- Estado vazio -->
                <div class="tabela-vazia">
                    <p>📭 Nenhum projeto cadastrado no catálogo.</p>
                    <br>
                    <c:if test="${sessionScope.autenticado == true}">
                        <a href="${pageContext.request.contextPath}/livros?acao=form"
                           class="btn btn-primario">➕ Cadastrar primeiro projeto</a>
                    </c:if>
                </div>
            </c:when>

            <c:otherwise>
                <!-- Tabela de projetos -->
                <div class="tabela-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Nº</th>
                                <th>Projeto</th>
                                <th>Stack</th>
                                <th>Link</th>
                                <c:if test="${sessionScope.autenticado == true}">
                                    <th>Ação</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="livro" items="${livros}" varStatus="status">
                                <tr>
                                    <td class="td-id">${status.count}</td>
                                    <td><strong>${livro.titulo}</strong></td>
                                    <td>${livro.autor}</td>
                                    <td class="td-isbn">
                                        <a href="${livro.isbn}" target="_blank" rel="noopener noreferrer">
                                            🔗 Abrir projeto
                                        </a>
                                    </td>
                                    <c:if test="${sessionScope.autenticado == true}">
                                        <td>
                                            <%-- Formulário de exclusão por ID --%>
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/livros"
                                                  onsubmit="return confirm('Deseja remover o projeto \'${livro.titulo}\' do catálogo?');">
                                                <input type="hidden" name="acao" value="excluir">
                                                <input type="hidden" name="id"   value="${livro.id}">
                                                <button type="submit" class="btn btn-perigo">
                                                    🗑️ Excluir
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Botão de ação -->
    <c:if test="${sessionScope.autenticado == true}">
        <div style="margin-top: 8px;">
            <a href="${pageContext.request.contextPath}/livros?acao=form"
               class="btn btn-primario">➕ Cadastrar Novo Projeto</a>
        </div>
    </c:if>

    <!-- Caixa de contato -->
    <div class="card contato-box">
        <h3>💬 Interessado em algum projeto?</h3>
        <p>Ficarei feliz em conversar sobre oportunidades, colaborações ou tirar dúvidas técnicas.</p>
        <a href="mailto:dionescavalcante88@gmail.com" class="btn btn-primario">
            ✉️ dionescavalcante88@gmail.com
        </a>
    </div>

</main>

<footer>
    <p>Biblioteca DionDefalt &copy; 2026 — Sistema desenvolvido em
       <span>Java · Servlets · JSP · JSF</span></p>
</footer>

</body>
</html>
