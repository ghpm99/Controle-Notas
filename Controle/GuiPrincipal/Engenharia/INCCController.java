/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Engenharia;

import Util.PaneController;
import Util.CarregarGui;
import Util.INCC;
import Util.Modulos;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class INCCController implements Initializable, PaneController{

    private ArrayList<INCC> inccs = new ArrayList<>();
    @FXML
    private TableColumn indiceColumn;

    @FXML
    private TableView<INCC> incc;

    @FXML
    private TextField indice;

    @FXML
    private DatePicker mes;

    @FXML
    private TableColumn mesColumn;
    private BorderPane pai;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    private INCC atual = new INCC();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        incc.setRowFactory(s -> {
            TableRow<INCC> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    INCC incc = linha.getItem();
                    setINCC(incc);
                }
            });
            return linha;
        });
        mesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<INCC, String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<INCC, String> param){
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getMes()));
                return simpleObject;
            }
        });
        indiceColumn.setCellValueFactory(new PropertyValueFactory<INCC, Double>("indice"));
        indice.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            editarTexto(newValue);
        });        
        atualizarTabela();
    }

    private void editarTexto(String newValue){
        String temp = newValue.isEmpty() ? "0" : newValue.replaceAll("[^0-9.]", "");
        indice.setText(temp);
        indice.positionCaret(temp.length());
    }

    private void addINCC(INCC incc){
        inccs.add(incc);
    }

    private void limparArray(){
        inccs.clear();
    }
    
    private void atualizarTabela(){
        limparArray();
        //Cliente.INSTANCIA.buscar("INCC", "MES", "%").stream().forEach(s -> addINCC(Cliente.INSTANCIA.buscarIncc(s)));
        incc.setItems(FXCollections.observableArrayList(inccs));
    }

    @FXML
    void salvar(ActionEvent event){
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar();
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception{
                atualizarINCC();
                //Cliente.INSTANCIA.INCC(atual);                
                return null;
            }

            @Override
            protected void succeeded(){
                atualizarTabela();
                setINCC(new INCC());
                pai.setDisable(false);
                carregar.fechar();
            }

        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void atualizarINCC(){
        atual.setMes(new Date(Date.from(Instant.from(mes.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime()));
        atual.setIndice(Double.valueOf(indice.getText()));
    }
    
    private void setINCC(INCC incc){
        atual = incc;
        atualizarCampos();
    }

    private void atualizarCampos(){
        mes.setValue(Instant.ofEpochMilli(atual.getMes().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        indice.setText(String.valueOf(atual.getIndice()));
    }

    @Override
    public void setPaneController(BorderPane controller){
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

}
