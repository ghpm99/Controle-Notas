/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Conexao.Cliente;
import GuiPrincipal.Fornecedor.EscolherFornecedorController;
import GuiPrincipal.Main;
import GuiPrincipal.Nota.NotaAlterarController;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Util.Mensagem;
import Util.CarregarGui;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import static Util.Conversao.stringToLong;
import Util.CustomTask;
import Util.HistoricoController;
import Util.Modulos;
import Util.PaneController;
import classes.contrato.Contrato;
import classes.contrato.item.ItemContrato;
import classes.contrato.mapa.MapaContrato;
import classes.fornecedor.Fornecedor;
import classes.nota.Nota;
import classes.relatorio.Relatorio;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class ContratoController implements Initializable, PaneController {

    /**
     * Initializes the controller class.
     */
    private BorderPane pai;
    @FXML
    private TextField numero;
    @FXML
    private TextField responsavel;
    @FXML
    private TextField total;
    @FXML
    private TextField fornecedor;
    @FXML
    private TextField gasto;
    @FXML
    private TextField saldo;
    @FXML
    private TextField descricao;
    @FXML
    private DatePicker emissao;
    @FXML
    private TableView<ItemContrato> itens;

    @FXML
    private TableColumn item;
    @FXML
    private TableColumn planoItem;
    @FXML
    private TableColumn descricaoItem;
    @FXML
    private TableColumn tipoItem;
    @FXML
    private TableColumn custoItem;
    @FXML
    private TableColumn numeroMapa;
    @FXML
    private TableColumn descricaoMapa;
    @FXML
    private TableColumn<ItemContrato, String> valorTotal;
    @FXML
    private TableColumn<ItemContrato, String> saldoItem;
    @FXML
    private TableColumn<ItemContrato, String> gastoItem;
    @FXML
    private Label status;

    private Contrato contrato;

    private Modulos modulo = Modulos.CONTRATOS;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO      

        this.item.setCellValueFactory(new PropertyValueFactory<>("item"));
        this.planoItem.setCellValueFactory(new PropertyValueFactory<>("plano"));
        this.descricaoItem.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.tipoItem.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        this.custoItem.setCellValueFactory(new PropertyValueFactory<>("custo"));
        this.numeroMapa.setCellValueFactory(new PropertyValueFactory<>("numeroMapa"));
        this.descricaoMapa.setCellValueFactory(new PropertyValueFactory<>("descricaoMapa"));
        this.valorTotal.setCellValueFactory((TableColumn.CellDataFeatures<ItemContrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal())));
        this.gastoItem.setCellValueFactory((TableColumn.CellDataFeatures<ItemContrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));
        this.saldoItem.setCellValueFactory((TableColumn.CellDataFeatures<ItemContrato, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getSaldo())));

        this.fornecedor.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    escolherFornecedor();
                }
            }
        });

        atualizar();

        editar();
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
     * @param contrato
     */
    public void setContrato(Contrato contrato) {
        this.contrato = Cliente.INSTANCIA.buscarcontratoCompleto(contrato.getId());
    }

    @FXML
    private void incluir() {
        atualizarContrato();
        //this.contrato = Cliente.incluirContrato(contrato);

        //this.contrato = (Contrato) new CarregarGui().mostrar(pai, new Salvar(contrato));
    }

    private void atualizarContrato() {
        this.contrato.setNumero(numero.getText());
        this.contrato.setEmissao(Date.from(Instant.from(emissao.getValue().atStartOfDay(ZoneId.systemDefault()))));
        //this.contrato.setResponsavel(responsavel.getText());
        this.contrato.setValorTotal(getTotal());
        this.contrato.setGastoTotal(getGasto());
        this.contrato.setSaldoTotal(getSaldo());
        this.contrato.setFornecedor(fornecedor.getText());
        this.contrato.setDescricao(descricao.getText());

    }

    private void atualizar() {
        //new Calcular(contrato);
        this.numero.setText(contrato.getNumero());
        //emissao.setValue(contrato.getEmissao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.emissao.setValue(Instant.ofEpochMilli(contrato.getEmissao().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        this.responsavel.setText(contrato.getAutor());
        this.total.setText(longToString(dinheiro, contrato.getValorTotal()));
        this.saldo.setText(longToString(dinheiro, contrato.getSaldoTotal()));
        this.gasto.setText(longToString(dinheiro, contrato.getGastoTotal()));
        this.fornecedor.setText(contrato.getFornecedor());
        this.descricao.setText(contrato.getDescricao());
        this.itens.setItems(FXCollections.observableArrayList(contrato.getItens()));

    }

    private void editar() {
        numero.setEditable(true);
        numero.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("CONTRATO", "NUMERO", contrato.getId(), numero.getText()), numero);
            }
        });
        emissao.setEditable(true);
        emissao.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Date novo = Date.from(Instant.from(newValue.atStartOfDay(ZoneId.systemDefault())));
            if (novo != null) {
                processarRetorno(Cliente.INSTANCIA.setDate("CONTRATO", "EMISSAO", contrato.getId(), novo), emissao);

            }
        });
        responsavel.setEditable(true);
        responsavel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("CONTRATO", "RESPONSAVEL", contrato.getId(), responsavel.getText()), responsavel);
            }
        });
        descricao.setEditable(true);
        descricao.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("CONTRATO", "DESCRICAO", contrato.getId(), descricao.getText()), descricao);
            }
        });
    }

    @FXML
    private void cancelar() {
        new CarregarGui().voltar(pai, modulo);
    }

    @FXML
    private void consultarMapa() {
        if (itens.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        consultarMapa(contrato.getMapa(itens.getSelectionModel().getSelectedItem()), false);

    }

    private void consultarMapa(MapaContrato mapa, boolean editavel) {

        MapaCotacaoAlterarController controller = new MapaCotacaoAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(modulo);
        controller.setMapa(mapa);
        controller.setEditavel(editavel);
        new CarregarGui().mostrar(pai, Modulos.MAPACOTACAOALTERAR, controller);

    }

    @FXML
    private void incluirMapa() {
        if (contrato.isCancelado() || !contrato.isAtivo()) {
            mensagem("Incluir mapa", "Não foi possivel incluir mapa, contrato cancelado/inativo", "Incluir mapa");
            return;
        }

        MapaContrato temp = Cliente.INSTANCIA.incluirMapacontrato(contrato.getId());

        contrato.adicionarMapa(temp);

        //incluir();
        atualizarContrato();
        consultarMapa(temp, true);
    }

    private long getTotal() {
        try {
            return stringToLong(dinheiro, total.getText());
        } catch (ParseException ex) {
            Logger.getLogger(ContratoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private long getGasto() {
        try {
            return stringToLong(dinheiro, gasto.getText());
        } catch (ParseException ex) {
            Logger.getLogger(ContratoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private long getSaldo() {
        try {
            return stringToLong(dinheiro, saldo.getText());
        } catch (ParseException ex) {
            Logger.getLogger(ContratoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @FXML
    private void incluirNota() {
        try {
            FXMLLoader loader = new FXMLLoader(IncluirNotaController.class.getResource("IncluirNota.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Incluir nota");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            IncluirNotaController controller = loader.getController();
            controller.setStage(stage);
            controller.setContrato(contrato.getId());

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(ContratoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void incluirPreNota() {
        if (contrato.isCancelado() || !contrato.isAtivo()) {
            mensagem("Incluir nota", "Não foi possivel incluir pre-nota, contrato cancelado/inativo", "Incluir nota");
            return;
        }
        CustomTask<Nota> task = new CustomTask<Nota>() {
            @Override
            protected Nota executar() throws Exception {
                return Cliente.INSTANCIA.incluirNota(contrato.getIdFornecedor(), contrato.getId());
            }
        };

        nota((Nota) new CarregarGui().mostrarTask(pai, task));
    }

    private void nota(Nota nota) {
        NotaAlterarController controller = new NotaAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(modulo);
        controller.setNota(Cliente.INSTANCIA.buscarNota(nota.getId()));
        new CarregarGui().mostrar(pai, Modulos.NOTAALTERAR, controller);
    }

    /*
     Precisa melhorar
     */
    @FXML
    private void cancelarContrato() {
        Cliente.INSTANCIA.inativarContrato(contrato.getId());
    }

    private void mensagem(String mensagem, String descricao, String titulo) {
        Mensagem mensagem2 = new Mensagem(mensagem, descricao);
        mensagem2.setTitulo(titulo);
        mensagem2.showDialog();
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    @FXML
    private void imprimir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", ".pdf"));
        new Relatorio().imprimirContrato(fileChooser.showSaveDialog(null), contrato);
        // new Relatorio().teste(fileChooser.showSaveDialog(null));
    }

    private void escolherFornecedor() {
        try {
            FXMLLoader loader = new FXMLLoader(EscolherFornecedorController.class.getResource(Modulos.ESCOLHERFORNECEDOR.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Escolher Fornecedor");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            EscolherFornecedorController controller = loader.getController();
            controller.setStage(stage);
            controller.setCusto(contrato);

            stage.showAndWait();

            if (controller.isOk()) {
                atualizarFornecedor();
            }

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizarFornecedor() {
        Fornecedor temp = Cliente.INSTANCIA.buscarFornecedorCompleto(contrato.getIdFornecedor());
        fornecedor.setText(temp.getRazaoSocial() + " " + temp.getCnpj());
        Cliente.INSTANCIA.setInt("CONTRATO", "FORNECEDOR", contrato.getId(), contrato.getIdFornecedor());
    }

    @FXML
    private void historico() {
        try {
            FXMLLoader loader = new FXMLLoader(HistoricoController.class.getResource(Modulos.HISTORICO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historico Contrato");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            HistoricoController controller = loader.getController();
            controller.setStage(stage);
            controller.setHistorico(Cliente.INSTANCIA.buscarHistorico("CONTRATO", contrato.getId()));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void succeeded() {
    }

    private void processarRetorno(int retorno, Node campo) {
        switch (retorno) {
            case -2:
                marcarErro(campo);
                break;
            case -1:
                marcarErro(campo);
                break;
            case 0:
                marcarErro(campo);
                break;
            case 1:
                marcarSucesso(campo);
                break;
            default:
                marcarAtencao(campo);
                break;
        }
    }

    private void marcarErro(Node node) {
        desmarcarSucesso(node);
        desmarcarAtencao(node);
        if (!node.getStyleClass().contains("erro")) {
            node.getStyleClass().add("erro");
        }
    }

    private void desmarcarErro(Node node) {
        if (node.getStyleClass().contains("erro")) {
            node.getStyleClass().remove("erro");
        }
    }

    private void marcarSucesso(Node node) {
        desmarcarErro(node);
        desmarcarAtencao(node);
        if (!node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().add("sucesso");
        }
    }

    private void desmarcarSucesso(Node node) {
        if (node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().remove("sucesso");
        }
    }

    private void marcarAtencao(Node node) {
        desmarcarSucesso(node);
        desmarcarErro(node);
        if (!node.getStyleClass().contains("atencao")) {
            node.getStyleClass().add("atencao");
        }
    }

    private void desmarcarAtencao(Node node) {
        if (node.getStyleClass().contains("atencao")) {
            node.getStyleClass().remove("atencao");
        }
    }
}
