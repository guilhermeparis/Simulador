/* Simulação 1: G/G/1/5 Chegada: 2..5 Saída: 3..5
 * Simulação 2: G/G/2/5 Chegada: 2..5 Saída: 3..5
 * 
 * Para ambas simulações, considere inicialmente a fila vazia e o primeiro cliente chega no tempo 2,0.
 * Realize a simulação com 100.000 aleatórios, ou seja, ao se utilizar o 100.000 aleatório, sua simulação deve se encerrar e a
 * distribuição de probabilidades, bem como os tempos acumulados para os estados da fila devem ser reportados. Além disso,
 * indique o número de perda de clientes (caso tenha havido perda) e o tempo global da simulação.
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
		double tempoGlobal = 0;
		int count = 100000; //Variável de controle da simulação

		
		Fila fila = new Fila(1, 5); //Iniciar a fila
		
		double saidaT0 = 3.0, saidaT1 = 5.0, chegadaT0 = 2.0, chegadaT1 = 5.0; //Intervalos médios de chegada e de saída

		double seed = 2.0, a = 2145853745, c = 1432159778, M = Math.pow(2, 32); //Parâmetros do Gerador de Números Pseudoaleatórios
		Gerador gerador = new Gerador(seed, a, c, M); //Iniciar o Gerador

		Escalonador escalonador = new Escalonador(); //Iniciar o Escalonador
		Evento eventoInicial = new Evento (2.0, "CH"); //Evento inicial da Simulação
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
						//Evento e1: saída gerada pelo algoritmo de chegada
						Evento e1 = new Evento ((tempoGlobal + sorteio), "SA");
						escalonador.eventoQueue.add(e1);
					}
				} 
				else
				{
					fila.loss();
				}
				//Fim do algoritmo de chegada. Agendar nova chegada para dar continuidade à Simulação.
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
					//Evento e3: saída gerada pelo algoritmo de saída.
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA");
					escalonador.eventoQueue.add(e3);
				}
				
			}//Fim do algoritmo de saída.
			else
			{
				//To do: evento de passagem de clientes entre filas.
			}
		}//End loop principal da Simulação

		System.out.println("Fim da simulação.\n");

		System.out.println("Distribuição de Probabilidads:\n");
		for (int i = 0; i < fila.estados.length; i++) {
			System.out.println
				("Estado["+i+"]"
				+ " Tempo: " + (String.format("%,.2f", fila.estados[i]))
				+ " Probabilidade: "+(String.format ("%,.10f",(fila.estados[i] / tempoGlobal)))
				);
		}
		
		System.out.println("\n" + fila.toString());
		
		System.out.println("\nTempo Global de Simulação: " + tempoGlobal);
		
	}//End main
}//End class
