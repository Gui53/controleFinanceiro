package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável por criar o banco de dados e as tabelas do sistema caso
 * ainda não existam.
 *
 * @author Guilherme
 * @see java.sql.Connection
 */
public class ConfiguraBanco {

    /**
     * Inicializa o banco de dados e cria as tabelas necessárias caso ainda não
     * existam.
     *
     * @param user Usuário do banco de dados
     * @param password Senha do banco de dados
     */
    
    private static final String USER = "root";
    private static final String PASSWORD = "suaSenha";
    
    public static void inicializar() {
        String url = "jdbc:mysql://localhost:3306/?useTimezone=true&serverTimezone=UTC";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS db_controlefinanceiro");
            stmt.executeUpdate("USE db_controlefinanceiro");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS `db_controlefinanceiro`.`tb_movimentacao` (
                   id INT AUTO_INCREMENT PRIMARY KEY,
                      descricao VARCHAR(255) NOT NULL,
                      valor_unitario DECIMAL(10,2) NOT NULL,
                      quantidade INT NOT NULL,
                      data DATE NOT NULL,
                      categoria VARCHAR(50) NOT NULL
                )ENGINE = InnoDB
            """);

            System.out.println("Banco de dados inicializado com sucesso!");

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado: " + e);
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar banco: " + e);
        }
    }
}
