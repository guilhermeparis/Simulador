/* Simulação 1: G/G/1/5 Chegada: 2..5 Saída: 3..5
 * Simulação 2: G/G/2/5 Chegada: 2..5 Saída: 3..5
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
		int count = 100000;

		//Iniciar a fila
		Fila fila = new Fila(1, 5); //Estados totais da fila: K+1 
		System.out.println(fila.toString()+"\n");

		//Intervalos médios de chegada e de saída
		double saidaT0 = 3.0, saidaT1 = 5.0, chegadaT0 = 2.0, chegadaT1 = 5.0;

		//Iniciar gerador e suas variáveis
		double seed = 2.0, a = 2145853745, c = 1432159778, M = Math.pow(2, 32);
		Gerador gerador = new Gerador(seed, a, c, M);

		//Iniciar escalonador
		Escalonador escalonador = new Escalonador();

		//Declarar o primeiro evento da simulação e adicioná-lo ao Escalonador
		Evento eventoInicial = new Evento (2.0, "CH");
		escalonador.eventoQueue.add(eventoInicial);

		for (int i = 0; i < count; i++)
		{
			System.out.println("Iteraçao: " + i);
			//Ver qual o próximo evento do escalonador (o elemento no topo da Priority Queue)
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTempo(), escalonador.eventoQueue.poll().getTipo());
			System.out.println("Próximo evento do escalonador: " + eventoAux.toString());

			//Verificar se o próximo Evento é CHEGADA ou SAÍDA
			if (eventoAux.getTipo() == "CH")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				System.out.println("Variação de tempo: " + deltaTempo);
				tempoGlobal = eventoAux.getTempo();
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.println("Pode entrar na fila (Status=" + fila.getStatus() + " < Capacidade=" + fila.getCapacidade() + ")? " + (fila.getStatus() < fila.getCapacidade()));

				if (fila.getStatus() < fila.getCapacidade())
				{	
					System.out.print("Acumular tempo no estado " + fila.getStatus() + " da fila: ");
					fila.in(deltaTempo);
					System.out.println("Incrementar fila -> clientes: " + fila.getStatus());
					System.out.print("Está de frente para o servidor? " + (fila.status <= fila.servidores) + " -> ");

					if(fila.status <= fila.servidores)
					{
						System.out.println("Agendar saída. ");
						double nps = gerador.next();
						System.out.println("Número Pseudoaleatório gerado: " + nps + ". ");
						double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
						//Evento e1: saída gerada pelo algoritmo de chegada
						Evento e1 = new Evento ((tempoGlobal + sorteio), "SA");
						escalonador.eventoQueue.add(e1);
						System.out.println("Evento gerado: " + e1.toString());
					}
					else
					{
						System.out.println("Não há servidor disponível para atender.");
					}
				} 
				else
				{
					//Não pôde entrar na fila
					fila.loss();
					System.out.println("Não pôde entrar na fila. Perdas contabilizadas: " + fila.getPerdas());
				}
				System.out.println("Fim do algoritmo. Agendar nova chegada. ");
				double nps = gerador.next();
				System.out.println("Número Pseudoaleatório gerado: " + nps + ". ");
				double sorteio =  chegadaT0 + ((chegadaT1 - chegadaT0) * nps);
				//Evento e2: chegada gerada pelo algoritmo de chegada
				Evento e2 = new Evento ((tempoGlobal + sorteio), "CH");
				escalonador.eventoQueue.add(e2);
				System.out.println("Evento gerado: " + e2.toString());
			}
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				System.out.println("Variação de tempo: " + deltaTempo);
				tempoGlobal = eventoAux.getTempo(); 
				System.out.println("Tempo Global: " + tempoGlobal);
				System.out.print("Acumular tempo no estado " + fila.getStatus() + " da fila: ");
				fila.out(deltaTempo);
				System.out.println("Cliente saiu. Estado atual da fila: " + fila.getStatus());

				System.out.print("Há mais clientes para atender? ");
				if (fila.status >= fila.servidores)
				{
					System.out.println((fila.status >= fila.servidores) + ": Agendar uma saída.");
					double nps = gerador.next();
					System.out.println("Número Pseudoaleatório gerado: " + nps + ". ");
					double sorteio = saidaT0 + ((saidaT1-saidaT0) * nps);
					//Evento e3: saída gerada pelo algoritmo de saída.
					Evento e3 = new Evento ((tempoGlobal + sorteio), "SA");
					escalonador.eventoQueue.add(e3);
					System.out.println("Evento gerado: " + e3.toString());
				}
				else
				{
					System.out.println((fila.status >= fila.servidores) + ": não é necessário agendar mais saídas.");
				}
				System.out.println("Fim do algoritmo de saída.");
			}

			else
			{
				//To do: evento de passagem de clientes entre filas.
			}
			System.out.println(fila.toString() + "\n");

		}//End loop principal da Simulação

		System.out.println("Fim da simulação.");

		for (int i = 0; i < fila.estados.length; i++) {
			System.out.println
				("Estado["+i+"]"
				+ " Tempo: " + (String.format("%,.2f", fila.estados[i]))
				+ " Probabilidade: "+(String.format ("%,.10f",(fila.estados[i] / tempoGlobal))));
		}
	
	}//End main

}//End class
