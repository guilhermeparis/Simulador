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
		
		double saidaT0 = 3.0, saidaT1 = 5.0, chegadaT0 = 2.0, chegadaT1 = 5.0; //Intervalos m�dios de chegada e de sa�da

		double seed = 2.0, a = 2145853745, c = 1432159778, M = Math.pow(2, 32); //Par�metros do Gerador de N�meros Pseudoaleat�rios
		Gerador gerador = new Gerador(seed, a, c, M); //Iniciar o Gerador

		Escalonador escalonador = new Escalonador(); //Iniciar o Escalonador
		Evento eventoInicial = new Evento (2.0, "CH"); //Evento inicial da Simula��o
		escalonador.eventoQueue.add(eventoInicial); //Evento inicial adicionado ao Escalonador

		for (int i = 0; i < count; i++)
		{
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTempo(), escalonador.eventoQueue.poll().getTipo());
			if (eventoAux.getTipo() == "CH")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				if (fila.getStatus() < fila.getCapacidade())
				{	
					fila.in(deltaTempo);
					if(fila.status <= fila.servidores)
					{
						double nps = gerador.next();
						double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
						//Evento e1: sa�da gerada pelo algoritmo de chegada
						Evento e1 = new Evento ((tempoGlobal + sorteio), "SA");
						escalonador.eventoQueue.add(e1);
					}
				} 
				else
				{
					fila.loss();
				}
				//Fim do algoritmo de chegada. Agendar nova chegada para dar continuidade � Simula��o.
				double nps = gerador.next();
				double sorteio =  chegadaT0 + ((chegadaT1 - chegadaT0) * nps);
				//Evento e2: chegada gerada pelo algoritmo de chegada
				Evento e2 = new Evento ((tempoGlobal + sorteio), "CH");
				escalonador.eventoQueue.add(e2);
			}
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo(); 
				fila.out(deltaTempo);
				if (fila.status >= fila.servidores)
				{
					double nps = gerador.next();
					double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
					//Evento e3: sa�da gerada pelo algoritmo de sa�da.
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA");
					escalonador.eventoQueue.add(e3);
				}
				
			}//Fim do algoritmo de sa�da.
			else
			{
				//To do: evento de passagem de clientes entre filas.
			}
		}//End loop principal da Simula��o

		System.out.println("Fim da simula��o.\n");

		System.out.println("Distribui��o de Probabilidads:\n");
		for (int i = 0; i < fila.estados.length; i++) {
			System.out.println
				("Estado["+i+"]"
				+ " Tempo: " + (String.format("%,.2f", fila.estados[i]))
				+ " Probabilidade: "+(String.format ("%,.10f",(fila.estados[i] / tempoGlobal)))
				);
		}
		
		System.out.println("\n" + fila.toString());
		
		System.out.println("\nTempo Global de Simula��o: " + tempoGlobal);
		
	}//End main
}//End class
