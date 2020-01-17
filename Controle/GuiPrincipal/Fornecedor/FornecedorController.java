/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Fornecedor;

import Conexao.Cliente;
import classes.fornecedor.Fornecedor;
import Util.CarregarGui;
import Util.HistoricoPesquisa;
import Util.Modificar;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class FornecedorController implements Initializable, PaneController{

    private BorderPane pai;
    @FXML
    private TableView tabela;
    @FXML
    private TableColumn inscMunicipalTableColumn;
    @FXML
    private TableColumn cnpjTableColumn;
    @FXML
    private TableColumn razaoSocialTableColumn;
    @FXML
    private TableColumn nomeFantasiaTableColumn;
    @FXML
    private TableColumn inscEstadualTableColumn;
    @FXML
    private ComboBox campos;
    @FXML
    private TextField campo;

    private ArrayList<Fornecedor> fornecedores = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("FORNECEDOR");        
        campos.setItems(FXCollections.observableArrayList(historico.getItens()));
        campos.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());
                
        tabela.setRowFactory(s -> {
            TableRow<Fornecedor> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    visualizar(linha.getItem());
                }
            });
            return linha;
        });
        inscMunicipalTableColumn.setCellValueFactory(new PropertyValueFactory<>("inscMunicipal"));
        cnpjTableColumn.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        razaoSocialTableColumn.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        nomeFantasiaTableColumn.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
        inscEstadualTableColumn.setCellValueFactory(new PropertyValueFactory<>("inscEstadual"));
        fornecedores = Cliente.INSTANCIA.buscarUltimosFornecedores(50);
        
        atualizarTabela();
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller){
        this.pai = controller;
    }

    /**
     *
     * @param evt
     */
    @FXML
    public void novoFornecedor(ActionEvent evt){
        visualizar(Cliente.INSTANCIA.incluirFornecedor());
    }

    /**
     *
     * @param evt
     */
    @FXML
    public void alterarFornecedor(ActionEvent evt){
        try{
            pai.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Modulos.FORNECEDORALTERAR.getCaminho()));
            Parent parent = (Parent) loader.load();
            PaneController controller = (PaneController) loader.getController();
            controller.setPaneController(pai);
            Modificar controle = (Modificar) loader.getController();
            controle.isNew(false);
            pai.setCenter(parent);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void buscar(){
        fornecedores = Cliente.INSTANCIA.buscarFornecedorCampo((String) campos.getSelectionModel().getSelectedItem(), campo.getText().toUpperCase());
        atualizarTabela();
    }

    @FXML
    public void buscarEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            buscar();
        }
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void visualizar(Fornecedor item){
        FornecedorAlterarController controller = new FornecedorAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(Modulos.FORNECEDOR);
        controller.setFornecedor(Cliente.INSTANCIA.buscarFornecedorCompleto(item.getId()));
        new CarregarGui().mostrar(pai, Modulos.FORNECEDORALTERAR, controller);
    }

    private void atualizarTabela(){
        tabela.setItems(FXCollections.observableArrayList(fornecedores));
    }

    @Override
    public void succeeded() {
    }
}
