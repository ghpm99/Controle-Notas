/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Util.LogController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author guilherme.machado
 */
public class MostrarLog {
    
    public void mostrarLog(ArrayList<String> log){
        try {            
            FXMLLoader loader = new FXMLLoader(LogController.class.getResource("Log.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Log");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            LogController controller = loader.getController();
            controller.setStage(stage);
            controller.setLog(log);

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
