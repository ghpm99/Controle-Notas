/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Financeiro;

import Util.Dado;
import java.util.Date;

/**
 * @author Guilherme
 */
public class Fluxo extends Dado{

    private long valor, saldo, antes;
    private int operacao, nota;

    /**
     *
     * @return
     */
    public long getValor(){
        return valor;
    }

    /**
     *
     * @param valor
     */
    public void setValor(long valor){
        this.valor = valor;
    }

    /**
     *
     * @return
     */
    public long getSaldo(){
        return saldo;
    }

    /**
     *
     * @param saldo
     */
    public void setSaldo(long saldo){
        this.saldo = saldo;
    }

    /**
     *
     * @return
     */
    public long getAntes(){
        return antes;
    }

    /**
     *
     * @param antes
     */
    public void setAntes(long antes){
        this.antes = antes;
    }

    /**
     *
     * @return
     */
    public int getOperacao(){
        return operacao;
    }

    /**
     *
     * @param operacao
     */
    public void setOperacao(int operacao){
        this.operacao = operacao;
    }

    /**
     *
     * @return
     */
    public int getNota(){
        return nota;
    }

    /**
     *
     * @param nota
     */
    public void setNota(int nota){
        this.nota = nota;
    }

}
