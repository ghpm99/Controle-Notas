/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Diario;
import classes.financeiro.NotaFinanceiro;
import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Util.Mensagem;
import classes.relatorio.NotaAnexo;
import classes.relatorio.Relatorio;
import Util.Calcular;
import Util.CarregarGui;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Modulos;
import Util.PaneController;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class DiarioAlterarController implements Initializable, PaneController {

    @FXML
    private TextField numero, total;
    @FXML
    private DatePicker pagamento;
    @FXML
    private TableView notasAbertoTabela;
    @FXML
    private Label rodape;
    @FXML
    private TableColumn<NotaFinanceiro, String> numeroNota, fornecedorNota, cnpjNota, fluxoNota, vencimentoNotaNota, vencimentoNota, vencimentoRealNota, valorNota, descontoNota, jurosNota,
            valorLiquidoNota, tipoNota, linhaNota, codigoNota, classificadaNota;

    private SimpleDateFormat sdf;

    private Diario diario;

    private Modulos modulo;

    private BorderPane pai;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        numeroNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getNumero()));
        fornecedorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getFornecedor()));
        cnpjNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCnpj()));
        fluxoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getFluxo())));
        vencimentoNotaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoNota())));
        vencimentoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimento())));
        vencimentoRealNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal())));
        valorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));
        descontoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getDesconto())));
        jurosNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getJuros())));
        valorLiquidoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValorLiquido())));
        tipoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getTipo()));
        linhaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));
        codigoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));
        classificadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isClassificada() ? "Sim" : "Não"));

        if (!diario.isAprovado()) {
            editar();
        }

        total.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Cliente.INSTANCIA.setLong("DIARIONOTAS", "TOTAL", diario.getId(), editarTexto(total.getText()));
        });

        pagamento.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Cliente.INSTANCIA.setDate("DIARIONOTAS", "PAGAMENTO", diario.getId(), Date.from(Instant.from(pagamento.getValue().atStartOfDay(ZoneId.systemDefault()))));
        });

        diario.setNotas(Cliente.INSTANCIA.buscarNotasDiario(diario.getId()));

        atualizar();
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    @FXML
    void buscarNotas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(DiarioBuscarNotasController.class.getResource(Modulos.DIARIOBUSCARNOTAS.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Escolher Notas");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            DiarioBuscarNotasController controller = loader.getController();
            controller.setStage(stage);
            controller.setDiario(diario);

            stage.showAndWait();

            atualizarTabela();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void excluirNota(ActionEvent event) {
        NotaFinanceiro notaTemp = (NotaFinanceiro) notasAbertoTabela.getSelectionModel().getSelectedItem();
        if (notaTemp == null) {
            return;
        }
        Mensagem mensagem = new Mensagem("Exclui nota", "Deseja excluir a nota do diario:\n" + notaTemp.getNumero() + "-" + notaTemp.getFornecedor());
        mensagem.setTitulo("Exclui");
        if (mensagem.showDialog()) {
            Cliente.INSTANCIA.excluirNotaEmDiario(notaTemp.getId(), diario.getId());
            diario.getNotas().remove(notaTemp);
            atualizar();
        }
    }

    @FXML
    void limparDiario(ActionEvent event) {
        Mensagem mensagem = new Mensagem("Limpar Diario", "Tem certeza que deseja excluir todas as notas do diario?\nEssa ação não poderá ser desfeita.");
        mensagem.setTitulo("Limpar Diario");
        if (mensagem.showDialog()) {
            Iterator ite = diario.getNotas().iterator();
            while (ite.hasNext()) {
                NotaFinanceiro temp = (NotaFinanceiro) ite.next();
                Cliente.INSTANCIA.excluirNotaEmDiario(temp.getId(), diario.getId());
                ite.remove();
            }
            atualizar();
        }

    }

    @FXML
    void imprimir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", ".pdf"));
        new Relatorio().imprimirDiario(fileChooser.showSaveDialog(null), diario);
    }

    @FXML
    void incluirNota() {
        try {
            FXMLLoader loader = new FXMLLoader(IncluirPagamentoController.class.getResource(Modulos.INCLUIRPAGAMENTO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Incluir Pagamento");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            IncluirPagamentoController controller = loader.getController();
            controller.setStage(stage);
            controller.setDiario(diario);

            stage.showAndWait();

            atualizarTabela();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void voltar(ActionEvent event) {
        new CarregarGui().voltar(pai, modulo);
    }

    private void atualizar() {
        numero.setText("DC-" + new DecimalFormat("00000").format(diario.getId()));
        pagamento.setValue(Instant.ofEpochMilli(diario.getPagamento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        atualizarTabela();
        atualizarRodape();
    }

    private void atualizarRodape() {
        rodape.setText("Diário " + numero.getText() + " - Total: " + total.getText() + " - Total de notas: " + diario.getNotas().size());
    }

    private void atualizarTabela() {
        Collections.sort(diario.getNotas(), (NotaFinanceiro o1, NotaFinanceiro o2) -> Long.compare(o1.getVencimento().getTime(), o2.getVencimento().getTime()));
        Collections.sort(diario.getNotas(), (NotaFinanceiro o1, NotaFinanceiro o2) -> o1.getNumero().compareTo(o2.getNumero()));
        Collections.sort(diario.getNotas(), (NotaFinanceiro o1, NotaFinanceiro o2) -> o1.getFornecedor().compareTo(o2.getFornecedor()));

        Calcular.calcularDiario(diario);
        total.setText(Conversao.longToString(dinheiro, diario.getTotal()));
        notasAbertoTabela.getItems().clear();
        notasAbertoTabela.setItems(FXCollections.observableArrayList(diario.getNotas()));
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    private void editar() {
        descontoNota.setCellFactory(TextFieldTableCell.forTableColumn());
        descontoNota.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<NotaFinanceiro, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<NotaFinanceiro, String> event) {
                event.getRowValue().setDesconto(editarTexto(event.getNewValue()));
                Cliente.INSTANCIA.atualizarDescontoNotaFinanceiro(event.getRowValue().getId(), event.getRowValue().getDesconto());
                atualizarTabela();
            }
        });
        jurosNota.setCellFactory(TextFieldTableCell.forTableColumn());
        jurosNota.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<NotaFinanceiro, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<NotaFinanceiro, String> event) {
                event.getRowValue().setJuros(editarTexto(event.getNewValue()));
                Cliente.INSTANCIA.atualizarJurosNotaFinanceiro(event.getRowValue().getId(), event.getRowValue().getJuros());
                atualizarTabela();
            }
        });
        tipoNota.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList("Deposito", "Boleto")));
        tipoNota.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<NotaFinanceiro, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<NotaFinanceiro, String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setTipo(t.getNewValue());
                t.getRowValue().setTipo(t.getNewValue());
                Cliente.INSTANCIA.atualizarTipoNotaFinanceiro(t.getRowValue().getId(), t.getRowValue().getTipo());
                atualizarTabela();
            }
        ;
        });
        
        linhaNota.setCellFactory(TextFieldTableCell.forTableColumn());
        linhaNota.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<NotaFinanceiro, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<NotaFinanceiro, String> event) {
                event.getRowValue().setLinha(event.getNewValue());
                Cliente.INSTANCIA.atualizarLinhaNotaFinanceiro(event.getRowValue().getId(), event.getRowValue().getLinha());
                atualizarTabela();
            }
        });

        codigoNota.setCellFactory(TextFieldTableCell.forTableColumn());
        codigoNota.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<NotaFinanceiro, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<NotaFinanceiro, String> event) {
                event.getRowValue().setCodigo(event.getNewValue());
                Cliente.INSTANCIA.atualizarCodigoNotaFinanceiro(event.getRowValue().getId(), event.getRowValue().getCodigo());
                atualizarTabela();
            }
        });
    }

    private Long editarTexto(String newValue) {
        return (newValue.isEmpty() ? 0 : Long.valueOf(newValue.replaceAll("[^0-9-]", "")));
    }

    @Override
    public void succeeded() {
    }

    @FXML
    void exportarNotas() {
        ArrayList<String> tiposAnexo = new ArrayList<>();
        ArrayList<NotaAnexo> notas = new ArrayList<>();
        diario.getNotas().forEach(s -> {
            NotaAnexo nota = new NotaAnexo();
            nota.setId(s.getIdNota());
            nota.setNumero(s.getNumero());
            notas.add(nota);
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExportarAnexoDiario.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Exportar Notas");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            ExportarAnexoDiarioController controller = loader.getController();
            controller.setStage(stage);
            controller.setArrayTipoAnexo(tiposAnexo);

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", ".pdf"));

        new Relatorio().exportarNotas(notas, pai, fileChooser.showSaveDialog(null), null, tiposAnexo);
    }

    @FXML
    void exportarDiario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));
        new Relatorio().exportarDiario(fileChooser.showSaveDialog(null), diario);
    }
}
