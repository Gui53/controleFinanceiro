package visao;
 
import dao.MovimentacaoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Movimentacao;
 
/**
 * Tela principal: lista as movimentações numa JTable e permite
 * Novo / Editar / Excluir / Atualizar.
 *
 * @author guilh
 */
public class TelaPrincipal extends JFrame {
 
    private final MovimentacaoDAO dao = new MovimentacaoDAO();
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 
    public TelaPrincipal() {
        setTitle("Controle Financeiro");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
 
        // Colunas da tabela. "Id" fica escondido implicitamente na primeira coluna
        // e é usado como referência para editar/excluir a linha selecionada.
        modeloTabela = new DefaultTableModel(
                new Object[]{"Id", "Tipo", "Descrição", "Valor", "Categoria", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // a edição é feita pela TelaCadastro, não direto na tabela
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
 
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
 
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);
 
        btnNovo.addActionListener(e -> abrirCadastro(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());
 
        carregarTabela();
    }
 
    private void carregarTabela() {
        modeloTabela.setRowCount(0); // limpa antes de recarregar
        List<Movimentacao> lista = dao.listarObjetos();
 
        for (Movimentacao m : lista) {
            modeloTabela.addRow(new Object[]{
                m.getId(),
                m.getTipo(),
                m.getDescricao(),
                m.getValorUnitario(),
                m.getCategoria(),
                m.getData().format(formatter)
            });
        }
    }
 
    private int getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma movimentação na tabela primeiro.");
            return -1;
        }
        return (int) modeloTabela.getValueAt(linha, 0);
    }
 
    private void editarSelecionado() {
        int id = getIdSelecionado();
        if (id == -1) {
            return;
        }
        Movimentacao mov = dao.buscarPorId(id);
        abrirCadastro(mov);
    }
 
    private void excluirSelecionado() {
        int id = getIdSelecionado();
        if (id == -1) {
            return;
        }
 
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir essa movimentação?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
 
        if (confirmacao == JOptionPane.YES_OPTION) {
            dao.deletar(id);
            carregarTabela();
        }
    }
 
    private void abrirCadastro(Movimentacao movParaEditar) {
        TelaCadastro tela = new TelaCadastro(this, movParaEditar);
        tela.setVisible(true);
        carregarTabela(); // recarrega ao fechar o diálogo, mesmo se ele salvou ou cancelou
    }
}