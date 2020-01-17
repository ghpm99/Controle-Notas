/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Engenharia;

import Util.PaneController;
import classes.nota.item.ItemNota;
import classes.nota.Nota;
import classes.relatorio.Relatorio;

import Util.ItemRelatorio;
import Util.Modulos;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class PlanoContaController implements Initializable, PaneController {

    private ArrayList<Nota> notas = new ArrayList<Nota>();
    @FXML
    private ComboBox campos;
    @FXML
    private TextField campo;
    @FXML
    private TableColumn numeroMapa;
    @FXML
    private TableColumn fornecedor;
    @FXML
    private TableColumn descricao;
    @FXML
    private TableColumn itemOrcamento;
    @FXML
    private TableColumn planoContas;
    @FXML
    private TableColumn notaFiscal;
    @FXML
    private TableColumn retencao;
    @FXML
    private TableColumn total;
    @FXML
    private TableColumn emissao;
    @FXML
    private Label status;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private BorderPane pai;
    @FXML
    private TableView<ItemRelatorio> orcamentos;
    @FXML
    private Button alterar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        numeroMapa.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("numeroMapa"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("descricao"));
        itemOrcamento.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("itemOrcamento"));
        planoContas.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("planoContas"));
        notaFiscal.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("notaFiscal"));
        retencao.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("retencao"));
        total.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("total"));
        emissao.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("emissao"));
//        Cliente.INSTANCIA.buscar("NOTA", "NUMERO", "%").stream().forEach(s -> {
//           // notas.add(new Nota(s));
//        });
        atualizarTabela();
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FXML
    void buscarEnter(ActionEvent event) {

    }

    @FXML
    void buscarOrcamento(ActionEvent event) {

    }

    @FXML
    void gerar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));
        new Relatorio().orcamento(fileChooser.showSaveDialog(null), notas);
    }

    @FXML
    void desativar(ActionEvent event) {

    }

    private void atualizarTabela() {
        ArrayList<ItemRelatorio> itensRela = new ArrayList<>();
        notas.stream().forEach(nota -> {
            
                
            
        });
        orcamentos.setItems(FXCollections.observableArrayList(itensRela));
    }

    @Override
    public void succeeded() {
    }

}
