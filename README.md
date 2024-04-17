# Trabalho da disciplina de Simulação e Métodos Analíticos 
## Engenharia de Software - PUCRS 
  
### Esta versão imprime no console a descrição de todo o processo do algoritmo de Simulação ao executar a classe Simulador.java. 
  
O início do programa descreve a situação inicial da Fila de Clientes da Simulação: 

> Estado inicial da Fila 1: [Status=0, Servidores=2, Capacidade=3, Atendidos=0, Perdas=0, Estados=[0.0, 0.0, 0.0, 0.0]]<br>
Estado inicial da Fila 2: [Status=0, Servidores=1, Capacidade=5, Atendidos=0, Perdas=0, Estados=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]
  
* **Status**: quantidade atual de clientes na fila.<br> 
* **Servidores**: quantidade de servidores que atendem os clientes.<br> 
* **Capacidade**: capacidade de clientes que podem estar em espera na fila.<br> 
* **Atendidos**: acumulador do total de clientes atendidos.<br> 
* **Perdas**: acumulador de clientes que não puderam ingressar na fila (quando a fila de espera está na sua capacidade máxima).<br> 
* **Estados**: vetor que acumula o tempo de cada estado da fila. O espaço de estados da fila é calculado por capacidade+1.<br> 
  
### Descrição do debug do algoritmo de Chegada: 

#### Caso 1. O cliente entrou na fila 1.

>Iteração: 0<br>
Evento do escalonador: [CH, 1.5]<br>
Tempo Global: 1.5<br>
Pode entrar na fila 1? true<br>
Cliente entrou na fila 1.<br>
Acumular variação de tempo (1.5) no estado 0 da fila 1.<br>
Incrementar fila 1 -> Clientes: 1<br>
Fila 1: [Status=1, Servidores=2, Capacidade=3, Atendidos=0, Perdas=0, Estados=[1.5, 0.0, 0.0, 0.0]]<br>
Está de frente para um servidor? true-> Agendar uma Passagem de cliente.<br>
X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.3326917001977563<br>
Evento gerado: [PA, 4.832691700197756]<br>
Fim do algoritmo. Agendar uma nova Chegada.<br>
X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.133223295211792<br>
Evento gerado: [CH, 2.899669885635376]<br>
  
### Informações geradas: 
* **Evento do escalonador: [CH, 1.5]** -- O evento do Escalonador que será simulado. É necessário garantir a correta sequência dos eventos ordenados pelo tempo.  
* **Tempo Global: 1.5** -- Atualização do tempo global da simulação, parte do algoritmo de Chegada. 
* **Pode entrar na fila 1? _true_ -> Cliente entrou na fila 1.** -- Parte do algoritmo de Chegada, aqui se verifica se há espaço na fila de clientes. Neste exemplo, há espaço na fila, portanto o algoritmo deve seguir para realizar as atualizações na fila.
* **Acumular variação de tempo (1.5) no estado 0 da fila 1.** -- A variação de tempo que deve ser acumulada na posição atual do vetor de estados antes de incrementar o número de clientes na fila.
* **Incrementar fila 1 -> Clientes: 1** -- Parte do algoritmo de chegada. Exibe o número atual de clientes na fila.
* **Fila 1: [Status=1, Servidores=2, Capacidade=3, Atendidos=0, Perdas=0, Estados=[1.5, 0.0, 0.0, 0.0]]** -- O estado da fila após a simulação de um evento. As informações referente à fila (variação de tempo e número de clientes) dadas acima agora precisam estar presentes.
* **Está de frente para o servidor? _true_ -> Agendar uma Passagem de cliente.** -- Verificação realizada pelo algoritmo de Chegada que mostra se o cliente está de frente para um servidor disponível. Se _true_, uma saída para esse cliente é agendada; se não, o cliente continua aguardando na fila até estar de frente para um servidor disponível para atendê-lo.
* **X: 2.0 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.3326917001977563** -- Parâmetros e resultado da geração de um Número Pseudoaleatório (NPS) distribuído uniformemente entre 0 e 1. É necessário garantir a sequência de números pseudoaleatórios gerados, e para tanto a _seed_ (X) deverá ser diferente a cada vez que um número for gerado. 
* **Evento gerado: [PA, 4.832691700197756]** -- O evento gerado pelo algoritmo após “encaixá-lo” em um intervalo uniformemente distribuído entre o intervalo de valores médios do tipo de evento que se deseja gerar. É necessário garantir que o tempo e o tipo estejam corretos antes de adicionar o evento ao Escalonador. 
* **Fim do algoritmo. Agendar uma nova Chegada.** -- Parte do algoritmo de chegada, no momento em que é necessário agendar uma nova Chegada para dar continuidade à Simulação. 
* **X: 1.428899972E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.133223295211792** -- Parâmetros e resultado do cálculo do próximo NPS. 
* **Evento gerado: [CH, 2.899669885635376]** -- O evento gerado pelo algoritmo. 

### Variações do algoritmo de chegada:

#### Caso 2: O cliente não pôde entrar na fila 1.

>Pode entrar na fila 1? false -> Não pôde entrar na fila 1. Perdas contabilizadas: 1<br>
Fim do algoritmo. Agendar uma nova Chegada.<br>
X: 2.07775744E8 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.23283280432224274<br>
Evento gerado: [CH, 3001.0482855988666]<br>

* **Pode entrar na fila 1? _false_ -> Não pôde entrar na fila 1. Perdas contabilizadas: 1** -- Neste caso, não havia espaço para o cliente entrar na fila e houve uma desistência por parte do cliente, portanto, deve-ser contabilizar a perda e seguir para o final do algoritmo de chegada onde deve-se agendar uma nova chegada.

#### Caso 3: O cliente entrou na fila, mas os servidores estão ocupados atendendo outros clientes.

>Está de frente para um servidor? false -> Não há servidor disponível para atender na fila 1.<br>
Fim do algoritmo. Agendar uma nova Chegada.<br>
X: 7.53854464E8 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.07421004772186279<br>
Evento gerado: [CH, 17.490424416959286]<br>

* **Está de frente para um servidor? _false_ -> Não há servidor disponível para atender na fila 1.** -- Neste caso, o cliente continua na fila de espera, aguardando até que um servidor esteja disponível para atendê-lo. O algoritmo prossegue para o fim e agenda uma nova chegada.

### Descrição do debug do algoritmo de Passagem de Clientes.

#### Caso 1: O cliente passou da fila 1 para a fila 2.

>Iteração: 2<br>
Evento do escalonador: [PA, 4.832691700197756]<br>
Tempo Global: 4.832691700197756<br>
Cliente saiu na fila 1.<br>
Acumular variação de tempo (1.9330218145623803) no estado 2 da fila 1.<br>
Decrementar fila 1 -> Clientes: 1<br>
Fila 1: [Status=1, Servidores=2, Capacidade=3, Atendidos=1, Perdas=0, Estados=[1.5, 1.399669885635376, 1.9330218145623803, 0.0]]<br>
Precisa agendar mais passagens da Fila 1? false -> Não é necessário agendar mais passagens.<br>
Pode entrar na fila 2? true -> Cliente entrou na fila 2.<br>
Acumular variação de tempo (1.9330218145623803) no estado 0 da fila 2.<br>
Incrementar fila 2 -> Clientes: 1<br>
Fila 2: [Status=1, Servidores=1, Capacidade=5, Atendidos=0, Perdas=0, Estados=[1.9330218145623803, 0.0, 0.0, 0.0, 0.0, 0.0]]<br>
Está de frente para o servidor? true -> Agendar uma Saída.<br>
X: 1.606981632E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS: 0.6810625791549683<br>
Evento gerado: [SA, 7.5137542793527246]<br>
Fim do algoritmo de Passagem.<br>

### Informações geradas:
###### (incluímos somente as informações que não apareceram posteriormente)

* **Precisa agendar mais passagens da Fila 1? false -> Não é necessário agendar mais passagens.** -- Verificação do algoritmo de Passagem, que agenda uma passagem de cliente caso o número de clientes na fila seja maior que o número de servidores.
* **Pode entrar na fila 2? _true_ -> Cliente entrou na fila 2.** -- Igual ao caso do algoritmo de Chegada, porém para a fila 2. Caso a verificação resulte _true_ devemos acumular a variação de tempo na fila correspondente e incrementar o número de clientes; do contrário, devemos contabilizar uma perda. Após essa verificação, serão exibidos a variação de tempo, o incremento do número de clientes na fila 2 e os detalhes da fila após inserir essas informações.
* **Está de frente para o servidor? true -> Agendar uma Saída.** -- Neste caso, quando o cliente está de frente para um servidor disponível, agendamos uma saída, que marca a efetiva saída do cliente do simulador. Após essa verificação, assim como no algoritmo de chegada, deve-se calcular um novo NPS para criação de um evento e, por fim, inserir o evento de saída gerado no Escalonador.

### Variações do algoritmo de Passagem de Clientes:

#### Caso 2: O algoritmo de passagem necessita agendar mais uma Passagem de cliente.

>Precisa agendar mais passagens da Fila 1? true -> Agendar uma Passagem de cliente.<br>
X: 3.18729728E8 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.154038667678833<br>
Evento gerado: [PA, 19.60649871826172]<br>

* **Precisa agendar mais passagens da Fila 1? true -> Agendar uma Passagem de Cliente.** -- No caso de necessitar agendar mais uma Passagem de Cliente, deve-se calcular um novo NPS, gerar um evento de passagem e adicioná-lo ao Escalonador.

#### Caso 3: O cliente saiu da fila 1 e não havia espaço na fila 2.
>Pode entrar na fila 2? false -> Não pôde entrar na fila 2. Perdas contabilizadas: 1<br>
Fim do algoritmo.

* **>Pode entrar na fila 2? false -> Não pôde entrar na fila 2. Perdas contabilizadas: 1** -- Igual ao algoritmo de Chegada, não havia espaço para o cliente entrar na fila 2 e houve uma desistência, deve-ser contabilizar a perda e seguir para o final do algoritmo de passagem.

#### Caso 4: O cliente entrou na fila 2, mas não está de frente para um servidor disponível.

>Está de frente para o servidor? false -> Não há servidor disponível para atender na fila 2.<br>
Fim do algoritmo de Passagem.

* **Está de frente para o servidor? false -> Não há servidor disponível para atender na fila 2.** -- Igual ao algoritmo de Chegada, o cliente permanece na fila até estar de frente para um servidor disponível para então agendar sua saída. Neste caso, a saída será gerada posteriormente no algoritmo de Saída.

### Descrição do debug do algoritmo de Saída.

#### Caso 1: O cliente saiu do sistema.

>Iteração: 5<br>
Evento do escalonador: [SA, 7.5137542793527246]<br>
Tempo Global: 7.5137542793527246<br>
Acumular variação de tempo (1.3291693041101098) no estado 2 da fila 2.<br>
Cliente saiu. Estado atual da fila: 1<br>
Fila 2: [Status=1, Servidores=1, Capacidade=5, Atendidos=1, Perdas=0, Estados=[1.9330218145623803, 1.1624513864517212, 1.3291693041101098, 0.0, 0.0, 0.0]]<br>
Ainda há cliente de frente para um servidor disponível? true -> Agendar uma Saída.<br>
X: 3.701820928E9 a: 2.145853745E9 c: 1.432159778E9 M: 4.294967296E9 NPS gerado: 0.2580409049987793<br>
Evento gerado: [SA, 9.771795184351504]<br>
Fim do algoritmo de saída.<br>

### Informações geradas:

* **Ainda há cliente de frente para um servidor disponível? true -> Agendar uma Saída.** -- Após a saída do cliente, é verificado na fila se há mais um cliente para ser atendido. Neste caso, deve-se calcular um NPS e gerar um evento de saída. Por fim, o algoritmo de saída termina.

### Variações do algoritmo de Saída:

#### Caso 2: o cliente na fila não está de frente para um servidor disponível.
* **Ainda há cliente de frente para um servidor disponível? false -> Não é necessário agendar mais saídas.** -- Não há mais clientes na fila 2, portanto, não é necessário agendar outra saída. O algoritmo de saída termina.

### Ao fim da Simulação, as informações de probabilidade de cada estado são exibidas: 

#### Total de iterações: 100000

#### Distribuição de Probabilidades da Fila 1:

* Estado[0] Tempo: 1.703,73 Probabilidade: 0,0203006220
* Estado[1] Tempo: 27.838,91 Probabilidade: 0,3317108103
* Estado[2] Tempo: 22.203,74 Probabilidade: 0,2645656645
* Estado[3] Tempo: 2.780,86 Probabilidade: 0,0331349466

#### Fila 1: [Status=2, Servidores=2, Capacidade=3, Atendidos=33515, Perdas=176, Estados=[1703.7346823280677, 27838.911197340116, 22203.73834946705, 2780.8585267877206]]

#### Distribuição de Probabilidades da Fila 2: 

* Estado[0] Tempo: 1.517,89 Probabilidade: 0,0180861850
* Estado[1] Tempo: 8.373,92 Probabilidade: 0,0997783279
* Estado[2] Tempo: 14.876,92 Probabilidade: 0,1772639901
* Estado[3] Tempo: 15.431,72 Probabilidade: 0,1838745682
* Estado[4] Tempo: 11.585,61 Probabilidade: 0,1380468194
* Estado[5] Tempo: 3.153,86 Probabilidade: 0,0375794214

#### Fila 2: [Status=3, Servidores=1, Capacidade=5, Atendidos=32792, Perdas=720, Estados=[1517.887517936062, 8373.920663899742, 14876.923890738748, 15431.718276085798, 11585.613209327217, 3153.86216666596]]

* Tempo Global de Simulação: 83925.24551248131
