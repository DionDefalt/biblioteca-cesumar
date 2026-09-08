package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável por abrir conexões com o banco de dados PostgreSQL (RDS)
 * e garantir que a tabela de projetos exista antes do sistema começar
 * a ser usado.
 *
 * As credenciais de conexão vêm de variáveis de ambiente (DB_HOST,
 * DB_PORT, DB_NAME, DB_USER, DB_PASSWORD), definidas no .env local ou
 * na Task Definition do ECS em produção — mesmo padrão usado no
 * projeto task-manager.
 */
public class ConexaoBanco {

    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "5432");
    private static final String DBNAME = System.getenv("DB_NAME");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static final String URL =
        "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DBNAME
        + "?sslmode=require";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Driver JDBC do PostgreSQL não encontrado. Verifique se a dependência "
                + "org.postgresql:postgresql está no pom.xml.", e
            );
        }
    }

    /**
     * Abre e retorna uma nova conexão com o banco de dados.
     * Cada método do DAO deve abrir sua própria conexão e fechá-la
     * logo em seguida (try-with-resources), evitando conexões presas.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cria a tabela de projetos (livros) caso ela ainda não exista.
     * Chamado uma vez na inicialização do DAO.
     */
    public static void inicializarBanco() {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id             SERIAL PRIMARY KEY,
                titulo         VARCHAR(255) NOT NULL,
                autor          VARCHAR(255) NOT NULL,
                ano_publicacao INT NOT NULL,
                isbn           VARCHAR(255) NOT NULL UNIQUE
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
