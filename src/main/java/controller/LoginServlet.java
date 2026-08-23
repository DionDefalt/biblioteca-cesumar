package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Controller responsável pela autenticação da área
 * administrativa do catálogo (cadastro e exclusão de projetos).
 *
 * O login é feito através de um mini-formulário embutido diretamente
 * no cabeçalho de lista.jsp — não existe mais uma página de login
 * separada.
 *
 * Mapeamento de ações:
 *   GET  /login?acao=logout    → Encerra a sessão e volta ao catálogo
 *   GET  /login                → (fallback) apenas volta ao catálogo
 *   POST /login                → Processa o login enviado pelo mini-formulário
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("logout".equals(acao)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }

        // Não existe mais página de login separada: sempre volta ao catálogo.
        response.sendRedirect(request.getContextPath() + "/livros");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String senha   = request.getParameter("senha");

        String usuarioEsperado = System.getenv("ADMIN_USER");
        String senhaEsperada   = System.getenv("ADMIN_PASSWORD");

        boolean credenciaisValidas =
                usuarioEsperado != null && usuarioEsperado.equals(usuario) &&
                senhaEsperada   != null && senhaEsperada.equals(senha);

        if (credenciaisValidas) {
            HttpSession session = request.getSession(true);
            session.setAttribute("autenticado", true);
            session.setAttribute("usuarioLogado", usuario);
            response.sendRedirect(request.getContextPath() + "/livros");
        } else {
            // Redireciona de volta ao catálogo com um parâmetro de erro,
            // já que o formulário de login vive dentro de lista.jsp.
            response.sendRedirect(request.getContextPath() + "/livros?erroLogin=1");
        }
    }
}
