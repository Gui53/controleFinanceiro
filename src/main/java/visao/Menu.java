/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

import java.util.Scanner;

/**
 *
 * @author guilh
 */
public class Menu {

    public void menuInicial() {

        Scanner s = new Scanner(System.in);
        System.out.println("CONTROLE FINANCEIRO\n\n"
                + "1 - Registrar gasto");
        int i = s.nextInt();

        switch (i) {
            case 1:
                System.out.println("Descrição:");
                String descricao = s.next();
                
                System.out.println("Valor:");
                double valor = s.nextDouble();

                System.out.println("Data:");
                String data = s.next();

                break;
            default:
                throw new AssertionError();
        }
    }

}
