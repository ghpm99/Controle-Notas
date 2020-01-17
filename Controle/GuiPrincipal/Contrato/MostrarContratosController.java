/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import classes.contrato.Contrato;
import Util.CarregarGui;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class MostrarContratosController implements Initializable,PaneController {

    @FXML
    private TableColumn<Contrato, String> statusContrato;

    @FXML
    private TableColumn<Contrato, String> total;

    @FXML
    private TableColumn<Contrato, String> observacao;

    @FXML
    private TableColumn<Contrato, String> numero;

    @FXML
    private TableColumn<Contrato, String> contrato;

    @FXML
    private TableView<Contrato> contratos;

    @FXML
    private TableColumn<Contrato, String> fornecedor;

    @FXML
    private TableColumn<Contrato, String> saldo;

    @FXML
    private TableColumn<Contrato, String> gasto;

    @FXML
    private TableColumn<Contrato, String> descricao;
    
    private Stage stage;
    
    private BorderPane controller;   
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contratos.setRowFactory(s -> {
            TableRow<Contrato> linha = new TableRow<Contrato>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    Contrato contrato = linha.getItem();
                    visualizar(contrato);
                }
            });
            return linha;
        });
        numero.setCellValueFactory(new PropertyValueFactory<Contrato, String>("numero"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<Contrato, String>("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory<Contrato, String>("descricao"));
        total.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Contrato, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Contrato, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getValorTotal()));
            }
        });
        gasto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Contrato, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Contrato, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });
        saldo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Contrato, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Contrato, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getSaldoTotal()));
            }
        });
        observacao.setCellValueFactory(new PropertyValueFactory<Contrato, String>("observacao"));
        contrato.setCellValueFactory(new PropertyValueFactory<Contrato, String>("contrato"));
        statusContrato.setCellValueFactory(new PropertyValueFactory<Contrato, String>("status"));
    }    

    @Override
    public void setPaneController(BorderPane controller){
        this.controller = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    private void visualizar(Contrato contrato){
        if(controller == null){
            return;
        }
        ContratoController contratoController = new ContratoController();
        contratoController.setPaneController(this.controller);
        contratoController.setContrato(contrato);
        this.stage.close();
        new CarregarGui().mostrar(this.controller, Modulos.CONTRATO, contratoController);
    }
    
    public void setContratos(ArrayList<Contrato> arg0){
        contratos.getItems().clear();
        contratos.setItems(FXCollections.observableArrayList(arg0));
    }

    @Override
    public void succeeded() {
    }
}
