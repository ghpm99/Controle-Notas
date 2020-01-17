/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Fluxo;
import classes.relatorio.Relatorio;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class FluxoController implements Initializable, PaneController {

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
        atualizar();
    }

    private void atualizar() {
        financeiro.getItems().clear();
        financeiro.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.listarFluxo()));
    }

    @FXML
    void exportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

        new Relatorio().exportarFluxo(fileChooser.showSaveDialog(null), new ArrayList<>(financeiro.getItems()),false);
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

    
    
}
