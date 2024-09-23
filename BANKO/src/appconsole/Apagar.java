package appconsole;

/**
 * SI - POO - Prof. Fausto Ayres
 * Teste da Fachada
 * 
 */

import regras_negocio.Fachada;

public class Apagar {

	public Apagar() {
		try {
			Fachada.apagarConta(1);		
			System.out.println("apagou conta");
		} catch (Exception e) {
			System.out.println("--->"+e.getMessage());
		}	
	}

	public static void main(String[] args) {
		new Apagar();
	}
}
