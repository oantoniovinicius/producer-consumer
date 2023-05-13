/* ***************************************************************
* Autor............: Antonio Vinicius Silva Dutra
* Matricula........: 202110810
* Inicio...........: 06/05/2023
* Ultima alteracao.: 10/05/2023
* Nome.............: Principal.java
* Funcao...........: Inicia a GUI do programa pelo metodo start()
*************************************************************** */
import control.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Principal extends Application{
  //instanciando o controlador principal
  MainControl mainController = new MainControl();  

  public static void main(String[] args) throws Exception{
    launch(args);
  }

  /********************************************************************
  * Metodo: start()
  * Funcao: responsavel por inicializar a interface grafica da aplicacao e exibir a tela de inicio na janela principal.
  * Parametros: primaryStage= objeto Stage que representa a janela principal da aplicacao.
  * Retorno: void
  ****************************************************************** */
  @Override
  public void start(Stage primaryStage) throws Exception {  
    primaryStage.setTitle("DOMINO'S PIZZA");
    Parent loader = FXMLLoader.load((getClass().getResource("./view/MainScreen.fxml")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("./resources/icon.png")));
    Scene scene = new Scene(loader);  
    
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }//fim do metodo start()
}//fim da classe Principal
