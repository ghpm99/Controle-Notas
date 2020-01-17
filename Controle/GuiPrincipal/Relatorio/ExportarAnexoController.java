/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Relatorio;

import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class ExportarAnexoController implements Initializable, PaneController {

    @FXML
    private ListView<String> saidaAnexos;
    @FXML
    private ListView<String> tiposAnexo;
    @FXML
    private ListView<String> saidaNota;
    @FXML
    private ListView<String> tiposNota;

    private ArrayList<String> tiposAnexoSelecionado;
    private ArrayList<String> tiposAnexoDisponiveis = new ArrayList<>();
    private ArrayList<String> tiposNotaSelecionado;
    private ArrayList<String> tiposNotaDisponiveis = new ArrayList<>();

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposAnexoDisponiveis.add("Nota");
        tiposAnexoDisponiveis.add("Documento");
        tiposAnexoDisponiveis.add("Boleto");

        tiposNotaDisponiveis.add("NF");
        tiposNotaDisponiveis.add("NFE");
        tiposNotaDisponiveis.add("NFS");
        tiposNotaDisponiveis.add("NFSE");
        tiposNotaDisponiveis.add("REC");
        tiposNotaDisponiveis.add("CT");
        tiposNotaDisponiveis.add("CTE");
        tiposNotaDisponiveis.add("PA");
        tiposNotaDisponiveis.add("SE");

        atualizar();
    }

    private void atualizar() {
        tiposAnexo.setItems(FXCollections.observableArrayList(tiposAnexoDisponiveis));
        tiposNota.setItems(FXCollections.observableArrayList(tiposNotaDisponiveis));
        if (tiposAnexoSelecionado != null) {
            saidaAnexos.setItems(FXCollections.observableArrayList(tiposAnexoSelecionado));
        }
        if (tiposNotaSelecionado != null) {
            saidaNota.setItems(FXCollections.observableArrayList(tiposNotaSelecionado));
        }
    }

    @FXML
    void cancelarButton(ActionEvent event) {
        tiposAnexoSelecionado.clear();
        tiposNotaSelecionado.clear();
        stage.close();
    }

    @FXML
    void exportarButton(ActionEvent event) {
        stage.close();
    }

    @Override
    public void setPaneController(BorderPane controller) {

    }

    @Override
    public void setModuloVoltar(Modulos modulo) {

    }

    @Override
    public void succeeded() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void RemoverTipoAnexo(ActionEvent event) {
        String temp = saidaAnexos.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposAnexoDisponiveis.add(temp);
        tiposAnexoSelecionado.remove(temp);
        atualizar();
    }

    @FXML
    void adicionarTipoAnexo(ActionEvent event) {
        String temp = tiposAnexo.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposAnexoSelecionado.add(temp);
        tiposAnexoDisponiveis.remove(temp);
        atualizar();
    }

    @FXML
    void RemoverTipoNota(ActionEvent event) {
        String temp = saidaNota.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposNotaDisponiveis.add(temp);
        tiposNotaSelecionado.remove(temp);
        atualizar();
    }

    @FXML
    void adicionarTipoNota(ActionEvent event) {
        String temp = tiposNota.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposNotaSelecionado.add(temp);
        tiposNotaDisponiveis.remove(temp);
        atualizar();
    }

    public void setArrayTipoAnexo(ArrayList<String> tipo) {
        tiposAnexoSelecionado = tipo;
    }

    public void setArrayTipoNota(ArrayList<String> tipo) {
        tiposNotaSelecionado = tipo;
    }

}
