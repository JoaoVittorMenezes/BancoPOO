package appconsole;

/**
 * SI - POO - Prof. Fausto Ayres
 * Teste da Fachada
 * 
 */

import regras_negocio.Fachada;

public class Alterar2 {

	public Alterar2() {
		try {
			Fachada.creditarValor(1, "0001", "1111", 1000.0);
			Fachada.creditarValor(2, "0001", "1111", 1000.0);
			System.out.println("creditou ");
		} catch (Exception e) {
			System.out.println("--->" + e.getMessage());
		}

		try {
			Fachada.debitarValor (3,"0003", "3333", 1000.0);
			Fachada.transferirValor(1,"0001","1111", 1000.0, 2);
			System.out.println("debitou ");
		} catch (Exception e) {
			System.out.println("--->" + e.getMessage());
		}
		
	}

	public static void main(String[] args) {
		new Alterar2();
	}
}
