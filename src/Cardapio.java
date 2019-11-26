
public class Cardapio {

	private final int DIAS;
	private Prato[] PRATOS;
	private int numPratos = 0;
	private final int ORCAMENTO;
	private final int[] DIAS_VALIDOS = { 1 , 21 };
	private final int[] PRATOS_VALIDOS = { 1 , 50 };
	private final int[] ORCAMENTO_VALIDO = { 0 , 100 };
	private final int[] LUCRO_ORDENADO;
	private final int[] PRATOS_FEITOS;

	public Cardapio(int dias, int pratos, int orcamento) {
		this.DIAS = dias;
		this.PRATOS = new Prato[pratos];
		this.ORCAMENTO = orcamento;
		this.LUCRO_ORDENADO = new int[pratos];
		this.PRATOS_FEITOS = new int[dias];
		for(int i=0; i<pratos; i++) {
			LUCRO_ORDENADO[i] = -1;
		}
		for(int i=0; i<dias; i++) {
			PRATOS_FEITOS[i] = -1;
		}
	}

	public int getDias() {
		return this.DIAS;
	}

	public Prato getPrato(int i) {
		return this.PRATOS[i];
	}

	public Prato[] getPratos() {
		return this.PRATOS;
	}

	public int getNumPratos() {
		return this.numPratos;
	}

	public int getOrcamento() {
		return this.ORCAMENTO;
	}

	public int[] getLucroOrdenado() {
		return this.LUCRO_ORDENADO;
	}

	public int[] getPratosFeitos() {
		return PRATOS_FEITOS;
	}

	private boolean isDiasValidos() {
		if (this.DIAS >= DIAS_VALIDOS[0] && this.DIAS <= DIAS_VALIDOS[1])
			return true;
		return false;
	}

	private boolean isPratosValidos() {
		if (this.PRATOS.length >= PRATOS_VALIDOS[0] && this.PRATOS.length <= PRATOS_VALIDOS[1])
			return true;
		return false;
	}

	private boolean isOrcamentoValido() {
		if (this.ORCAMENTO >= ORCAMENTO_VALIDO[0] && this.ORCAMENTO <= ORCAMENTO_VALIDO[1])
			return true;
		return false;
	}
	
	public boolean isCardapioValido() {
		if(isDiasValidos() && isPratosValidos() && isOrcamentoValido())
			return true;
		return false;
	}
	
	public void newPrato(Prato prato) {
		if(this.numPratos < this.PRATOS.length) {
			this.PRATOS[this.numPratos] = prato;
			this.numPratos++;
			ordenaLucro(prato.getCusto(), prato.getLucro());
		}
	}

	public void newPratoFeito(int dia, int prato) {
		if(dia < this.PRATOS_FEITOS.length) {
			this.PRATOS_FEITOS[dia] = prato;
		}
	}
	private void ordenaLucro(int custo, int lucro) {
		int pos=0;
		if(numPratos == 1)
			LUCRO_ORDENADO[0] = 0;
		else {
			int i;
			for(i=0; i<numPratos-1; i++) {
				if(lucro > PRATOS[LUCRO_ORDENADO[i]].getLucro()) {
					pos = i;
					i=numPratos;
				} else if(lucro == PRATOS[LUCRO_ORDENADO[i]].getLucro()) {
					if(custo <= PRATOS[LUCRO_ORDENADO[i]].getCusto()) {
						pos = i;
						i=numPratos;
					}
				}
			}
			if(i==numPratos-1)
				pos = i;
			for(i=numPratos-1; i>pos; i--)
				LUCRO_ORDENADO[i] = LUCRO_ORDENADO[i-1];
			LUCRO_ORDENADO[pos] = numPratos-1;
		}
//		for(int i=0; i<numPratos; i++)
//			System.out.print(LUCRO_ORDENADO[i] + " ");
//		System.out.println();
	}

	public double lucro() {
		double lucroTotal = 0;
		for(int i=0; i<PRATOS_FEITOS.length; i++) {
			if(PRATOS_FEITOS[i] == -1)
				return 0.0;
			else {
				// existe ontem?
				if(i>0) {
					// o prato de hoje é o mesmo de ontem?
					if(PRATOS_FEITOS[i] == PRATOS_FEITOS[i-1]) {
						// existe anteontem?
						if(i>1) {
							// o prato de hoje é igual ao de anteontem mas diferente do de anteontem?
							if(PRATOS_FEITOS[i] != PRATOS_FEITOS[i-2]) {
								lucroTotal += PRATOS[LUCRO_ORDENADO[PRATOS_FEITOS[i]]].getLucro() * 0.5;
							}
						} else {
							lucroTotal += PRATOS[LUCRO_ORDENADO[PRATOS_FEITOS[i]]].getLucro() * 0.5;
						}
					} else {
						lucroTotal += PRATOS[LUCRO_ORDENADO[PRATOS_FEITOS[i]]].getLucro();
					}
				} else {
					lucroTotal += PRATOS[LUCRO_ORDENADO[PRATOS_FEITOS[i]]].getLucro();
				}
			}
		}
		return lucroTotal;
	}

	@Override
	public String toString() {
		StringBuilder pratos = new StringBuilder();
		for(int i=0; i<numPratos; i++)
			pratos.append(this.PRATOS[i] + "\n");
		return DIAS + " " + PRATOS.length + " " + ORCAMENTO + "\n" + pratos;
	}
}
