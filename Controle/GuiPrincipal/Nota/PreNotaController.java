/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import Conexao.Cliente;
import classes.nota.Nota;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
 * @author Eduardo
 */
public class PreNotaController implements Initializable, PaneController {

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
    private TableColumn<Nota, String> fornecedor;
    @FXML
    private TableColumn<Nota, String> descricao;
    @FXML
    private TableColumn<Nota, String> valor;
    @FXML
    private TableColumn<Nota, String> vencimento,vencimentoReal;
    
    @FXML
    private TableColumn<Nota, String> prenota;
    @FXML
    private TableColumn<Nota, String> classificada;
    @FXML
    private TableColumn<Nota, String> lancada;
    @FXML
    private TableColumn<Nota, String> observacao;
    @FXML
    private TableColumn<Nota,String> incluso;
    @FXML
    private Label status;
    private SimpleDateFormat sdf;
    private ArrayList<Nota> notasResult = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notas.setRowFactory(s -> {
            TableRow<Nota> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                    Nota nota = linha.getItem();
                    visualizar(nota);
                }
            });
            return linha;
        });
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("NOTA");
        campos.setItems(FXCollections.observableArrayList(historico.getItens()));
        campos.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());

        numero.setCellValueFactory(new PropertyValueFactory("numero"));
        fornecedor.setCellValueFactory(new PropertyValueFactory("fornecedor"));
        descricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        valor.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getGastoTotal())));
        vencimento.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimento())));
        vencimentoReal.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal())));
        incluso.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getInclusao())));
        prenota.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(c.getValue().isPreNota() ? "Sim" : "Nao"));

        classificada.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(c.getValue().isClassificada() ? "Sim" : "Nao"));
        lancada.setCellValueFactory((TableColumn.CellDataFeatures<Nota, String> c) -> new SimpleStringProperty(c.getValue().isLancada() ? "Sim" : "Em aberto"));
        observacao.setCellValueFactory(new PropertyValueFactory("observacao"));

        Cliente.INSTANCIA.buscarPreNotas().forEach(s -> addNota(s));
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    public void alterarNota(ActionEvent evt) {

        if (notas.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        visualizar(notas.getSelectionModel().getSelectedItem());

    }

    private void visualizar(Nota nota) {
        NotaAlterarController controller = new NotaAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(Modulos.PRENOTA);
        controller.setNota(Cliente.INSTANCIA.buscarNota(nota.getId()));
        new CarregarGui().mostrar(pai, Modulos.NOTAALTERAR, controller);
    }

    private void addNota(Nota arg0) {
        if (!arg0.isCancelado()) {
            this.notasResult.add(arg0);
            atualizar();
        }
    }

    private void atualizar() {
        notas.setItems(FXCollections.observableArrayList(notasResult));
    }

    private void resetarNotas(ArrayList<Nota> arg0) {
        this.notasResult = arg0;
        atualizar();
    }

    private void limparTabela() {
        this.notasResult.clear();
        //atualizar();
    }

    private void setStatus(String msg) {
        this.status.setText(msg);
    }

    /**
     *
     * @param event
     */
    @FXML
    public void buscarEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            buscarNota();
        }
    }

    @FXML
    private void buscarNota() {
        CarregarGui carregar = new CarregarGui();
        carregar.mostrar();
        pai.setDisable(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                limparTabela();

                return null;
            }

            @Override
            protected void succeeded() {
                atualizar();
                carregar.fechar();
                pai.setDisable(false);
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

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
