package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável por abrir conexões com o banco de dados SQLite e garantir
 * que a tabela de livros exista antes do sistema começar a ser usado.
 *
 * O banco inteiro fica em um único arquivo (biblioteca.db), criado
 * automaticamente na primeira execução — não é necessário instalar
 * nenhum servidor de banco de dados separado.
 */
public class ConexaoSQLite {

    private static final String URL = "jdbc:sqlite:biblioteca.db";

    /**
     * Abre e retorna uma nova conexão com o banco de dados.
     * Cada método do DAO deve abrir sua própria conexão e fechá-la
     * logo em seguida (try-with-resources), evitando conexões presas.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Cria a tabela de livros caso ela ainda não exista.
     * Chamado uma vez na inicialização do DAO.
     */
    public static void inicializarBanco() {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id             INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo         TEXT NOT NULL,
                autor          TEXT NOT NULL,
                ano_publicacao INTEGER NOT NULL,
                isbn           TEXT NOT NULL UNIQUE
            )
            """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar o banco de dados.", e);
        }
    }
}
