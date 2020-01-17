/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal;

import Conexao.Cliente;
import Util.PrincipalGUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Guilherme
 */
public class Main extends Application{

    private static Main instancia;

    private static Stage stage;

    private Parent parent;
    private PrincipalGUI atualControler;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.stage = primaryStage;
        setTitulo("Controle de custos");
        String imagem = LogarFxController.class.getResource("icon.png").toExternalForm();
        stage.getIcons().add(new Image(imagem));
        setScene(Caminhos.LOGAR);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args){
        Cliente.INSTANCIA.iniciar();
        getInstancia().launch(args);
    }

    public static Main getInstancia(){
        if(instancia == null){
            instancia = new Main();
        }
        return instancia;
    }

    /**
     *
     * @param caminhos
     */
    public void setScene(Caminhos caminhos){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhos.getCaminho()));
            parent = (Parent) loader.load();
            getInstancia().atualControler = ((PrincipalGUI) loader.getController());     
            Scene scene = new Scene(parent);
            stage.setResizable(caminhos.getResizable());
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

                @Override
                public void handle(WindowEvent event){
                    Cliente.INSTANCIA.fechar();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param titulo
     */
    public void setTitulo(String titulo){
        stage.setTitle(titulo);
    }

    /**
     *
     */
    public void maximizar(){
        stage.setMaximized(true);
    }

    /**
     *
     * @return
     */
    public static Stage getStage(){
        return stage;
    }

    /**
     *
     * @return
     */
    public String getTitulo(){
        return stage.getTitle();
    }

    public void notificar(){
        atualControler.notificar();
    }

    public Parent getParent(){
        return parent;
    }

    public PrincipalGUI getAtualControler(){        
        return atualControler;
    }

    public void ping(long latencia){
        if(atualControler != null)
        atualControler.setPing(latencia);
    }
    
}
