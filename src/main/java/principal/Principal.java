package principal;

import conexao.*;
import visao.Menu;

/**
 *
 * @author guilh
 */
public class Principal {

    public static void main(String[] args) {
        
        ConfiguraBanco.inicializar();
        
        Conexao.inicializar();
        
        /*Menu menu = new Menu();
        
        menu.menuInicial();*/
        
    }
}
