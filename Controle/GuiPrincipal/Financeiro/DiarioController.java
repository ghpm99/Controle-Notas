/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Diario;
import GuiPrincipal.Nota.NotaAlterarController;
import GuiPrincipal.Util.Mensagem;
import Util.CarregarGui;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class DiarioController implements Initializable, PaneController {

    private BorderPane pai;
    @FXML
    private TableColumn<Diario, String> numeroDiario;

    @FXML
    private TableColumn<Diario, String> pagamentoDiario;

    @FXML
    private TableColumn<Diario, String> aprovadorDiario;

    @FXML
    private TableColumn<Diario, String> totalDiario;

    @FXML
    private ChoiceBox<String> status;

    @FXML
    private TableView<Diario> diario;

    private SimpleDateFormat sdf;

    private ArrayList<Diario> diarios = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        status.setItems(FXCollections.observableArrayList("Abertas", "Postadas"));
        status.getSelectionModel().selectFirst();
        status.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    diarios = Cliente.INSTANCIA.listarDiarios(newValue);
                    atualizarTabela();
                });

        numeroDiario.setCellValueFactory((TableColumn.CellDataFeatures<Diario, String> c) -> new SimpleStringProperty("DC-" + new DecimalFormat("00000").format(c.getValue().getId())));
        pagamentoDiario.setCellValueFactory((TableColumn.CellDataFeatures<Diario, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getPagamento())));
        aprovadorDiario.setCellValueFactory((TableColumn.CellDataFeatures<Diario, String> c) -> new SimpleStringProperty(c.getValue().isAprovado() ? "Aprovado" : "Em aberto"));
        totalDiario.setCellValueFactory((TableColumn.CellDataFeatures<Diario, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getTotal())));

        diario.setRowFactory(s -> {
            TableRow<Diario> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                    visualizar(Cliente.INSTANCIA.buscarDiario(linha.getItem().getId()));
                }
            });
            return linha;
        });

        atualizar();
    }

    private void atualizar() {
        diarios.clear();
        diarios = Cliente.INSTANCIA.listarDiarios("Abertas");
        atualizarTabela();
    }

    private void atualizarTabela() {
        diario.setItems(FXCollections.observableArrayList(diarios));
    }

    @FXML
    void novo(ActionEvent event) {
        visualizar(Cliente.INSTANCIA.incluirDiario());
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void visualizar(Diario diario) {
        DiarioAlterarController controller = new DiarioAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(Modulos.DIARIO);
        controller.setDiario(diario);
        new CarregarGui().mostrar(pai, Modulos.DIARIOALTERAR, controller);

    }

    @FXML
    void postar() {
        Diario temp = diario.getSelectionModel().getSelectedItem();
        if (temp == null || temp.isAprovado()) {
            return;
        }

        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar();

        Task<Integer> task = new Task<Integer>() {

            @Override
            protected Integer call() throws Exception {
                return Cliente.INSTANCIA.postarDiario(temp.getId());
            }

            @Override
            protected void succeeded() {
                String descricao = "";
                try {
                    super.succeeded();
                    pai.setDisable(false);
                    switch (get()) {
                        case 1:
                            descricao = "Diario postado com sucesso";
                            break;
                        case 0:
                            descricao = "Ocorreu um erro, diario nao postado";
                            break;
                        case -1:
                            descricao = "Erro de comunicação";
                            break;
                        default:
                            descricao = "desconhecido";
                            break;
                    }
                    carregar.fechar();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(NotaAlterarController.class.getName()).log(Level.SEVERE, null, ex);

                    carregar.fechar();
                }

                Mensagem mensagem2 = new Mensagem("Postar Diario", descricao);
                mensagem2.setTitulo("Postar Diario");
                mensagem2.showDialog();

                atualizarTabela();
            }

            @Override
            protected void failed() {
                super.failed();
                pai.setDisable(false);
                carregar.fechar();

                Mensagem mensagem2 = new Mensagem("Erro", getException().getMessage());
                mensagem2.setTitulo("Postar Diario");
                mensagem2.showDialog();
            }

        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void succeeded() {
    }
}
