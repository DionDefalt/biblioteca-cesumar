package controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Filter que protege as ações administrativas do LivroServlet
 * (formulário de cadastro, cadastrar e excluir), exigindo sessão
 * autenticada. A listagem (acao=listar ou sem acao) permanece
 * pública, para que o catálogo continue visível a qualquer visitante
 * em modo somente-leitura.
 *
 * Cobre tanto o clique acidental num botão quanto uma chamada direta
 * à URL (ex: /livros?acao=excluir&id=1 digitado manualmente).
 */
@WebFilter("/livros")
public class LoginFilter implements Filter {

    private static final Set<String> ACOES_PROTEGIDAS = Set.of("form", "cadastrar", "excluir");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request   = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String acao = request.getParameter("acao");

        boolean acaoProtegida = acao != null && ACOES_PROTEGIDAS.contains(acao);

        if (!acaoProtegida) {
            // Listagem: sempre pública.
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean autenticado = session != null && Boolean.TRUE.equals(session.getAttribute("autenticado"));

        if (autenticado) {
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
