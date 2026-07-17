package dao;

import model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) responsável pelo acesso e manipulação
 * dos dados dos livros.
 *
 * Versão com persistência real em banco de dados via JDBC + SQLite.
 * Todas as consultas usam PreparedStatement, o que evita SQL Injection
 * (os valores enviados pelo usuário nunca são concatenados diretamente
 * na string do SQL).
 */
public class LivroDAO {

    static {
        ConexaoBanco.inicializarBanco();
        semearDadosIniciais();
    }

    /**
     * Insere os três livros de exemplo apenas se o banco estiver vazio
     * (evita duplicar os dados toda vez que o servidor reinicia).
     */
    private static void semearDadosIniciais() {
        LivroDAO dao = new LivroDAO();
        if (dao.listarTodos().isEmpty()) {
            dao.cadastrar(new Livro(0, "Clean Code", "Robert C. Martin", 2008, "9780132350884"));
            dao.cadastrar(new Livro(0, "Design Patterns", "Gang of Four", 1994, "9780201633610"));
            dao.cadastrar(new Livro(0, "Java Efetivo", "Joshua Bloch", 2018, "9788550804606"));
        }
    }

    /**
     * Cadastra um novo livro no banco de dados.
     * O id é gerado automaticamente pelo SQLite (AUTOINCREMENT).
     */
    public void cadastrar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, isbn) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setString(4, livro.getIsbn());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar livro.", e);
        }
    }

    /**
     * Retorna todos os livros cadastrados, ordenados por id.
     */
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, ano_publicacao, isbn FROM livros ORDER BY id";

        try (Connection conn = ConexaoBanco.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros.", e);
        }

        return livros;
    }

    /**
     * Remove um livro do banco pelo ID.
     * @return true se algum registro foi removido, false se não encontrado
     */
    public boolean excluirPorId(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir livro.", e);
        }
    }

    /**
     * Remove um livro do banco pelo ISBN.
     * @return true se algum registro foi removido, false se não encontrado
     */
    public boolean excluirPorIsbn(String isbn) {
        String sql = "DELETE FROM livros WHERE isbn = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir livro.", e);
        }
    }

    /**
     * Verifica se já existe um livro cadastrado com o ISBN informado.
     * (A coluna isbn também tem uma restrição UNIQUE no banco, como
     * segunda camada de proteção contra duplicatas.)
     */
    public boolean isbnJaExiste(String isbn) {
        String sql = "SELECT 1 FROM livros WHERE isbn = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar ISBN.", e);
        }
    }

    /**
     * Busca um único livro pelo ID.
     * @return o livro encontrado, ou null se não existir
     */
    public Livro buscarPorId(int id) {
        String sql = "SELECT id, titulo, autor, ano_publicacao, isbn FROM livros WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearLivro(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro.", e);
        }
    }

    /**
     * Converte uma linha do ResultSet em um objeto Livro.
     */
    private Livro mapearLivro(ResultSet rs) throws SQLException {
        return new Livro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getInt("ano_publicacao"),
            rs.getString("isbn")
        );
    }
}
