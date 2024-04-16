# Trabalho da disciplina de Simulação e Métodos Analíticos 
## Engenharia de Software 
### PUCRS 
  
#### Esta versão imprime no console a descrição de todo o processo do algoritmo de Simulação ao executar a classe Simulador.java. 
  
O início do programa descreve a situação inicial da Fila de Clientes da Simulação: 
  
> Fila [Status=0, Capacidade=5, Servidores=1, Atendidos=0, Perdas=0, Estados=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0]] 
  
* **Status**: quantidade atual de clientes na fila.<br> 
* **Capacidade**: capacidade de clientes que podem estar em espera na fila.<br> 
* **Servidores**: quantidade de servidores que atendem os clientes.<br> 
* **Atendidos**: acumulador do total de clientes atendidos.<br> 
* **Perdas**: acumulador de clientes que não puderam ingressar na fila (quando a fila de espera está na sua capacidade máxima).<br> 
* **Estados**: um array que acumula o tempo de cada estado da fila. O espaço de estados da fila é calculado por capacidade+1.<br> 
  
A seguir, um exemplo da descrição do algoritmo de chegada fornecida pelo programa, bem como informações adicionais para garantir a correta execução do Simulador. 

>Iteração: 0<br>
Evento do escalonador: [CH, 1.5]<br>
Tempo Global: 1.5<br>
Pode entrar na fila 1? true<br>
Cliente entrou na fila 1.<br>
Acumular variação de tempo (1.5) no estado 0 da fila 1.<br>
Incrementar fila 1 -> Clientes: 1<br>
Fila 1: [Status=1, Servidores=2, Capacidade=3, Atendidos=0, Perdas=0, Estados=[1.5, 0.0, 0.0, 0.0]]<br>
Agendar uma Passagem de cliente.<br>
X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.3326917001977563<br>
Evento gerado: [PA, 4.832691700197756]<br>
Fim do algoritmo. Agendar uma nova Chegada.<br>
X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.133223295211792<br>
Evento gerado: [CH, 2.899669885635376]<br>
  
Uma breve explicação das informações geradas: 
* **Evento do escalonador: [CH, 1.5]** -- O evento do Escalonador que será simulado. É necessário garantir a correta sequência dos eventos ordenados pelo tempo.  
* **Tempo Global: 1.5** -- Atualização do tempo global da simulação, parte do algoritmo de Chegada. 
* **Pode entrar na fila 1? true** -- Parte do algoritmo de Chegada que verifica se permite ou não a entrada de um cliente na Fila. Se true, o cliente entra na fila; se não, uma perda é contabilizada.
* **Cliente entrou na fila 1.** -- Há espaço na fila, o cliente entra.
* **Acumular variação de tempo (1.5) no estado 0 da fila 1.** -- A variação de tempo que deve ser acumulado na posição atual do vetor de de estados da fila antes de incrementar o número de clientes na fila.
* **Incrementar fila 1 -> Clientes: 1** -- Parte do algoritmo de chegada. Exibe o número atual de clientes na fila.
* * **Fila 1: [Status=1, Servidores=2, Capacidade=3, Atendidos=0, Perdas=0, Estados=[1.5, 0.0, 0.0, 0.0]]** -- O estado da fila após a simulação de um evento. As informações referente à fila (variação de tempo e número de clientes) dadas acima devem estar presentes.
* **Está de frente para o servidor? true -> Agendar saída.** -- Verificação realizada pelo algoritmo de Chegada que mostra se o cliente está de frente para um servidor disponível. Se true, uma saída para esse cliente é agendada; se não, o cliente continua aguardando na fila até estar de frente para um servidor disponível para atendê-lo. 
* **X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9** -- Caso a verificação anterior resulte true, é necessário gerar um número pseudoaleatório. Aqui são exibidos os parâmetros que serão utilizados pelo gerador. É necessário garantir a sequência de números pseudoaleatórios gerados para agendar eventos, para isso a seed (X) será diferente a cada vez que um novo número for gerado. 
* **Número Pseudoaleatório gerado: 0.3326917001977563.** -- O número pseudoaleatório gerado distribuído uniformemente entre 0 e 1. 
* **Evento gerado: [SA, 5.665383400395513]** -- O evento gerado pelo algoritmo após “encaixá-lo” em um intervalo uniformemente distribuído entre o intervalo de valores médios de chegada ou de saída (a depender do tipo de evento que se deseja gerar). É necessário garantir que o tempo e o tipo estejam corretos antes de adicionar o evento ao Escalonador. 
* **Fim do algoritmo. Agendar nova chegada.** -- Aviso do final do algoritmo de chegada, quando é necessário agendar uma nova Chegada para dar continuidade à Simulação. 
* **X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9** -- Parâmetros para cálculo do próximo número pseudoaleatório. 
* **Número Pseudoaleatório gerado: 0.133223295211792.** -- O próximo número pseudoaleatório gerado já distribuído uniformemente. 
* **Evento gerado: [CH, 4.399669885635376]** -- O evento gerado pelo algoritmo. 


Quando ocorre a perda de um cliente, é importante verificar se essa está sendo contabilizada. Neste caso, algumas informações diferentes serão exibidas: 
  
>Pode entrar na fila (Status=5 < Capacidade=5)? false<br> 
Não pôde entrar na fila. Perdas contabilizadas: 1<br> 
Fim do algoritmo. Agendar nova chegada.<br> 
  
Um exemplo da descrição do algoritmo de Saída fornecida pelo programa: 
  
>Iteraçao: 2<br> 
Próximo evento do escalonador: [SA, 5.665383400395513]<br> 
Variação de tempo: 1.2657135147601366<br> 
Tempo Global: 5.665383400395513<br> 
Acumular tempo no estado 2 da fila: 1.2657135147601366<br> 
Cliente saiu. Estado atual da fila: 1<br> 
Há mais clientes para atender? true: Agendar uma saída.<br> 
X: 1.223700992E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9<br> 
Número Pseudoaleatório gerado: 0.37415456771850586.<br> 
Evento gerado: [SA, 9.413692535832524]<br> 
Fim do algoritmo de saída.<br> 
Fila [Status=1, Capacidade=5, Servidores=1, Atendidos=1, Perdas=0, Estados=[2.0, 2.3996698856353, 1.2657135147601, 0.0, 0.0, 0.0]]<br> 
  
Poucas informações de debug do algoritmo de Saída diferem do algoritmo de Chegada: 

  * **Acumular tempo no estado 2 da fila: 1.2657135147601366** -- O valor de tempo a ser acumulado na posição do vetor de estados da fila (deve ser acumulado antes de decrementar o número de clientes na fila). 
  * **Cliente saiu. Estado atual da fila: 1** -- Parte do algoritmo de saída. Exibe o número atualizado de clientes na fila. 
  * **Há mais clientes para atender? true: Agendar uma saída.** -- Parte do algoritmo de saída. Verifica se há clientes no buffer de espera que precisam ser atendidos. 

Ao fim da Simulação, as informações de probabilidade de cada estado são exibidas: 

* Estado[0] Tempo: 2,00 Probabilidade: 0,0000107359
* Estado[1] Tempo: 15,82 Probabilidade: 0,0000849058
* Estado[2] Tempo: 232,37 Probabilidade: 0,0012473593
* Estado[3] Tempo: 9.971,05 Probabilidade: 0,0535242711
* Estado[4] Tempo: 94.522,14 Probabilidade: 0,5073917277
* Estado[5] Tempo: 64.153,83 Probabilidade: 0,3443756425
