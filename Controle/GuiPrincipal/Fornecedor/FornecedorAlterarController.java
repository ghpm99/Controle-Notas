/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Fornecedor;

import Conexao.Cliente;
import GuiPrincipal.Contrato.MostrarContratosController;
import GuiPrincipal.Financeiro.MostrarPagamentosController;
import GuiPrincipal.GuiMainController;
import GuiPrincipal.Main;
import GuiPrincipal.Nota.MostrarNotasController;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import Util.CarregarGui;
import Util.HistoricoController;
import Util.Modulos;
import Util.PaneController;
import classes.fornecedor.Fornecedor;
import classes.fornecedor.contato.Contato;
import classes.fornecedor.endereco.Endereco;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Guilherme
 */
public class FornecedorAlterarController implements Initializable, PaneController {

    @FXML
    private TextField razaoSocialLabel;
    @FXML
    private TextField nomeFantasiaLabel;
    @FXML
    private TextField siglaLabel;
    @FXML
    private TextField tipoEmpresaLabel;
    @FXML
    private TextField cnpjLabel;
    @FXML
    private TextField inscEstadualLabel;
    @FXML
    private TextField inscMunicipalLabel;
    @FXML
    private TextField naturezaLabel;
    @FXML
    private TableView<Endereco> endereçoTableView;
    @FXML
    private TableColumn<Endereco, String> nomeEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> endereçoEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> codigoPostalEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> ruaEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> numeroEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> cidadeEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> distritoEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> estadoEndereçoTableColumn;
    @FXML
    private TableColumn<Endereco, String> paisEndereçoTableColumn;
    @FXML
    private TableView<Contato> contatoTableView;
    @FXML
    private TableColumn<Contato, String> descricaoContatoTableColumn;
    @FXML
    private TableColumn<Contato, String> tipoContatoTableColumn;
    @FXML
    private TableColumn<Contato, String> numeroContatoTableColumn;
    @FXML
    private Label status;
    @FXML
    private Label dataCriacaoLabel;
    @FXML
    private Label codigoLabel;

    private BorderPane pai;

    private Fornecedor fornecedor;

    private Modulos modulo = Modulos.FORNECEDOR;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nomeEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("nome"));
        endereçoEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("endereço"));
        codigoPostalEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("codigoPostal"));
        ruaEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("rua"));
        numeroEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("numero"));
        cidadeEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("cidade"));
        distritoEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("distrito"));
        estadoEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("estado"));
        paisEndereçoTableColumn.setCellValueFactory(new PropertyValueFactory<Endereco, String>("pais"));

        descricaoContatoTableColumn.setCellValueFactory(new PropertyValueFactory<Contato, String>("descricao"));
        descricaoContatoTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descricaoContatoTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<Contato, String> event) {                
                Cliente.INSTANCIA.setString("CONTATO", "DESCRICAO", event.getRowValue().getId(), event.getNewValue());
                atualizarTabelaContato();
            }
        });
        
        tipoContatoTableColumn.setCellValueFactory(new PropertyValueFactory<Contato, String>("tipo"));
        tipoContatoTableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(FXCollections.observableArrayList("TELEFONE","E-MAIL")));
        tipoContatoTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<Contato, String> event) {
                Cliente.INSTANCIA.setString("CONTATO", "TIPO", event.getRowValue().getId(), event.getNewValue());
                atualizarTabelaContato();
            }
        });
        
        numeroContatoTableColumn.setCellValueFactory(new PropertyValueFactory<Contato, String>("numero"));
        numeroContatoTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        numeroContatoTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<Contato, String> event) {
                Cliente.INSTANCIA.setString("CONTATO", "NUMERO", event.getRowValue().getId(), event.getNewValue());
                atualizarTabelaContato();
            }
        });
        
        razaoSocialLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarRazaoSocial();
            }
        });

        nomeFantasiaLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarNomeFantasia();
            }
        });

        siglaLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarSigla();
            }
        });

        tipoEmpresaLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarTipoEmpresa();
            }
        });

        cnpjLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarCnpj();
            }
        });

        inscEstadualLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarInscEstadual();
            }
        });

        inscMunicipalLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarInscMunicipal();
            }
        });

        naturezaLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                atualizarNatureza();
            }
        });

        atualizarCampos();
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
     */
    @FXML
    public void voltar() {
        new CarregarGui().voltar(pai, modulo);
    }

    public void atualizarCampos() {
        razaoSocialLabel.setText(fornecedor.getRazaoSocial());
        nomeFantasiaLabel.setText(fornecedor.getNomeFantasia());
        siglaLabel.setText(fornecedor.getSigla());
        tipoEmpresaLabel.setText(fornecedor.getTipoEmpresa());
        cnpjLabel.setText(fornecedor.getCnpj());
        inscEstadualLabel.setText(fornecedor.getInscEstadual());
        inscMunicipalLabel.setText(fornecedor.getInscMunicipal());
        naturezaLabel.setText(fornecedor.getNatureza());
        dataCriacaoLabel.setText(sdf.format(fornecedor.getInclusao()));
        codigoLabel.setText(String.valueOf(fornecedor.getId()));
        atualizarTabelaContato();
    }

    public void atualizarFornecedor() {

    }

    public void atualizarTabelaContato(){
        contatoTableView.getItems().clear();
        contatoTableView.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.listarContatoFornecedor(fornecedor.getId())));
    }
   
    @FXML
    public void salvar() {
        atualizarFornecedor();
        int retorno = 0;
        switch (retorno) {
            case -1:
                status.setText("Erro a atualizar");
                break;
            case 0:
                status.setText("Falhou em atualizar");
                break;
            case 1:
                status.setText("Atualizado com sucesso");
                break;
            default:
                status.setText("Erro");
                break;
        }
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    @FXML
    void banco(ActionEvent event) {
        
    }

    @FXML
    void contabilidade(ActionEvent event) {

    }

    @FXML
    void historico(ActionEvent event) {
         try {
            FXMLLoader loader = new FXMLLoader(HistoricoController.class.getResource(Modulos.HISTORICO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historico Fornecedor");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            HistoricoController controller = loader.getController();
            controller.setStage(stage);
            controller.setHistorico(Cliente.INSTANCIA.buscarHistorico("FORNECEDOR",fornecedor.getId()));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void contratos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(Modulos.MOSTRARCONTRATOS.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mostrar Contratos");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            MostrarContratosController contratosController = loader.getController();
            contratosController.setPaneController(pai);
            contratosController.setStage(stage);
            contratosController.setContratos(Cliente.INSTANCIA.listarContratosFornecedor(fornecedor.getId()));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    void incluirContato(ActionEvent event) {
        Cliente.INSTANCIA.incluirContatoFornecedor(fornecedor.getId());
        atualizarTabelaContato();
    }

    @FXML
    void incluirEndereço(ActionEvent event) {

    }

    @FXML
    void notas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(Modulos.MOSTRARNOTAS.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mostrar Notas");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            MostrarNotasController contratosController = loader.getController();
            contratosController.setPaneController(pai);
            contratosController.setStage(stage);
            contratosController.setNotas(Cliente.INSTANCIA.buscarNotasFornecedor(fornecedor.getId()));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void pagamentosAbertos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(Modulos.MOSTRARFINANCEIRO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mostrar Pagamentos Abertos");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            MostrarPagamentosController contratosController = loader.getController();
            contratosController.setPaneController(pai);
            contratosController.setStage(stage);
            contratosController.setPagamento(Cliente.INSTANCIA.listarPagamentosFornecedor(fornecedor.getId(), false));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void pagamentosBaixados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(GuiMainController.class.getResource(Modulos.MOSTRARFINANCEIRO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mostrar Pagamentos Baixados");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            MostrarPagamentosController contratosController = loader.getController();
            contratosController.setPaneController(pai);
            contratosController.setStage(stage);
            contratosController.setPagamento(Cliente.INSTANCIA.listarPagamentosFornecedor(fornecedor.getId(), true));

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void removerContato(ActionEvent event) {

    }

    @FXML
    void removerEndereço(ActionEvent event) {

    }

    private void atualizarRazaoSocial() {
        atualizar("RAZAOSOCIAL", razaoSocialLabel.getText(), "Razao social");
    }

    private void atualizarNomeFantasia() {
        atualizar("NOMEFANTASIA", nomeFantasiaLabel.getText(), "Nome fantasia");
    }

    private void atualizarSigla() {
        atualizar("SIGLA", siglaLabel.getText(), "Sigla");
    }

    private void atualizarTipoEmpresa() {
        //atualizar("TIPOEMPRESA", tipoEmpresaLabel.getText(), "Tipo empresa");
    }

    private void atualizarCnpj() {
        atualizar("CNPJ", cnpjLabel.getText(), "Cnpj");
    }

    private void atualizarInscEstadual() {
        atualizar("INSCESTADUAL", inscEstadualLabel.getText(), "Insc estadual");
    }

    private void atualizarInscMunicipal() {
        atualizar("INSCMUNICIPAL", inscMunicipalLabel.getText(), "Insc municipal");
    }

    private void atualizarNatureza() {
        //atualizar("NATUREZA", naturezaLabel.getText(), "Natureza");
    }

    private void atualizar(String campo, String valor, String arg0) {
        processarRetorno(Cliente.INSTANCIA.setString("FORNECEDOR", campo, fornecedor.getId(), valor), arg0);
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
    }
}
