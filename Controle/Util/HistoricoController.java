/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import classes.nota.item.ItemNota;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Historico;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class HistoricoController implements Initializable {

    @FXML
    private TableView historico;

    @FXML
    private TableColumn hora;

    @FXML
    private TableColumn usuario;

    @FXML
    private TableColumn evento;
    
    private Stage stage;
    
    
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hora.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Historico, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Historico, String> c){
                return new SimpleStringProperty(sdf.format(new Date(c.getValue().getHora())));
            }
        });
        usuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        evento.setCellValueFactory(new PropertyValueFactory<>("evento"));
    }
    
    public void setHistorico(ArrayList<Historico> historicos){
        historico.setItems(FXCollections.observableArrayList(historicos));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
