/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes.Contrato.Produto;

import Classes.Base.Produto;
import java.util.Date;

/**
 * @author Guilherme
 */
public class ProdutoContrato extends Produto{

    private long qnt;
    private long precoUnitario;
    private long precoTotal;

    /**
     *
     * @return
     */
    public long getQnt(){
        return qnt;
    }

    /**
     *
     * @param qnt
     */
    public void setQnt(long qnt){
        this.qnt = qnt;
    }

    /**
     *
     * @return
     */
    public long getPrecoUnitario(){
        return precoUnitario;
    }

    /**
     *
     * @param precoUnitario
     */
    public void setPrecoUnitario(long precoUnitario){
        this.precoUnitario = precoUnitario;
    }

    /**
     *
     * @return
     */
    public long getPrecoTotal(){
        return precoTotal;
    }

    /**
     *
     * @param precoTotal
     */
    public void setPrecoTotal(long precoTotal){
        this.precoTotal = precoTotal;
    }
    
    
}