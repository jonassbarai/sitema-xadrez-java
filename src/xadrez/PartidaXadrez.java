package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro= new Tabuleiro(8,8);
		setupInicial();
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez [][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i=0; i< tabuleiro.getLinhas();i++)
			for(int j=0;j < tabuleiro.getColunas();j++) 
				mat[i][j] =(PecaXadrez) tabuleiro.peca(i, j);
		
		return mat;
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.ColocarPeca(peca, new PosicaoXadrez(coluna,linha).paraPosicao());
	}
	
	private void setupInicial() {
		colocarNovaPeca('b',6, new Torre(tabuleiro,Cor.WHITE));
		colocarNovaPeca('e',8, new Rei(tabuleiro,Cor.BLACK));
		colocarNovaPeca('e',1, new Rei(tabuleiro,Cor.WHITE));
	}
	
	private int turno;
	private Cor Jogador;
	private boolean check;
	private boolean checkMate;

}
