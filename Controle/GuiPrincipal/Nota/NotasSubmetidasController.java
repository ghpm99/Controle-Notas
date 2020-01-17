/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import Conexao.Cliente;
import GuiPrincipal.Util.CarregarController;
import classes.nota.Nota;
import classes.nota.NotasTemp;
import Util.CarregarGui;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.HistoricoPesquisa;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class NotasSubmetidasController implements Initializable, PaneController{

    private BorderPane pai;
    @FXML
    private ComboBox<String> campos;
    @FXML
    private TextField campo;
    @FXML
    private TableView<Nota> notas;
    @FXML
    private TableColumn<Nota, String> numero;
    @FXML
    private TableColumn serie;
    @FXML
    private TableColumn fornecedor;
    @FXML
    private TableColumn descricao;
    @FXML
    private TableColumn valor;
    @FXML
    private TableColumn vencimento;
    @FXML
    private TableColumn vencimentoReal;
    @FXML
    private TableColumn tipo;
    @FXML
    private TableColumn prenota;
    @FXML
    private TableColumn classificada;
    @FXML
    private TableColumn aprovado;
    @FXML
    private TableColumn lancada;
    @FXML
    private TableColumn observacao;
    @FXML
    private Label status;

    private SimpleDateFormat sdf;

    private ArrayList<Nota> notasResult;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){

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

        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("NOTA");
        campos.setItems(FXCollections.observableArrayList(historico.getItens()));
        campos.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());

        numero.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getNumero()));
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

        this.notasResult = Cliente.INSTANCIA.listarNotasSubmetidas();

        atualizar();
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
    public void aprovarNota(ActionEvent evt){
        Nota temp = notas.getSelectionModel().getSelectedItem();
        if(temp != null){
            int retorno = Cliente.INSTANCIA.workflowNota(temp.getIdPmWeb(), "approved");
            switch(retorno){
                case -1:
                    status.setText("falhou");
                    break;
                case 0:
                    status.setText("Nota " + temp.getNumero() + " aprovada com sucesso");
                    break;
                default:
                    status.setText("");
                    break;
            }
        }
    }

    public void retornarNota(ActionEvent evt){

    }

    public void rejeitarNota(ActionEvent evt){

    }

    private void visualizar(Nota nota){
        NotasTemp.setCampo(campos.getSelectionModel().getSelectedItem());
        NotasTemp.setValor(campo.getText());
        NotaAlterarController controller = new NotaAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(Modulos.NOTASUBMETIDAS);
        controller.setNota(Cliente.INSTANCIA.buscarNota(nota.getId()));
        new CarregarGui().mostrar(pai, Modulos.NOTAALTERAR, controller);
    }

    private void addNota(Nota arg0){
        this.notasResult.add(arg0);

    }

    private void atualizar(){
        notas.setItems(FXCollections.observableArrayList(notasResult));
    }

    private void resetarNotas(ArrayList<Nota> arg0){
        this.notasResult = arg0;
        atualizar();
    }

    private void limparTabela(){
        this.notasResult.clear();

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
        if(event.getCode() == KeyCode.ENTER){
            buscarNota();
        }
    }

    @FXML
    private void buscarNota(){
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar(new CarregarController(){
            @Override
            public void run(){
                Platform.runLater(() -> setStatus("Pesquisando " + campo.getText()));
                barraCarregar2.setProgress(1.0f);
                limparTabela();
                notasResult = Cliente.INSTANCIA.buscarNota(campos.getValue(), campo.getText());
                circuloCarregar1.setProgress(1);
                barraCarregar2.setProgress(0.6f);
                Platform.runLater(() -> setStatus(notasResult.size() + " resultados encontrados para " + campo.getText()));
                atualizar();
                pai.setDisable(false);
                barraCarregar2.setProgress(0.9f);

            }

            @Override
            public void sucesso(){
                campo.requestFocus();
                campo.selectAll();
            }

            @Override
            public void falha(){

            }
        });

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
    public void succeeded(){

    }

}
