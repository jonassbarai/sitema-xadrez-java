package xadrez;

import tabuleiro.Peca;
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
	
	public boolean[][] movimentosPossiveis( PosicaoXadrez posicaoOrigem){
		Posicao posicao = posicaoOrigem.paraPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	public PecaXadrez movimentoxadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem,destino);
		Peca pecaCapturada = fazerMovimento(origem,destino);
		return (PecaXadrez)pecaCapturada;
	}
	
	 private void validarPosicaoOrigem(Posicao posicao) {
		 if(!tabuleiro.haPeca(posicao))
			 throw new XadrezException("n�o h� peca nessa posi��o ");
		 
		 if(!tabuleiro.peca(posicao).haMovimetosPossiveis())
	 		 throw new XadrezException("nao ha movimentos possiveis para a peca escolhida");
	 }
	 
	 private void validarPosicaoDestino(Posicao origem, Posicao destino) {
			if(!tabuleiro.peca(origem).movimentoPossivel(destino))
				throw new XadrezException ("a peca escolhida nao pode ir ao local de destino");
		}
	 
	 private Peca fazerMovimento(Posicao origem, Posicao destino) {
		 Peca p = tabuleiro.removerPeca(origem);
		 Peca pecaCapturada = tabuleiro.removerPeca(destino);
		 tabuleiro.ColocarPeca(p, destino);
		 return pecaCapturada;
	 }
	 
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.ColocarPeca(peca, new PosicaoXadrez(coluna,linha).paraPosicao());
	}
	
	private void setupInicial() {
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.WHITE));
        colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.BLACK));
        colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.BLACK));
	}
	
	private int turno;
	private Cor Jogador;
	private boolean check;
	private boolean checkMate;

}
