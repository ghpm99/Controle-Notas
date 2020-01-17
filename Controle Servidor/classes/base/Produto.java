/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Base;

import Util.Dado;

/**
 * @author Guilherme
 */
public class Produto extends Dado{

    private String codigo;
    private String descricao;
    private String unidade;

    /**
     *
     * @return
     */
    public String getCodigo(){
        return codigo;
    }

    /**
     *
     * @param codigo
     */
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    /**
     *
     * @return
     */
    public String getDescricao(){
        return descricao;
    }

    /**
     *
     * @param descricao
     */
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    /**
     *
     * @return
     */
    public String getUnidade(){
        return unidade;
    }

    /**
     *
     * @param unidade
     */
    public void setUnidade(String unidade){
        this.unidade = unidade;
    }

}
