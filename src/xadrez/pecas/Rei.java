package xadrez.pecas;

import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez{

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "K";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		return mat;
	}

}
