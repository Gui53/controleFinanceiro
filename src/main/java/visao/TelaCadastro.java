package visao;
 
import dao.MovimentacaoDAO;
import enums.CategoriaEnum;
import enums.TipoMov;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.Movimentacao;
 
/**
 * Formulário de cadastro/edição. Se receber uma Movimentacao no construtor,
 * abre em modo "editar" (campos preenchidos, salvar chama dao.editar).
 * Se receber null, abre em modo "novo" (salvar chama dao.inserir).
 *
 * @author guilh
 */
public class TelaCadastro extends JDialog {
 
    private final MovimentacaoDAO dao = new MovimentacaoDAO();
    private final Movimentacao movEmEdicao; // null quando é um cadastro novo
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 
    private final JComboBox<TipoMov> comboTipo = new JComboBox<>(TipoMov.values());
    private final JTextField campoDescricao = new JTextField();
    private final JTextField campoValor = new JTextField();
    private final JComboBox<CategoriaEnum> comboCategoria = new JComboBox<>(CategoriaEnum.values());
    private final JTextField campoData = new JTextField();
 
    public TelaCadastro(JFrame pai, Movimentacao movParaEditar) {
        super(pai, true); // true = modal, bloqueia a tela principal até fechar
        this.movEmEdicao = movParaEditar;
 
        setTitle(movEmEdicao == null ? "Nova movimentação" : "Editar movimentação");
        setSize(350, 260);
        setLocationRelativeTo(pai);
        setLayout(new GridLayout(6, 2, 5, 5));
 
        add(new JLabel("Tipo:"));
        add(comboTipo);
        add(new JLabel("Descrição:"));
        add(campoDescricao);
        add(new JLabel("Valor:"));
        add(campoValor);
        add(new JLabel("Categoria:"));
        add(comboCategoria);
        add(new JLabel("Data (dd/MM/yyyy):"));
        add(campoData);
 
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnSalvar);
        add(btnCancelar);
 
        if (movEmEdicao != null) {
            preencherCampos(movEmEdicao);
        }
 
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }
 
    private void preencherCampos(Movimentacao mov) {
        comboTipo.setSelectedItem(mov.getTipo());
        campoDescricao.setText(mov.getDescricao());
        campoValor.setText(String.valueOf(mov.getValorUnitario()));
        comboCategoria.setSelectedItem(mov.getCategoria());
        campoData.setText(mov.getData().format(formatter));
    }
 
    private void salvar() {
        // Validação básica antes de tentar montar o objeto.
        // Sem isso, um valor não-numérico ou data mal formatada quebra com uma
        // exceção "feia" em vez de avisar o usuário.
        String descricao = campoDescricao.getText().trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descrição não pode ficar em branco.");
            return;
        }
 
        double valor;
        try {
            valor = Double.parseDouble(campoValor.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números (ex: 150.50).");
            return;
        }
 
        LocalDate data;
        try {
            data = LocalDate.parse(campoData.getText().trim(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
            return;
        }
 
        TipoMov tipo = (TipoMov) comboTipo.getSelectedItem();
        CategoriaEnum categoria = (CategoriaEnum) comboCategoria.getSelectedItem();
 
        Movimentacao mov = new Movimentacao(descricao, valor, tipo, data, categoria);
 
        boolean sucesso;
        if (movEmEdicao == null) {
            sucesso = dao.inserir(mov);
        } else {
            sucesso = dao.editar(movEmEdicao.getId(), mov);
        }
 
        if (sucesso) {
            dispose(); // fecha o diálogo e volta pra tela principal, que recarrega a tabela
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar. Veja o console para detalhes.");
        }
    }
}