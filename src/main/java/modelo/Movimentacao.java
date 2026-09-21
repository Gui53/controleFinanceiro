package modelo;
 
import enums.CategoriaEnum;
import enums.TipoMov;
import java.time.LocalDate;
 
/**
 *
 * @author guilh
 */
public class Movimentacao {
 
    private int id;
    private String descricao;
    private double valorUnitario;
    private TipoMov tipo;
    private LocalDate data;
    private CategoriaEnum categoria;
 
    public Movimentacao() {
        this("", 0.0, null, LocalDate.now(), null);
    }
 
    public Movimentacao(String descricao, double valorUnitario, TipoMov tipo, LocalDate data, CategoriaEnum categoria) {
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.tipo = tipo;
        this.data = data;
        this.categoria = categoria;
    }
 
    // Novo: usado quando o objeto vem do banco (ex: DAO.listarObjetos/buscarPorId),
    // pois nesse caso já sabemos o id do registro.
    public Movimentacao(int id, String descricao, double valorUnitario, TipoMov tipo, LocalDate data, CategoriaEnum categoria) {
        this(descricao, valorUnitario, tipo, data, categoria);
        this.id = id;
    }
 
    public int getId() {
        return id;
    }
 
    // Novo: necessário pois antes o id nunca era atribuído a um objeto Java em memória.
    public void setId(int id) {
        this.id = id;
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
 
    public LocalDate getData() {
        return data;
    }
 
    public void setData(LocalDate data) {
        this.data = data;
    }
 
    public TipoMov getTipo() {
        return tipo;
    }
 
    public void setTipo(TipoMov tipo) {
        this.tipo = tipo;
    }
 
    public CategoriaEnum getCategoria() {
        return categoria;
    }
 
    public void setCategoria(CategoriaEnum categoria) {
        this.categoria = categoria;
    }
 
}