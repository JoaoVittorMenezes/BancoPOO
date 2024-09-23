package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import modelo.Conta;
import modelo.ContaEspecial;
import modelo.Correntista;


public class Repositorio {
	private ArrayList<Conta> contas = new ArrayList<>();
	private ArrayList<Correntista> correntistas  = new ArrayList<>(); 

	public Repositorio() {
		carregarObjetos();
	}
	public void adicionarConta(Conta c)	{
		contas.add(c);
	}

	public void remover(Conta c)	{
		contas.remove(c);
	}

	public Conta localizarConta(int id)	{
		for(Conta c : contas)
			if(c.getId() == id)
				return c;
		return null;
	}

	public void adicionarCorrentista(Correntista c)	{
		correntistas.add(c);
	}


	public Correntista localizarCorrentista(String cpf)	{
		for(Correntista c : correntistas)
			if(c.getCpf().equals(cpf))
				return c;
		return null;
	}
	public Correntista localizarCorrentistaNome(String nome)	{
		for(Correntista c : correntistas)
			if(c.getNome().equals(nome))
				return c;
		return null;
	}

	public ArrayList<Correntista> getCorrentistas() {
		return correntistas;
	}
	
	public ArrayList<Conta> getContas() 	{
		return contas;
	}

	public int getTotalContas()	{
		return contas.size();
	}

	public int getTotalCorrentistas()	{
		return correntistas.size();
	}

	public int gerarIdConta() {
		if (contas.isEmpty())
			return 1;
		else {
			Conta ultimo = contas.get(contas.size()-1);
			return ultimo.getId() + 1;
		}
	}
	
	
	public void carregarObjetos()  	{
		// carregar para o repositorio os objetos dos arquivos csv
		try {
			//caso os arquivos nao existam, serao criados vazios
			File f1 = new File( new File(".\\correntistas.csv").getCanonicalPath() ) ; 
			File f2 = new File( new File(".\\contas.csv").getCanonicalPath() ) ; 
			if (!f1.exists() || !f2.exists() ) {
				//System.out.println("criando arquivo .csv vazio");
				FileWriter arquivo1 = new FileWriter(f1); arquivo1.close();
				FileWriter arquivo2 = new FileWriter(f2); arquivo2.close();
				return;
			}
		}
		catch(Exception ex)		{
			throw new RuntimeException("criacao dos arquivos vazios:"+ex.getMessage());
		}

		String linha;	
		String[] partes;	
		Correntista co;
		Conta c;

		try	{
			String cpf, nome, senha;
			File f = new File( new File(".\\correntistas.csv").getCanonicalPath() )  ;
			Scanner arquivo1 = new Scanner(f);	 
			while(arquivo1.hasNextLine()) 	{
				linha = arquivo1.nextLine().trim();		
				partes = linha.split(";");	
				//System.out.println(Arrays.toString(partes));
				cpf = partes[0];
				nome = partes[1];
				senha = partes[2];
				co = new Correntista(cpf, nome, senha);
				this.adicionarCorrentista(co);
			} 
			arquivo1.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de correntistas:"+ex.getMessage());
		}

		try	{
			String tipo, data, saldo, limite = null, listaCorrentistas;
			File f = new File( new File(".\\contas.csv").getCanonicalPath())  ;
			Scanner arquivo2 = new Scanner(f);	 
			while(arquivo2.hasNextLine()) 	{
				linha = arquivo2.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				if (partes.length == 5) {
					tipo = partes[0];
					data = partes[1];
					saldo = partes[2];
					limite = partes[3];
					listaCorrentistas = partes[4];	
				}
				else {
					tipo = partes[0];
					data = partes[1];
					saldo = partes[2];
					listaCorrentistas = partes[3];	
				}
				
				String[] lista = listaCorrentistas.replace("[", "").replace("]", "").split(",");
				if(tipo.equals("CONTA")) {
					c = new Conta(data);
					this.adicionarConta(c);
					c.setSaldo(Double.parseDouble(saldo));
					for (int i=0; i < lista.length; i++){
						if (i == 0) { 
							co = localizarCorrentista(lista[i]);
							c.adicionar(co);
							co.adicionar(c);
							co.setTitular(true);
							}
						else {
							co = localizarCorrentista(lista[i]);
							c.adicionar(co);
							co.adicionar(c);
						}
					}
					
				}
				else {
					
					c = new ContaEspecial(data, Double.parseDouble(limite));
					this.adicionarConta(c);
					c.setSaldo(Double.parseDouble(saldo));
					for (int i=0; i < lista.length; i++){
						if (i == 0) { 
							co = localizarCorrentista(lista[i]);
							c.adicionar(co);
							co.adicionar(c);
							co.setTitular(true);
							}
						else {
							co = localizarCorrentista(lista[i]);
							c.adicionar(co);
							co.adicionar(c);
						}
						
					}
						
				}
			}
			arquivo2.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de contas:"+ex.getMessage());
		}
	}

	//--------------------------------------------------------------------
	public void	salvarObjetos()  {
		//gravar nos arquivos csv os objetos que est�o no reposit�rio
		try	{
			File f = new File( new File(".\\correntistas.csv").getCanonicalPath())  ;
			FileWriter arquivo1 = new FileWriter(f); 
			for(Correntista co : correntistas) 	{
				arquivo1.write(co.getCpf()+";"+co.getNome()+";"+co.getSenha() + "\n");	
			} 
			arquivo1.close();
		}
		catch(Exception e){
			throw new RuntimeException("problema na cria��o do arquivo correntistas "+e.getMessage());
		}

		try	{
			File f = new File( new File(".\\contas.csv").getCanonicalPath())  ;
			FileWriter arquivo2 = new FileWriter(f) ; 
			ArrayList<String> lista ;
			for(Conta c : contas) {
				lista = new ArrayList<>();
				for(Correntista co : c.getCorrentistas()) {
					lista.add(co.getCpf());
				}
				

				if(c instanceof ContaEspecial c1 )
					arquivo2.write("CONTAESPECIAL;" + c1.getData() +";" 
							+ c1.getSaldo() +";"+ c1.getLimite() +";"+ lista +"\n");	
				else
					arquivo2.write("CONTA;" + c.getData() +";" 
							+ c.getSaldo() +";"+ lista +"\n");	

			} 
			arquivo2.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na cria��o do arquivo contas "+e.getMessage());
		}

	}
}