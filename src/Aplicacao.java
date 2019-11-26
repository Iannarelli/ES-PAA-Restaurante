import java.util.Scanner;

public class Aplicacao {
	
	private static final Scanner LEITOR = new Scanner(System.in);
	private static Cardapio[] cardapios = new Cardapio[0];

	public static void main(String[] args) {
		System.out.println("Otimiza��o de Troco - Paradigma Guloso\n");
		System.out.println("Orienta��es:\n- Para cada op��o de card�pio, informar em uma linha o "
				+ "n�mero de dias (de 1 a 21), o n�mero de pratos poss�veis para cozinhar (de 1 a "
				+ "50)\n\te o valor dispon�vel (de 0 a 100). Nas linhas subsequentes, forne�a "
				+ "informa��es dos pratos dispon�veis, sendo o seu custo e o seu lucro,\n\tsendo "
				+ "um prato em cada linha.\n- As informa��es fornecidas devem representar n�meros "
				+ "inteiros e separadas, quando na mesma linha, por um espa�o. Para novas op��es "
				+ "de card�pio,\n\tinformar a mesma sequ�ncia de dados ap�s a linha do �ltimo prato "
				+ "dispon�vel do card�pio. Para encerrar a entrada de dados, forne�a um\n\tcard�pio "
				+ "em que os tr�s par�metros sejam 0.\n\nExemplo:\n2 3 50\t(2 dias, 3 pratos "
				+ "dispon�veis, 50 para custeio)\n2 3\t(1� prato, 2 de custo, 3 de lucro)\n5 1\t(2� "
				+ "prato, 5 de custo, 1 de lucro)\n4 8\t(3� prato, 4 de custo, 8 de lucro\n...\t"
				+ "(caso haja mais card�pios, repetir o padr�o de informa��es)\n0 0 0\t(para "
				+ "encerrar a entrada de dados)\n\nVamos l�? Pode come�ar:");
		boolean canExit;
		do {
			canExit = false;
			System.out.printf("Card�pio %d: ", cardapios.length+1);
			String linha = LEITOR.nextLine();
			String[] entrada = linha.split(" ");
			if (entrada.length != 3)
				System.out.println("Entrada inv�lida, tente novamente: dias pratos or�amento");
			else {
				try {
					int[] dados = new int[3];
					for (int i = 0; i < 3; i++)
						dados[i] = Integer.parseInt(entrada[i]);
					if (dados[0] == 0 && dados[1] == 0 && dados[2] == 0) {
						canExit = true;
					} else {
						Cardapio cardapio = new Cardapio(dados[0], dados[1], dados[2]);
						if(cardapio.isCardapioValido()) {
							montaPratos(cardapio, dados[1]);
							Cardapio[] cardapiosAux = new Cardapio[cardapios.length+1];
							if(cardapios.length > 0) {
								for(int i=0; i<cardapios.length; i++)
									cardapiosAux[i] = cardapios[i];
							}
							cardapiosAux[cardapiosAux.length-1] = cardapio;
							cardapios = cardapiosAux;
						} else
							System.out.println("Valores informados fora dos limites permitidos. "
									+ "Tente novamente: dias pratos or�amento");
					}
				} catch (NumberFormatException nf) {
					System.out.println(
							"Algum dado informado n�o est� no padr�o esperado. Tente novamente: dias pratos orcamento");
				}
			}
		} while (!canExit);
		for(int i=0; i<cardapios.length; i++) {
//			System.out.println(cardapios[i].toString());
			if(lucroMaximo(cardapios[i])) {
				System.out.println("\nLucro m�ximo: " + cardapios[i].lucro());
				System.out.print("Sequ�ncia de pratos, por dia: ");
				for(int j=0; j<cardapios[i].getPratosFeitos().length; j++) {
					System.out.print(cardapios[i].getLucroOrdenado()[cardapios[i].getPratosFeitos()[j]]+1 + " ");
				}
				System.out.println();
			} else {
				System.out.print("\n0.0\n");
			}
		}
	}

	public static void montaPratos(Cardapio cardapio, int pratos) {
		for(int i = cardapio.getNumPratos(); i<pratos; i++) {
			System.out.printf("Prato %d: ", i+1);
			String linha = LEITOR.nextLine();
			String[] entrada = linha.split(" ");
			if (entrada.length != 2) {
				System.out.println("Entrada inv�lida, tente novamente: custo lucro");
				i--;
			}
			else {
				try {
					int custo = Integer.parseInt(entrada[0]);
					int lucro = Integer.parseInt(entrada[1]);
					Prato prato = new Prato(custo, lucro);
					if(prato.isPratoValido()) {
						cardapio.newPrato(prato);						
					} else {
						System.out.println("Valores informados fora dos limites permitidos. "
								+ "Tente novamente: custo lucro");
						i--;
					}
				} catch (NumberFormatException nf) {
					System.out.println(
							"Algum dado informado n�o est� no padr�o esperado. Tente novamente: custo lucro");
					i--;
				}
			}
		}
	}
	
	public static boolean lucroMaximo(Cardapio cardapio) {
		int orcamento = 0;

		int dias = cardapio.getDias();
		int numPratos = cardapio.getNumPratos();
		Prato[] pratos = cardapio.getPratos();
		int[] lucroOrdenado = cardapio.getLucroOrdenado();

		int i;
		for(i=0; i<dias; i++) {
			int j;
			for(j=0; j<numPratos; j++) {
				int custo = pratos[lucroOrdenado[j]].getCusto();
				int lucro = pratos[lucroOrdenado[j]].getLucro();
				if(orcamento + custo <= cardapio.getOrcamento()) {
					// existe ontem?
					if(i>0) {
						// o prato de maior lucro � igual ao de ontem?
						if(j == cardapio.getPratosFeitos()[i-1]) {
							// existe anteontem? 
							if(i>1) {
								// o prato atual � igual ao prato de anteontem?
								if(j == cardapio.getPratosFeitos()[i-2]) {
									// existem outros pratos?
									if(j < numPratos-1) {
										int x = j+1;
										// verifica os pr�ximos pratos
										while(x < numPratos) {
											// o pr�ximo prato ainda mant�m dentro do or�amento?
											if(orcamento + pratos[lucroOrdenado[x]].getCusto() <= cardapio.getOrcamento()) {
												cardapio.newPratoFeito(i, x);
												orcamento += custo;
												x = numPratos+1;
												j = numPratos;
											} else {
												x++;
											}
										}
										// verificou os pr�ximos pratos e n�o h� um de melhor lucro dentro do or�amento?
										if(x == numPratos) {
											cardapio.newPratoFeito(i, j);
											orcamento += custo;
											j = numPratos;
										}
									// n�o existem outros pratos
									} else {
										cardapio.newPratoFeito(i, j);
										orcamento += custo;
										j = numPratos;
									}
								// o prato atual n�o � igual ao prato de anteontem, mas igual ao de ontem
								} else {
									// existem outros pratos?
									if(j < numPratos-1) {
										int x = j+1;
										// verifica os pr�ximos pratos
										while(x < numPratos) {
											// a metade do lucro atual � melhor do que do pr�ximo prato? 
											if(lucro*0.5 > pratos[lucroOrdenado[x]].getLucro()) {
												cardapio.newPratoFeito(i, j);
												orcamento += custo;
												x = numPratos+1;
												j = numPratos;
											} else {
												// o custo ainda est� dentro do or�amento?
												if(orcamento + pratos[lucroOrdenado[x]].getCusto() <= cardapio.getOrcamento()) {
													cardapio.newPratoFeito(i, x);
													orcamento += custo;
													x = numPratos+1;
													j = numPratos;
												} else {
													x++;
												}
											}
										}
										// verificou os pr�ximos pratos e n�o h� um de melhor lucro sem estourar o or�amento?
										if(x == numPratos) {
											cardapio.newPratoFeito(i, j);
											orcamento += custo;
											j = numPratos;
										}
									} else {
										cardapio.newPratoFeito(i, j);
										orcamento += custo;
										j = numPratos;
									}
								}
							// n�o existe anteontem, mas o prato atual � igual ao prato de ontem
							} else {
								// existem outros pratos?
								if(j < numPratos-1) {
									int x = j+1;
									// verifica os pr�ximos pratos
									while(x < numPratos) {
										// a metade do lucro atual � melhor do que do pr�ximo prato? 
										if(lucro*0.5 > pratos[lucroOrdenado[x]].getLucro()) {
											cardapio.newPratoFeito(i, j);
											orcamento += custo;
											x = numPratos+1;
											j = numPratos;
										} else {
											// o custo ainda est� dentro do or�amento?
											if(orcamento + pratos[lucroOrdenado[x]].getCusto() <= cardapio.getOrcamento()) {
												cardapio.newPratoFeito(i, x);
												orcamento += custo;
												x = numPratos+1;
												j = numPratos;
											} else {
												x++;
											}
										}
									}
									// verificou os pr�ximos pratos e n�o h� um de melhor lucro sem estourar o or�amento?
									if(x == numPratos) {
										cardapio.newPratoFeito(i, j);
										orcamento += custo;
										j = numPratos;
									}
								} else {
									cardapio.newPratoFeito(i, j);
									orcamento += custo;
									j = numPratos;
								}
							}
						// o prato atual n�o foi feito ontem
						} else {
							cardapio.newPratoFeito(i, j);
							orcamento += custo;
							j = numPratos;
						}
					// n�o existem ontem, � o primeiro dia!
					} else {
						cardapio.newPratoFeito(i, j);
						orcamento += custo;
						j = numPratos;
					}
				}
			}
			if(j == numPratos)
				i = dias;
		}
		if(i == dias)
			return true;
		else
			return false;
	}
}
