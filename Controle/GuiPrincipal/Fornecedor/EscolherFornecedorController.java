/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Fornecedor;

import Conexao.Cliente;
import classes.base.Custo;
import classes.fornecedor.Fornecedor;
import Util.HistoricoPesquisa;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class EscolherFornecedorController implements Initializable{

    @FXML
    private TableView fornecedores;

    @FXML
    private TableColumn nome;

    @FXML
    private TableColumn cnpj;

    @FXML
    private ChoiceBox campos;

    @FXML
    private TextField campo;
    
    private Stage stage;
    
    private Custo custo;
    
    private boolean ok;

    private ArrayList<Fornecedor> fornecedoresArray = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("FORNECEDOR");        
        campos.setItems(FXCollections.observableArrayList(historico.getItens()));
        campos.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());
        
        fornecedores.setRowFactory(s -> {
            TableRow<Fornecedor> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    //aqui seleciona
                    salvar(linha.getItem());
                }
            });
            return linha;
        });
        cnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        nome.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        fornecedoresArray = Cliente.INSTANCIA.buscarUltimosFornecedores(50);
        atualizarTabela();
    }

    @FXML
    public void buscarEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            buscar();
        }
    }
    
    @FXML
    void buscar(){
        fornecedoresArray = Cliente.INSTANCIA.buscarFornecedorCampo((String) campos.getSelectionModel().getSelectedItem(), campo.getText().toUpperCase());
        atualizarTabela();
    }

  
    void salvar(Fornecedor item){
        
        custo.setIdFornecedor(item.getId());
        ok = true;
        stage.close();
    }
    
    @FXML
    void cancelar(){
        ok = false;
        stage.close();
    }
    
    private void atualizarTabela(){
        fornecedores.setItems(FXCollections.observableArrayList(fornecedoresArray));
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public void setCusto(Custo custo){
        this.custo = custo;
    }
        
    public boolean isOk(){
        return ok;
    }
}
