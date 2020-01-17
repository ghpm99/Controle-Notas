/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes.Nota.anexo;

/**
 * @author Guilherme
 */
public class AnexoNota {
private int id;
    
    private String nome,tipo,caminho;

    /**
     *
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getNome(){
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     *
     * @return
     */
    public String getTipo(){
        return tipo;
    }

    /**
     *
     * @param tipo
     */
    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    /**
     *
     * @return
     */
    public String getCaminho(){
        return caminho;
    }

    /**
     *
     * @param caminho
     */
    public void setCaminho(String caminho){
        this.caminho = caminho;
    }
    
    
}
