/* Fila 1 - G/G/2/3, chegadas entre 1..4, atendimento entre 3..4
 * Fila 2 - G/G/1/5, atendimento entre 2..3
 * 
 * Para a simulação, considere inicialmente as filas vazias e o primeiro cliente chega no tempo 1,5.
 * Realizem a simulação com 100.000 aleatórios, ou seja, ao se utilizar o 100.000 aleatório, asimulação deve
 * se encerrar e a distribuição de probabilidades, bem como os tempos acumulados para os estados de cada fila
 * devem ser reportados. Além disso, indique o número de perda de clientes (caso tenha havido perda) de cada
 * fila e o tempo global da simulação.
 * 
 * A/B/C/K/N/D
 * A: distribuição do intervalo de chegada
 * B: distribuição do intervalo de saída
 * C: servidores da fila
 * K: capacidade total da fila
 * N: tamanho da população
 * D: política de atendimento de clientes
 */
import java.lang.Math;

public class Simulador {

	public static void main(String[] args)
	{
		//Variáveis de controle da simulação
		double tempoGlobal = 0; 
		int k = 0;
		int count = 100000;
		
		//Iniciar a fila. Ordem dos parâmetros: Servidores, Capacidade, Intervalos Médio de chegada e Intervalos médios de Saída
		Fila fila1 = new Fila(2, 3, 1.0, 4.0, 3.0, 4.0);
		System.out.println("Estado inicial da Fila 1: " + fila1.toString());
		Fila fila2 = new Fila(1, 5, 0.0, 0.0, 2.0, 3.0);
		System.out.println("Estado inicial da Fila 2: " + fila2.toString()+"\n");

		//Iniciar o gerador e seus parâmetros.
		double seed = 2.0;
		double a = 2145853745.0;
		double c = 1432159778.0;
		double M = Math.pow(2, 32);
		Gerador gerador = new Gerador(seed, a, c, M);

		Evento eventoInicial = new Evento (1.5, "CH"); //Evento inicial da Simulação

		Escalonador escalonador = new Escalonador(); //Iniciar escalonador 
		escalonador.eventoQueue.add(eventoInicial); //Evento inicial adicionado ao Escalonador

		//Loop principal da Simulação
		while (gerador.count < 100000)
		{
			System.out.println("Iteração: " + (gerador.count));
			//Verificar o próximo evento do Escalonador.
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTempo(), escalonador.eventoQueue.poll().getTipo());
			System.out.println("Evento do escalonador: " + eventoAux.toString());

			//EVENTO DE CHEGADA
			if (eventoAux.getTipo() == "CH")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.print("Pode entrar na fila 1? " + (fila1.getStatus() < fila1.getCapacidade()) + " -> ");

				if (fila1.getStatus() < fila1.getCapacidade())
				{
					System.out.println("Cliente entrou na fila 1.");
					System.out.println("Acumular variação de tempo (" + deltaTempo + ") no estado " + fila1.getStatus() + " da fila 1.");
					fila1.in(deltaTempo);
					System.out.println("Incrementar fila 1 -> Clientes: " + fila1.getStatus());
					System.out.println("Fila 1: " + fila1.toString());
					System.out.print("Está de frente para um servidor? " + (fila1.getStatus() <= fila1.getServidores()) + " -> ");

					if(fila1.getStatus() <= fila1.getServidores())
					{
						//Para Filas em Tandem, agendamos uma Passagem ao invés de uma Saída.
						System.out.println("Agendar uma Passagem de cliente.");
						double nps = gerador.next();
						System.out.println(" NPS gerado: " + nps);
						double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
						Evento e1 = new Evento ((tempoGlobal + sorteio), "PA"); //Evento e1: Passagem de cliente gerada pelo algoritmo de chegada.
						escalonador.eventoQueue.add(e1); //Evento adicionado ao Escalonador.
						System.out.println("Evento gerado: " + e1.toString());
					}
					else
					{
						System.out.println("Não há servidor disponível para atender na fila 1.");
					}
				}
				
				else
				{
					//Não pôde entrar na fila
					fila1.loss();
					System.out.println("Não pôde entrar na fila 1. Perdas contabilizadas: " + fila1.getPerdas());
				}
				
				//Fim do algoritmo de Chegada. Agendar uma nova Chegada.
				System.out.println("Fim do algoritmo. Agendar uma nova Chegada. ");
				double nps = gerador.next();
				System.out.println(" NPS gerado: " + nps);
				double sorteio =  fila1.chegadaMin + ((fila1.chegadaMax - fila1.chegadaMin) * nps);
				Evento e2 = new Evento ((tempoGlobal + sorteio), "CH"); //Evento de chegada gerado pelo fim do algoritmo de chegada.
				escalonador.eventoQueue.add(e2); //Evento adicionado ao Escalonador.
				System.out.println("Evento gerado: " + e2.toString()+"\n");
			}

			//EVENTO DE SAÍDA
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo(); 

				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.println("Acumular variação de tempo (" + deltaTempo + ") no estado " + fila2.getStatus() + " da fila 2. ");
				fila2.out(deltaTempo);
				System.out.println("Cliente saiu. Estado atual da fila: " + fila2.getStatus());
				System.out.println("Fila 2: " + fila2.toString());

				System.out.print("Ainda há cliente de frente para um servidor disponível? " + (fila2.getStatus() >= fila2.getServidores()) + " -> ");
				if (fila2.getStatus() >= fila2.getServidores())
				{
					System.out.println("Agendar uma Saída.");
					double nps = gerador.next();
					System.out.println(" NPS gerado: " + nps);
					double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA"); //Evento e3: saída gerada pelo algoritmo de saída.
					escalonador.eventoQueue.add(e3); //Evento adicionado ao Escalonador.
					System.out.println("Evento gerado: " + e3.toString());
				}
				else
				{
					System.out.println("Não é necessário agendar mais saídas.");
				}

				System.out.println("Fim do algoritmo de saída.\n");
			}//Fim do algoritmo de Saída.
			

			//EVENTO DE PASSAGEM
			else if (eventoAux.getTipo() == "PA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);

				//Saída da fila 1.
				System.out.println("Cliente saiu na fila 1.");
				System.out.println("Acumular variação de tempo (" + deltaTempo + ") no estado " + fila1.getStatus() + " da fila 1.");
				fila1.out(deltaTempo);
				System.out.println("Decrementar fila 1 -> Clientes: " + fila1.getStatus());
				System.out.println("Fila 1: " + fila1.toString());

				System.out.print("Precisa agendar mais passagens da Fila 1? " + (fila1.getStatus() >= fila1.getServidores()) + " -> ");
				if(fila1.getStatus() >= fila1.getServidores())
				{
					System.out.println("Agendar uma Passagem de cliente.");
					double nps = gerador.next();
					System.out.println(" NPS gerado: " + nps);
					double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
					Evento e4 = new Evento ((tempoGlobal + sorteio), "PA"); //Evento e4: Passagem de clientes gerada pelo algoritmo de passagem.
					escalonador.eventoQueue.add(e4);
					System.out.println("Evento gerado: " + e4.toString());
				}
				else
				{
					System.out.println("Não é necessário agendar mais passagens.");
				}

				//Chegada na fila 2.
				System.out.print("Pode entrar na fila 2? " + (fila2.getStatus() < fila2.getCapacidade()) + " -> ");
				if (fila2.getStatus() < fila2.getCapacidade())
				{
					System.out.println("Cliente entrou na fila 2.");
					System.out.println("Acumular variação de tempo (" + deltaTempo + ") no estado " + fila2.getStatus() + " da fila 2. ");
					fila2.in(deltaTempo);
					System.out.println("Incrementar fila 2 -> Clientes: " + fila2.getStatus());
					System.out.println("Fila 2: " + fila2.toString());

					System.out.print("Está de frente para o servidor? " + (fila2.getStatus() <= fila2.getServidores()) + " -> ");
					if(fila2.getStatus() <= fila2.getServidores())
					{
						System.out.println("Agendar uma Saída. ");
						double nps = gerador.next();
						System.out.println(" NPS: " + nps);
						double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
						Evento e5 = new Evento ((tempoGlobal + sorteio), "SA"); //Evento e5: Saída de cliente gerada pelo algoritmo de saída. 
						escalonador.eventoQueue.add(e5); //Evento adicionado ao Escalonador.
						System.out.println("Evento gerado: " + e5.toString());
					}
					else
					{
						System.out.println("Não há servidor disponível para atender na fila 2.");
					}

				}

				else
				{
					//Não pôde entrar na fila 2
					fila2.loss();
					System.out.println("Não pôde entrar na fila 2. Perdas contabilizadas: " + fila2.getPerdas());
				}

				System.out.println("Fim do algoritmo de Passagem.\n");
			}//Fim do algoritmo de Passagem.

		}//Fim do loop principal da Simulação.
		
		//Exibição dos Resultados da Simulação.
		System.out.println("Resultados da simulação.\n");
		System.out.println("Total de iterações: " + k + "\n");
		System.out.println("Distribuição de Probabilidades da Fila 1: \n");

		for (int i = 0; i < fila1.estados.length; i++) {
			System.out.println
			("Estado["+i+"] Tempo: "+(String.format("%,.2f", fila1.estados[i]))+" Probabilidade: "+(String.format ("%,.10f",(fila1.estados[i] / tempoGlobal))));
		}

		System.out.println("\nFila 1: " + fila1.toString() + "\n");
		System.out.println("Distribuição de Probabilidades da Fila 2: \n");

		for (int i = 0; i < fila2.estados.length; i++) {
			System.out.println
			("Estado["+i+"] Tempo: " + (String.format("%,.2f", fila2.estados[i]))+" Probabilidade: "+(String.format ("%,.10f",(fila2.estados[i] / tempoGlobal))));
		}

		System.out.println("\nFila 2: " + fila2.toString() + "\n");
		System.out.println("Tempo Global de Simulação: " + tempoGlobal);

	}//Fim da main.

}//Fim da classe.