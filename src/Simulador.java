/* Fila 1 - G/G/2/3, chegadas entre 1..4, atendimento entre 3..4
 * Fila 2 - G/G/1/5, atendimento entre 2..3
 * 
 * Para a simula��o, considere inicialmente as filas vazias e o primeiro cliente chega no tempo 1,5.
 * Realizem a simula��o com 100.000 aleat�rios, ou seja, ao se utilizar o 100.000 aleat�rio, asimula��o deve
 * se encerrar e a distribui��o de probabilidades, bem como os tempos acumulados para os estados de cada fila
 * devem ser reportados. Al�m disso, indique o n�mero de perda de clientes (caso tenha havido perda) de cada
 * fila e o tempo global da simula��o.
 * 
 * A/B/C/K/N/D
 * A: distribui��o do intervalo de chegada
 * B: distribui��o do intervalo de sa�da
 * C: servidores da fila
 * K: capacidade total da fila
 * N: tamanho da popula��o
 * D: pol�tica de atendimento de clientes
 */
import java.lang.Math;

public class Simulador {

	public static void main(String[] args)
	{

		double tempoGlobal = 0; 
		int count = 100000; //Vari�vel de controle da simula��o

		//Iniciar a fila
		//Ordem dos par�metros: Servidores, Capacidade, Intervalos M�dio de chegada e Intervalos m�dios de Sa�da
		Fila fila1 = new Fila(2, 3, 1.0, 4.0, 3.0, 4.0);
		System.out.println("Estado inicial da fila 1: " + fila1.toString());
		Fila fila2 = new Fila(1, 5, 0.0, 0.0, 2.0, 3.0);
		System.out.println("Estado inicial da fila 2: " + fila2.toString()+"\n");

		//Par�metros do Gerador de N�meros Pseudoaleat�rios
		double seed = 2.0;
		double a = 2145853745.0;
		double c = 1432159778.0;
		double M = Math.pow(2, 32);
		Gerador gerador = new Gerador(seed, a, c, M); //Iniciar o Gerador

		Evento eventoInicial = new Evento (1.5, "CH"); //Evento inicial da Simula��o
		Escalonador escalonador = new Escalonador(); //Iniciar escalonador 
		escalonador.eventoQueue.add(eventoInicial); //Evento inicial adicionado ao Escalonador

		for (int i = 0; i < count; i++)
		{
			System.out.println("Itera�ao: " + i);

			//Ver qual o pr�ximo evento do escalonador (o elemento no topo da Priority Queue)
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTempo(), escalonador.eventoQueue.poll().getTipo());
			System.out.println("Evento do escalonador: " + eventoAux.toString());

			//EVENTO DE CHEGADA
			if (eventoAux.getTipo() == "CH")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.println("Pode entrar na fila 1? " + (fila1.getStatus() < fila1.getCapacidade()));

				if (fila1.getStatus() < fila1.getCapacidade())
				{
					System.out.println("Cliente entrou na fila 1.");
					System.out.println("Acumular varia��o de tempo (" + deltaTempo + ") no estado " + fila1.getStatus() + " da fila 1.");
					fila1.in(deltaTempo);
					System.out.println("Incrementar fila 1 -> Clientes: " + fila1.getStatus());
					System.out.println("Fila 1: " + fila1.toString());
					
					System.out.print("Est� de frente para o servidor? " + (fila1.getStatus() <= fila1.getServidores()) + " -> ");

					if(fila1.getStatus() <= fila1.getServidores())
					{
						//Para Filas em Tandem, agendamos uma Passagem ao inv�s de uma Sa�da.
						System.out.println("Agendar uma Passagem de cliente. ");
						double nps = gerador.next();
						System.out.println(" NPS gerado: " + nps);
						double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
						//Evento e1: Passagem de cliente gerada pelo algoritmo de chegada
						Evento e1 = new Evento ((tempoGlobal + sorteio), "PA");
						escalonador.eventoQueue.add(e1);
						System.out.println("Evento gerado: " + e1.toString());
					}
					else
					{
						System.out.println("N�o h� servidor dispon�vel para atender na fila 1.");
					}
				} 
				else
				{
					//N�o p�de entrar na fila
					fila1.loss();
					System.out.println("N�o p�de entrar na fila 1. Perdas contabilizadas: " + fila1.getPerdas());
				}
				System.out.println("Fim do algoritmo. Agendar uma nova Chegada. ");
				double nps = gerador.next();
				System.out.println(" NPS gerado: " + nps);
				double sorteio =  fila1.chegadaMin + ((fila1.chegadaMax - fila1.chegadaMin) * nps);
				//Evento e2: chegada gerada pelo algoritmo de chegada
				Evento e2 = new Evento ((tempoGlobal + sorteio), "CH");
				escalonador.eventoQueue.add(e2);
				System.out.println("Evento gerado: " + e2.toString());
			}

			//EVENTO DE SAIDA
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo(); 
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.println("Acumular varia��o de tempo (" + deltaTempo + ") no estado " + fila2.getStatus() + " da fila 2. ");
				fila2.out(deltaTempo);
				System.out.println("Cliente saiu. Estado atual da fila: " + fila2.getStatus());
				System.out.println("Fila 2: " + fila2.toString());

				System.out.print("H� mais clientes para atender na fila 2? " + (fila2.getStatus() >= fila2.getServidores()) + " -> ");
				if (fila2.getStatus() >= fila2.getServidores())
				{
					System.out.println("Agendar uma Sa�da.");
					double nps = gerador.next();
					System.out.println(" NPS gerado: " + nps);
					double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
					//Evento e3: sa�da gerada pelo algoritmo de sa�da.
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA");
					escalonador.eventoQueue.add(e3);
					System.out.println("Evento gerado: " + e3.toString());
				}
				else
				{
					System.out.println((fila2.getStatus() >= fila2.getServidores()) + ": n�o � necess�rio agendar mais sa�das.");
				}
				System.out.println("Fim do algoritmo de sa�da.");
			}

			//EVENTO DE PASSAGEM
			else if (eventoAux.getTipo() == "PA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);

				System.out.println("Acumular varia��o de tempo (" + deltaTempo + ") no estado " + fila1.getStatus() + " da fila 1. ");
				fila1.out(deltaTempo);
				System.out.println("Cliente saiu da fila 1. Estado atual: " + fila1.getStatus());
				System.out.println("Fila 1: " + fila1.toString());
				
				System.out.println("Precisa agendar mais passagens da fila 1? " + (fila1.getStatus() >= fila1.getServidores()));
				if(fila1.getStatus() >= fila1.getServidores())
				{
					System.out.println("Agendar uma Passagem de cliente. ");
					double nps = gerador.next();
					System.out.println(" NPS gerado: " + nps);
					double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
					//Evento e4: passagem de clientes gerada pelo algoritmo de passagem
					Evento e4 = new Evento ((tempoGlobal + sorteio), "PA");
					escalonador.eventoQueue.add(e4);
					System.out.println("Evento gerado: " + e4.toString());
				}

				//Chegada na fila 2
				System.out.println("Pode entrar na fila 2? " + (fila2.getStatus() < fila2.getCapacidade()));
				if (fila2.getStatus() < fila2.getCapacidade())
				{
					System.out.println("Cliente entrou na fila 2.");
					System.out.println("Acumular varia��o de tempo (" + deltaTempo + ") no estado " + fila2.getStatus() + " da fila 2. ");
					fila2.in(deltaTempo);
					System.out.println("Incrementar fila 2 -> clientes: " + fila2.getStatus());
					System.out.println("Fila 2: " + fila2.toString());
					
					System.out.print("Est� de frente para o servidor? " + (fila2.getStatus() <= fila2.getServidores()) + " -> ");

					if(fila2.getStatus() <= fila2.getServidores())
					{
						System.out.println("Agendar uma Sa�da. ");
						double nps = gerador.next();
						System.out.println(" NPS: " + nps);
						double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
						//Evento e5: Sa�da de cliente gerada pelo algoritmo de sa�da
						Evento e5 = new Evento ((tempoGlobal + sorteio), "SA");
						escalonador.eventoQueue.add(e5);
						System.out.println("Evento gerado: " + e5.toString());
					}
					else
					{
						System.out.println("N�o h� servidor dispon�vel para atender na fila 2.");
					}
				} 
				else
				{
					//N�o p�de entrar na fila 2
					fila2.loss();
					System.out.println("N�o p�de entrar na fila 2. Perdas contabilizadas: " + fila2.getPerdas());
				}
			}
			System.out.println("");

		}//End loop principal da Simula��o

		System.out.println("Fim da simula��o.\n");
		System.out.println("Distribui��o de Probabilidades da Fila 1: \n");

		for (int i = 0; i < fila1.estados.length; i++) {
			System.out.println
			("Estado["+i+"]"
					+ " Tempo: " + (String.format("%,.2f", fila1.estados[i]))
					+ " Probabilidade: "+(String.format ("%,.10f",(fila1.estados[i] / tempoGlobal)))
					);
		}
		
		System.out.println("\n" + fila1.toString() + "\n");

		System.out.println("Distribui��o de Probabilidades da Fila 2: \n");
		

		for (int i = 0; i < fila2.estados.length; i++) {
			System.out.println
			("Estado["+i+"]"
					+ " Tempo: " + (String.format("%,.2f", fila2.estados[i]))
					+ " Probabilidade: "+(String.format ("%,.10f",(fila2.estados[i] / tempoGlobal)))
					);
		}

		System.out.println("\n" + fila2.toString() + "\n");

		
		System.out.println("Tempo Global de Simula��o: " + tempoGlobal);

	}//End main

}//End class
