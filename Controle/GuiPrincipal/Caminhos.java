/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal;

/**
 *
 * @author Eduardo
 */
public enum Caminhos{

    LOGAR("Logar", "LogarFx.fxml",false),
    GUIMAIN("GuiMain", "GuiMain.fxml",true);

    private String nome, caminho;
    private boolean resizable;
    Caminhos(String nome, String caminho,boolean resizable){
        this.nome = nome;
        this.caminho = caminho;
        this.resizable = resizable;
    }

    /**
     *
     * @return
     */
    public String getNome(){
        return this.nome;
    }

    /**
     *
     * @return
     */
    public String getCaminho(){
        return this.caminho;
    }

    /**
     *
     * @return
     */
    public boolean getResizable(){
        return this.resizable;
    }
}
