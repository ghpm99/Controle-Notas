/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal;

import Conexao.Cliente;
import Util.PrincipalGUI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class LogarFxController implements Initializable, PrincipalGUI {

    @FXML
    private Label status;
    @FXML
    private Label conexao;
    @FXML
    private PasswordField senha;
    @FXML
    private TextField login;
    @FXML
    private AnchorPane fundo;
    private Stage stage;

    @FXML
    private Label sobre;

    @FXML
    private ChoiceBox banco;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            String[] fundos = new String[]{"1.gif", "2.gif", "3.gif", "4.gif", "5.jpg", "6.jpg", "7.jpg", "8.jpg", "9.jpg"};
            String imagem = LogarFxController.class.getResource("fundos/" + fundos[new Random().nextInt(9)]).toExternalForm();
            fundo.setStyle("-fx-background-image: url('" + imagem + "');"
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch;"
                    + "-fx-background-repeat: no-repeat;"
                    + "-fx-background-size: 110%, 110%;");//960,540     

            sendOk();
            banco.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.buscarBancos()));
            banco.getSelectionModel().select(0);
        } catch (Exception ex) {
            Logger.getLogger(LogarFxController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendOk() {
        if (Cliente.INSTANCIA.liberado()) {
            conexao.setText("Conectou.");
        } else {
            conexao.setStyle("-fx-text-fill: red;");
            conexao.setText("Não foi possivel conectar.");
        }
        sobre.setText(Cliente.INSTANCIA.getSobre() + "\nId:" + UUID.nameUUIDFromBytes("".getBytes()));
    }

    @FXML
    private void logarKey(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            logar();
        }
    }

    @FXML
    private void cancelar() {
        Cliente.INSTANCIA.fechar();
    }

    @FXML
    private void logar() {
        if (Cliente.INSTANCIA.login(login.getText(), senha.getText(), (String) banco.getSelectionModel().getSelectedItem())) {
            Main.getInstancia().setScene(Caminhos.GUIMAIN);
            Main.getInstancia().setTitulo("Controle de custos - " + Cliente.INSTANCIA.getNomeConta() + " - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            Main.getInstancia().maximizar();

        } else {
            Main.getInstancia().getAtualControler().setStatus("Usuário e/ou senha\nincorreto(s)");
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void setStatus(String arg) {
        Platform.runLater(() -> status.setText(arg));
    }

    @Override
    public void setPing(long latencia) {

    }

}
