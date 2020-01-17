/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes.contrato.item;

import classes.base.Item;
import classes.contrato.produto.ProdutoContrato;
import java.util.ArrayList;

/**
 * @author Guilherme
 */
public class ItemContrato extends Item{
    
    private int idContrato;
    private int idMapa;
    
    private ArrayList<ProdutoContrato> produtos = new ArrayList<>();

    public int getIdContrato(){
        return idContrato;
    }

    public void setIdContrato(int idContrato){
        this.idContrato = idContrato;
    }

    public int getIdMapa(){
        return idMapa;
    }

    public void setIdMapa(int idMapa){
        this.idMapa = idMapa;
    }

    
    
    
    public ArrayList<ProdutoContrato> getProdutos() {
        return produtos;
    }

    /**
     *
     * @param produtos
     */
    public void setProdutos(ArrayList<ProdutoContrato> produtos) {
        this.produtos = produtos;
    }

    /**
     *
     * @param produto
     */
    public void adicionarProduto(ProdutoContrato produto) {
        this.produtos.add(produto);
    }

    /**
     *
     * @param produto
     */
    public void removerProduto(ProdutoContrato produto) {
        this.produtos.remove(produto);
    }
}
