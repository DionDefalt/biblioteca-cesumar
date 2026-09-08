package controller;

import dao.LivroDAO;
import model.Livro;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

/**
 * Managed Bean do JSF — compatível com JSF 4.0 + Tomcat 10 sem CDI.
 * Usa armazenamento na sessão HTTP manualmente.
 */
public class LivroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Livro livro = new Livro();
    private LivroDAO livroDAO = new LivroDAO();

    public String cadastrar() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (isNuloOuVazio(livro.getTitulo()) ||
            isNuloOuVazio(livro.getAutor())  ||
            isNuloOuVazio(livro.getIsbn())) {
            ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Erro", "Todos os campos são obrigatórios."));
            return null;
        }

        if (livro.getAnoPublicacao() < 1000 || livro.getAnoPublicacao() > 2099) {
            ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Erro", "Ano inválido. Informe entre 1000 e 2099."));
            return null;
        }

        String isbnLimpo = livro.getIsbn().replaceAll("[-\\s]", "");
        if (!isbnLimpo.matches("\\d{10}|\\d{13}")) {
            ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Erro", "ISBN inválido. Use 10 ou 13 dígitos."));
            return null;
        }

        if (livroDAO.isbnJaExiste(livro.getIsbn().trim())) {
            ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Erro", "ISBN já cadastrado."));
            return null;
        }

        livroDAO.cadastrar(livro);
        livro = new Livro();
        ctx.addMessage(null, new FacesMessage(
            FacesMessage.SEVERITY_INFO, "Sucesso", "Livro cadastrado com sucesso!"));
        return null;
    }

    public List<Livro> getLivros() {
        return livroDAO.listarTodos();
    }

    public String excluir(int id) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        boolean removido = livroDAO.excluirPorId(id);
        if (removido) {
            ctx.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Sucesso", "Livro removido."));
        }
        return null;
    }

    private boolean isNuloOuVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
}