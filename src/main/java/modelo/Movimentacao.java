package modelo;

import enums.CategoriaEnum;
import java.time.LocalDate;

/**
 *
 * @author guilh
 */
public class Movimentacao {
    
    private int id;
    private String descricao;
    private double valorUnitario;
    private int quantidade;
    private LocalDate data;
    private CategoriaEnum categoria;

    public Movimentacao() {
        this("", 0.0, 0, null, null);
    }
    
    public Movimentacao(String descricao, double valorUnitario, int quantidade, LocalDate data, CategoriaEnum categoria) {
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
        this.data = data;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }
    
    
    
}
