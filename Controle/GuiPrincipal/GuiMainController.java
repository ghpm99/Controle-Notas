/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal;

import Conexao.Cliente;
import classes.conta.Conta;
import Util.Modulos;
import Util.PaneController;
import Util.PrincipalGUI;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class GuiMainController implements Initializable, PrincipalGUI {

    @FXML
    private TreeView<String> list;
    @FXML
    private BorderPane area;
    @FXML
    private Label ping;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        TreeItem<String> controle = new TreeItem<>("Controle");
        
        TreeItem<String> ordem = new TreeItem<>("Ordem de compra");
        TreeItem<String> produto = new TreeItem<>("Produto");
        TreeItem<String> nota = new TreeItem<>("Nota");        
        TreeItem<String> preNota = new TreeItem<>("Pre-Nota");
        TreeItem<String> notaSubmetidas = new TreeItem<>("Nota submetidas");
        TreeItem<String> locacao = new TreeItem<>("Locacao");
        TreeItem<String> relatorio = new TreeItem<>("Relatorio");
        TreeItem<String> saida = new TreeItem<>("Saida");
        TreeItem<String> conta = new TreeItem<>("Conta");
        TreeItem<String> caminhos = new TreeItem<>("Caminhos");
        TreeItem<String> plano = new TreeItem<>("Plano de Conta");
        TreeItem<String> fornecedor = new TreeItem<>("Fornecedor");
        TreeItem<String> servidor = new TreeItem<>("Servidor");
        TreeItem<String> mapa = new TreeItem<>("Mapa de cotacao");
        TreeItem<String> item = new TreeItem<>("Item Orcamento");
        TreeItem<String> contrato = new TreeItem<>("Contratos");
        TreeItem<String> incc = new TreeItem<>("INCC");
        TreeItem<String> abertasFinanceiro = new TreeItem<>("Notas em aberto");        
        TreeItem<String> fluxoFinanceiro = new TreeItem<>("Fluxo");
        TreeItem<String> resumoFinanceiro = new TreeItem<>("Detalhado");
        TreeItem<String> renomear = new TreeItem<>("Renomear");
        TreeItem<String> sobre = new TreeItem<>("Sobre");
        TreeItem<String> diario = new TreeItem<>("Diario");
        TreeItem<String> sugestao = new TreeItem<>("Sugestao");
        /*
         Tree item do quarto nivel
         */
        TreeItem<String> relatorioNota = new TreeItem<>("Relatorio Nota");
        TreeItem<String> relatorioOrcamento = new TreeItem<>("Relatorio Orcamento");

        relatorio.getChildren().add(relatorioNota);
        relatorio.getChildren().add(relatorioOrcamento);

        Conta nivel = Cliente.INSTANCIA.getNivelConta();

        if (nivel.isCompra()) {
            TreeItem<String> compra = new TreeItem<>("Compra");
            compra.getChildren().add(ordem);
            compra.getChildren().add(produto);
            compra.getChildren().add(nota);
            compra.getChildren().add(preNota);
            compra.getChildren().add(fornecedor);
            compra.getChildren().add(relatorio);
            compra.getChildren().add(mapa);
            compra.getChildren().add(contrato);
            controle.getChildren().add(compra);
        }
        if (nivel.isAlmoxarife()) {
            TreeItem<String> almoxarife = new TreeItem<>("Almoxarife");
            almoxarife.getChildren().add(ordem);
            almoxarife.getChildren().add(produto);
            almoxarife.getChildren().add(nota);
            almoxarife.getChildren().add(preNota);
            almoxarife.getChildren().add(locacao);
            almoxarife.getChildren().add(saida);
            almoxarife.getChildren().add(fornecedor);
            almoxarife.getChildren().add(contrato);
            controle.getChildren().add(almoxarife);
        }
        if (nivel.isAdministracao()) {
            TreeItem<String> administracao = new TreeItem<>("Administracao");
            administracao.getChildren().add(ordem);
            administracao.getChildren().add(produto);
            administracao.getChildren().add(nota);
            administracao.getChildren().add(preNota);
            administracao.getChildren().add(fornecedor);
            administracao.getChildren().add(contrato);
            administracao.getChildren().add(relatorio);
            controle.getChildren().add(administracao);
        }
        if (nivel.isEngenharia()) {
            TreeItem<String> engenharia = new TreeItem<>("Engenharia");
            engenharia.getChildren().add(ordem);
            engenharia.getChildren().add(produto);
            engenharia.getChildren().add(nota);
            engenharia.getChildren().add(preNota);
            engenharia.getChildren().add(relatorio);
            engenharia.getChildren().add(plano);
            engenharia.getChildren().add(mapa);
            engenharia.getChildren().add(item);
            engenharia.getChildren().add(contrato);
            engenharia.getChildren().add(incc);
            controle.getChildren().add(engenharia);
        }
        if (nivel.isFinanceiro()) {
            TreeItem<String> financeiro = new TreeItem<>("Financeiro");
            financeiro.getChildren().add(abertasFinanceiro);
            financeiro.getChildren().add(diario);
            financeiro.getChildren().add(fluxoFinanceiro);
            financeiro.getChildren().add(resumoFinanceiro);
            controle.getChildren().add(financeiro);
        }
        if (nivel.isGerencia()) {
            TreeItem<String> gerencia = new TreeItem<>("Gerencia");
            gerencia.getChildren().add(caminhos);
            gerencia.getChildren().add(relatorio);
            gerencia.getChildren().add(servidor);
            gerencia.getChildren().add(notaSubmetidas);
            controle.getChildren().add(gerencia);
        }
        if (nivel.isCompra() && nivel.isAlmoxarife() && nivel.isAdministracao() && nivel.isEngenharia() && nivel.isFinanceiro() && nivel.isGerencia() && nivel.getNivel() >= 10) {
            controle.getChildren().add(conta);            
        }
        controle.getChildren().add(renomear);
        controle.getChildren().add(sugestao);
        controle.getChildren().add(sobre);
        
        /*
         seta o controle para nao se expandir
         */
        controle.setExpanded(false);

        /*
         adiciona o controle na lista view
         */
        list.setRoot(controle);

        /*
         adiciona evento de selecionar na lista
         */
        list.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) -> {
            modulo(newValue.getValue());
        });
    }

    private void modulo(String valor) {
        area.setDisable(true);
        Stage stage = new Stage();
        mostrar(stage);
        Task<Parent> task = new Task<Parent>() {
            @Override
            protected Parent call() throws Exception {
                try {
                    long antes = new Date().getTime();
                    Modulos modulo = Modulos.getModulo(valor);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(modulo.getCaminho()));
                    Parent parent = (Parent) loader.load();
                    PaneController controller = (PaneController) loader.getController();
                    controller.setPaneController(area);
                    long depois = new Date().getTime();
                    long diferença = depois - antes;
                    Main.getInstancia().ping(diferença);
                    return parent;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                try {
                    Main.getInstancia().setTitulo("Controle de custo " + Cliente.INSTANCIA.getNomeConta() + " " + valor);
                    area.getChildren().clear();
                    area.setCenter(get());
                    area.setDisable(false);
                    //W:1166.0 H:706.0
                    //System.out.println("W:" + area.getWidth() + " H:" + area.getHeight());
                    fechar(stage);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GuiMainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(GuiMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void fechar(Stage stage) {
        stage.close();
    }

    private void mostrar(Stage stage) {
        ProgressIndicator pi = new ProgressIndicator();
        pi.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(Main.getInstancia().getStage());
        Scene scene = new Scene(pi);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void notificar() {
    }

    @Override
    public void setStatus(String status) {
    }

    @Override
    public void setPing(long latencia) {
        Platform.runLater(() -> {
            this.ping.setText("Latencia " + latencia + " ms");
        });

    }

}
