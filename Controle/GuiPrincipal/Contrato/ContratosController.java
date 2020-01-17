/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Conexao.Cliente;
import classes.contrato.Contrato;
import classes.contrato.ContratosTemp;
import Util.CarregarGui;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.HistoricoPesquisa;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
 * @author guilherme.machado
 */
public class ContratosController implements Initializable, PaneController{

    /**
     * Initializes the controller class.
     */
    private ArrayList<Contrato> contratosArray = new ArrayList<>();
    @FXML
    private TableView<Contrato> contratos;
    @FXML
    private TextField campo;
    @FXML
    private ComboBox<String> campos;
    @FXML
    private TableColumn<Contrato, String> numero;
    @FXML
    private TableColumn<Contrato, String> fornecedor;
    @FXML
    private TableColumn<Contrato, String> descricao;
    @FXML
    private TableColumn<Contrato, String> total;
    @FXML
    private TableColumn<Contrato, String> gasto;
    @FXML
    private TableColumn<Contrato, String> saldo;
    @FXML
    private TableColumn<Contrato, String> observacao;
    @FXML
    private TableColumn<Contrato, String> contrato;
    @FXML
    private TableColumn<Contrato, String> statusContrato;
    @FXML
    private Label status;
    private BorderPane pai;
    private CarregarGui carregar = new CarregarGui();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        contratos.setRowFactory(s -> {
            TableRow<Contrato> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){                    
                    visualizar(linha.getItem());
                }
            });
            return linha;
        });
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        total.setCellValueFactory((TableColumn.CellDataFeatures<Contrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValorTotal())));
        gasto.setCellValueFactory((TableColumn.CellDataFeatures<Contrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getGastoTotal())));        
        saldo.setCellValueFactory((TableColumn.CellDataFeatures<Contrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getSaldoTotal())));
        observacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        statusContrato.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        Cliente.INSTANCIA.getUltimosContratos(50).forEach(s -> contratosArray.add(s));
        
        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("CONTRATO");        
        campos.setItems(FXCollections.observableArrayList(historico.getItens()));
        campos.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());
        
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

    @FXML
    private void visualizar(){
        if(contratos.getSelectionModel().getSelectedItem() == null){
            return;
        }
        visualizar(contratos.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void novo(){
        visualizar(Cliente.INSTANCIA.incluirContrato());
        
    }

    private void visualizar(Contrato contrato){
        ContratosTemp.setCampo(campos.getSelectionModel().getSelectedItem());
        ContratosTemp.setValor(campo.getText());
        ContratoController controller = new ContratoController();
        controller.setPaneController(pai);
        controller.setContrato(contrato);
        new CarregarGui().mostrar(pai, Modulos.CONTRATO, controller);
    }

    private void addContrato(Contrato arg0){
        this.contratosArray.add(arg0);
        atualizarTabela();
    }

    @FXML
    private void buscar(){

        carregar.mostrar();
        pai.setDisable(true);
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception{
                contratosArray.clear();
//                Cliente.INSTANCIA.buscar("CONTRATO", campos.getValue(), campo.getText()).stream().forEach(s -> {
//                    addContrato(Cliente.INSTANCIA.buscarContrato(s));
//                });
                    Cliente.INSTANCIA.buscarContratoSimples(campos.getValue(), campo.getText().toUpperCase()).forEach(s -> {
                        addContrato(s);
                    });
                return null;
            }

            @Override
            protected void succeeded(){
                atualizarTabela();
                carregar.fechar();
                status.setText(contratosArray.size() + " resultados encontrados para " + campo.getText());
                pai.setDisable(false);
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void buscarEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            buscar();
        }
    }

    private void atualizarTabela(){
        contratos.setItems(FXCollections.observableArrayList(contratosArray));
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

}
