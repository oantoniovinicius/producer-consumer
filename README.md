# Producer-Consumer-problem (Problema do produtor-consumidor)
O "problema do produtor e consumidor" é um clássico problema de programação concorrente e sistemas operacionais envolvendo uso de recursos compartilhados em uma região crítica.
A aplicação foi realizada para fins educacionais, visando o estudo de criação de processos/threads e utilização de semáforos no controle de região crítica para a resolução
do problema do produtor-consumidor.

## Sobre o código
Na execução do código, o produtor vai produzir pizzas e levar até a mesa enquanto a mesa ainda não estiver cheia, nesse caso, enquanto não chegar ao número máximo de pizzas: 2.
O número máximo de pizzas corresponde ao tamanho do buffer que será utilizado pelo semáforo.
Por outro lado, o consumidor vai buscar a pizza e retirar da mesa enquanto houver pizzas para serem retiradas, ou seja, ele só retira do buffer se houver algo para ser retirado, 
caso contrário, ele aguarda até que o produtor coloque algo (pizza) no buffer (mesa);

## Funcionalidades

- Simulação de uma pizzaria com um produtor e um consumidor;
- Controlador de velocidade;
- Botões para pausar as threads do produtor e do consumidor;
- Botão para reiniciar a simulação;

## Aplicação em execução
![Layout](https://github.com/oantoniovinicius/producer-consumer/blob/main/resources/executionGif.gif)

## Autores

- [@oantoniovinicius](https://www.github.com/oantoniovinicius)
