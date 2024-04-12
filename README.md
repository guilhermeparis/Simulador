Trabalho da disciplina Simulação e Métodos Analíticos do curso de Engenharia de Software da PUCRS.

Esta versão imprime no console a descrição de todo o processo do algoritmo de Simulação ao executar a classe Simulador.java.

O início do programa descreve a situação inicial da Fila de Clientes da Simulação:

Fila [Status=0, Capacidade=5, Servidores=1, Atendidos=0, Perdas=0, Estados=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]
  Status: quantidade atual de clientes na fila.
  Capacidade: capacidade de clientes que podem estar em espera na fila.
  Servidores: quantidade de servidores que atendem os clientes.
  Atendidos: acumulador do total de clientes atendidos.
  Perdas: acumulador de clientes que não puderam ingressar na fila (quando a fila de espera está na sua capacidade máxima).
  Estados: um array que acumula o tempo de cada estado da fila. O espaço de estados da fila é calculado por capacidade+1.

A seguir, exibimos um exemplo da descrição do algoritmo de chegada fornecida pelo programa, bem como informações adicionais para garantir a correta execução do Simulador.

Iteraçao: 0
Próximo evento do escalonador: [CH, 2.0]
Delta tempo: 2.0
Tempo Global: 2.0
Pode entrar na fila (Status=0 < Capacidade=5)? true
Acumular tempo no estado 0 da fila: 2.0
Incrementar fila -> clientes: 1
Está de frente para o servidor? true -> Agendar saída. 
X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9
Número Pseudoaleatório gerado:: 0.3326917001977563. 
Evento gerado: [SA, 5.665383400395513]
Fim do algoritmo. Agendar nova chegada. 
X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9
Número Pseudoaleatório gerado:: 0.133223295211792. 
Evento gerado: [CH, 4.399669885635376]
Fila [Status=1, Capacidade=5, Servidores=1, Atendidos=0, Perdas=0, Estados=[2.0, 0.0, 0.0, 0.0, 0.0, 0.0]]

Aqui explicamos as informações geradas:

  Iteraçao: 0 -- Valor da variável count do Simulador: útil para saber em qual iteração ocorreu algum erro.
  
  Próximo evento do escalonador: [CH, 2.0] -- O evento do Escalonador que será simulado. É necessário garantir a correta sequência dos eventos ordenados pelo tempo.
  
  Delta tempo: 2.0 -- Cálculo da variação de tempo (Tempo Global - Tempo do Evento Agendado). É o valor de tempo a ser acumulado na posição correta do vetor de Estados da Fila.
  
  Tempo Global: 2.0 //Atualização do tempo global da simulação, parte do algoritmo de Chegada.
  
  Pode entrar na fila (Status=0 < Capacidade=5)? true -- Parte do algoritmo de Chegada que verifica se permite ou não a entrada de um cliente na Fila. Se true, o cliente entra na fila; senão, uma perda é contabilizada.
  
  Acumular tempo no estado 0 da fila: 2.0 -- Exibe o valor de tempo a ser acumulado na posição do vetor de estados da fila (deve ser acumulado antes de incrementar o número de clientes na fila).
  
  Incrementar fila -> clientes: 1 -- Parte do algoritmo de chegada. Exibe o número atual de clientes na fila.
  
  Está de frente para o servidor? true -> Agendar saída. -- Verificação realizada pelo algoritmo de Chegada que mostra se o cliente está de frente para um servidor disponível. Se true, uma saída para esse cliente é agendada; senão, o cliente continua aguardando na fila até estar de frente para um servidor disponível para atendê-lo.
  
  X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 -- Caso a verificação anterior resulte true, é necessários gerar um número pseudoaleatório. Aqui são exibidos os parâmetros que serão utilizados pelo gerador. É necessário garantir a sequência de números pseudoaleatórios gerados para agendar eventos, para isso a seed (X) será diferente a cada vez que um novo número for gerado.
  
  Número Pseudoaleatório gerado: 0.3326917001977563. -- O número pseudoaleatório gerado distribuído uniformemente entre 0 e 1.
  
  Evento gerado: [SA, 5.665383400395513] -- O evento gerado pelo algoritmo após “encaixá-lo” em um intervalo uniformemente distribuído entre o intervalo de valores médios de chegada ou de saída (a depender do tipo de evento que se deseja gerar). É necessário garantir que o tempo e o tipo estejam corretos antes de adicionar o evento ao Escalonador.
  
  Fim do algoritmo. Agendar nova chegada. -- Aviso do final do algoritmo de chegada, quando é necessário agendar uma nova Chegada para dar continuidade à Simulação.
  
  X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 -- Parâmetros para cálculo do próximo número pseudoaleatório.
  
  Número Pseudoaleatório gerado: 0.133223295211792. -- O próximo número pseudoaleatório gerado já distribuido uniformemente.
  
  Evento gerado: [CH, 4.399669885635376] -- O evento gerado pelo algoritmo.
  
  Fila [Status=1, Capacidade=5, Servidores=1, Atendidos=0, Perdas=0, Estados=[2.0, 0.0, 0.0, 0.0, 0.0, 0.0]] -- O estado da fila após a simulação de um evento.

Quando ocorre a perda de um cliente, as seguintes informações são exibidas:

Pode entrar na fila (Status=5 < Capacidade=5)? false
Não pôde entrar na fila. Perdas contabilizadas: 1
Fim do algoritmo. Agendar nova chegada. 


Abaixo, um exemplo da descrição do algoritmo de Saída fornecida pelo programa.

Iteraçao: 2
Próximo evento do escalonador: [SA, 5.665383400395513]
Variação de tempo: 1.2657135147601366
Tempo Global: 5.665383400395513
Acumular tempo no estado 2 da fila: 1.2657135147601366
Cliente saiu. Estado atual da fila: 1
Há mais clientes para atender? true: Agendar uma saída.
X: 1.223700992E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9
Número Pseudoaleatório gerado: 0.37415456771850586. 
Evento gerado: [SA, 9.413692535832524]
Fim do algoritmo de saída.
Fila [Status=1, Capacidade=5, Servidores=1, Atendidos=1, Perdas=0, Estados=[2.0, 2.399669885635376, 1.2657135147601366, 0.0, 0.0, 0.0]]

Explicamos abaixo apenas as informações de debug do algoritmo de Saída que diferem do algoritmo de Chegada.

  Acumular tempo no estado 2 da fila: 1.2657135147601366 -- O valor de tempo a ser acumulado na posição do vetor de estados da fila (deve ser acumulado antes de decrementar o número de clientes na fila).

  Cliente saiu. Estado atual da fila: 1 -- Parte do algoritmo de saída. Exibe o número atualizado de clientes na fila.
  
  Há mais clientes para atender? true: Agendar uma saída. -- Parte do algoritmo de saída. Verifica se há clientes no buffer de espera que precisam ser atendidos.

Ao fim da Simulação, as informações de probabilidade de cada estado são exibidas:

Estado[0]Tempo: 2,00 Probabilidade: 0,0000107359
Estado[1]Tempo: 15,82 Probabilidade: 0,0000849058
Estado[2]Tempo: 232,37 Probabilidade: 0,0012473593
Estado[3]Tempo: 9.971,05 Probabilidade: 0,0535242711
Estado[4]Tempo: 94.522,14 Probabilidade: 0,5073917277
Estado[5]Tempo: 64.153,83 Probabilidade: 0,3443756425
