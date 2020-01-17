/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import classes.financeiro.DadoBancario;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class DadoBancarioController implements Initializable, PaneController {

    @FXML
    private TableView<DadoBancario> dadosBancariosTable;

    @FXML
    private TableColumn<DadoBancario, String> bancoTableColumn;

    @FXML
    private TableColumn<DadoBancario, String> cnpjTableColumn;

    @FXML
    private TableColumn<DadoBancario, String> nomeTableColumn;

    @FXML
    private TableColumn<DadoBancario, String> agenciaTableColumn;

    @FXML
    private TableColumn<DadoBancario, String> contaTableColumn;

    @FXML
    private TableColumn<DadoBancario, String> tipoTableColumn;

    private BorderPane controller;

    private ArrayList<DadoBancario> dados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bancoTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getBanco().getNome());
            }
        });
        bancoTableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn());
        bancoTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setBanco(null);
                atualizarTabela();
            }
        });
        
        cnpjTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getCnpj());
            }
        });
        cnpjTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cnpjTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setCnpj(event.getNewValue());
                atualizarTabela();
            }
        });
        
        nomeTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getNome());
            }
        });
        nomeTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nomeTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setNome(event.getNewValue());
                atualizarTabela();
            }
        });
        
        agenciaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getAgencia());
            }
        });
        agenciaTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        agenciaTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setAgencia(event.getNewValue());
                atualizarTabela();
            }
        });
        
        contaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getConta());
            }
        });
        contaTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contaTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setConta(event.getNewValue());
                atualizarTabela();
            }
        });
        
        tipoTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DadoBancario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DadoBancario, String> c) {
                return new SimpleStringProperty(c.getValue().getTipo().getTipo());
            }
        });
        tipoTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tipoTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DadoBancario, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<DadoBancario, String> event) {
                event.getRowValue().setTipo(null);
                atualizarTabela();
            }
        });
        
        atualizarTabela();
    }

    private void atualizarTabela() {
        dadosBancariosTable.getItems().clear();
        dadosBancariosTable.setItems(FXCollections.observableArrayList(dados));
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.controller = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setDadoBancario(ArrayList<DadoBancario> arg0) {
        this.dados = arg0;
    }

    @FXML
    void incluir(ActionEvent event) {

    }

    @Override
    public void succeeded() {
    }
}
