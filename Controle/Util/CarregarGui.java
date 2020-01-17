/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import GuiPrincipal.GuiMainController;
import GuiPrincipal.Main;
import GuiPrincipal.Util.CarregarController;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author guilherme.machado
 */
public class CarregarGui {

    Stage stage;

    /**
     *
     */
    public void mostrar() {
        ProgressIndicator pi = new ProgressIndicator();
        pi.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(Main.getInstancia().getStage());
        Scene scene = new Scene(pi);
        stage.setScene(scene);        
        stage.show();
    }

    public void mostrar(CarregarController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(CarregarController.class.getResource("Carregar.fxml"));
            loader.setController(controller);
            Parent parent = (Parent) loader.load();

            stage = new Stage();

            stage.setTitle("Carregando");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(Main.getInstancia().getStage());

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    long antes = new Date().getTime();
                    controller.run();
                    long depois = new Date().getTime();
                    long diferença = depois - antes;
                    Main.getInstancia().ping(diferença);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    stage.hide();
                    controller.sucesso();
                }

                @Override
                protected void failed() {
                    super.failed();
                    getException().printStackTrace();
                    stage.hide();
                    controller.falha();
                }

            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

//            ProgressIndicator pi = new ProgressIndicator();
//            pi.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
//            stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.initOwner(Main.getStage());
//            Scene scene = new Scene(pi);
//            stage.setScene(scene);
//            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void fechar() {

        stage.close();
    }

   
    public <T> T mostrarTask(BorderPane pai, CustomTask<T> task) {
        try {            
            pai.setDisable(true);
            mostrar();            
            task.setOnSucceeded((WorkerStateEvent t) -> {
                pai.setDisable(false);
                fechar();
            });
            task.setOnFailed((WorkerStateEvent t) -> {
                pai.setDisable(false);
                t.getSource().getException().printStackTrace();
                fechar();
            });
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            
            thread.start();
            
            return task.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(CarregarGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param pai
     * @param modulo
     * @param paneController
     */
    public void mostrar(BorderPane pai, Modulos modulo, PaneController paneController) {
        pai.setDisable(true);
        mostrar();

        Task<Parent> task = new Task<Parent>() {

            @Override
            protected Parent call() throws Exception {
                long antes = new Date().getTime();
                Parent parent = null;
                try {
                    FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(modulo.getCaminho()));
                    loader.setController(paneController);
                    parent = (Parent) loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long depois = new Date().getTime();
                long diferença = depois - antes;
                Main.getInstancia().ping(diferença);
                return parent;
            }

            @Override
            protected void succeeded() {
                try {
                    pai.getChildren().clear();
                    pai.setCenter(get());
                    pai.setDisable(false);
                    fechar();
                    paneController.succeeded();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarregarGui.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(CarregarGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     *
     * @param pai
     * @param modulo
     */
    public void voltar(BorderPane pai, Modulos modulo) {
        pai.setDisable(true);
        mostrar();

        Task<Parent> task = new Task<Parent>() {

            @Override
            protected Parent call() throws Exception {
                long antes = new Date().getTime();
                Parent parent = null;
                try {
                    FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(modulo.getCaminho()));
                    parent = (Parent) loader.load();
                    PaneController controller = loader.getController();
                    controller.setPaneController(pai);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                long depois = new Date().getTime();
                long diferença = depois - antes;
                Main.getInstancia().ping(diferença);
                return parent;
            }

            @Override
            protected void succeeded() {
                try {
                    pai.getChildren().clear();
                    pai.setCenter(get());
                    pai.setDisable(false);
                    fechar();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CarregarGui.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(CarregarGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
