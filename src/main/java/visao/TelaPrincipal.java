package visao;

import dao.MovimentacaoDAO;
import enums.TipoMov;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.Movimentacao;

/**
 * Tela principal: resumo (receitas/despesas/saldo), busca, tabela e ações
 * de Novo / Editar / Excluir / Atualizar.
 *
 * @author guilh
 */
public class TelaPrincipal extends JFrame {

    // Paleta de cores da tela. Centralizada aqui pra não espalhar Color.decode
    // por todo canto e facilitar trocar o esquema depois.
    private static final Color COR_FUNDO = new Color(245, 246, 250);
    private static final Color COR_CABECALHO = new Color(33, 47, 80);
    private static final Color COR_RECEITA = new Color(46, 125, 50);
    private static final Color COR_DESPESA = new Color(198, 40, 40);
    private static final Color COR_BOTAO_NOVO = new Color(25, 118, 210);
    private static final Color COR_BOTAO_EDITAR = new Color(245, 124, 0);
    private static final Color COR_BOTAO_EXCLUIR = new Color(198, 40, 40);
    private static final Color COR_BOTAO_ATUALIZAR = new Color(97, 97, 97);

    private final MovimentacaoDAO dao = new MovimentacaoDAO();
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat formatterMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private JLabel lblTotalReceitas;
    private JLabel lblTotalDespesas;
    private JLabel lblSaldo;

    public TelaPrincipal() {
        setTitle("Controle Financeiro");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(0, 10));

        add(criarCabecalho(), BorderLayout.NORTH);

        JPanel painelCentro = new JPanel(new BorderLayout(0, 10));
        painelCentro.setBackground(COR_FUNDO);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        painelCentro.add(criarPainelResumo(), BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new Object[]{"Id", "Tipo", "Descrição", "Valor", "Categoria", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // edição acontece via TelaCadastro, não direto na célula
            }
        };
        tabela = new JTable(modeloTabela);
        estilizarTabela();

        sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);

        JPanel painelTabela = new JPanel(new BorderLayout(0, 8));
        painelTabela.setBackground(COR_FUNDO);
        painelTabela.add(criarPainelBusca(), BorderLayout.NORTH);
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        painelCentro.add(painelTabela, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        add(criarPainelBotoes(), BorderLayout.SOUTH);

        carregarTabela();
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_CABECALHO);
        painel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel titulo = new JLabel("Controle Financeiro");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        painel.add(titulo, BorderLayout.WEST);

        return painel;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 12, 0));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        lblTotalReceitas = new JLabel("R$ 0,00", SwingConstants.CENTER);
        lblTotalDespesas = new JLabel("R$ 0,00", SwingConstants.CENTER);
        lblSaldo = new JLabel("R$ 0,00", SwingConstants.CENTER);

        painel.add(criarCard("Receitas", lblTotalReceitas, COR_RECEITA));
        painel.add(criarCard("Despesas", lblTotalDespesas, COR_DESPESA));
        painel.add(criarCard("Saldo", lblSaldo, COR_CABECALHO));

        return painel;
    }

    private JPanel criarCard(String titulo, JLabel lblValor, Color cor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 230), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(120, 120, 120));

        lblValor.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblValor.setForeground(cor);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(8, 0));
        painel.setBackground(COR_FUNDO);

        JLabel lbl = new JLabel("Buscar:");
        JTextField campoBusca = new JTextField();

        campoBusca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar(campoBusca.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar(campoBusca.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar(campoBusca.getText());
            }
        });

        painel.add(lbl, BorderLayout.WEST);
        painel.add(campoBusca, BorderLayout.CENTER);
        return painel;
    }

    private void filtrar(String texto) {
        if (texto.isBlank()) {
            sorter.setRowFilter(null);
        } else {
            // Filtra pelas colunas Descrição (2) e Categoria (4); case-insensitive.
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto), 2, 4));
        }
    }

    private void estilizarTabela() {
        tabela.setRowHeight(28);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setGridColor(new Color(235, 235, 240));
        tabela.setSelectionBackground(new Color(207, 226, 255));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(COR_CABECALHO);
        tabela.getTableHeader().setForeground(Color.WHITE);

        // Renderer único cuidando de zebra + cor por tipo + alinhamento.
        // Fica num renderer só (em vez de um por coluna) porque a lógica de
        // zebra e seleção precisa ser igual em todas as colunas.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 248, 250));
                }

                setHorizontalAlignment(column == 3 ? SwingConstants.RIGHT : SwingConstants.LEFT);

                // Coluna "Tipo" (índice 1): colore o texto conforme RECEITA/DESPESA.
                String tipoNaLinha = String.valueOf(table.getModel().getValueAt(table.convertRowIndexToModel(row), 1));
                if (!isSelected) {
                    c.setForeground(column == 1
                            ? (TipoMov.RECEITA.name().equals(tipoNaLinha) ? COR_RECEITA : COR_DESPESA)
                            : Color.DARK_GRAY);
                }

                if (column == 3 && value instanceof Number) {
                    setText(formatterMoeda.format(value));
                }

                return c;
            }
        };

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        tabela.getColumnModel().getColumn(0).setPreferredWidth(30); // Id
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180); // Descrição
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        botao.setOpaque(true);
        return botao;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton btnNovo = criarBotao("+ Novo", COR_BOTAO_NOVO);
        JButton btnEditar = criarBotao("Editar", COR_BOTAO_EDITAR);
        JButton btnExcluir = criarBotao("Excluir", COR_BOTAO_EXCLUIR);
        JButton btnAtualizar = criarBotao("Atualizar", COR_BOTAO_ATUALIZAR);

        painel.add(btnNovo);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnAtualizar);

        btnNovo.addActionListener(e -> abrirCadastro(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());

        return painel;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Movimentacao> lista = dao.listarObjetos();

        double totalReceitas = 0;
        double totalDespesas = 0;

        for (Movimentacao m : lista) {
            modeloTabela.addRow(new Object[]{
                m.getId(),
                m.getTipo(),
                m.getDescricao(),
                m.getValorUnitario(),
                m.getCategoria(),
                m.getData().format(formatterData)
            });

            if (m.getTipo() == TipoMov.RECEITA) {
                totalReceitas += m.getValorUnitario();
            } else {
                totalDespesas += m.getValorUnitario();
            }
        }

        lblTotalReceitas.setText(formatterMoeda.format(totalReceitas));
        lblTotalDespesas.setText(formatterMoeda.format(totalDespesas));
        lblSaldo.setText(formatterMoeda.format(totalReceitas - totalDespesas));
    }

    private int getIdSelecionado() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma movimentação na tabela primeiro.");
            return -1;
        }
        int linhaModelo = tabela.convertRowIndexToModel(linhaView); // necessário pois a tabela está filtrada/ordenada
        return (int) modeloTabela.getValueAt(linhaModelo, 0);
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
        carregarTabela();
    }
}