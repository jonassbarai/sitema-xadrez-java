package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	
	private int turno;
	private Cor jogador;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promocao;
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
	
	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}
	public PecaXadrez getPromocao() {
		return promocao;
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
		
		PecaXadrez pecaMovida = (PecaXadrez)tabuleiro.peca(destino);
		
		//jogada especial promoção
		promocao = null;
		if(pecaMovida instanceof Peao) {
			if((pecaMovida.getCor() ==Cor.WHITE && destino.getLinha() ==0) || pecaMovida.getCor() ==Cor.BLACK && destino.getLinha() ==7) {
				promocao = (PecaXadrez)tabuleiro.peca(destino);
				promocao = recolocarPecaPromovida("Q");
			}
		}		
		
		check = (testeCheck(oponente(jogador))) ? true : false;
		
		if(testeCheckMate(oponente(jogador))) {
			checkMate = true;			
		}
		else {
		proximoTurno();
		}
		//movimento especial en passant
		if(pecaMovida instanceof Peao &&(destino.getLinha() == origem.getLinha()-2 ||destino.getLinha() == origem.getLinha()+2)) {
			enPassantVulneravel = pecaMovida;
		}
		else {
			enPassantVulneravel = null;
		}
		
		return (PecaXadrez)pecaCapturada;
	}
	
	public PecaXadrez recolocarPecaPromovida(String tipoPeca) {
		if(promocao == null) {
			throw new XadrezException("nao ha peca ha ser promovida");
		}
		if(!tipoPeca.equals("B") && !tipoPeca.equals("C") && !tipoPeca.equals("Q") && !tipoPeca.equals("t")){
			throw new InvalidParameterException("tipo de promocao invalida");
		}
		
		Posicao pos = promocao.getPosicaoXadrez().paraPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(tipoPeca, promocao.getCor());
		tabuleiro.ColocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);		
		
		return novaPeca;
	}
	
	private PecaXadrez novaPeca(String tipoPeca, Cor cor){
		if(tipoPeca.equals("B")) return new Bispo(tabuleiro, cor);
		if(tipoPeca.equals("C")) return new Cavalo(tabuleiro, cor);
		if(tipoPeca.equals("Q")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);

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
		 PecaXadrez p =(PecaXadrez) tabuleiro.removerPeca(origem);
		 p.incrementaContador();
		 Peca pecaCapturada = tabuleiro.removerPeca(destino);
		 tabuleiro.ColocarPeca(p, destino);
		 
		 if(pecaCapturada != null) {
			 pecasNoTabuleiro.remove(pecaCapturada);
			 pecasCapturadas.add(pecaCapturada);
		 }
		 
		 //movimento especial roque pequeno
		 if(p instanceof Rei && destino.getColuna() == origem.getColuna()+2) {
			 Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna()+3);
			 Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna()+1);
			 PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
			 tabuleiro.ColocarPeca(torre, destinoT);
			 torre.incrementaContador();
		 }
		 
		//movimento especial roque grande
		 if(p instanceof Rei && destino.getColuna() == origem.getColuna()-2) {
			 Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna()-4);
			 Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna()-1);
			 PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
			 tabuleiro.ColocarPeca(torre, destinoT);
			 torre.incrementaContador();
		 }
		 //movimento especial en passant
		 if(p instanceof Peao) {
			 if(origem.getColuna()!= destino.getColuna() && pecaCapturada ==null) {
				 Posicao peaoPosicao;
				 if(p.getCor() ==Cor.WHITE) {
					 peaoPosicao = new Posicao(destino.getLinha()+1, destino.getColuna());
				 }
				 else { 
					 peaoPosicao = new Posicao(destino.getLinha()-1, destino.getColuna());
				 }
				 pecaCapturada = tabuleiro.removerPeca(peaoPosicao);
				 pecasCapturadas.add(pecaCapturada);
				 pecasNoTabuleiro.remove(pecaCapturada);
			 }
		 }
		 		 
		 return pecaCapturada;
	 }
	 
	 
	private void desfazerMovimento(Posicao origem,Posicao destino, Peca pecaCapturada) {
		PecaXadrez p =(PecaXadrez)tabuleiro.removerPeca(destino);
		p.decrementaContador();
		tabuleiro.ColocarPeca(p, origem);
		if(pecaCapturada != null) {
			tabuleiro.ColocarPeca(pecaCapturada, destino);
			 pecasNoTabuleiro.add(pecaCapturada);
			 pecasCapturadas.remove(pecaCapturada);
		 }
		
		//movimento especial roque pequeno
		 if(p instanceof Rei && destino.getColuna() == origem.getColuna()+2) {
			 Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna()+3);
			 Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna()+1);
			 PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
			 tabuleiro.ColocarPeca(torre, origemT);
			 torre.decrementaContador();;
		 }
		 
		//movimento especial roque grande
		 if(p instanceof Rei && destino.getColuna() == origem.getColuna()-2) {
			 Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna()-4);
			 Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna()-1);
			 PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
			 tabuleiro.ColocarPeca(torre, origemT);
			 torre.decrementaContador();
		 }
		 
		//movimento especial en passant
		 if(p instanceof Peao) {
			 if(origem.getColuna()!= destino.getColuna() && pecaCapturada ==enPassantVulneravel){
				PecaXadrez peao = (PecaXadrez)tabuleiro.removerPeca(destino);
				
				 Posicao peaoPosicao;
				 if(p.getCor() ==Cor.WHITE) {
					 peaoPosicao = new Posicao(3, destino.getColuna());
				 }
				 else { 
					 peaoPosicao = new Posicao(4, destino.getColuna());
				 }
				 tabuleiro.ColocarPeca(peao, peaoPosicao);
			 }
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
		 	colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
		 	colocarNovaPeca('c',1, new Bispo(tabuleiro, Cor.WHITE));
		 	colocarNovaPeca('b',1, new Cavalo(tabuleiro, Cor.WHITE));
		 	colocarNovaPeca('d',1, new Rainha(tabuleiro, Cor.WHITE));		 	
	        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
	        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
		 	colocarNovaPeca('f',1, new Bispo(tabuleiro, Cor.WHITE));
	        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
		 	colocarNovaPeca('g',1, new Cavalo(tabuleiro, Cor.WHITE));
	        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE, this));
	        
	        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		 	colocarNovaPeca('c',8, new Bispo(tabuleiro, Cor.BLACK));
		 	colocarNovaPeca('b',8, new Cavalo(tabuleiro, Cor.BLACK));
		 	colocarNovaPeca('d',8, new Rainha(tabuleiro, Cor.BLACK));		 	
	        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
	        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
	        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK,this));
	        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK,this));
	        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK,this));
	        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK,this));
	        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK,this));
		 	colocarNovaPeca('f',8, new Bispo(tabuleiro, Cor.BLACK));
	        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK,this));
	        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK,this));
		 	colocarNovaPeca('g',8, new Cavalo(tabuleiro, Cor.BLACK));
	        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK,this));
	}	
	

}