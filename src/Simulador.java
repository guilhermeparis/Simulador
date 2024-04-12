/* Simula��o 1: G/G/1/5 Chegada: 2..5 Sa�da: 3..5
 * Simula��o 2: G/G/2/5 Chegada: 2..5 Sa�da: 3..5
 * 
 * Para ambas simula��es, considere inicialmente a fila vazia e o primeiro cliente chega no tempo 2,0.
 * Realize a simula��o com 100.000 aleat�rios, ou seja, ao se utilizar o 100.000 aleat�rio, sua simula��o deve se encerrar e a
 * distribui��o de probabilidades, bem como os tempos acumulados para os estados da fila devem ser reportados. Al�m disso,
 * indique o n�mero de perda de clientes (caso tenha havido perda) e o tempo global da simula��o.
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

		Fila fila = new Fila(1, 5); //Iniciar a fila
		System.out.println("Estado inicial da fila: " + fila.toString()+"\n");

		double saidaT0 = 3.0, saidaT1 = 5.0, chegadaT0 = 2.0, chegadaT1 = 5.0; //Intervalos m�dios de chegada e de sa�da

		double seed = 2.0, a = 2145853745, c = 1432159778, M = Math.pow(2, 32); //Par�metros do Gerador de N�meros Pseudoaleat�rios
		Gerador gerador = new Gerador(seed, a, c, M); //Iniciar o Gerador

		Escalonador escalonador = new Escalonador(); //Iniciar escalonador

		Evento eventoInicial = new Evento (2.0, "CH"); //Evento inicial da Simula��o 
		escalonador.eventoQueue.add(eventoInicial); //Evento inicial adicionado ao Escalonador

		for (int i = 0; i < count; i++)
		{
			System.out.println("Itera�ao: " + i);

			//Ver qual o pr�ximo evento do escalonador (o elemento no topo da Priority Queue)
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTempo(), escalonador.eventoQueue.poll().getTipo());
			System.out.println("Pr�ximo evento do escalonador: " + eventoAux.toString());

			//Verificar se o pr�ximo Evento � CHEGADA ou SA�DA
			if (eventoAux.getTipo() == "CH")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				System.out.println("Varia��o de tempo: " + deltaTempo);
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.println("Pode entrar na fila (Status=" + fila.getStatus() + " < Capacidade=" + fila.getCapacidade() + ")? " + (fila.getStatus() < fila.getCapacidade()));

				if (fila.getStatus() < fila.getCapacidade())
				{	
					System.out.print("Acumular tempo no estado " + fila.getStatus() + " da fila: ");
					fila.in(deltaTempo);
					System.out.println("Incrementar fila -> clientes: " + fila.getStatus());
					System.out.print("Est� de frente para o servidor? " + (fila.status <= fila.servidores) + " -> ");

					if(fila.status <= fila.servidores)
					{
						System.out.println("Agendar sa�da. ");
						double nps = gerador.next();
						System.out.println("N�mero Pseudoaleat�rio gerado: " + nps + ". ");
						double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
						//Evento e1: sa�da gerada pelo algoritmo de chegada
						Evento e1 = new Evento ((tempoGlobal + sorteio), "SA");
						escalonador.eventoQueue.add(e1);
						System.out.println("Evento gerado: " + e1.toString());
					}
					else
					{
						System.out.println("N�o h� servidor dispon�vel para atender.");
					}
				} 
				else
				{
					//N�o p�de entrar na fila
					fila.loss();
					System.out.println("N�o p�de entrar na fila. Perdas contabilizadas: " + fila.getPerdas());
				}
				System.out.println("Fim do algoritmo. Agendar nova chegada. ");
				double nps = gerador.next();
				System.out.println("N�mero Pseudoaleat�rio gerado: " + nps + ". ");
				double sorteio =  chegadaT0 + ((chegadaT1 - chegadaT0) * nps);
				//Evento e2: chegada gerada pelo algoritmo de chegada
				Evento e2 = new Evento ((tempoGlobal + sorteio), "CH");
				escalonador.eventoQueue.add(e2);
				System.out.println("Evento gerado: " + e2.toString());
			}
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				System.out.println("Varia��o de tempo: " + deltaTempo);
				tempoGlobal = eventoAux.getTempo(); 
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.print("Acumular tempo no estado " + fila.getStatus() + " da fila: ");
				fila.out(deltaTempo);
				System.out.println("Cliente saiu. Estado atual da fila: " + fila.getStatus());

				System.out.print("H� mais clientes para atender? ");
				if (fila.status >= fila.servidores)
				{
					System.out.println((fila.status >= fila.servidores) + ": Agendar uma sa�da.");
					double nps = gerador.next();
					System.out.println("N�mero Pseudoaleat�rio gerado: " + nps + ". ");
					double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
					//Evento e3: sa�da gerada pelo algoritmo de sa�da.
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA");
					escalonador.eventoQueue.add(e3);
					System.out.println("Evento gerado: " + e3.toString());
				}
				else
				{
					System.out.println((fila.status >= fila.servidores) + ": n�o � necess�rio agendar mais sa�das.");
				}
				System.out.println("Fim do algoritmo de sa�da.");
			}

			else
			{
				//To do: evento de passagem de clientes entre filas.
			}
			System.out.println(fila.toString() + "\n");

		}//End loop principal da Simula��o

		System.out.println("Fim da simula��o.\n");
		System.out.println("Distribui��o de Probabilidades: \n");

		for (int i = 0; i < fila.estados.length; i++) {
			System.out.println
				("Estado["+i+"]"
				+ " Tempo: " + (String.format("%,.2f", fila.estados[i]))
				+ " Probabilidade: "+(String.format ("%,.10f",(fila.estados[i] / tempoGlobal)))
				);
		}
		
		System.out.println("\n" + fila.toString() + "\n");
		System.out.println("Tempo Global de Simula��o: " + tempoGlobal);
		
	}//End main

}//End class
