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
 * dos dados dos projetos listados no catálogo.
 *
 * Reaproveita o modelo original de "livro" reinterpretando os campos:
 * titulo = nome do projeto, autor = stack/tecnologias usadas,
 * anoPublicacao = ano do projeto, isbn = link ao vivo do projeto.
 *
 * Persistência via JDBC + PostgreSQL (RDS). Todas as consultas usam
 * PreparedStatement, o que evita SQL Injection (os valores enviados
 * pelo usuário nunca são concatenados diretamente na string do SQL).
 */
public class LivroDAO {

    static {
        ConexaoBanco.inicializarBanco();
        semearDadosIniciais();
    }

    /**
     * Insere os projetos de exemplo apenas se o banco estiver vazio
     * (evita duplicar os dados toda vez que o servidor reinicia).
     */
    private static void semearDadosIniciais() {
        LivroDAO dao = new LivroDAO();
        if (dao.listarTodos().isEmpty()) {
            dao.cadastrar(new Livro(0,
                "Task Manager AWS",
                "Node.js, Express, PostgreSQL, Docker, ECS, ECR, RDS",
                2026,
                "https://taskmanager.dionesdev.com.br"));
            dao.cadastrar(new Livro(0,
                "Mural de Recados",
                "Node.js, Docker",
                2026,
                "https://mural-recado.onrender.com/"));
        }
    }

    /**
     * Cadastra um novo projeto no catálogo.
     * O id é gerado automaticamente pelo PostgreSQL (SERIAL).
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
            throw new RuntimeException("Erro ao cadastrar projeto.", e);
        }
    }

    /**
     * Retorna todos os projetos cadastrados, ordenados por id.
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
            throw new RuntimeException("Erro ao listar projetos.", e);
        }

        return livros;
    }

    /**
     * Remove um projeto do catálogo pelo ID.
     * @return true se algum registro foi removido, false se não encontrado
     */
    public boolean excluirPorId(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir projeto.", e);
        }
    }

    /**
     * Remove um projeto do catálogo pelo link (antigo "ISBN").
     * @return true se algum registro foi removido, false se não encontrado
     */
    public boolean excluirPorIsbn(String isbn) {
        String sql = "DELETE FROM livros WHERE isbn = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir projeto.", e);
        }
    }

    /**
     * Verifica se já existe um projeto cadastrado com o mesmo link.
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
            throw new RuntimeException("Erro ao verificar link do projeto.", e);
        }
    }

    /**
     * Busca um único projeto pelo ID.
     * @return o projeto encontrado, ou null se não existir
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
            throw new RuntimeException("Erro ao buscar projeto.", e);
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
