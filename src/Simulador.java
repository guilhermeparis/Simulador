/* Fila 1: G/G/1 chegadas: 2..4 sa�das: 1..2
 * Fila 2: G/G/2/5 sa�das: 4..8
 * Fila 3: G/G/2/10 saidas: 5..15
 * 
 * Roteamentos
 * Fila 1: 0.8 para Fila 2 e 0.2 para Fila 3
 * Fila 2: 0.3 retorna para Fila 1, 0.2 sai do sistema, 0.5 retorna para Fila 2
 * Fila 3: 0.3 sai do sistema, 0.7 retorna para Fila 3
 * 
 * Para a simula��o, considere inicialmente as filas vazias e que o primeiro cliente chega no tempo 2,0. Realizem a
 * simula��o com 100.000 aleat�rios, ou seja, ao se utilizar o 100.000 aleat�rio, a simula��o deve se encerrar e a
 * distribui��o de probabilidades, bem como os tempos acumulados para os estados de cada fila devem ser reportados. 
 * Al�m disso, indique o n�mero de perda de clientes (caso tenha havido perda) de cada fila e o tempo global da simula��o.
 * 
 * Nota��o de Kendall: A/B/C/K/N/D
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
		//Vari�veis de controle da simula��o
		double tempoGlobal = 0; 

		//Iniciar a fila. Ordem dos par�metros: Servidores, Capacidade, Intervalos M�dio de chegada e Intervalos m�dios de Sa�da
		Fila fila1 = new Fila(2, 3, 1.0, 4.0, 3.0, 4.0);
		System.out.println("Estado inicial da Fila 1: " + fila1.toString());
		Fila fila2 = new Fila(1, 5, 0.0, 0.0, 2.0, 3.0);
		System.out.println("Estado inicial da Fila 2: " + fila2.toString()+"\n");

		//Iniciar o gerador e seus par�metros.
		double seed = 2.0;
		double a = 2145853745.0;
		double c = 1432159778.0;
		double M = Math.pow(2, 32);
		Gerador gerador = new Gerador(seed, a, c, M);

		Evento eventoInicial = new Evento ("CH1", 1.5); //Evento inicial da Simula��o
		
		Escalonador escalonador = new Escalonador(); //Iniciar escalonador 
		escalonador.eventoQueue.add(eventoInicial); //Evento inicial adicionado ao Escalonador

		while (gerador.count < 100000)
		{
			//Verificar o pr�ximo evento do Escalonador.
			Evento eventoAux = new Evento (escalonador.eventoQueue.peek().getTipo(), escalonador.eventoQueue.poll().getTempo());

			//EVENTO DE CHEGADA
			if (eventoAux.getTipo() == "CH1")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				
				if (fila1.getStatus() < fila1.getCapacidade())
				{
					fila1.in(deltaTempo);
					
					if(fila1.getStatus() <= fila1.getServidores())
					{
						double nps = gerador.next();
						double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
						Evento e = new Evento ("PA12", (tempoGlobal + sorteio));
						escalonador.eventoQueue.add(e);
					}
					
				}
				
				else
				{
					fila1.loss();
				}
				
				//Fim do algoritmo de Chegada. Agendar uma nova Chegada.
				double nps = gerador.next();
				double sorteio =  fila1.chegadaMin + ((fila1.chegadaMax - fila1.chegadaMin) * nps);
				Evento e2 = new Evento ("CH1", (tempoGlobal + sorteio));
				escalonador.eventoQueue.add(e2);
			}

			//EVENTO DE SA�DA
			else if (eventoAux.getTipo() == "SA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo(); 
				fila2.out(deltaTempo);
				
				if (fila2.getStatus() >= fila2.getServidores())
				{
					double nps = gerador.next();
					double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
					Evento e = new Evento ("SA", (tempoGlobal + sorteio));
					escalonador.eventoQueue.add(e); //Evento adicionado ao Escalonador.
				}	
				
			}//Fim do algoritmo de Sa�da.
			
			//EVENTO DE PASSAGEM
			else if (eventoAux.getTipo() == "PA")
			{
				double deltaTempo = eventoAux.getTempo() - tempoGlobal;
				tempoGlobal = eventoAux.getTempo();
				
				//Sa�da da fila 1.
				fila1.out(deltaTempo);
				
				if(fila1.getStatus() >= fila1.getServidores())
				{
					double nps = gerador.next();
					double sorteio = fila1.saidaMin + ((fila1.saidaMax-fila1.saidaMin) * nps);
					Evento e = new Evento ("PA", (tempoGlobal + sorteio));
					escalonador.eventoQueue.add(e);
				}
				
				//Chegada na fila 2.
				if (fila2.getStatus() < fila2.getCapacidade())
				{
					fila2.in(deltaTempo);
					
					if(fila2.getStatus() <= fila2.getServidores())
					{
						double nps = gerador.next();
						double sorteio = fila2.saidaMin + ((fila2.saidaMax-fila2.saidaMin) * nps);
						Evento e = new Evento ("SA", (tempoGlobal + sorteio)); 
						escalonador.eventoQueue.add(e);
					}
					
				}
				
				else
				{
					fila2.loss();
				}
				
			}//Fim do algoritmo de Passagem.
			
		}//Fim do loop principal da Simula��o.

		//Exibi��o dos Resultados da Simula��o.
		System.out.println("Resultados da simula��o.\n");
		System.out.println("Distribui��o de Probabilidades da Fila 1: \n");
		
		for (int i = 0; i < fila1.estados.length; i++) {
			System.out.println
			("Estado["+i+"] Tempo: "+(String.format("%,.2f", fila1.estados[i]))+" Probabilidade: "+(String.format ("%,.10f",(fila1.estados[i] / tempoGlobal))));
		}
		
		System.out.println("\nFila 1: " + fila1.toString() + "\n");
		System.out.println("Distribui��o de Probabilidades da Fila 2: \n");
		
		for (int i = 0; i < fila2.estados.length; i++) {
			System.out.println
			("Estado["+i+"] Tempo: " + (String.format("%,.2f", fila2.estados[i]))+" Probabilidade: "+(String.format ("%,.10f",(fila2.estados[i] / tempoGlobal))));
		}
		
		System.out.println("\nFila 2: " + fila2.toString() + "\n");
		System.out.println("Tempo Global de Simula��o: " + tempoGlobal);
		
	}//Fim da main.
	
}//Fim da classe.