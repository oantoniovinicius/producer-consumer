/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 06/05/2023
* Ultima alteracao.: 13/05/2023
* Nome.............: Consumer.java
* Funcao...........: executa os comandos de thread do consumidor e controla as animacoes do consumidor e regiao critica
*************************************************************** */
package model;

import control.*;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

public class Consumer extends Thread {
  private MainControl controller;
  private ImageView pizzaImage1;
  private ImageView pizzaImage2;
  private ImageView consumerImage;
  ImageView[] images = new ImageView[2];
  private int speed = 6;
  private int positionX;
  private int positionY;

  /********************************************************************
  * Metodo: Consumer()
  * Funcao: construtor da classe
  * Parametros: objeto MainControl
  * Retorno: void
  ****************************************************************** */
  public Consumer(MainControl controller){
    this.controller = controller;
    this.consumerImage = controller.getImageViewConsumer();
    pizzaImage1 = controller.getImageViewFirstPizza();
    pizzaImage2 = controller.getImageViewSecondPizza();
    images[0] = pizzaImage1;
    images[1] = pizzaImage2;
  }

  /********************************************************************
  * Metodo: run()
  * Funcao: executa um loop infinito e dentro desse loop eh responsavel por movimentar o consumidor, controlar o acesso 
  a regiao critica e consumir/buscar um item (pizza). Utiliza os metodos moveRight() e moveLeft() que, atraves da classe 
  Platform.runLater() atualiza a posicao do produtor na tela
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void run() {
    try{
      while(true){        
        //buscando a pizza
        buscaPizza();

        controller.getSemaphoreFull().acquire(); //decrementa contador de posicoes ocupadas
        controller.getSemaphoreMutex().acquire(); //entra na regiao critica
        
        //recebendo a pizza
        consomePizza(); 
        
        controller.getSemaphoreMutex().release(); //deixa a regiao critica
        controller.getSemaphoreEmpty().release(); //incrementa o contador de posicoes vazias

        //vai embora com a pizza
        levaPizza();
      } 
    }catch (InterruptedException e){ 
      System.out.println("Erro de exceção!");
    }
  }//fim do metodo run()

  /********************************************************************
  * Metodo: consomePizza()
  * Funcao: desativa a imagem da pizza do balcao caso nao tenha mais pizza para ser colocada no lugar 
  da pizza que vai ser retirada
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void consomePizza() {
    setPizza();
  }//fim do metodo consomePizza()

  /********************************************************************
  * Metodo: buscaPizza()
  * Funcao: responsavel por comandos de movimentacao do consumidor: move o consumidor ate o balcao e atualiza sua imagem 
  sempre que o metodo for chamado novamente dentro do loop
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void buscaPizza(){
    //resetando a imagem do consumidor no loop
    Platform.runLater( () -> controller.changeImageConsumer("./resources/consumidor.png"));
    Platform.runLater( () -> consumerImage.setY(positionY = 0));
    rotate(360);
    
    Platform.runLater( () -> consumerImage.setX(positionX));//setando a posicao inicial do produtor no eixo X
    Platform.runLater( () -> consumerImage.setY(positionY));//setando a posicao inicial do produtor no eixo y
    //movendo o consumidor ate o balcao
    moveLeft(-260);
  }//fim do metodo buscaPizza()

  /********************************************************************
  * Metodo: levaPizza()
  * Funcao: responsavel por comandos de movimentacao do consumidor: troca a imagem do consumidor para mostrar
  que ele pegou a pizza e move o consumidor do balcao ate a saida 
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void levaPizza(){
    //buscando a pizza
    Platform.runLater( () -> controller.changeImageConsumer("./resources/consumidorPizza.png"));
    Platform.runLater( () -> consumerImage.setY(positionY = -13));
    rotate(180);
    moveRight(40);
  }//fim do metodo levaPizza()

  /********************************************************************
  * Metodo: setPizza()
  * Funcao: utilizando o contador de pizzas do controle, desativa a imagem de acordo com o index do array e 
  decrementa o valor do contador de pizzas
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void setPizza(){
    images[controller.getPizzaNum()-1].setVisible(false);
    images[controller.getPizzaNum()-1].setOpacity(0);
    controller.decreasePizza();
  }

  /********************************************************************
  * Metodo: moveLeft()
  * Funcao: responsavel por mover a imagem do consumidor para a esquerda usando a classe Platform
  * Parametros: int coordX = coordenada que informa para onde o consumidor deve se mover
  * Retorno: void
  ****************************************************************** */
  public void moveLeft(int coordX){
    while(positionX != coordX){
      try {
        Thread.sleep(speed);
      } catch (InterruptedException e) {
        System.out.println("Erro de exceção!");
      }
      positionX--;
      Platform.runLater( () -> consumerImage.setX(positionX));
    }
  }//fim do metodo moveLeft

  /********************************************************************
  * Metodo: moveRight()
  * Funcao: responsavel por mover a imagem do consumidor para a direita usando a classe Platform
  * Parametros: int coordX = coordenada que informa para onde o consumidor deve se mover
  * Retorno: void
  ****************************************************************** */
  public void moveRight(int coordX){
    while(positionX != coordX){
      try {
        Thread.sleep(speed);
      } catch (InterruptedException e) {
        System.out.println("Erro de exceção!");
      }
      positionX++;
      Platform.runLater( () -> consumerImage.setX(positionX));
    }
  }//fim do metodo moveRight

  public void rotate(int angle){
    Platform.runLater( () -> consumerImage.setRotate(angle));
  }//fim do metodo rotate()

  /********************************************************************
  * Metodo: speedUp()
  * Funcao: aumenta a velocidade do consumidor. Quanto menor o valor do speed, maior a velocidade.
  No metodo, enquanto a velocidade for > 2, eh possivel aumentar a velocidade.
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void speedUp(){
    if(speed > 2){
      speed -= 2;
    }
  }//fim do metodo speedUp()

  /********************************************************************
  * Metodo: slowDown()
  * Funcao: diminui a velocidade do consumidor. Quanto maior o valor do speed, menor a velocidade.
  No metodo, enquanto a velocidade for <= 30, eh possivel diminuir a velocidade.
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void slowDown(){
    if(speed <= 30){
      speed += 2;
    }
  }//fim do metodo slowDown()
}
