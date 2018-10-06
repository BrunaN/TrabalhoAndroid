package br.ufc.quixada.dadm.server.agenda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agenda {
	
	String nome;
	int id;
	int idade;
	List<Contato> listaTelefone;
	
	public Agenda(String nome, int id, List<Contato> listaTelefone, int idade) {
		super();
		this.nome = nome;
		this.id = id;
		this.listaTelefone = listaTelefone;
		this.idade = idade;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getIdade() {
		return idade;
	}
	
	public void removeContatoId(int id) {
		List<Contato> toRemove = new ArrayList<Contato>();
		for(Contato c: this.listaTelefone){
		    if(c.getId() == id){
		        toRemove.add(c);
		    }
		}
		this.listaTelefone.removeAll(toRemove);
	}
	
	public void editContato(int id, String nome, String telefone, String endereco) {
		for(Contato c: this.listaTelefone){
		    if(c.getId() == id){
		        c.setEndereco(endereco);
		        c.setNome(nome);
		        c.setTelefone(telefone);
		        break;
		    }
		}
	}
	
	public void adicionarContato(Contato contato) {
		this.listaTelefone.add(contato);
	}
	
	public List<Contato> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(List<Contato> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}
	
	@Override
	public String toString() {
		return "Message_ToString_[nome=" + nome + ", id=" + id + ", listaTelefone="
				+ listaTelefone.toString() + ", idade=" + idade + "]";
	}

	
	
}
