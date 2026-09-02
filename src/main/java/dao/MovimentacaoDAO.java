/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.Movimentacao;

/**
 *
 * @author guilh
 */
public class MovimentacaoDAO {
    public boolean inserir(Movimentacao mov){
            String sql = """
                         INSERT INTO tb_movimentacao(descricao, valor_unitario, quantidade, data, categoria)
                         VALUES(?, ?, ?, ?, ?)
                         """;
            
            try{
                PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);
                
                stmt.setString(1, mov.getDescricao());
                stmt.setDouble(2, mov.getValorUnitario());
                stmt.setInt(3, mov.getQuantidade());
                stmt.setDate(4, java.sql.Date.valueOf(mov.getData()));
                stmt.setString(5, mov.getCategoria().name());
                
                stmt.execute();
                stmt.close();
                
                System.out.println("MOVIMENTAÇÃO CADASTRADA!");
                
                return true;
                
            }catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }
}
