/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Conta;

import Conexao.Cliente;
import Util.Modulos;
import Util.PaneController;
import classes.conta.Conta;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class ContaAlterarController implements Initializable, PaneController {

    private Conta conta;
    private BorderPane pai;
    private Modulos modulo = Modulos.CONTA;
    

    @FXML
    private TextField nome;

    @FXML
    private TextField login;
    
    @FXML
    private TextField categoria;

    @FXML
    private TextField email;
    
    @FXML
    private PasswordField senha;

    @FXML
    private CheckBox compra;

    @FXML
    private CheckBox almoxarife;

    @FXML
    private CheckBox administracao;

    @FXML
    private CheckBox engenharia;

    @FXML
    private CheckBox financeiro;

    @FXML
    private CheckBox gerencia;

    @FXML
    private CheckBox logado;

    @FXML
    private TextField ultimoLogin;

    @FXML
    private TextField expira;

    @FXML
    private CheckBox block;
    @FXML
    private Slider nivel;
    
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        /*
         private int id, nivel;
         private String nome, login, categoria, senha, email;
         private boolean block, compra, almoxarife, administracao, engenharia, financeiro, gerencia, logado;
         private long ultimoLogin, expira;
         */
       
    }

    @FXML
    void salvar(ActionEvent event) {
        atualizarConta();
           
        stage.close();
        
    }
     @FXML
    void alterarSenha(){
        int retorno = Cliente.INSTANCIA.atualizarSenhaConta(conta.getId(),senha.getText());
         System.out.println(retorno);
    }

    private void atualizarConta(){
        conta.setLogin(login.getText());
        conta.setNome(nome.getText());
        conta.setCategoria(categoria.getText());
        conta.setEmail(email.getText());
        conta.setCompra(compra.isSelected());
        conta.setAlmoxarife(almoxarife.isSelected());
        conta.setAdministracao(administracao.isSelected());
        conta.setEngenharia(engenharia.isSelected());
        conta.setFinanceiro(financeiro.isSelected());
        conta.setGerencia(gerencia.isSelected());
        conta.setBlock(block.isSelected());
        conta.setNivel((int)nivel.getValue());
    }
    
    public void atualizarCampos() {     
        login.setText(conta.getLogin());
        nome.setText(conta.getNome());
        categoria.setText(conta.getCategoria());
        email.setText(conta.getEmail());
        compra.setSelected(conta.isCompra());
        almoxarife.setSelected(conta.isAlmoxarife());
        administracao.setSelected(conta.isAdministracao());
        engenharia.setSelected(conta.isEngenharia());
        financeiro.setSelected(conta.isFinanceiro());
        gerencia.setSelected(conta.isGerencia());
        logado.setSelected(conta.isLogado());
        ultimoLogin.setText(new Date(conta.getUltimoLogin()).toString());
        expira.setText(new Date(conta.getExpira()).toString());
        block.setSelected(conta.isBlock());
        nivel.setValue(conta.getNivel());
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
        
    }
    

    void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void succeeded() {
       
    }

}
