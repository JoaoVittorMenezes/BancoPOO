package modelo;

public class ContaEspecial extends Conta{
	private double limite;

	public ContaEspecial(String data, double limite) {
		super(data);
		this.limite = limite;
		
	}

	public void debitar(double valor) throws Exception{
		if (saldo + limite >= valor) {
			this.saldo -= valor;
		}
		else
			throw new Exception("Saldo insuficiente!");
	}
	
	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite = limite;
	}
	
	public String toString() {
        String texto =  "id=" + id + ", data=" + data + ", saldo=" + saldo + ", limite=" + limite;

        texto += ", correntistas:";
        for(Correntista co : getCorrentistas())
            texto += co.getCpf() + ",";
        return texto;
    }
	
	
	
}
