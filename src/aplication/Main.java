package aplication;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class Main {

	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in); 	
		
		
		PartidaXadrez partida = new PartidaXadrez();
		List<PecaXadrez> capturadas = new ArrayList<>();
		
		while(!partida.getCheckMate()) {
			try {
				UI.limpaTela();
				UI.printPartida(partida,capturadas);	
				System.out.println();
				System.out.print("origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);
				
				boolean[][] movimentosPossiveis = partida.movimentosPossiveis(origem);
				UI.limpaTela();
				UI.printTabuleiro(partida.getPecas(),movimentosPossiveis);
				
				System.out.println();
				System.out.print("destino: ");
				PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);
				
				PecaXadrez pecaCapturada = partida.movimentoxadrez(origem, destino);
				if(pecaCapturada!=null)
					capturadas.add(pecaCapturada);
				
				if(partida.getPromocao() != null) {
					System.out.println("digite a peca para a promocao(B/C/Q/T)");
					String tipoPeca =sc.nextLine().toUpperCase();
					while(!tipoPeca.equals("B") && !tipoPeca.equals("C") && !tipoPeca.equals("Q") && !tipoPeca.equals("T")){
						System.out.println("valor invalido! digite a peca para a promocao(B/C/Q/T)");
						 tipoPeca =sc.nextLine().toUpperCase();
					}
					partida.recolocarPecaPromovida(tipoPeca);
				}
			}
			catch(XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			
		}	
		UI.limpaTela();
		UI.printPartida(partida, capturadas);
		
		
	}

}