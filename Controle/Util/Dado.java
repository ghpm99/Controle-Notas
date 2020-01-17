/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Date;

/**
 * @author Guilherme
 */
public class Dado{

    private int id;
    private Date inclusao = new Date();
    private Date atualizacao = new Date();
    private String autor = "";
    private boolean cancelado;
    private boolean ativo;

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
    public Date getInclusao(){
        return inclusao;
    }

    /**
     *
     * @param inclusao
     */
    public void setInclusao(Date inclusao){
        this.inclusao = inclusao;
    }

    /**
     *
     * @return
     */
    public Date getAtualizacao(){
        return atualizacao;
    }

    /**
     *
     */
    public void setAtualizacao(){
        this.atualizacao = new Date();
    }

    /**
     *
     * @param date
     */
    public void setAtualizacao(Date date){
        this.atualizacao = date;
    }

    /**
     *
     * @return
     */
    public String getAutor(){
        return autor;
    }

    /**
     *
     * @param autor
     */
    public void setAutor(String autor){
        this.autor = autor;
    }

    /**
     *
     * @return
     */
    public boolean isCancelado(){
        return cancelado;
    }

    /**
     *
     * @param cancelado
     */
    public void setCancelado(boolean cancelado){
        this.cancelado = cancelado;
    }

    /**
     *
     * @return
     */
    public boolean isAtivo(){
        return ativo;
    }

    /**
     *
     * @param ativo
     */
    public void setAtivo(boolean ativo){
        this.ativo = ativo;
    }

}
