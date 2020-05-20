package aplication;

import xadrez.PartidaXadrez;

public class Main {

	public static void main(String[] args) {
	
		PartidaXadrez partida = new PartidaXadrez();
		UI.printTabuleiro(partida.getPecas());
	}

}
