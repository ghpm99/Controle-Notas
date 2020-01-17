/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class HistoricoPesquisa {
    
    private String campo,valor;
    
    private ArrayList<String> itens = new ArrayList<>();

    /**
     *
     * @return
     */
    public String getCampo() {
        return campo;
    }

    /**
     *
     * @param campo
     */
    public void setCampo(String campo) {
        this.campo = campo;
    }

    /**
     *
     * @return
     */
    public String getValor() {
        return valor;
    }

    /**
     *
     * @param valor
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getItens() {
        return itens;
    }

    /**
     *
     * @param itens
     */
    public void setItens(ArrayList<String> itens) {
        this.itens = itens;
    }
    
    
}
