
public class Prato {

	private final int CUSTO;
	private final int LUCRO;
	private final int[] CUSTO_VALIDO = { 1 , 50 };
	private final int[] LUCRO_VALIDO = { 1 , 10000 };

	public Prato(int custo, int lucro) {
		this.CUSTO = custo;
		this.LUCRO = lucro;
	}

	public int getCusto() {
		return this.CUSTO;
	}

	public int getLucro() {
		return this.LUCRO;
	}

	private boolean isCustoValido() {
		if (this.CUSTO >= CUSTO_VALIDO[0] && this.CUSTO <= CUSTO_VALIDO[1])
			return true;
		return false;
	}

	private boolean isLucroValido() {
		if (this.LUCRO >= LUCRO_VALIDO[0] && this.LUCRO <= LUCRO_VALIDO[1])
			return true;
		return false;
	}
	
	public boolean isPratoValido() {
		if(isCustoValido() && isLucroValido())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return CUSTO + " " + LUCRO;
	}
}
