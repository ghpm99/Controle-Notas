/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

/**
 *
 * @author guilherme.machado
 */
public class Banco {
    
    //ID, CAMINHO, USUARIO, SENHA, NOME, ATIVO
    private final int ID;
    private String CAMINHO,USUARIO,SENHA,NOME;
    private boolean ATIVO,PMWEB;

    /**
     *
     * @param ID
     * @param CAMINHO
     * @param USUARIO
     * @param SENHA
     * @param NOME
     * @param ATIVO
     * @param PMWEB
     */
    public Banco(int ID, String CAMINHO, String USUARIO, String SENHA, String NOME, boolean ATIVO,boolean PMWEB) {
        this.ID = ID;
        this.CAMINHO = CAMINHO;
        this.USUARIO = USUARIO;
        this.SENHA = SENHA;
        this.NOME = NOME;
        this.ATIVO = ATIVO;
        this.PMWEB = PMWEB;
    }

    /**
     *
     * @return
     */
    public int getID() {
        return ID;
    }

    /**
     *
     * @return
     */
    public String getCAMINHO() {
        return CAMINHO;
    }

    /**
     *
     * @return
     */
    public String getUSUARIO() {
        return USUARIO;
    }

    /**
     *
     * @return
     */
    public String getSENHA() {
        return SENHA;
    }

    /**
     *
     * @return
     */
    public String getNOME() {
        return NOME;
    }

    /**
     *
     * @return
     */
    public boolean isATIVO() {
        return ATIVO;
    }

    /**
     *
     * @param CAMINHO
     */
    public void setCAMINHO(String CAMINHO) {
        this.CAMINHO = CAMINHO;
    }

    /**
     *
     * @param USUARIO
     */
    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    /**
     *
     * @param SENHA
     */
    public void setSENHA(String SENHA) {
        this.SENHA = SENHA;
    }

    /**
     *
     * @param NOME
     */
    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    /**
     *
     * @param ATIVO
     */
    public void setATIVO(boolean ATIVO) {
        this.ATIVO = ATIVO;
    }

    /**
     *
     * @return
     */
    public boolean isPMWEB(){
        return PMWEB;
    }

    /**
     *
     * @param PMWEB
     */
    public void setPMWEB(boolean PMWEB){
        this.PMWEB = PMWEB;
    }
    
    
}
