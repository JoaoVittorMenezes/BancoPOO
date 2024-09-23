package modelo;

import java.util.ArrayList;

public class Conta {
	private static int contador = 0;
	protected int id;
	protected String data;
	protected double saldo;
	private ArrayList<Correntista> correntistas = new ArrayList<>();

	public Conta(String data) {
		super();
		this.id = contador++;
		this.data = data;
		this.saldo = 0;
		
	}
	
	public void creditar(double valor) {
		this.saldo += valor;
	}
	
	public void debitar(double valor) throws Exception{
		if (saldo >= valor) {
			this.saldo -= valor;
		}
		else
			throw new Exception("Saldo insuficiente!");
		
	}
	
	public void transferir(double valor, Conta destino) throws Exception{
		this.debitar(valor);
		destino.creditar(valor);
		
	}
	
	public void adicionar(Correntista c) {
		correntistas.add(c);
	}
	
	public void remover(Correntista c) {
		correntistas.remove(c);
	}
	
	public Correntista localizarCorrentista(String cpf){
		for(Correntista c : correntistas){
			if(c.getCpf().equals(cpf))
				return c;
		}
		return null;
	}
	
	/*public void transferir(double valor, Conta destino) {
		for(Conta c : contas) {
			if (c.getId() == destino.getId()) {
				c.saldo += valor;
				this.saldo -= valor;
			}
		}
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public boolean isTitular(String cpf) {
		if (correntistas.get(0).getCpf().equals(cpf))
			return true;
		return false;
	}

	public ArrayList<Correntista> getCorrentistas() {
		return correntistas;
	}

	public void setCorrentistas(ArrayList<Correntista> correntistas) {
		this.correntistas = correntistas;
	}
	public String toString() {
        String texto =  "id=" + id + ", data=" + data + ", saldo=" + saldo;

        texto += ", correntistas:";
        for(Correntista co : correntistas)
            texto += co.getCpf() + ",";
        return texto;
    }
	
	
}
