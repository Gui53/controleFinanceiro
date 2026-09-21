package dao;

import conexao.Conexao;
import enums.CategoriaEnum;
import enums.TipoMov;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Movimentacao;

/**
 *
 * @author guilh
 */
public class MovimentacaoDAO {

    public boolean inserir(Movimentacao mov) {

        String sql = """
                         INSERT INTO tb_movimentacao(tipo_movimentacao, descricao, valor_unitario, data, categoria)
                         VALUES(?, ?, ?, ?, ?)
                         """;

        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setString(1, mov.getTipo().name());
            stmt.setString(2, mov.getDescricao());
            stmt.setDouble(3, mov.getValorUnitario());
            stmt.setDate(4, java.sql.Date.valueOf(mov.getData()));
            stmt.setString(5, mov.getCategoria().name());

            stmt.execute();
            stmt.close();

            System.out.println("MOVIMENTAÇÃO CADASTRADA!");

            return true;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }

    public boolean editar(int id, Movimentacao mov) {

        String sql = """
                     UPDATE tb_movimentacao
                     SET tipo_movimentacao = ?, descricao = ?, valor_unitario = ?, categoria = ?, data = ?
                     WHERE id = ?;
                     """;

        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setString(1, mov.getTipo().name());
            stmt.setString(2, mov.getDescricao());
            stmt.setDouble(3, mov.getValorUnitario());
            stmt.setString(4, mov.getCategoria().name());
            stmt.setDate(5, java.sql.Date.valueOf(mov.getData()));
            stmt.setInt(6, id);

            int linhasAlteradas = stmt.executeUpdate();
            stmt.close();

            if (linhasAlteradas > 0) {
                System.out.println("MOVIMENTAÇÃO EDITADA!");
                return true;
            }

            System.out.println("Nenhuma movimentação encontrada com esse ID.");
            return false;

        } catch (Exception e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }

    public boolean existe(int id) {
        String sql = """
                     SELECT id FROM tb_movimentacao
                     WHERE id = ?;
                     """;

        try {

            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();

            return resultado.next();
        } catch (SQLException e) {
            System.out.println("ERRO: " + e);
            throw new RuntimeException(e);
        }

    }

    public boolean deletar(int id) {

        String sql = """
                     DELETE FROM tb_movimentacao 
                     WHERE id = ?;
                     """;
        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setInt(1, id);

            int linhasAlteradas = stmt.executeUpdate();

            stmt.close();

            if (linhasAlteradas > 0) {
                System.out.println("MOVIMENTAÇÃO EXCLUÍDA!");
                return true;
            }

            System.out.println("Não existe movimentação com esse ID.");
            return false;
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }

    }

    public void listar() {
        String sql = """
                     SELECT * FROM tb_movimentacao;
                     """;
        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String descricao = resultado.getString("descricao");
                double valor = resultado.getDouble("valor_unitario");
                LocalDate data = resultado.getDate("data").toLocalDate();
                String tipoMov = resultado.getString("tipo_movimentacao");
                String categoria = resultado.getString("categoria");

                System.out.println("ID: " + id);
                System.out.println("Tipo: " + tipoMov);
                System.out.println("Descrição: " + descricao);
                System.out.println("Valor: " + valor);
                System.out.println("Categoria: " + categoria);
                System.out.println("Data: " + data);
                System.out.println("-------------------------");
                System.out.println("");
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }
    
    // a JTable da tela Swing. Reaproveita a mesma query do listar().
    public List<Movimentacao> listarObjetos() {
        String sql = """
                     SELECT * FROM tb_movimentacao ORDER BY id;
                     """;
        List<Movimentacao> lista = new ArrayList<>();
 
        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
 
            while (resultado.next()) {
                Movimentacao mov = new Movimentacao(
                        resultado.getInt("id"),
                        resultado.getString("descricao"),
                        resultado.getDouble("valor_unitario"),
                        TipoMov.valueOf(resultado.getString("tipo_movimentacao")),
                        resultado.getDate("data").toLocalDate(),
                        CategoriaEnum.valueOf(resultado.getString("categoria"))
                );
                lista.add(mov);
            }
            resultado.close();
            stmt.close();
 
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
 
        return lista;
    }
 
    // Novo: necessário para pré-preencher o formulário de edição com os dados
    // atuais do registro selecionado na tabela.
    public Movimentacao buscarPorId(int id) {
        String sql = """
                     SELECT * FROM tb_movimentacao WHERE id = ?;
                     """;
 
        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
 
            if (resultado.next()) {
                Movimentacao mov = new Movimentacao(
                        resultado.getInt("id"),
                        resultado.getString("descricao"),
                        resultado.getDouble("valor_unitario"),
                        TipoMov.valueOf(resultado.getString("tipo_movimentacao")),
                        resultado.getDate("data").toLocalDate(),
                        CategoriaEnum.valueOf(resultado.getString("categoria"))
                );
                resultado.close();
                stmt.close();
                return mov;
            }
 
            resultado.close();
            stmt.close();
            return null;
 
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }
}
