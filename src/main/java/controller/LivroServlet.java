package controller;

import dao.LivroDAO;
import model.Livro;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet Controller responsável por gerenciar todas as requisições
 * relacionadas aos livros. Segue o padrão MVC como camada Controller.
 *
 * Mapeamento de ações:
 *   GET  /livros?acao=listar   → Exibe lista de livros
 *   GET  /livros?acao=form     → Exibe formulário de cadastro
 *   POST /livros?acao=cadastrar → Processa o cadastro
 *   POST /livros?acao=excluir  → Processa a exclusão
 */
@WebServlet("/livros")
public class LivroServlet extends HttpServlet {

    // Instância do DAO para acesso aos dados
    private LivroDAO livroDAO = new LivroDAO();

    /**
     * Trata requisições GET: listagem e exibição de formulário.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if (acao == null || acao.equals("listar")) {
            // Ação padrão: listar todos os livros
            listarLivros(request, response);

        } else if (acao.equals("form")) {
            // Exibir formulário de cadastro (JSP)
            request.getRequestDispatcher("/views/cadastro.jsp")
                   .forward(request, response);

        } else if (acao.equals("excluir")) {
            // Excluir por ID via GET (link direto da lista)
            excluirLivro(request, response);

        } else {
            // Ação desconhecida: redireciona para lista
            response.sendRedirect(request.getContextPath() + "/livros");
        }
    }

    /**
     * Trata requisições POST: cadastro e exclusão por formulário.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define encoding para suportar caracteres especiais (acentos)
        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");

        if ("cadastrar".equals(acao)) {
            cadastrarLivro(request, response);

        } else if ("excluir".equals(acao)) {
            excluirLivro(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/livros");
        }
    }

    // ---------------------------------------------------------------
    // Métodos privados de cada funcionalidade
    // ---------------------------------------------------------------

    /**
     * Lista todos os livros e encaminha para a view de listagem.
     */
    private void listarLivros(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Livro> livros = livroDAO.listarTodos();
        request.setAttribute("livros", livros);
        request.getRequestDispatcher("/views/lista.jsp")
               .forward(request, response);
    }

    /**
     * Valida e processa o cadastro de um novo livro.
     */
    private void cadastrarLivro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Captura os parâmetros do formulário
        String titulo        = request.getParameter("titulo");
        String autor         = request.getParameter("autor");
        String anoStr        = request.getParameter("anoPublicacao");
        String isbn          = request.getParameter("isbn");

        // --- Validações ---

        // Campos obrigatórios não podem ser vazios
        if (isNuloOuVazio(titulo) || isNuloOuVazio(autor) ||
            isNuloOuVazio(anoStr) || isNuloOuVazio(isbn)) {

            request.setAttribute("erro", "Todos os campos são obrigatórios.");
            request.getRequestDispatcher("/views/cadastro.jsp")
                   .forward(request, response);
            return;
        }

        // Ano deve ser um número inteiro válido
        int ano;
        try {
            ano = Integer.parseInt(anoStr.trim());
            if (ano < 1000 || ano > 2099) {
                throw new NumberFormatException("Ano fora do intervalo.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Ano de publicação inválido. Informe um ano entre 1000 e 2099.");
            request.getRequestDispatcher("/views/cadastro.jsp")
                   .forward(request, response);
            return;
        }

        // ISBN deve ter 10 ou 13 dígitos (apenas números e hifens)
        String isbnLimpo = isbn.replaceAll("[-\\s]", "");
        if (!isbnLimpo.matches("\\d{10}|\\d{13}")) {
            request.setAttribute("erro", "ISBN inválido. Informe 10 ou 13 dígitos numéricos.");
            request.getRequestDispatcher("/views/cadastro.jsp")
                   .forward(request, response);
            return;
        }

        // ISBN não pode ser duplicado
        if (livroDAO.isbnJaExiste(isbn.trim())) {
            request.setAttribute("erro", "Já existe um livro cadastrado com este ISBN.");
            request.getRequestDispatcher("/views/cadastro.jsp")
                   .forward(request, response);
            return;
        }

        // --- Cadastro ---
        Livro novoLivro = new Livro();
        novoLivro.setTitulo(titulo.trim());
        novoLivro.setAutor(autor.trim());
        novoLivro.setAnoPublicacao(ano);
        novoLivro.setIsbn(isbn.trim());

        livroDAO.cadastrar(novoLivro);

        // Redireciona para lista com mensagem de sucesso
        response.sendRedirect(request.getContextPath() + "/livros?sucesso=cadastrado");
    }

    /**
     * Processa a exclusão de um livro por ID ou ISBN.
     */
    private void excluirLivro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String isbn  = request.getParameter("isbn");
        boolean removido = false;

        if (!isNuloOuVazio(idStr)) {
            try {
                int id = Integer.parseInt(idStr.trim());
                removido = livroDAO.excluirPorId(id);
            } catch (NumberFormatException e) {
                // ID inválido: ignora e redireciona
            }
        } else if (!isNuloOuVazio(isbn)) {
            removido = livroDAO.excluirPorIsbn(isbn.trim());
        }

        if (removido) {
            response.sendRedirect(request.getContextPath() + "/livros?sucesso=excluido");
        } else {
            response.sendRedirect(request.getContextPath() + "/livros?erro=naoEncontrado");
        }
    }

    /**
     * Verifica se uma string é nula ou vazia.
     */
    private boolean isNuloOuVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
