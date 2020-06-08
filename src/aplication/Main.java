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
		
		while(true) {
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
	}

}