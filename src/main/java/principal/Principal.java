package principal;

import conexao.*;
import javax.swing.SwingUtilities;
import visao.TelaPrincipal;

/**
 *
 * @author guilh
 */
public class Principal {

    public static void main(String[] args) {

        ConfiguraBanco.inicializar();

        Conexao.inicializar();

        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));

    }
}
