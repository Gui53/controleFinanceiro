package visao;

import dao.MovimentacaoDAO;
import enums.CategoriaEnum;
import enums.TipoMov;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        TipoMov tipo;
        CategoriaEnum cEnum;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int i = 0;
        while (i != 3) {
            System.out.println("CONTROLE FINANCEIRO\n\n"
                    + "1 - Registrar movimentação\n"
                    + "2 - Exibir Registros\n"
                    + "3 - Sair");

            i = s.nextInt();
            switch (i) {
                case 1:
                    System.out.println("TIPO DA MOVIMENTAÇÃO:\n"
                            + "  1 - ENTRADA,\n"
                            + "  2 - SAÍDA,\n");
                    int k = s.nextInt();

                    switch (k) {
                        case 1:
                            tipo = TipoMov.values()[k - 1];

                            break;

                        case 2:
                            tipo = TipoMov.values()[k - 1];

                            break;
                        default:
                            throw new AssertionError();
                    }
                    s.nextLine();

                    System.out.println("Descrição:");
                    String descricao = s.nextLine();

                    System.out.println("Categoria:\n"
                            + "  1 - ALIMENTAÇÃO,\n"
                            + "  2 - ROUPA,\n"
                            + "  3 - TRANSPORTE,\n"
                            + "  4 - LAZER,\n"
                            + "  5 - EDUCAÇÃO,\n"
                            + "  6 - SAÚDE,\n"
                            + "  7 -  ASSINATURA,\n"
                            + "  8 - SALÁRIO MENSAL\n"
                            + "  9 -  OUTROS");
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

                        case 9:
                            cEnum = CategoriaEnum.values()[j - 1];

                            break;
                        default:
                            throw new AssertionError();
                    }
                    System.out.println("Valor:");
                    double valor = s.nextDouble();

                    System.out.println("Quantidade:");
                    int quantidade = s.nextInt();

                    System.out.println("Data:");
                    LocalDate data = LocalDate.parse(s.next(), formatter);

                    Movimentacao mov = new Movimentacao(descricao, valor, quantidade, tipo, data, cEnum);

                    dao.inserir(mov);
                    break;
                case 2:
                    System.out.println("CONSULTA:");
                    dao.listar();

                    break;

                case 3:
                    System.out.println("SAINDO DO SISTEMA...");
                    System.out.println("SISTEMA DESLIGADO!");
                    break;
                default:
                    System.out.println("Número inválido");                 
            }
        }
    }

}
