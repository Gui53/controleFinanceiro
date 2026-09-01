package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por realizar e armazenar a conexão com o banco de dados
 * MySQL.
 *
 * @author Guilherme
 * @see java.sql.Connection
 */
public class Conexao {

    /**
     * Instância única da conexão com o banco.
     */
    private static Connection instancia = null;

    private static final String USER = "root";
    private static final String PASSWORD = "suaSenha";

    /**
     * Inicializa a conexão com o banco de dados utilizando usuário e senha
     * informados.
     *
     * @param user Usuário do banco de dados
     * @param password Senha do banco de dados
     * @return boolean Retorna true caso a conexão seja realizada com sucesso e
     * false caso ocorra erro
     */
    public static boolean inicializar() {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);

            String server = "localhost";
            String database = "db_controlefinanceiro";

            String url = "jdbc:mysql://" + server + ":3306/"
                    + database + "?useTimezone=true&serverTimezone=UTC";

            instancia = DriverManager.getConnection(url, USER, PASSWORD);

            if (instancia != null) {
                System.out.println("Status: Conectado!");
                return true;
            }

            return false;

        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado.");
            return false;

        } catch (SQLException e) {
            System.out.println("Erro de conexão:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retorna a instância atual da conexão.
     *
     * @return Connection Conexão ativa com o banco
     */
    public static Connection getConexao() {
        return instancia;
    }
}
