package regras_negocio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import modelo.Conta;
import modelo.Correntista;
import modelo.ContaEspecial;
import repositorio.Repositorio;

public class Fachada {
	private Fachada() {}		
	private static Repositorio repositorio = new Repositorio();
	
	public static ArrayList<Correntista> listarCorrentistas(){
		ArrayList<Correntista> ordenar = new ArrayList<>(repositorio.getCorrentistas());
		Collections.sort(ordenar, new Comparator<Correntista>() {
	        @Override
	        public int compare(Correntista c1, Correntista c2) {
	            return c1.getCpf().compareTo(c2.getCpf());
	        }
	    });
	    
	    return ordenar;
	}
	
	public static ArrayList<Conta> listarContas(){
		return repositorio.getContas();
	}
	
	public static void criarCorrentista(String cpf, String nome, String senha) throws Exception{
		cpf = cpf.trim();
		nome = nome.trim();
		senha = senha.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		if(c!=null) {
			throw new Exception("Não criou correntista, cpf:" + cpf + " já cadastrado.");
		}
		
		if(!senha.matches("^\\d{4}$"))
			throw new Exception("A senha deve ser numérica e com apenas 4 números!");
		
		c = new Correntista(cpf, nome, senha);
		
		repositorio.adicionarCorrentista(c);
		repositorio.salvarObjetos();
	}
	
	public static void criarConta(String cpf) throws Exception {
		cpf = cpf.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		else {
			if(c.isTitular() == true)
				throw new Exception("Correntista com cpf= " + cpf + " já é titular de uma conta.");
		}
		
		LocalDate hoje = LocalDate.now();
		DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String data = hoje.format(f1);
		Conta c1 = new Conta(data);
		c.adicionar(c1);
		c1.adicionar(c);
		
		c.setTitular(true);
		repositorio.adicionarConta(c1);
		repositorio.salvarObjetos();
		
	}
	
	public static void criarContaEspecial(String cpf, double limite) throws Exception {
		cpf = cpf.trim();

		Correntista c = repositorio.localizarCorrentista(cpf);
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		else {
			if(c.isTitular() == true)
				throw new Exception("Correntista com cpf= " + cpf + " já é titular de uma conta.");
		}
		
		if (limite < 50) {
			throw new Exception("O limite deve ser maior ou igual a 50.");
		}
		
		LocalDate hoje = LocalDate.now();
		DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String data = hoje.format(f1);
		
		ContaEspecial c1 = new ContaEspecial(data, limite);
		c.adicionar(c1);
		c1.adicionar(c);
		
		c.setTitular(true);
		
		repositorio.adicionarConta(c1);
		repositorio.salvarObjetos();
	}
	
	public static void inserirCorrentistaConta(int id, String cpf) throws Exception {
		cpf = cpf.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		Conta conta = repositorio.localizarConta(id);
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		
		if (conta == null)
			throw new Exception("Conta não encontrada com id: " + id);
		
		Correntista c1 = conta.localizarCorrentista(cpf);
		if(c1 != null) {
			throw new Exception ("Correntista com o cpf: " + cpf + " já participa da conta com id=" + id);
		}
		
		
		c.adicionar(conta);
		conta.adicionar(c);
		
		repositorio.adicionarConta(conta);
		repositorio.adicionarCorrentista(c);
		repositorio.salvarObjetos();
		
	}
	
	public static void removerCorrentistaConta(int id, String cpf) throws Exception {
		cpf = cpf.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		Conta conta = repositorio.localizarConta(id);
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		
		if (conta == null)
			throw new Exception("Conta não encontrada com id: " + id);
		
		Correntista c1 = conta.localizarCorrentista(cpf);
		if(c1 == null) {
			throw new Exception ("Correntista com o cpf: " + cpf + " não participa da conta com id=" + id);
		}
		else if (conta.getCorrentistas().get(0).getCpf().equals(cpf))
			throw new Exception("Correntista com cpf: " + cpf + " não pode ser removido pois é o titular da conta.");
		
		
		
		conta.remover(c);
		c.remover(conta);
		
		repositorio.salvarObjetos();
	}
	
	public static void apagarConta(int id) throws Exception {
		Conta conta = repositorio.localizarConta(id);
		if (conta == null)
			throw new Exception("Conta não encontrada com id= " + id);
		
		if (conta.getSaldo() != 0)
			throw new Exception("Não é possível apagar a conta com id = " + id + " pois ela ainda tem saldo.");
		
		Correntista titular = conta.getCorrentistas().get(0);
		titular.setTitular(false);
		
		for (Correntista c : conta.getCorrentistas()) {
			c.remover(conta);
		}
		
		conta.getCorrentistas().clear();
		
		repositorio.remover(conta);
		repositorio.salvarObjetos();
	}
	
	public static void creditarValor(int id, String cpf, String senha, double valor) throws Exception {
		cpf = cpf.trim();
		senha = senha.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		Conta conta = repositorio.localizarConta(id);
		
		if(valor <= 0)
			throw new Exception("Só é possível creditar valores maiores ou iguais a zero.");
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		
		if (conta == null)
			throw new Exception("Conta não encontrada com id: " + id);
		
		Correntista c1 = conta.localizarCorrentista(cpf);
		if(c1 == null) {
			throw new Exception ("Correntista com o cpf: " + cpf + " não participa da conta com id=" + id);
		}
		
		if(!senha.matches("^\\d{4}$"))
			throw new Exception("A senha deve ser numérica e com apenas 4 números!");
		
		if(!c.getSenha().equals(senha))
			throw new Exception("Senha incorreta!");
		
		conta.creditar(valor);
		
		repositorio.salvarObjetos();
		
	}
	
	public static void debitarValor(int id, String cpf, String senha, double valor) throws Exception {
		cpf = cpf.trim();
		senha = senha.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		Conta conta = repositorio.localizarConta(id);
		
		if(valor <= 0)
			throw new Exception("Só é possível debitar valores maiores ou iguais a zero.");
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		
		if (conta == null)
			throw new Exception("Conta não encontrada com id: " + id);
		
		Correntista c1 = conta.localizarCorrentista(cpf);
		if(c1 == null) {
			throw new Exception ("Correntista com o cpf: " + cpf + " não participa da conta com id=" + id);
		}
		
		if(!senha.matches("^\\d{4}$"))
			throw new Exception("A senha deve ser numérica e com apenas 4 números!");
		
		if(!c.getSenha().equals(senha))
			throw new Exception("Senha incorreta!");
		
		conta.debitar(valor);
		repositorio.salvarObjetos();
		
	}
	
	public static void transferirValor(int id1, String cpf, String senha, double valor, int id2) throws Exception {
		cpf = cpf.trim();
		senha = senha.trim();
		
		Correntista c = repositorio.localizarCorrentista(cpf);
		Conta contaOrigem = repositorio.localizarConta(id1);
		Conta contaDestino = repositorio.localizarConta(id2);
		
		if(valor <= 0)
			throw new Exception("Só é possível transferir valores maiores ou iguais a zero.");
		
		if (c == null)
			throw new Exception("Correntista não encontrado com o cpf: " + cpf);
		
		if (contaOrigem == null)
			throw new Exception("Conta de origem não encontrada com id: " + id1);
		
		if (contaDestino == null)
			throw new Exception("Conta de destino não encontrada com id: " + id2);
		
		Correntista c1 = contaOrigem.localizarCorrentista(cpf);
		if(c1 == null) {
			throw new Exception ("Correntista com o cpf: " + cpf + " não participa da conta com id=" + id1);
		}
		
		if(!senha.matches("^\\d{4}$"))
			throw new Exception("A senha deve ser numérica e com apenas 4 números!");
		
		if(!c.getSenha().equals(senha))
			throw new Exception("Senha incorreta!");
		
		contaOrigem.debitar(valor);
		contaDestino.creditar(valor);
		repositorio.salvarObjetos();
	}
	
	public static Conta localizarConta(int id) {
		Conta c = repositorio.localizarConta(id);
		return c;
	}
	

}