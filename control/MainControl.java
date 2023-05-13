/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 06/05/2023
* Ultima alteracao.: 13/05/2023
* Nome.............: MainControl.java
* Funcao...........: gerencia e controla a GUI do fxml, inicia as threads e controla métodos da GUI
*************************************************************** */
package control;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class MainControl implements Initializable{
  //instanciando todos os imageView e um array de imageView que vai receber os imageView das pizzas nas threads
  @FXML private ImageView producerImage;
  @FXML private ImageView consumerImage;
  @FXML private ImageView pizzaImageOne;
  @FXML private ImageView pizzaImageTwo;
  ImageView[] images = new ImageView[2];

  //instanciando todos os botoes
  @FXML private Button speedUpProducer;
  @FXML private Button slowDownProducer;
  @FXML private Button speedUpConsumer;
  @FXML private Button slowDownConsumer;
  @FXML private Button pausarConsumidor;
  @FXML private Button pausarProdutor;
  @FXML private Button reiniciar;
  @FXML private Button Iniciar;

  //instanciando elementos de texto e visuais
  @FXML private Line lineBlue;
  @FXML private Line lineRed;
  @FXML private Text textBlack;
  @FXML private Text textRed;

  //instanciando as threads que serao iniciadas
  private Producer producer;
  private Consumer consumer;

  //variaveis de controle do semaforo
  private static int pizzaNum = 0; //contador de pizzas
  private static final int buffer = 2;
  private static Semaphore semaphoreMutex = new Semaphore(1); //controla o acesso a regiao critica, 1 = regiao critica vazia
  private static Semaphore semaphoreEmpty = new Semaphore(buffer); //contas as posicoes vazias do buffer
  private static Semaphore semaphoreFull = new Semaphore(0); //contas as posicoes ocupadas do buffer

  /********************************************************************
  * Metodo: createThreads()
  * Funcao: cria as threads do produtor e consumidor e chama o metodo startThreads() para iniciar as threads criadas
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void createThreads(){
    producer = new Producer(this);//cria a Thread do trem preto
    consumer = new Consumer(this);//cria a Thread do trem vermelho
    startThreads();
  }//fim do metodo createThreads
  
  /********************************************************************
  * Metodo: startThreads()
  * Funcao: inicia as threads
  * Parametros: nenhum
  * Retorno: void
  ****************************************************************** */
  public void startThreads(){
    producer.start();
    consumer.start();
  }//fim do metodo startThreads()

  /********************************************************************
  * Metodo: startButton()
  * Funcao: chama o createThreads() para iniciar a simulacao e ativa os elementos graficos e de controle da GUI
  * Parametros: ActionEvent event = acao de clicar
  * Retorno: void
  ****************************************************************** */
  @FXML void startButton(ActionEvent event) {
    createThreads();
    
    Iniciar.setDisable(true);
    Iniciar.setVisible(false);
    producerImage.setVisible(true);
    consumerImage.setVisible(true);

    textBlack.setVisible(true);
    textRed.setVisible(true);
    speedUpProducer.setDisable(false);
    speedUpProducer.setVisible(true);
    speedUpConsumer.setDisable(false);
    speedUpConsumer.setVisible(true);
    slowDownProducer.setDisable(false);
    slowDownProducer.setVisible(true);
    slowDownConsumer.setDisable(false);
    slowDownConsumer.setVisible(true);
    lineBlue.setDisable(false);
    lineBlue.setVisible(true);
    lineRed.setDisable(false);
    lineRed.setVisible(true);
    pausarConsumidor.setDisable(false);
    pausarConsumidor.setVisible(true);
    pausarProdutor.setDisable(false);
    pausarProdutor.setVisible(true);
    reiniciar.setDisable(false);
    reiniciar.setVisible(true);
  }//fim do metodo startButton()

  /********************************************************************
  * Metodo: pauseConsumer()
  * Funcao: pausa a threads do consumidor verificando o texto no botao de pausar.
  Suspende a thread caso o botao seja "pausar consumidor" e atualiza o texto para "retomar consumidor", continuando a thread
  de onde ela pausou
  * Parametros: ActionEvent event = acao de clicar
  * Retorno: void
  ****************************************************************** */
  @FXML void pauseConsumer(ActionEvent event) {
    if(pausarConsumidor.getText().equals("PAUSAR CONSUMIDOR")){
      consumer.suspend();
      pausarConsumidor.setText("RETOMAR CONSUMIDOR");
    }else{
      consumer.resume();
      pausarConsumidor.setText("PAUSAR CONSUMIDOR");
    }
  }//fim do metodo pauseConsumer()

  /********************************************************************
  * Metodo: pauseProducer()
  * Funcao: pausa a threads do produtor verificando o texto no botao de pausar.
  Suspende a thread caso o botao seja "pausar produtor" e atualiza o texto para "retomar produtor", continuando a thread
  de onde ela pausou
  * Parametros: ActionEvent event = acao de clicar
  * Retorno: void
  ****************************************************************** */
  @FXML void pauseProducer(ActionEvent event) {
    if(pausarProdutor.getText().equals("PAUSAR PRODUTOR")){
      producer.suspend();
      pausarProdutor.setText("RETOMAR PRODUTOR");
    }else{
      producer.resume();
      pausarProdutor.setText("PAUSAR PRODUTOR");
    }
  }//fim do metodo pauseProducer()

  /********************************************************************
  * Metodo: restart()
  * Funcao: botao para reiniciar a simulacao. Apos clicar no botao, ele mata as threads com o metodo stop(),
  reinicia as variaveis de controle e inicia novas threads.
  * Parametros: ActionEvent event = acao de clicar
  * Retorno: void
  ****************************************************************** */
  @FXML void restart(ActionEvent event) {
    producer.stop();
    consumer.stop();
    producer = new Producer(this);
    consumer = new Consumer(this);
    setMutex(1);
    setEmpty(2);
    setFull(0);
    setPizzaNum(0);
    pizzaImageOne.setVisible(false);
    pizzaImageTwo.setVisible(false);
    changeImageProducer("./resources/produtor.png");
    producer.rotate(180);
    producer.start();
    consumer.start();
  }
  
  /********************************************************************
  * Metodo: initialize()
  * Funcao: desativa todos os elementos graficos exceto pelo primeiro background e o botao iniciar ate que esse botao iniciar
  seja clicado,  e atribui funcoes para os botoes de aumentar e diminuir velocidade
  * Parametros: URL location, resources
  * Retorno: void
  ****************************************************************** */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    pizzaImageOne.setDisable(true);
    pizzaImageTwo.setDisable(true);
    pizzaImageOne.setVisible(false);
    pizzaImageTwo.setVisible(false);
    speedUpProducer.setDisable(true);
    speedUpProducer.setVisible(false);
    slowDownProducer.setVisible(false);
    speedUpConsumer.setDisable(true);
    speedUpConsumer.setVisible(false);
    slowDownConsumer.setVisible(false);
    lineBlue.setDisable(true);
    lineBlue.setVisible(false);
    lineRed.setDisable(true);
    lineRed.setVisible(false);
    pausarConsumidor.setDisable(true);
    pausarConsumidor.setVisible(false);
    pausarProdutor.setVisible(false);
    pausarProdutor.setDisable(true);
    reiniciar.setDisable(true);
    textBlack.setVisible(false);
    textRed.setVisible(false);
    reiniciar.setVisible(false);

    speedUpProducer.setOnMouseClicked(Event ->{
      producer.speedUp();//aumenta velocidade do produtor
    });

    slowDownProducer.setOnMouseClicked(Event ->{
      producer.slowDown();//diminui velocidade do produtor
    });

    speedUpConsumer.setOnMouseClicked(Event ->{
      consumer.speedUp();//aumenta velocidade do consumidor
    });

    slowDownConsumer.setOnMouseClicked(Event ->{
      consumer.slowDown();//diminui velocidade do consumidor
    });
  }//fim do metodo initialize

  //incrementa +1 no contador de pizzas
  public void increasePizza(){
    pizzaNum++;
  }
  //decrementa -1 no contador de pizzas
  public void decreasePizza(){
    pizzaNum--;
  }

  //atualiza a imagem do produtor para uma nova imagem a partir de um url
  public void changeImageProducer(String url){
    producerImage.setImage(new Image(url));
  }
  //atualiza a imagem do consumidor para uma nova imagem a partir de um url
  public void changeImageConsumer(String url){
    consumerImage.setImage(new Image(url));
  }

  //abaixo metodos de setters e getters
  public static void setMutex(int valor){
    semaphoreMutex = new Semaphore(valor);
  }
  public static void setEmpty(int buffer){
    semaphoreEmpty = new Semaphore(buffer);
  } 
  public static void setFull(int i){
    semaphoreFull = new Semaphore(i);
  }
  public static void setPizzaNum(int valor){
    pizzaNum = valor;
  }
  public ImageView getImageViewProducer() {
    return producerImage;
  }
  public ImageView getImageViewConsumer() {
    return consumerImage;
  }
  public ImageView getImageViewFirstPizza() {
    return pizzaImageOne;
  }
  public ImageView getImageViewSecondPizza() {
    return pizzaImageTwo;
  }
  public Semaphore getSemaphoreMutex() {
    return semaphoreMutex;
  }
  public Semaphore getSemaphoreFull() {
    return semaphoreFull;
  }
  public Semaphore getSemaphoreEmpty() {
    return semaphoreEmpty;
  }
  public int getPizzaNum() {
    return pizzaNum;
  }
}//fim da classe MainControl()