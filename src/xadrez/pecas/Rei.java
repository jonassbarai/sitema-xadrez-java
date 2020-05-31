package xadrez.pecas;

import tabuleiro.Posicao;
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
	
	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p==null || p.getCor() != getCor();	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);
		
		//acima
		p.setValores(posicao.getLinha() -1, posicao.getColuna());
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		//abaixo
				p.setValores(posicao.getLinha() +1, posicao.getColuna());
				if(getTabuleiro().posicaoExiste(p) && podeMover(p))
					mat[p.getLinha()][p.getColuna()] = true;
		
		//direita
		p.setValores(posicao.getLinha(), posicao.getColuna()+1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		//esquerda
				p.setValores(posicao.getLinha(), posicao.getColuna()-1);
				if(getTabuleiro().posicaoExiste(p) && podeMover(p))
					mat[p.getLinha()][p.getColuna()] = true;
				
		//noroeste
		p.setValores(posicao.getLinha()-1, posicao.getColuna()-1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		//suldoeste
		p.setValores(posicao.getLinha()+1, posicao.getColuna()-1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		//nordeste
		p.setValores(posicao.getLinha()-1, posicao.getColuna()+1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;
		
		//suldeste
		p.setValores(posicao.getLinha()+1, posicao.getColuna()+1);
		if(getTabuleiro().posicaoExiste(p) && podeMover(p))
			mat[p.getLinha()][p.getColuna()] = true;

		
		
		
		return mat;
	}

}
