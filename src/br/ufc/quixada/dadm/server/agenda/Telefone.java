package br.ufc.quixada.dadm.server.agenda;

import java.io.Serializable;

public class Telefone {
	
	public Telefone(String nome, long telefone) {
		super();
		this.nome = nome;
		this.telefone = telefone;
	}
	
	String nome;
	long telefone;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public long getTelefone() {
		return telefone;
	}
	public void setTelefone(long telefone) {
		this.telefone = telefone;
	}
	@Override
	public String toString() {
		return "Nome=" + nome + " -  Telefone=" + telefone;
	}
	
	
	
}
