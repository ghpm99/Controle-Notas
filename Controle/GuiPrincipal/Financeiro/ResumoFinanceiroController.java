/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Fluxo;
import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Relatorio.RelatorioParametrosController;
import GuiPrincipal.Util.CarregarController;
import classes.relatorio.Relatorio;
import Util.CarregarGui;

import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;

import Util.CustomTask;
import Util.Modulos;
import Util.PaneController;
import Util.Parametros;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class ResumoFinanceiroController implements Initializable, PaneController {

    private BorderPane pai;
    @FXML
    private TableView<Fluxo> financeiro;
    @FXML
    private TableColumn<Fluxo, String> valor;
    @FXML
    private TableColumn<Fluxo, String> saldo;
    @FXML
    private TableColumn<Fluxo, String> dia;
    @FXML
    private TableColumn<Fluxo, String> diario;
    @FXML
    private TableColumn<Fluxo, String> autor;
    @FXML
    private TableColumn<Fluxo, String> antes;

    private SimpleDateFormat sdf;

    private Parametros parametros;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dia.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getInclusao())));
        valor.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));
        diario.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> {
            if (c.getValue().getNota() == 0) {
                return new SimpleStringProperty("0");
            } else {
                return new SimpleStringProperty("DC-" + new DecimalFormat("00000").format(c.getValue().getNota()));
            }
        });
        saldo.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getSaldo())));
        antes.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getAntes())));
        autor.setCellValueFactory((TableColumn.CellDataFeatures<Fluxo, String> c) -> new SimpleStringProperty(String.valueOf(c.getValue().getAutor())));

    }

    private void atualizar() {
        financeiro.getItems().clear();
        financeiro.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.buscarFluxoDetalhado(parametros)));
    }

    @FXML
    void exportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

        File temp = fileChooser.showSaveDialog(null);
        if (temp == null) {
            return;
        }
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar(new CarregarController(){
            @Override
            public void run(){
                Platform.runLater(() -> setStatus("Iniciando Relatorio"));
                barraCarregar2.setProgress(0.0f);  
                Relatorio relatorio = new Relatorio();
                barraCarregar2.setProgress(0.5f);
                Platform.runLater(() -> setStatus("Gerando Relatorio"));
                relatorio.exportarFluxo(temp, new ArrayList<>(financeiro.getItems()), true);  
                barraCarregar2.setProgress(1.0f);               
                pai.setDisable(false);

            }

            @Override
            public void sucesso(){
                
            }

            @Override
            public void falha(){

            }
        });
        
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

    @FXML
    public void parametros() {
        try {
            parametros = new Parametros();
            FXMLLoader loader = new FXMLLoader(RelatorioNotaController.class.getResource(Modulos.RELATORIOPARAMETROS.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Parametros");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            RelatorioParametrosController controller = loader.getController();
            controller.setStage(stage);
            controller.setParametro(parametros);

            stage.showAndWait();

            if (controller.isOk()) {
                atualizar();
            }

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
