package aplication;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class Main {

	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in); 	
		
		
		PartidaXadrez partida = new PartidaXadrez();
		while(true) {
			try {
				UI.limpaTela();
				UI.printTabuleiro(partida.getPecas());	
				System.out.println();
				System.out.print("origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);
				
				System.out.println();
				System.out.print("destino: ");
				PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);
				
				PecaXadrez pecaCapturada = partida.movimentoxadrez(origem, destino);
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
