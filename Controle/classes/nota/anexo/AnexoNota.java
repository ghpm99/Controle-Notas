/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes.nota.anexo;

/**
 * @author Guilherme
 */
public class AnexoNota {

    private int id;
    
    private String nome,tipo,caminho;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getCaminho(){
        return caminho;
    }

    public void setCaminho(String caminho){
        this.caminho = caminho;
    }
    
    
}
