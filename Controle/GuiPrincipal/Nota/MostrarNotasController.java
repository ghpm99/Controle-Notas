/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import Conexao.Cliente;
import classes.nota.Nota;
import Util.CarregarGui;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
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
public class MostrarNotasController implements Initializable,PaneController {

   @FXML
    private TableColumn<Nota, String> tipo;

    @FXML
    private TableColumn<Nota, String> observacao;

    @FXML
    private TableColumn<Nota, String> numero;

    @FXML
    private TableColumn<Nota, String> vencimentoReal;

    @FXML
    private TableColumn<Nota, String> aprovado;

    @FXML
    private TableView<Nota> notas;

    @FXML
    private TableColumn<Nota, String> valor;

    @FXML
    private TableColumn<Nota, String> vencimento;

    @FXML
    private TableColumn<Nota, String> descricao;

    @FXML
    private TableColumn<Nota, String> prenota;

    @FXML
    private TableColumn<Nota, String> classificada;

    @FXML
    private TableColumn<Nota, String> lancada;

    @FXML
    private TableColumn<Nota, String> serie;

    @FXML
    private TableColumn<Nota, String> fornecedor;

    private SimpleDateFormat sdf;
    
    private BorderPane controller;
    
    private Stage stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notas.setRowFactory(s -> {
            TableRow<Nota> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    visualizar(linha.getItem());
                }
            });
            return linha;
        });
        sdf = new SimpleDateFormat("dd/MM/yyyy");
       

        numero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){

            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> p){
                // p.getValue() returns the Person instance for a particular TableView row

                return new ReadOnlyObjectWrapper(p.getValue().getNumero());
            }
        });
        numero.setCellFactory(new Callback<TableColumn<Nota, String>, TableCell<Nota, String>>(){
            @Override
            public TableCell<Nota, String> call(final TableColumn<Nota, String> personStringTableColumn){
                return new TableCell<Nota, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty){
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.

                        setText(empty ? "" : item);
                        if(item == null){
                            return;
                        }

                    }

                };
            }
        });
        serie.setCellValueFactory(new PropertyValueFactory("serie"));
        fornecedor.setCellValueFactory(new PropertyValueFactory("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        valor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });
        vencimento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(sdf.format(c.getValue().getVencimento()));
            }
        });
        vencimentoReal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){

                return new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal()));
            }
        });
        tipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        prenota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(c.getValue().isPreNota() ? "Sim" : "Nao");
            }
        });
        
        classificada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(c.getValue().isClassificada() ? "Sim" : "Nao");
            }
        });
        lancada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(c.getValue().isLancada() ? "Sim" : "Em aberto");
            }
        });
        observacao.setCellValueFactory(new PropertyValueFactory("observacao"));
        aprovado.setCellValueFactory(new PropertyValueFactory("status"));
    }    
    
    private void visualizar(Nota nota){        
        NotaAlterarController notaController = new NotaAlterarController();
        notaController.setPaneController(controller);
        notaController.setModuloVoltar(Modulos.NOTA);
        notaController.setNota(Cliente.INSTANCIA.buscarNota(nota.getId()));
        stage.close();
        new CarregarGui().mostrar(controller, Modulos.NOTAALTERAR, notaController);
    }

    @Override
    public void setPaneController(BorderPane controller){
        this.controller = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public void setNotas(ArrayList<Nota> arg0){
        notas.getItems().clear();
        notas.setItems(FXCollections.observableArrayList(arg0));
    }

    @Override
    public void succeeded() {
    }
}
