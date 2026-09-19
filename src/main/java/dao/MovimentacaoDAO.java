/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexao.Conexao;
import enums.CategoriaEnum;
import enums.TipoMov;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import modelo.Movimentacao;

/**
 *
 * @author guilh
 */
public class MovimentacaoDAO {

    public boolean inserir(Movimentacao mov) {

        String sql = """
                         INSERT INTO tb_movimentacao(tipo_movimentacao, descricao, valor_unitario, quantidade, data, categoria)
                         VALUES(?, ?, ?, ?, ?, ?)
                         """;

        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setString(1, mov.getTipo().name());
            stmt.setString(2, mov.getDescricao());
            stmt.setDouble(3, mov.getValorUnitario());
            stmt.setInt(4, mov.getQuantidade());
            stmt.setDate(5, java.sql.Date.valueOf(mov.getData()));
            stmt.setString(6, mov.getCategoria().name());

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
                     SET tipo_movimentacao = ?, descricao = ?, valor_unitario = ?, quantidade = ?, categoria = ?, data = ?
                     WHERE id = ?;
                     """;

        try {
            PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);

            stmt.setString(1, mov.getTipo().name());
            stmt.setString(2, mov.getDescricao());
            stmt.setDouble(3, mov.getValorUnitario());
            stmt.setInt(4, mov.getQuantidade());
            stmt.setString(5, mov.getCategoria().name());
            stmt.setDate(6, java.sql.Date.valueOf(mov.getData()));
            stmt.setInt(7, id);

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
                int qtd = resultado.getInt("quantidade");
                LocalDate data = resultado.getDate("data").toLocalDate();
                String tipoMov = resultado.getString("tipo_movimentacao");
                String categoria = resultado.getString("categoria");

                System.out.println("ID: " + id);
                System.out.println("Tipo: " + tipoMov);
                System.out.println("Descrição: " + descricao);
                System.out.println("Valor: " + valor);
                System.out.println("Quantidade: " + qtd);
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
}
