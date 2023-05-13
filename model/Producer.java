/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 06/05/2023
* Ultima alteracao.: 13/05/2023
* Nome.............: Producer.java
* Funcao...........: executa os comandos de thread do produtor e controla as animacoes do produtor e regiao critica
*************************************************************** */
package model;

import control.*;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class Producer extends Thread {
  private MainControl controller;
  private ImageView producerImage;
  private ImageView pizzaImageOne;
  private ImageView pizzaImageTwo;
  ImageView[] images = new ImageView[2];
  private int speed = 6;
  private int positionX;
  private int positionY;

  /********************************************************************
  * Metodo: Producer()
  * Funcao: construtor da classe
  * Parametros: objeto MainControl
  * Retorno: void
  ****************************************************************** */
  public Producer(MainControl controller){
    this.controller = controller;
    this.producerImage = controller.getImageViewProducer();
    pizzaImageOne = controller.getImageViewFirstPizza();
    pizzaImageTwo = controller.getImageViewSecondPizza();
    images[0] = pizzaImageOne;
    images[1] = pizzaImageTwo;
  }//fim do metodo Producer()

  /********************************************************************
  * Metodo: run()
  * Funcao: executa um loop infinito e dentro desse loop eh responsavel por movimentar o produtor, controlar o acesso 
  a regiao critica e produzir um item (pizza). Utiliza os metodos moveRight() e moveLeft() que, atraves da classe Platform.runLater() 
  atualiza a posicao do produtor na tela
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void run() {
    try{
      while(true){
        produzPizza(); //gera uma pizza para colocar no buffers
        
        controller.getSemaphoreEmpty().acquire(); //decrementa o contador de posicoes vazias
        controller.getSemaphoreMutex().acquire(); //entra na regiao critica
        
        adicionaPizza(); //coloca uma nova pizza no buffer
        
        controller.getSemaphoreMutex().release(); //deixa a regiao critica
        controller.getSemaphoreFull().release(); //incrementa o contador de posicoes ocupadas
        //saida da regiao critica concluida
      }
    }catch (InterruptedException e){ 
      System.out.println("Erro de exceção!");
    }
  }//fim do metodo run()

  /********************************************************************
  * Metodo: produzPizza()
  * Funcao: responsavel por comandos de movimentacao do produtor: move o produtor ate o forno, troca a imagem do produtor
  para mostrar ele pegando a pizza e leva a pizza ate a regiao critica (balcao)
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void produzPizza() {
    Platform.runLater( () -> producerImage.setX(positionX));//setando a posicao inicial do produtor no eixo X
    Platform.runLater( () -> producerImage.setY(positionY));//setando a posicao inicial do produtor no eixo y
    //movendo o produtor ate o forno
    moveLeft(-260);

    //entregando a pizza
    Platform.runLater( () -> controller.changeImageProducer("./resources/produtorPizza.png"));
    Platform.runLater( () -> producerImage.setY(positionY = 7));
    rotate(360);
    moveRight(40);
  }//fim do metodo produzPizza()

   /********************************************************************
  * Metodo: adicionaPizza()
  * Funcao: troca a imagem do produtor para a imagem dele sem a pizza e coloca as pizzas no balcao apos verificacao dos semaforos
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void adicionaPizza(){ 
    Platform.runLater( () -> controller.changeImageProducer("./resources/produtor.png"));
    Platform.runLater( () -> producerImage.setY(positionY = 0));
    rotate(180);

    setPizza();
  } // fim do metodo adicionaPizza()


  /********************************************************************
  * Metodo: setPizza()
  * Funcao: utilizando o contador de pizzas do controle, ativa a imagem de acordo com o index do array e incrementa o valor do contador
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void setPizza(){    
    images[controller.getPizzaNum()].setVisible(true);
    images[controller.getPizzaNum()].setOpacity(1);
    controller.increasePizza();
  }//fim do metodo setPizza()

  /********************************************************************
  * Metodo: moveLeft()
  * Funcao: responsavel por mover a imagem do produtor para a esquerda usando a classe Platform
  * Parametros: int coordX = coordenada que informa para onde o produtor deve se mover
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
      Platform.runLater( () -> producerImage.setX(positionX));
    }
  }//fim do metodo moveLeft

  /********************************************************************
  * Metodo: moveRight()
  * Funcao: responsavel por mover a imagem do produtor para a direita usando a classe Platform
  * Parametros: int coordX = coordenada que informa para onde o produtor deve se mover
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
      Platform.runLater( () -> producerImage.setX(positionX));
    }
  }//fim do metodo moveRight

  public void rotate(int angle){
    Platform.runLater( () -> producerImage.setRotate(angle));
  }//fim do metodo rotate()

  /********************************************************************
  * Metodo: speedUp()
  * Funcao: aumenta a velocidade do produtor. Quanto menor o valor do speed, maior a velocidade.
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
  * Funcao: diminui a velocidade do produtor. Quanto maior o valor do speed, menor a velocidade.
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