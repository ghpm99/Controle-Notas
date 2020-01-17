/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import GuiPrincipal.Util.MensagemController;
import Conexao.Cliente;
import GuiPrincipal.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Guilherme
 */
public class Mensagem{

    private String titulo = "Mensagem", mensagem, descricao;
    private boolean retorno;
    private Image imagem;

    /**
     *
     * @param mensagem
     * @param descricao
     */
    public Mensagem(String mensagem, String descricao){
        this.mensagem = mensagem;
        this.descricao = descricao;
    }

    /**
     *
     * @param titulo
     */
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    /**
     *
     * @return
     */
    public boolean getRetorno(){
        return retorno;
    }

    /**
     *
     * @param imagem
     */
    public void setImage(Image imagem){
      this.imagem = imagem;
    }

    /**
     *
     * @return
     */
    public boolean showDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(MensagemController.class.getResource("Mensagem.fxml"));
            GridPane anchor = (GridPane) loader.load();
            MensagemController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            controller.setStage(stage);
            controller.setMensagem(mensagem);
            controller.setDetalhes(descricao);
            controller.setBoolean(retorno);
            controller.setImagem(imagem);
            Scene scene = new Scene(anchor);
            stage.setScene(scene);
            stage.showAndWait();
            retorno = controller.getBoolean();
        } catch(IOException ex){
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
