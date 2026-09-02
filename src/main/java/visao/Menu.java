/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

import dao.MovimentacaoDAO;
import enums.CategoriaEnum;
import java.time.LocalDate;
import java.util.Scanner;
import modelo.Movimentacao;

/**
 *
 * @author guilh
 */
public class Menu {

    public void menuInicial() {

        Scanner s = new Scanner(System.in);
        MovimentacaoDAO dao = new MovimentacaoDAO();
        CategoriaEnum cEnum;
        System.out.println("CONTROLE FINANCEIRO\n\n"
                + "1 - Registrar gasto");
        int i = s.nextInt();

        switch (i) {
            case 1:
                System.out.println("Descrição:");
                String descricao = s.next();

                System.out.println("Categoria:\n"
                        + "  1 - ALIMENTAÇÃO,\n"
                        + "  2 - ROUPA,\n"
                        + "  3 - TRANSPORTE,\n"
                        + "  4 - LAZER,\n"
                        + "  5 - EDUCAÇÃO,\n"
                        + "  6 - SAÚDE,\n"
                        + "  7 -  ASSINATURA,\n"
                        + "  8 -  OUTROS");
                int j = s.nextInt();
                switch (j) {
                    case 1:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 2:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 3:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 4:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 5:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 6:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 7:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;

                    case 8:
                        cEnum = CategoriaEnum.values()[j - 1];

                        break;
                    default:
                        throw new AssertionError();
                }

                System.out.println("Valor:");
                double valor = s.nextDouble();

                System.out.println("Quantidade:");
                int quantidade = s.nextInt();

                /*System.out.println("Data:");
                String data = s.next();
                 */
                Movimentacao mov = new Movimentacao(descricao, valor, quantidade, LocalDate.now(), cEnum);

                dao.inserir(mov);
                break;
            default:
                throw new AssertionError();
        }
    }

}
