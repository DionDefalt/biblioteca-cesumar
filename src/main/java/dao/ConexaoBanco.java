package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável por abrir conexões com o banco de dados H2 e garantir
 * que a tabela de livros exista antes do sistema começar a ser usado.
 *
 * O banco inteiro fica em um único arquivo (biblioteca.mv.db), criado
 * automaticamente na primeira execução — não é necessário instalar
 * nenhum servidor de banco de dados separado.
 *
 * Usamos o H2 (em vez do SQLite) porque o H2 é escrito inteiramente em
 * Java, sem depender de bibliotecas nativas do sistema operacional —
 * isso evita problemas de carregamento de driver dentro de servidores
 * como o Tomcat, especialmente em pastas sincronizadas (OneDrive) ou
 * com caracteres acentuados no caminho.
 */
public class ConexaoBanco {

    private static final String URL = "jdbc:h2:./biblioteca";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Driver JDBC do H2 não encontrado. Verifique se a dependência "
                + "com.h2database:h2 está no pom.xml.", e
            );
        }
    }

    /**
     * Abre e retorna uma nova conexão com o banco de dados.
     * Cada método do DAO deve abrir sua própria conexão e fechá-la
     * logo em seguida (try-with-resources), evitando conexões presas.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    /**
     * Cria a tabela de livros caso ela ainda não exista.
     * Chamado uma vez na inicialização do DAO.
     */
    public static void inicializarBanco() {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id             INT AUTO_INCREMENT PRIMARY KEY,
                titulo         VARCHAR(255) NOT NULL,
                autor          VARCHAR(255) NOT NULL,
                ano_publicacao INT NOT NULL,
                isbn           VARCHAR(20) NOT NULL UNIQUE
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
