/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.contrato.mapa;

import classes.contrato.item.ItemContrato;
import classes.base.Mapa;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Guilherme
 */
public class MapaContrato extends Mapa{

    private Date assinatura = new Date();
    private Date criacao = new Date();
    private String observacaoAssinatura = "";
    private String observacaoSistema = "";
    private String fisico = "";
    private String encerramento = "";
    private String observacao = "";
    private int idContrato;

    private ArrayList<ItemContrato> itens = new ArrayList<>();

    public int getIdContrato(){
        return idContrato;
    }

    public void setIdContrato(int idContrato){
        this.idContrato = idContrato;
    }

    
    
    public Date getAssinatura(){
        return assinatura;
    }

    /**
     *
     * @param assinatura
     */
    public void setAssinatura(Date assinatura){
        this.assinatura = assinatura;
    }

    /**
     *
     * @return
     */
    public Date getCriacao(){
        return criacao;
    }

    /**
     *
     * @param criacao
     */
    public void setCriacao(Date criacao){
        this.criacao = criacao;
    }

    /**
     *
     * @return
     */
    public String getObservacaoAssinatura(){
        return observacaoAssinatura;
    }

    /**
     *
     * @param observacaoAssinatura
     */
    public void setObservacaoAssinatura(String observacaoAssinatura){
        this.observacaoAssinatura = observacaoAssinatura;
    }

    /**
     *
     * @return
     */
    public String getObservacaoSistema(){
        return observacaoSistema;
    }

    /**
     *
     * @param observacaoSistema
     */
    public void setObservacaoSistema(String observacaoSistema){
        this.observacaoSistema = observacaoSistema;
    }

    /**
     *
     * @return
     */
    public String getFisico(){
        return fisico;
    }

    /**
     *
     * @param fisico
     */
    public void setFisico(String fisico){
        this.fisico = fisico;
    }

    /**
     *
     * @return
     */
    public String getEncerramento(){
        return encerramento;
    }

    /**
     *
     * @param encerramento
     */
    public void setEncerramento(String encerramento){
        this.encerramento = encerramento;
    }

    /**
     *
     * @return
     */
    public String getObservacao(){
        return observacao;
    }

    /**
     *
     * @param observacao
     */
    public void setObservacao(String observacao){
        this.observacao = observacao;
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemContrato> getItens(){
        return itens;
    }

    /**
     *
     * @param itens
     */
    public void setItens(ArrayList<ItemContrato> itens){
        this.itens = itens;
    }

    /**
     *
     * @param item
     */
    public void adicionarItem(ItemContrato item){
        this.itens.add(item);
    }

    /**
     *
     * @param item
     */
    public void removerItem(ItemContrato item){
        this.itens.remove(item);
    }

}
