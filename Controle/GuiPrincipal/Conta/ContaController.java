/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Conta;

import Conexao.Cliente;
import classes.conta.Conta;
import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import Util.PaneController;
import Util.Modulos;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class ContaController implements Initializable, PaneController {

    private ArrayList<Conta> contasArray = new ArrayList<>();
    private BorderPane pai;
    @FXML
    private TableColumn nomeColuna;

    @FXML
    private TableColumn categoriaColuna;

    @FXML
    private TableView<Conta> contas;

    @FXML
    private TableColumn bloqueadoColuna;

    @FXML
    private TableColumn contaColuna;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nomeColuna.setCellValueFactory(new PropertyValueFactory("nome"));
        categoriaColuna.setCellValueFactory(new PropertyValueFactory("categoria"));
        bloqueadoColuna.setCellValueFactory(new PropertyValueFactory("block"));
        contaColuna.setCellValueFactory(new PropertyValueFactory("login"));
//        Cliente.INSTANCIA.buscar("CONTA", "NOME", "%").stream().forEach(s -> {
//            String[] temp = Cliente.INSTANCIA.buscar("CONTA", s);
//            contasArray.add(new Conta());
//        });
        contas.setRowFactory(s -> {
            TableRow<Conta> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                    alterarConta(linha.getItem());
                }
            });
            return linha;
        });

        atualizarTabela();
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
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void atualizarTabela() {
        contasArray = Cliente.INSTANCIA.listarContas();
        contas.setItems(FXCollections.observableArrayList(contasArray));
    }

    private void alterarConta(Conta conta) {

        try {
            FXMLLoader loader = new FXMLLoader(ContaAlterarController.class.getResource(Modulos.CONTAALTERAR.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Alterar Conta");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            ContaAlterarController controller = loader.getController();
            controller.setPaneController(pai);
            controller.setModuloVoltar(Modulos.CONTA);
            controller.setConta(conta);
            controller.atualizarCampos();
            controller.setStage(stage);

            stage.showAndWait();

            
                atualizarTabela();
            

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void incluir(){
        alterarConta(Cliente.INSTANCIA.incluirConta());
    }

    @Override
    public void succeeded() {
        
    }
}
