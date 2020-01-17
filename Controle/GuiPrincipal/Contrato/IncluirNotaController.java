/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Conexao.Cliente;
import classes.nota.Nota;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class IncluirNotaController implements Initializable{

    @FXML
    private TableColumn faturamentoDireto;

    @FXML
    private TableColumn numero;

    @FXML
    private TableView<Nota> notas;

    @FXML
    private TableColumn valor;

    @FXML
    private TableColumn fornecedor;

    @FXML
    private TableColumn descricao;
    private int idContrato;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        numero.setCellValueFactory(new PropertyValueFactory<Nota, String>("numero"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<Nota, String>("fornecedor"));
        faturamentoDireto.setCellValueFactory(new PropertyValueFactory<Nota, String>("faturamentoDireto"));
        descricao.setCellValueFactory(new PropertyValueFactory<Nota, String>("descricao"));
        valor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });
        notas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        notas.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.buscarNotaProblema()));
    }

     @FXML
    void cancelar(ActionEvent event) {
        stage.close();
    }

    @FXML
    void salvar(ActionEvent event) {
        notas.getSelectionModel().getSelectedItems().forEach(s -> {
            Cliente.INSTANCIA.setInt("NOTA", "IDCONTRATO", s.getId(), idContrato);
        });
        stage.close();
    }
    
    public void setContrato(int id){
        this.idContrato = id;
    }

    void setStage(Stage stage){
        this.stage = stage;
    }

    
}
