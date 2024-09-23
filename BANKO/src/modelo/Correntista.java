package modelo;

import java.util.ArrayList;

public class Correntista {
	private String cpf;
	private String nome;
	private String senha;
	protected boolean titular = false;
	private ArrayList<Conta> contas = new ArrayList<>();
	
	public Correntista(String cpf, String nome, String senha) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.senha = senha;
		
		}
	
	public void adicionar(Conta c) {
		contas.add(c);
	}
	
	public void remover (Conta c) {
		contas.remove(c);
	}
	
	public Conta localizarConta(int id){
		for(Conta c : contas){
			if(c.getId() == id)
				return c;
		}
		return null;
	}
	
	public double getSaldoTotal() {
		double soma = 0;
		for (Conta c : contas) {
			soma += c.getSaldo();
		}
		return soma;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public boolean isTitular() {
		return this.titular;
	}
	
	public void setTitular(boolean mudaTitular) {
		this.titular = mudaTitular;
	}

	public ArrayList<Conta> getContas() {
		return contas;
	}

	public void setContas(ArrayList<Conta> contas) {
		this.contas = contas;
	}
	
	public String toString() {
        String texto= "Correntista [cpf=" + cpf + ", nome=" + nome + ", senha=" + senha + "]";

        texto += ", contas:";
        for(Conta c : contas)
            texto += c.getId() + ",";
        return texto;
    }
	

}
