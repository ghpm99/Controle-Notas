/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import classes.contrato.mapa.MapaContrato;
import Util.PaneController;
import Util.Modulos;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class MapaCotacaoController implements Initializable, PaneController{

    private BorderPane pai;
    @FXML
    private ComboBox campos;
    @FXML
    private TextField campo;
    @FXML
    private TableView mapas;
    @FXML
    private TableColumn numero;
    @FXML
    private TableColumn fornecedor;
    @FXML
    private TableColumn descricao;
    @FXML
    private TableColumn autor;
    @FXML
    private TableColumn total;
    @FXML
    private TableColumn ordem;
    @FXML
    private TableColumn criacao;
    @FXML
    private Label status;

    private ArrayList<MapaContrato> contratos = new ArrayList<MapaContrato>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO

        carregarTabela();
        preencherTabela();
    }

    /**
     *
     * @param evt
     */
    public void alterarMapa(ActionEvent evt){

        if(mapas.getSelectionModel().getSelectedItem() == null){
            return;
        }
        try{
            pai.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Modulos.MAPACOTACAOALTERAR.getCaminho()));
            Parent parent = (Parent) loader.load();
            MapaCotacaoAlterarController controller = (MapaCotacaoAlterarController) loader.getController();
            controller.setPaneController(pai);
            
            
            pai.setCenter(parent);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param evt
     */
    public void novoMapa(ActionEvent evt){
        try{
            pai.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Modulos.MAPACOTACAOALTERAR.getCaminho()));
            Parent parent = (Parent) loader.load();
            MapaCotacaoAlterarController controller = (MapaCotacaoAlterarController) loader.getController();
            controller.setPaneController(pai);
            
            if(mapas.getSelectionModel().getSelectedItem() != null){
               
            } else{
                
            }
            pai.setCenter(parent);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addContrato(MapaContrato arg0){
        this.contratos.add(arg0);
        atualizar();
    }

    private void atualizar(){
        mapas.setItems(FXCollections.observableArrayList(contratos));
    }

    private void resetarMapa(ArrayList<MapaContrato> arg0){
        this.contratos = arg0;
        atualizar();
    }

    private void setStatus(String msg){
        this.status.setText(msg);
    }

    /**
     *
     * @param event
     */
    @FXML
    public void buscarEnter(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            buscarMapa();
        }
    }

    /**
     *
     */
    @FXML
    public void buscarMapa(){
        setStatus("Pesquisando...");
       
        ArrayList<MapaContrato> tempMapa = new ArrayList<MapaContrato>();
       
        resetarMapa(tempMapa);
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller){
        this.pai = controller;
    }

    private void carregarTabela(){
        numero.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("numero"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("descricao"));
        autor.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("autor"));
        //total.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("numero"));
        //ordem.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("numero"));
        criacao.setCellValueFactory(new PropertyValueFactory<MapaContrato, String>("assinatura"));
    }

    private void preencherTabela(){
        ArrayList<MapaContrato> temp2 = new ArrayList<MapaContrato>();
        
        resetarMapa(temp2);
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

}
