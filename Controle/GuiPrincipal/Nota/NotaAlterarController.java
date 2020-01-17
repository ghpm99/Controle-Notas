/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import Conexao.Cliente;
import GuiPrincipal.Fornecedor.EscolherFornecedorController;
import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Util.CarregarController;
import GuiPrincipal.Util.Mensagem;
import Util.Calcular;
import Util.CarregarGui;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import static Util.Conversao.quantidade;
import Util.CustomTask;
import Util.HistoricoController;
import Util.Modulos;
import Util.PaneController;
import classes.contrato.Contrato;
import classes.fornecedor.Fornecedor;
import classes.nota.Nota;
import classes.nota.anexo.AnexoNota;
import classes.nota.cobranca.Pagamento;
import classes.nota.item.ItemNota;
import classes.relatorio.Relatorio;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Guilherme
 */
public class NotaAlterarController implements Initializable, PaneController {

    /**
     * Initializes the controller class.
     */
    private BorderPane pai;
    @FXML
    private TextField numero, serie, prazo, fornecedor, saldoContrato, faturaAtual, saldoFaturar, faturamentoDireto, issqn, csrf, inss, irrf, valorImpostos, valorLiquido;
    @FXML
    private ChoiceBox<String> tipo;
    @FXML
    private DatePicker vencimento, vencimentoReal, emissao;

    @FXML
    private TableView<ItemNota> valores;
    @FXML
    private TableColumn<ItemNota, String> itemValores, planoValores,  precoUnidValores, qntValores, totalValores, unidValores;

    @FXML
    private Button ratearButton, novoButton, cancelarButton, estornarButton;

    private Nota nota;
    @FXML
    private Label status, rodape, statusNota;
    @FXML
    private TableView anexos;
    @FXML
    private TableColumn nomeAnexos, tipoAnexos, arquivoAnexos, tipoPagamentos;
    @FXML
    private TableView<Pagamento> pagamentos;
    @FXML
    private TableColumn<Pagamento, String> dataVencimentoPagamentos, dataPagamentoPagamentos, valorPagamentos, linhaDigitavelPagamentos, codigoBarrasPagamentos;

    private Modulos modulo = Modulos.NOTA;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    ArrayList<String> tipos = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //"NF", "NFE", "NFS", "NFSE", "REC", "CT", "CTE", "PA", "SE"
        tipos.add("NF");
        tipos.add("NFE");
        tipos.add("NFS");
        tipos.add("NFSE");
        tipos.add("REC");
        tipos.add("CT");
        tipos.add("CTE");
        tipos.add("PA");
        tipos.add("SE");

        tipo.setItems(FXCollections.observableArrayList(tipos));

        itemValores.setCellValueFactory(new PropertyValueFactory<>("item"));
        

        planoValores.setCellValueFactory(new PropertyValueFactory<>("plano"));


        unidValores.setCellValueFactory(new PropertyValueFactory<>("unidade"));


        qntValores.setCellValueFactory((TableColumn.CellDataFeatures<ItemNota, String> c) -> new SimpleStringProperty(longToString(quantidade, c.getValue().getQntTotal())));
        
        precoUnidValores.setCellValueFactory((TableColumn.CellDataFeatures<ItemNota, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoUnitario())));
       
        totalValores.setCellValueFactory((TableColumn.CellDataFeatures<ItemNota, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal())));
       
        nomeAnexos.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tipoAnexos.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        arquivoAnexos.setCellValueFactory(new PropertyValueFactory<>("caminho"));

        dataVencimentoPagamentos.setCellValueFactory((TableColumn.CellDataFeatures<Pagamento, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimento())));

        dataPagamentoPagamentos.setCellValueFactory((TableColumn.CellDataFeatures<Pagamento, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal())));

        valorPagamentos.setCellValueFactory((TableColumn.CellDataFeatures<Pagamento, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));

        tipoPagamentos.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        linhaDigitavelPagamentos.setCellValueFactory((TableColumn.CellDataFeatures<Pagamento, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));

        codigoBarrasPagamentos.setCellValueFactory((TableColumn.CellDataFeatures<Pagamento, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));

        fornecedor.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    escolherFornecedor();
                }
            }
        });
        anexos.setRowFactory(s -> {
            TableRow<AnexoNota> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                    visualizar(linha.getItem());
                }
            });
            return linha;
        });

        atualizarCampos();

        ratearButton.setDisable(!nota.isPreNota());
        novoButton.setDisable(!nota.isPreNota());
        cancelarButton.setDisable(!nota.isPreNota());

        tipo.setDisable(!nota.isPreNota());
        estornarButton.setDisable(nota.isLancada());

        if (this.nota.isPreNota() && !this.nota.isCancelado()) {
            editar();
        } else if (Cliente.INSTANCIA.getNivelConta().isGerencia()) {
            tipo.setDisable(false);
            editarCabecalho();
        }

    }

    private Long editarTexto(String newValue) {
        return (newValue.isEmpty() ? 0 : Long.valueOf(newValue.replaceAll("[^0-9-]", "")));
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @FXML
    public void voltar() {
        voltar(modulo);
    }

    private void voltar(Modulos modulo) {
        new CarregarGui().voltar(pai, modulo);
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    private void atualizarCampos() {
        numero.setText(nota.getNumero());
        serie.setText(nota.getSerie());
        if (tipos.contains(nota.getTipo())) {
            tipo.getSelectionModel().select(nota.getTipo());
        } else {
            tipo.getSelectionModel().select("SE");
        }
        fornecedor.setText(nota.getFornecedor());
        emissao.setValue(Instant.ofEpochMilli(nota.getEmissao().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        vencimento.setValue(Instant.ofEpochMilli(nota.getVencimento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        vencimentoReal.setValue(Instant.ofEpochMilli(nota.getVencimentoReal().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        faturamentoDireto.setText(nota.getFaturamentoDireto());
        statusNota.setText(nota.isPreNota() ? "Pre-Nota" : "");
        rodape.setText("Criado " + sdf.format(nota.getInclusao()) + " por " + nota.getAutor() + ". Atualizado em" + sdf.format(nota.getAtualizacao()) + ". Status: " + nota.getStatus());
        atualizarCamposValor();
        atualizarTabelaItens();
        atualizarTabelaAnexo();
    }

    private void atualizarCamposValor() {
        saldoContrato.setText(Conversao.longToString(dinheiro, nota.getValorTotal()));
        faturaAtual.setText(Conversao.longToString(dinheiro, nota.getGastoTotal()));
        saldoFaturar.setText(Conversao.longToString(dinheiro, nota.getSaldoTotal()));
        issqn.setText(Conversao.longToString(dinheiro, nota.getIssqn()));
        irrf.setText(Conversao.longToString(dinheiro, nota.getIrrf()));
        inss.setText(Conversao.longToString(dinheiro, nota.getInss()));
        csrf.setText(Conversao.longToString(dinheiro, nota.getCsrf()));
        valorImpostos.setText(Conversao.longToString(dinheiro, nota.getValorImpostos()));
        valorLiquido.setText(Conversao.longToString(dinheiro, nota.getValorLiquido()));
    }

    private void atualizarTabelaItens() {
        calcular();
        valores.getItems().clear();
        valores.setItems(FXCollections.observableArrayList(nota.getItens()));

        pagamentos.getItems().clear();
        pagamentos.setItems(FXCollections.observableArrayList(nota.getPagamentos()));

        requestFocus();
    }

//    @FXML
//    private void salvar() {
//
//        CarregarGui carregar = new CarregarGui();
//        pai.setDisable(true);
//        carregar.mostrar();
//
//        Task<Integer> task = new Task<Integer>() {
//
//            @Override
//            protected Integer call() throws Exception {
//                return Cliente.INSTANCIA.atualizarNota(nota);
//            }
//
//            @Override
//            protected void succeeded() {
//                try {
//                    super.succeeded();
//                    pai.setDisable(false);
//                    switch (get()) {
//                        case 1:
//                            status.setText("Nota Atualizada com sucesso");
//
//                            break;
//                        case 0:
//                            status.setText("Ocorreu um erro, nota nao atualizada");
//                            break;
//                        case -1:
//                            status.setText("Erro de comunicação");
//                            break;
//                        case -2:
//                            status.setText("Falhou");
//                            break;
//                        case -3:
//                            status.setText("Já existe nota com o mesmo numero");
//                            marcarErro(numero);
//                            break;
//                        case -4:
//                            status.setText("Vencimento/Vencimento Real antes da emissao");
//                            marcarErro(vencimento);
//                            break;
//                        case -5:
//                            status.setText("Não tem permissão");
//                            break;
//                        default:
//                            status.setText("desconhecido");
//                            break;
//                    }
//                    carregar.fechar();
//                } catch (InterruptedException | ExecutionException ex) {
//                    Logger.getLogger(NotaAlterarController.class.getName()).log(Level.SEVERE, null, ex);
//
//                    carregar.fechar();
//                }
//            }
//
//            @Override
//            protected void failed() {
//                super.failed();
//                pai.setDisable(false);
//                carregar.fechar();
//
//                getException().printStackTrace();
//            }
//
//        };
//
//        Thread thread = new Thread(task);
//        thread.setDaemon(true);
//        thread.start();
//        //System.out.println(task.get().getAutor());
//
//    }
    @FXML
    private void estornar() {
        if (!nota.isPreNota()) {
            Mensagem mensagem = new Mensagem("Estornar nota", "Deseja estornar nota?");
            mensagem.setTitulo("Estornar");
            if (mensagem.showDialog()) {
                if (nota.isLancada()) {
                    mensagem("Estornar nota", "Nota nao possivel estornar", "Estornar");
                } else {
                    CarregarGui carregar = new CarregarGui();

                    carregar.mostrar(new CarregarController() {
                        private String status;

                        @Override
                        public void run() {

                            barraCarregar2.setProgress(0.5);

                            switch (Cliente.INSTANCIA.estornarNota(nota.getId())) {
                                case 1:
                                    status = "Nota Estornada com sucesso";
                                    break;
                                case 0:
                                    status = "Erro de comunicação";
                                    break;
                                case -1:
                                    status = "Nota sem contrado";
                                    break;
                                case -2:
                                    status = "Falhou";
                                    break;
                                case -3:
                                    status = "Nota já é pre-nota";
                                    marcarErro(numero);
                                    break;
                                default:
                                    status = "desconhecido";
                                    break;
                            }

                            barraCarregar2.setProgress(0.9);
                        }

                        @Override
                        public void sucesso() {
                            mensagem("Estornar nota", status, "Estornar");
                            voltar();
                        }

                        @Override
                        public void falha() {

                        }
                    });
                }
            }
        } else {
            Mensagem mensagem = new Mensagem("Estornar nota", "Nota nao foi lançada");
            mensagem.setTitulo("Estornar");
            mensagem.showDialog();
        }
    }

    @FXML
    private void lancar() {

        if (nota.isCancelado()) {
            mensagem("Lançar nota", "Não foi possivel lançar, nota cancelada", "Lançar");
            return;
        }
        if (nota.getNumero().isEmpty()) {
            mensagem("Lançar nota", "Não foi possivel lançar, sem numero", "Lançar");
            return;
        }
        if (nota.getGastoTotal() == 0) {
            mensagem("Lançar nota", "Não foi possivel lançar, sem valor", "Lançar");
            return;
        }
        if (nota.getIdPmWeb() == 0) {
            mensagem("Lançar nota", "Não foi possivel lançar, sem id PMWeb", "Lançar");
            return;
        }
        if (nota.isPreNota()) {
            Mensagem mensagem = new Mensagem("Lançar nota", "Lançar pre-nota?");
            mensagem.setTitulo("Lançar");
            if (mensagem.showDialog()) {
                Mensagem mensagem2 = new Mensagem("Pre-Nota", "Tem certeza que deseja classificar a Pre-nota?");
                mensagem2.setTitulo("Classificar");
                if (mensagem2.showDialog()) {
                    CarregarGui carregar = new CarregarGui();

                    carregar.mostrar(new CarregarController() {
                        private String status;

                        @Override
                        public void run() {

                            barraCarregar2.setProgress(0.5);

                            switch (Cliente.INSTANCIA.lancarNota(nota.getId())) {
                                case 1:
                                    status = "Nota Lançada com sucesso";
                                    break;
                                case 0:
                                    status = "Erro de comunicação";
                                    break;
                                case -1:
                                    status = "Nota sem contrado";
                                    break;
                                case -2:
                                    status = "Nota sem ID Aprovação";
                                    break;
                                case -3:
                                    status = "Nota não é pre-nota";
                                    marcarErro(numero);
                                    break;
                                case -4:
                                    status = "Não foi possivel lançar, sem valor";
                                    marcarErro(faturaAtual);
                                    break;
                                default:
                                    status = "desconhecido";
                                    break;
                            }
                            barraCarregar2.setProgress(0.9);
                        }

                        @Override
                        public void sucesso() {
                            mensagem("Lançar nota", status, "Lançar");
                            voltar();
                        }

                        @Override
                        public void falha() {

                        }
                    });
                    return;
                }
            } else {
                return;
            }
        } else {
            mensagem("Lançar nota", "Nota já foi lançada.", "Lançar");
        }

    }

    @FXML
    private void dadosAdicionais() {
        try {
            FXMLLoader loader = new FXMLLoader(NotaAlterarController.class.getResource("DadosAdicionaisNota.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();
            DadosAdicionaisNotaController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Dados adicionais");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            controller.setObservacao(nota.getObservacao());
            controller.setDescricao(nota.getDescricao());
            controller.setIdPmWeb(nota.getIdPmWeb());
            controller.editavel(this.nota.isPreNota());
            Scene scene = new Scene(anchor);
            stage.setScene(scene);
            stage.showAndWait();
            nota.setObservacao(controller.getObservacao());
            nota.setDescricao(controller.getDescricao());
            nota.setIdPmWeb(controller.getIdPmWeb());
            if (this.nota.isPreNota() && !this.nota.isCancelado()) {
                Cliente.INSTANCIA.setString("NOTA", "DESCRICAO", nota.getId(), controller.getDescricao());
                Cliente.INSTANCIA.setString("NOTA", "OBSERVACAO", nota.getId(), controller.getObservacao());
                Cliente.INSTANCIA.setInt("NOTA", "IDPMWEB", nota.getId(), controller.getIdPmWeb());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void imprimir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", ".pdf"));
        new Relatorio().imprimirNota(fileChooser.showSaveDialog(null), nota);

    }

    @FXML
    private void cancelar() {
        if (nota.isCancelado()) {
            Mensagem mensagem = new Mensagem("Ativar nota", "Nota ja se encontra cancelada, deseja ativá-la?");
            mensagem.setTitulo("Cancelar");
            if (mensagem.showDialog()) {
                if (Cliente.INSTANCIA.cancelarNota(nota.getId())) {
                    mensagem("Ativar nota", "Nota ativada com sucesso", "Cancelar");
                    voltar();
                } else {
                    status.setText("Falhou");
                }

            }
        } else {
            Mensagem mensagem = new Mensagem("Cancelar nota", "Deseja cancelar nota?");
            mensagem.setTitulo("Cancelar");
            if (mensagem.showDialog()) {
                if (nota.isLancada()) {
                    mensagem("Cancelar nota", "Não foi possivel cancelar, nota lançada", "Cancelar");
                } else {
                    if (Cliente.INSTANCIA.cancelarNota(nota.getId())) {
                        mensagem("Cancelar nota", "Nota cancelada com sucesso", "Cancelar");
                        voltar();
                    } else {
                        status.setText("Falhou");
                    }
                }
            }
        }
    }

    private void requestFocus() {
        valores.requestFocus();
        valores.getSelectionModel().select(0);
        valores.getFocusModel().focus(0);
    }

    private void calcular() {
        Calcular.calcularNota(nota);
        atualizarCamposValor();
    }

    private void editarCabecalho() {
        numero.setEditable(true);
        numero.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                verificarNumero();
            }
        });
        serie.setEditable(true);
        serie.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("NOTA", "SERIE", nota.getId(), serie.getText()), "SERIE");
                marcarSucesso(serie);
            }
        });

        tipo.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    nota.setTipo(newValue);
                    Cliente.INSTANCIA.setString("NOTA", "TIPO", nota.getId(), newValue);
                    marcarSucesso(tipo);
                });

        emissao.setEditable(true);
        emissao.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Date novo = Date.from(Instant.from(newValue.atStartOfDay(ZoneId.systemDefault())));
            if (novo != null) {
                if (novo.after(new Date())) {
                    emissao.setValue(oldValue);
                    marcarErro(emissao);
                } else {
                    if (vencimento.getValue() != null) {
                        if (novo.after(Date.from(Instant.from(vencimento.getValue().atStartOfDay(ZoneId.systemDefault()))))) {
                            vencimento.setValue(newValue);
                        }
                    }
                    if (vencimentoReal.getValue() != null) {
                        if (novo.after(Date.from(Instant.from(vencimentoReal.getValue().atStartOfDay(ZoneId.systemDefault()))))) {
                            vencimentoReal.setValue(newValue);
                        }
                    }
                    processarRetorno(Cliente.INSTANCIA.setDate("NOTA", "EMISSAO", nota.getId(), novo), "EMISSAO");
                    marcarSucesso(emissao);
                }
            }
        });

        vencimento.setEditable(true);
        vencimento.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Date novo = Date.from(Instant.from(newValue.atStartOfDay(ZoneId.systemDefault())));
            if (novo != null) {
                if (novo.before(Date.from(Instant.from(emissao.getValue().atStartOfDay(ZoneId.systemDefault()))))) {
                    vencimento.setValue(oldValue);
                    marcarErro(vencimento);
                } else {
                    processarRetorno(Cliente.INSTANCIA.setDate("NOTA", "VENCIMENTO", nota.getId(), novo), "VENCIMENTO");
                    marcarSucesso(vencimento);
                }
            }
        });

        vencimentoReal.setEditable(true);
        vencimentoReal.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Date novo = Date.from(Instant.from(newValue.atStartOfDay(ZoneId.systemDefault())));
            if (novo != null) {

                if (novo.before(Date.from(Instant.from(emissao.getValue().atStartOfDay(ZoneId.systemDefault()))))) {
                    vencimentoReal.setValue(oldValue);
                    marcarErro(vencimentoReal);
                } else {
                    processarRetorno(Cliente.INSTANCIA.setDate("NOTA", "VENCIMENTOREAL", nota.getId(), novo), "VENCIMENTOREAL");
                    marcarSucesso(vencimentoReal);
                }
            }
        });

        prazo.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String temp = prazo.getText().replaceAll("[^0-9]", "");
                if (!temp.isEmpty()) {
                    Calendar calendario = Calendar.getInstance();
                    calendario.setTime(Date.from(Instant.from(emissao.getValue().atStartOfDay(ZoneId.systemDefault()))));
                    calendario.add(Calendar.DATE, Integer.valueOf(temp));
                    vencimento.setValue(Instant.ofEpochMilli(calendario.getTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                    vencimentoReal.setValue(vencimento.getValue());
                }
            }
        });
    }

    private void editar() {
        editarCabecalho();

        valorLiquido.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Cliente.INSTANCIA.setLong("NOTA", "VALORLIQUIDO", nota.getId(), nota.getValorLiquido());
        });
        faturaAtual.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Cliente.INSTANCIA.setLong("NOTA", "GASTOTOTAL", nota.getId(), nota.getGastoTotal());
        });
        saldoContrato.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Cliente.INSTANCIA.setLong("NOTA", "VALORTOTAL", nota.getId(), nota.getValorTotal());
        });
        saldoFaturar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Cliente.INSTANCIA.setLong("NOTA", "SALDOTOTAL", nota.getId(), nota.getSaldoTotal());
        });

        totalValores.setCellFactory(TextFieldTableCell.forTableColumn());
        totalValores.setOnEditCommit((TableColumn.CellEditEvent<ItemNota, String> event) -> {
            event.getRowValue().setPrecoTotal(editarTexto(event.getNewValue()));
            processarRetorno(Cliente.INSTANCIA.setLong("ITEMNOTA", "PRECOTOTAL", event.getRowValue().getId(), editarTexto(event.getNewValue())), "PRECOTOTAL");
            atualizarTabelaItens();
        });

        precoUnidValores.setCellFactory(TextFieldTableCell.forTableColumn());
        precoUnidValores.setOnEditCommit((TableColumn.CellEditEvent<ItemNota, String> event) -> {
            event.getRowValue().setPrecoUnitario(editarTexto(event.getNewValue()));
            //event.getRowValue().setPrecoTotal((event.getRowValue().getPrecoUnitario() * event.getRowValue().getQntTotal()) / 100);
            Cliente.INSTANCIA.setLong("ITEMNOTA", "PRECOUNITARIO", event.getRowValue().getId(), editarTexto(event.getNewValue()));
            atualizarTabelaItens();
        });

        qntValores.setCellFactory(TextFieldTableCell.forTableColumn());
        qntValores.setOnEditCommit((TableColumn.CellEditEvent<ItemNota, String> event) -> {
            event.getRowValue().setQntTotal(editarTexto(event.getNewValue()));
            //event.getRowValue().setPrecoTotal((event.getRowValue().getPrecoUnitario() * event.getRowValue().getQntTotal()) / 100);
            processarRetorno(Cliente.INSTANCIA.setLong("ITEMNOTA", "QNTTOTAL", event.getRowValue().getId(), editarTexto(event.getNewValue())), "QNTTOTAL");
            atualizarTabelaItens();
        });     

       
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
    private void ratear() {
        try {
            FXMLLoader loader = new FXMLLoader(RateioController.class.getResource("Rateio.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();
            RateioController controller = loader.getController();

            Stage stage = new Stage();

            stage.setTitle("Ratear");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            controller.setStage(stage);
            controller.setNota(nota);
            Scene scene = new Scene(anchor);
            stage.setScene(scene);
            stage.showAndWait();
            atualizarTabelaItens();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {

        }
    }

    private void verificarNumero() {
        if (numero.getText().isEmpty() || Cliente.INSTANCIA.verificarNumeroNota(nota.getIdFornecedor(), nota.getId(), numero.getText())) {
            numero.setText("000000000");
            nota.setNumero("000000000");
            marcarErro(numero);
        } else {
            nota.setNumero(numero.getText());
            marcarSucesso(numero);
        }
        processarRetorno(Cliente.INSTANCIA.setString("NOTA", "NUMERO", nota.getId(), numero.getText()), "NUMERO");
    }

    private boolean marcarErro(Node node) {
        desmarcarSucesso(node);
        if (!node.getStyleClass().contains("erro")) {
            node.getStyleClass().add("erro");
        }
        return true;
    }

    private boolean desmarcarErro(Node node) {
        if (node.getStyleClass().contains("erro")) {
            node.getStyleClass().remove("erro");
        }
        return false;
    }

    private boolean marcarSucesso(Node node) {
        desmarcarErro(node);
        if (!node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().add("sucesso");
        }
        return true;
    }

    private boolean desmarcarSucesso(Node node) {
        if (node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().remove("sucesso");
        }
        return false;
    }

    @FXML
    private void novo() {
        if (nota.getIdContrato() == 0) {
            return;
        }
        removerMarcacao();
        CustomTask<Contrato> task = new CustomTask<Contrato>() {
            @Override
            protected Contrato executar() throws Exception {
                Contrato contrato = Cliente.INSTANCIA.buscarcontratoCompleto(nota.getIdContrato());
                return contrato;
            }
        };
        Contrato con = new CarregarGui().mostrarTask(pai, task);
        Mensagem mensagem = new Mensagem("Incluir Pre-Nota.", "Deseja incluir Pre-Nota no contrato:\n" + con.getNumero() + "-" + con.getDescricao());
        mensagem.setTitulo("Incluir");
        if (mensagem.showDialog()) {
            CustomTask<Nota> task2 = new CustomTask<Nota>() {

                @Override
                protected Nota executar() throws Exception {
                    Nota temp = Cliente.INSTANCIA.incluirNota(nota.getIdFornecedor(), nota.getIdContrato());
                    return temp;
                }
            };

            setNota(new CarregarGui().mostrarTask(pai, task2));
            atualizarCampos();
            status.setText("Nota inclusa com sucesso");
        }

    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    private void escolherFornecedor() {
        if (!nota.isPreNota()) {
            return;
        }
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
            controller.setCusto(nota);

            stage.showAndWait();

            if (controller.isOk()) {
                atualizarFornecedor();
            }

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizarFornecedor() {
        Fornecedor temp = Cliente.INSTANCIA.buscarFornecedorCompleto(nota.getIdFornecedor());
        processarRetorno(Cliente.INSTANCIA.setFloat("NOTA", "IDFORNECEDOR", nota.getId(), nota.getIdFornecedor()), "Fornecedor");
        fornecedor.setText(temp.getRazaoSocial() + " " + temp.getCnpj());
        marcarSucesso(fornecedor);
        verificarNumero();
    }

    @FXML
    private void historico() {
        try {
            FXMLLoader loader = new FXMLLoader(HistoricoController.class.getResource(Modulos.HISTORICO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historico Nota");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            HistoricoController controller = loader.getController();
            controller.setStage(stage);
            controller.setHistorico(Cliente.INSTANCIA.buscarHistorico("NOTA", nota.getId()));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void anexar(String tipo) {

        FileChooser fileChooser = new FileChooser();
        File temp = fileChooser.showOpenDialog(null);
        if (temp == null) {
            return;
        }

        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar(new CarregarController() {
            @Override
            public void run() {
                Platform.runLater(() -> setStatus("Anexando " + tipo));
                barraCarregar2.setProgress(0.0f);
                Cliente.INSTANCIA.anexarNota(nota.getId(), temp, tipo);
                barraCarregar2.setProgress(0.5f);
                Platform.runLater(() -> setStatus("Atualizando tabela"));
                atualizarTabelaAnexo();
                barraCarregar2.setProgress(1.0f);
                pai.setDisable(false);

            }

            @Override
            public void sucesso() {

            }

            @Override
            public void falha() {

            }
        });
    }

    private void atualizarTabelaAnexo() {
        anexos.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.listarAnexosNota(nota.getId())));
    }

    private void visualizar(AnexoNota anexo) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Salvar anexo");
        File arquivo = chooser.showDialog(null);

        CustomTask<Void> task = new CustomTask<Void>() {

            @Override
            protected Void executar() throws Exception {
                Desktop.getDesktop().open(Cliente.INSTANCIA.lerAnexoNota(anexo.getId(), arquivo));
                return null;
            }
        };
        new CarregarGui().mostrarTask(pai, task);

    }

    @FXML
    void incluirPagamento(ActionEvent event) {
        if (!nota.isPreNota()) {
            return;
        }
        atualizarTabelaItens();
    }

    @FXML
    void removerPagamento(ActionEvent event) {
        if (!nota.isPreNota()) {
            return;
        }
        Pagamento pagamento = pagamentos.getSelectionModel().getSelectedItem();
        if (pagamento == null) {
            return;
        }
        Cliente.INSTANCIA.removerPagamentoNota(pagamento);
        nota.getPagamentos().remove(pagamento);
        atualizarTabelaItens();
    }

    private void processarRetorno(int retorno, String campo) {
        switch (retorno) {
            case -2:
                status.setText("Erro ao atualizar " + campo);
                break;
            case -1:
                status.setText("Falhou ao atualizar " + campo);
                break;
            case 0:
                status.setText("Nenhum registro atualizado " + campo);
                break;
            case 1:
                status.setText("Atualizado com sucesso " + campo);
                break;
            default:
                status.setText("Mais de um registro atualizado,valor " + retorno + " Campo: " + campo);
                break;
        }
    }

    @Override
    public void succeeded() {
        if (this.nota.isCancelado()) {
            mensagem("Nota Cancelada", "Nota Cancelada", "Nota Cancelada");
        }
    }

    private void removerMarcacao() {
        desmarcarNote(numero);
        desmarcarNote(fornecedor);
        desmarcarNote(emissao);
        desmarcarNote(vencimento);
        desmarcarNote(vencimentoReal);
        desmarcarNote(tipo);
    }

    private void desmarcarNote(Node note) {
        desmarcarErro(note);
        desmarcarSucesso(note);
    }
    
    @FXML
    void incluirItem(){
        
    }
    
    @FXML
    void duplicarItem(){
        
    }
    
    @FXML
    void excluirItem(){
        
    }
}
