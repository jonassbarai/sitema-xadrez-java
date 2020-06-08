package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	
	private int turno;
	private Cor jogador;
	private boolean check;
	private boolean checkMate;
	private Tabuleiro tabuleiro;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	
	
	
	public PartidaXadrez() {
		tabuleiro= new Tabuleiro(8,8);
		turno =1;
		jogador = Cor.WHITE;
		setupInicial();
	}
	
	public int getTurno() {
		return turno;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public Cor getJogador() {
		return jogador;
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
		
		if(testeCheck(jogador)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezException("voce nao pode se colocar em check");
		}
		
		check = (testeCheck(oponente(jogador))) ? true : false;
		
		if(testeCheckMate(oponente(jogador))) {
			checkMate = true;			
		}
		else {
		proximoTurno();
		}
		
		return (PecaXadrez)pecaCapturada;
	}
	
	 private void validarPosicaoOrigem(Posicao posicao) {
		 if(!tabuleiro.haPeca(posicao))
			 throw new XadrezException("nao ha peca nessa posi��o ");
		 
		 if(jogador != ((PecaXadrez)tabuleiro.peca(posicao)).getCor())
			 throw new XadrezException("nao e possivel mover a peca do adversario");
		 
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
		 
		 if(pecaCapturada != null) {
			 pecasNoTabuleiro.remove(pecaCapturada);
			 pecasCapturadas.add(pecaCapturada);
		 }		 
		 tabuleiro.ColocarPeca(p, destino);		 
		 return pecaCapturada;
	 }
	 
	 
	private void desfazerMovimento(Posicao origem,Posicao destino, Peca pecaCapturada) {
		Peca p = tabuleiro.removerPeca(destino);
		tabuleiro.ColocarPeca(p, origem);
		if(pecaCapturada != null) {
			tabuleiro.ColocarPeca(pecaCapturada, destino);
			 pecasNoTabuleiro.add(pecaCapturada);
			 pecasCapturadas.remove(pecaCapturada);
		 }
		
	}
	
	private Cor oponente(Cor cor) {
		return(cor ==Cor.WHITE)? Cor.BLACK : Cor.WHITE;
	}
	
	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x-> ((PecaXadrez)x).getCor()==cor).collect(Collectors.toList());
		for(Peca p :lista) {
			if(p instanceof Rei)
				return (PecaXadrez) p;
		}
		throw new IllegalStateException("nao existe o rei da cor" + cor);
	}
	
	private boolean testeCheck(Cor cor) {
		Posicao reiPosicao = rei(cor).getPosicaoXadrez().paraPosicao();
		List<Peca> pecaOponente = pecasNoTabuleiro.stream().filter(x-> ((PecaXadrez)x).getCor()==oponente(cor)).collect(Collectors.toList());
		for(Peca p: pecaOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testeCheckMate(Cor cor) {
		if (!testeCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i=0; i<tabuleiro.getLinhas(); i++) {
				for (int j=0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().paraPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazerMovimento(origem, destino);
						boolean testCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	 
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.ColocarPeca(peca, new PosicaoXadrez(coluna,linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	private void proximoTurno() {
		turno++;
		jogador= (jogador == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	
	private void setupInicial() {
		colocarNovaPeca('h', 7, new Torre(tabuleiro, Cor.WHITE));
        colocarNovaPeca('d', 1, new Torre(tabuleiro, Cor.WHITE));    
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
        
        colocarNovaPeca('b', 8, new Torre(tabuleiro, Cor.BLACK));        
        colocarNovaPeca('a', 8, new Rei(tabuleiro, Cor.BLACK));
	}	
	

}